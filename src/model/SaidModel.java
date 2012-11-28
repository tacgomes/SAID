package model;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ejml.alg.dense.decomposition.svd.SvdNumericalRecipes;
import org.ejml.data.DenseMatrix64F;

import classifier.Classifier;
import classifier.DataPoint;
import classifier.cluster.Cluster;
import classifier.proxmeasure.CosineSimilarity;
import classifier.proxmeasure.ProximityMeasure;

/**
 * This class contains several utility functions, mostly called directly
 * by the user interface.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
public class SaidModel {
	
	// possible weight shemes
	public static final int WS_TF    = 1;
	public static final int WS_TFIDF = 2;

	/** Name of the file that will save the log contents. */
	private String logFile_;

	/** Print stream that will be used to redirect the standard output to a log file. */
	private PrintStream ps_;

	/** List of files containg all files including those on subdirectories. */
	private ArrayList<File> fileList_;

	/** List of documents. */
	private ArrayList<Document> docs_;

	/** List of data points */
	private ArrayList<DataPoint> dpoints_;
	
	/** Number of invalid documents. */
	private int nInvalidDocs_;

	/** Classifier to perform clustering operations. */
	private Classifier classif_;

	/** Dictionary to read and save the words on the dictionary file. */
	private Dictionary dict_;

	/** Word data base in use. */
	private WordDataBase wdb_;

	/** List of words and corresponding weights. */
	private HashMap<String, Float> sysWeightTbl_;

	/** Directory where the documents are stored. */
	private	String docsDir_;

	/** Indicates if the system is using a dictionary. */
	private boolean dictEnabled_;

	/** Path in the file system where the dictionary file is stored. */
	private String dictPath_;

	/** Indicates if word aging is or is not enabled. */
	private boolean wordAging_;

	/** Time unit that a word can be on dictionary without reappear. */
	private int maxAge_;

	/** Indicates if the system is using or will use LSI. */
	private boolean lsiEnabled_;

	/** Percentage of the trace of S matrix to keep */
	private double threshold_;

	/** Scheme used to determine the weight of each word. */
	private int	weightScheme_;

	/** Matrix U computed from SVD. */
	private float[][] Uk_;
	
	/** Matrix S computed from SVD. */
	private float[] Sk_;
	
	
	/**
	 * Creates a new SAIDModel instance.
	 */
	public SaidModel() {
	}

	/**
	 * Creates a new SAIDModel instance.
	 */
	public SaidModel(String logFile) {
		logFile_ = logFile;
		// redirect standard output to the log file
		FileOutputStream fos;
		BufferedOutputStream bos;
		try {
			// open the file in append mode
			fos = new FileOutputStream(logFile, true);
			bos = new BufferedOutputStream(fos);
			ps_ = new PrintStream(bos, false);
			System.setOut(ps_);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Prepares the system to an new analysis.
	 */
	public void prepareNewAnalysis() {
		docs_         = new ArrayList<Document>();
		dpoints_      = new ArrayList<DataPoint>();
		wdb_          = new WordDataBase();
		sysWeightTbl_ = null;
		dictEnabled_  = false;
		wordAging_ = false;
		lsiEnabled_   = false;
		weightScheme_ = WS_TFIDF;
		nInvalidDocs_ = 0;
		classif_= new Classifier();
	}

	/**
	 * This method is responsible for reading the dictionary.
	 */
	public void readDictionary() {
		System.out.println("Reading the dictionary");
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		if (wordAging_) {
			System.out.println("        Evolution is on");
			dict_ = new Dictionary(dictPath_, maxAge_);
		}
		else {
			System.out.println("        Evolution is off");
			dict_ = new Dictionary(dictPath_);
		}
		cal = Calendar.getInstance();
		System.out.println("        finished in " + (cal.getTimeInMillis() - time) + " ms\n");
	}

	/**
	 * Returns a list of files containing all files including those in subdirectories.
	 * 
	 * @return	A list of files.
	 */
	public ArrayList<File> getSortedFileList() {
		fileList_ = new ArrayList<File>();
		fillFileList(new File(docsDir_));
		// sorting files by name
		Collections.sort(fileList_, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		return fileList_;
	}

	/**
	 * This method fills recursively the list of files.
	 * 
	 * @param	file	The file to get recursively the list of files.
	 */
	private void fillFileList(File file) {
		if (file.isFile()) {
			fileList_.add(file);
		}
		else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					fillFileList(files[i]);
				}
			}
		}
	}

	/**
	 * Adds a new document to the list of documents.
	 * 
	 * @param	file	The document to add.
	 */
	public void addDocument(File file) {
		if (dictEnabled_) {
			docs_.add(new Document(file, wdb_, dict_));
		}
		else {
			docs_.add(new Document(file, wdb_));
		}
	}
	
	/**
	 * Adds a new document to the system.
	 * 
	 * @param	file	The new document.
	 */
	public void addStreamingClusteringDocument(File file) {
		sysWeightTbl_ = null;
		Document doc  = new Document(file, wdb_, dict_);
		doc.calculateVector(1, getTermVocabulary(), wdb_, docs_.size());
		if (doc.isValid() == true) {
			docs_.add(doc);
			doc.setIndex(docs_.size() - 1);
			dpoints_.add(doc);
			classif_.addDataPoint(doc);
			classif_.streamingClustering().assignDataPoint(doc);
		}
		else {
			System.out.println("        " + doc.getPath() + File.separator
					+ doc.getName() + " is not valid and will be removed");
			nInvalidDocs_++;
		}
		
	}

	/**
	 * Calculates the weights for all words that appeared on the documents corpus.
	 */
	@SuppressWarnings("unchecked")
	private void calculateWeights() {
		sysWeightTbl_ = new HashMap<String, Float>();
		for (Document doc: docs_) {
			HashMap<String, Float> docWeightTbl = getWeightTableOfDocument(doc);
			Iterator iter = docWeightTbl.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String ,Float> e = (Map.Entry<String, Float>)iter.next();
				String word  = e.getKey();
				Float weight = (float)e.getValue();
				if (sysWeightTbl_.containsKey(word)) {
					sysWeightTbl_.put(word, sysWeightTbl_.get(word) + weight);
				}
				else {
					sysWeightTbl_.put(word, weight);
				}
			}
		}
	}

	/**
	 * Calculates the vector for each document.
	 */
	public void calculateVectors() {
		System.out.println("Calculating document vectors");
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();

		int i = 0;
		while (i < docs_.size()) {
			Document doc = docs_.get(i);
			doc.calculateVector(weightScheme_, getTermVocabulary(), wdb_, docs_.size());
			if (doc.isValid() == true) {
				doc.setIndex(i);
				dpoints_.add(doc);
			}
			else {
				System.out.println("        " + doc.getPath() + File.separator
						+ doc.getName() + " is not valid and will be removed");
				docs_.remove(doc);
				nInvalidDocs_++;
				i--;
			}
			i++;
		}

		classif_.setDataPoints(dpoints_);
		
		cal = Calendar.getInstance();
		System.out.println("        finished in " + (cal.getTimeInMillis() - time) + " ms");
	}

	/**
	 * Returns the term document matrix.
	 * 
	 * @return Term document matrix.
	 */
	private double[][] getTermDocumentMatrix() {
		int nWords     = getTermVocabulary().size();
		int nDocuments = dpoints_.size();
		double[][] tdm = new double[nWords][nDocuments];
		for (int i = 0; i < dpoints_.size(); i++) {
			float[] v = dpoints_.get(i).getVector();
			for (int j = 0; j < v.length ; j++) {
				tdm[j][i] = v[j];
			}
		}
		return tdm;
	}

	/**
	 * Applies the Latent Semantic Indexing technic and sets the
	 * vector of each document based on the results of the V matrix
	 * obtained by the Singule Value Decomposition algorithm.
	 */
	public void applyLatentSemanticIndexing() {
		System.out.println("\nApplying Latent Semantic Indexing");
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		
		// run the SVD algorithm on the term document matrix
		SvdNumericalRecipes svd = new SvdNumericalRecipes();
		svd.decompose(new DenseMatrix64F(getTermDocumentMatrix()));

		// calculate the squared frobenius norm
		double sqFrobNorm = 0;
		for (double singularVal: svd.getSingularValues()) {
			sqFrobNorm += singularVal * singularVal;
		}

		// calculate the rank of the matrix based on a given threshold
		double[] S = svd.getSingularValues();
		int rank = S.length;
		double sum = 0;
		for (int index = 0; index < S.length; index++) {
			double singularVal = S[index];
			sum += singularVal * singularVal;
			if (sum / sqFrobNorm >= threshold_) {
				rank = index;
				break;
			}
		}
		
		// save the first `rank' singular values
		Sk_ = new float[rank];
		for (int i = 0; i < rank; i++) {
			Sk_[i] = (float) S[i];
		}
		
		// save a low rank aproximation of the U matrix
		double U[] = svd.getU().getData();
		int nRows = svd.getU().getNumRows();
		int nCols = svd.getU().getNumCols();
		Uk_ = new float [nRows][rank];
		for (int i = 0; i < nRows; i++) {
			int offset = i * nCols;
			for (int j = 0; j < rank; j++) {
				Uk_[i][j] = (float) U[offset + j];
			}
		}
		
		// set the new vector for each document
		double V[] = svd.getV().getData();
		nCols  = svd.getV().getNumCols();
		for (int i = 0; i < dpoints_.size(); i++) {
			float[] vector = new float[rank];
			int offset = i * nCols;
			for (int j = 0; j < rank; j++) {
				vector[j] = (float) V[offset + j];
			}
			dpoints_.get(i).setVector(vector);
		}
		
		cal = Calendar.getInstance();
		System.out.println("        finished in " + (cal.getTimeInMillis() - time) + " ms");
	}
	
	/**
	 * Returns a list of words that match with the incompleted word.
	 * 
	 * @param	incompletedWord	The incompleted word.
	 * 
	 * @return	A list of words that can complete the given word.
	 */
	public ArrayList<String> getMatchedWordList(String incompletedWord) {
		ArrayList<String> matchedWords = new ArrayList<String>();
		
		for (String term: getTermVocabulary()) {
			if (term.startsWith(incompletedWord.toLowerCase())) {
				matchedWords.add(term);
			}
		}
		
		Collections.sort(matchedWords, new Comparator<String>() {
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
		});
		
		return matchedWords;
	}
	
	/**
	 * Calculates the query vector from a list of words.
	 * 
	 * @param	queryWords	List of words necessary to make the query vector.
	 * 
	 * @return	The query vector.
	 */
	private float[] getQueryVector(String[] queryWords) {
		ArrayList<String> termVocabulary = getTermVocabulary();
		float[] queryVector = new float[termVocabulary.size()];
		Arrays.fill(queryVector, 0);
		
		for (String word: queryWords) {
			int index = termVocabulary.indexOf(word);
			if (index == -1) { // if word exists in the term vocabulary
				return null;
			}
			queryVector[index] = 1;
		}
				
		if (lsiEnabled_) {	
			float[] qUk = new float[Uk_[0].length]; // will contain q * Uk
			Arrays.fill(qUk, 0);
			for (int i = 0; i < qUk.length; i++) {
				for (int j = 0; j < queryVector.length; j++) {
					qUk[i] += queryVector[j] * Uk_[j][i];
				}
			}

			// will contain q * Uk * Sk-1. (Sk-1 is the inverse of Sk)
			float[] qUkSk = new float[Uk_[0].length];
			
			for (int i = 0; i < qUkSk.length; i++) {
				qUkSk[i] = qUk[i] * (1 / Sk_[i]);
			}
				
			queryVector = qUkSk;
		}
		
		return queryVector;
	}
	
	/**
	 * Returns an HashMap with the indexes of the clusters that match the query
	 * and a score that indicates how much the cluster and the query match.
	 * 
	 * @param	query	The query to do.
	 * 
	 * @return	HashMap with the indexes of the clusters and respective scores.
	 */
	public HashMap<Integer, Float> getMatchedClusterIndexes(String query) {
		float[] queryVector = getQueryVector(query.split(" "));
		if (queryVector == null) {
			return null; // the query doesn't cointain any word from the term vocabulary 
		}
		
		ProximityMeasure cosSim = new CosineSimilarity();
		ArrayList<Cluster> clusters = classif_.getClusters();
		int k = classif_.getFinalK();
						
		// calculate the score for each cluster
		int[]   indexes = new int[k];
		float[] scores  = new float[k];
		for (int i = 0; i < k; i++) {
			scores[i]  = cosSim.getProximity(queryVector, clusters.get(i).getCentroid());
			indexes[i] = i;
		}
		
		// sort the indexes of the clusters decreasing by score
		for (int i = 0; i < k-1; i++) {
			for (int j = i+1; j < k; j++) {
				if (scores[indexes[j]] > scores[indexes[i]]) {
					int tmp    = indexes[j];
					indexes[j] = indexes[i];
					indexes[i] = tmp;
				}
			}
		}
		
		// keep only the indexes of the clusters that have a score greater than zero
		int i = 0;
		HashMap<Integer, Float> clusterIndexes = new HashMap<Integer, Float>();
		while (i < k && scores[indexes[i]] > 0) {
			clusterIndexes.put(indexes[i], scores[indexes[i]]);
			i++;
		}

		return clusterIndexes;
	}

	/**
	 * Calculates the weight table for a given document.
	 * 
	 * @param	doc	The document to calculate the weight table.
	 * 
	 * @return	A HashMap containing the words and corresponding weights.
	 */
	public HashMap<String, Float> getWeightTableOfDocument(Document doc) {
		HashMap<String, Float> wt = new HashMap<String, Float>();
		for (String word: doc.getWords()) {
			int tf = doc.getTermFrequencyOf(word);
			float weight = 0;
			switch (weightScheme_) {
				case WS_TF:
					weight = tf;
					break;
				case WS_TFIDF:
					float idf = (float) Math.log((float)docs_.size() / wdb_.getDocumentFreqOf(word));
					weight  = tf * idf;
					break;
			}
			wt.put(word, weight);
		}
		return wt;
	}

	/**
	 * Calculates the weight table for a given cluster.
	 * If LSI is not enabled, the centroid of the cluster is used to obtain
	 * the weight table. If LSI is enabled, the weight table for the cluster
	 * is obtained by sum the weight tables of the members of that cluster.
	 * 
	 * @param	clusterIndex	The index of the cluster to calculate the weight table.
	 * 
	 * @return	HashMap containing the words and corresponding weights.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Float> getWeightTableOfCluster(int clusterIndex) {
		HashMap<String, Float> wt = new HashMap<String, Float>();
		ArrayList<String> vocabularyWords = getTermVocabulary();
		Cluster c = classif_.getClusters().get(clusterIndex);

		if (lsiEnabled_ == false) {
			for (int i = 0; i < Document.vectorSize(); i++) {
				if (c.getCentroid()[i] != 0) {
					wt.put(vocabularyWords.get(i), c.getCentroid()[i] * c.size());
				}
			}
		}
		else {
			for (DataPoint dp: c.getDataPoints()) {
				Document doc = docs_.get(dp.getIndex());
				Iterator iter = getWeightTableOfDocument(doc).entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String ,Float> e = (Map.Entry<String, Float>)iter.next();
					String word  = e.getKey();
					Float weight = (float)e.getValue();
					if (vocabularyWords.contains(word)) {
						if (wt.containsKey(word)) {
							wt.put(word, wt.get(word) + weight);
						}
						else {
							wt.put(word, weight);
						}
					}
				}
			}
		}
		 
		return wt;
	}

	/**
	 * Returns a string containing all log information.
	 * 
	 * @return	A string containing all log information.
	 */
	public String getLog() {
		ps_.flush();
		String line;
		StringBuilder log = new StringBuilder();
		try {
			// use buffering, reading one line at a time
			BufferedReader input =  new BufferedReader(new FileReader(logFile_));
			while ((line = input.readLine()) != null) {
				log.append(line + System.getProperty("line.separator"));
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return log.toString();
	}

	/**
	 * Erases the contents of the log file.
	 */
	public void clearLog() {
		try {
			// opening the file in overwrite mode ...
			// ... will clear the contents of the file
			new FileOutputStream(logFile_);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the data saved in internal buffers to the log file.
	 */
	public void flushPrintStream() {
		ps_.flush();
	}

	/**
	 * Returns a list of the words that compose the term vocabulary.
	 * 
	 * @return	List of the words that will be used as term vocabulary.
	 */
	private ArrayList<String> getTermVocabulary() {
		return dictEnabled_ ? dict_.getWords() : wdb_.getWords(); 
	}

	/**
	 * Returns the directory where the document corpus is stored.
	 * 
	 * @return	Directory where the document corpus is stored.
	 */
	public String getDocumentsDirectory() {
		return docsDir_;
	}

	/**
	 * Sets the directory where the document corpus is stored.
	 * 
	 * @param	docsDir	Directory where the document corpus is stored.
	 */
	public void setDocumentsDirectory(String docsDir) {
		docsDir_ = docsDir;
	}

	/**
	 * Sets the path in the file system where the dictionary file is located.
	 * 
	 * @param	dictPath	Path where the dictionary file is located.
	 */
	public void setDictionary(String dictPath) {
		dictEnabled_ = true;
		dictPath_    = dictPath;
		wordAging_   = false;
	}

	/**
	 * Sets the path in the file system where the dictionary file is located 
	 * and enables word aging.
	 * 
	 * @param	dictPath	Path where the dictionary file is located.
	 * @param	maxAge 		Time unit that a word can be on dictionary without reappear.
	 */
	public void setDictionary(String dictPath, int maxAge) {
		dictEnabled_ = true;
		dictPath_    = dictPath;
		wordAging_   = true;
		maxAge_      = maxAge;
	}

	/**
	 * Sets the max age for the words in the dictionary.
	 * 
	 * @param	maxAge	Time unit that a word can be on dictionary without reappear.
	 */
	public void setDecayTime(int maxAge) {
		maxAge_    = maxAge;
		wordAging_ = true;
	}

	/**
	 * Returns whether the system is using a dictionary or not.
	 * 
	 * @return	True if and only if the system is using a dictionary.
	 */
	public boolean isDictionaryEnabled() {
		return dictEnabled_;
	}

	/**
	 * Activates the use of Latent Semantic Indexing
	 * 
	 * @param	threshold	Percentage of the trace of S matrix to keep.
	 */
	public void setLatentSemanticIndexing(double threshold) {
		lsiEnabled_ = true;
		threshold_  = threshold;
	}

	/**
	 * Checks if the system is using or will use Latent Semantic Indexing
	 * 
	 * @return	True if and only if the use of LSI is activated.
	 */
	public boolean isLatentSemanticIndexingEnabled() {
		return lsiEnabled_;
	}

	/**
	 * Sets the weight scheme used to calculate the weight of each word.
	 * 
	 * @param	weightScheme	Scheme used to determine the weight of each word.
	 */
	public void setWeightScheme(int weightScheme) {
		weightScheme_ = weightScheme;
	}

	/**
	 * Returns a list of all documents in the document corpus.
	 * 
	 * @return	List of all documents in the document corpus.
	 */
	public ArrayList<Document> documents() {
		return docs_;
	}

	/**
	 * Returns the classifier intance being used to perform clustering related operations.
	 * 
	 * @return	Classifier instance being used.
	 */
	public Classifier classifier() {
		return classif_;
	}

	/**
	 * Returns the dictionary instance being used.
	 * 
	 * @return	Dictionary instance being used.
	 */
	public Dictionary dictionary() {
		return dict_;
	}

	/**
	 * Returns the word data base instance being used.
	 * 
	 * @return	WordDataBase instance being used.
	 */
	public WordDataBase wdb() {
		return wdb_;
	}

	/**
	 * Returns a weight table for all words that appeared in the document corpus.
	 * 
	 * @return	HashMap containing the words and corresponding weights.
	 */
	public HashMap<String, Float> getSystemWeightTable() {
		if (sysWeightTbl_ == null) {
			calculateWeights();
		}
		return sysWeightTbl_;
	}

	/**
	 * Returns the number of documents in the document corpus.
	 * 
	 * @return	Number of documents in the document corpus.
	 */
	public int getNumDocuments() {
		return docs_.size();
	}

	/**
	 * Returns a list of all data points.
	 * 
	 * @return	List of all data points.
	 */
	public ArrayList<DataPoint> getDataPoints() {
		return dpoints_;
	}

	/**
	 * Returns the number of invalid documents in the document corpus.
	 * 
	 * @return	Number of invalid documents in the document corpus.
	 */
	public int getNumInvalidDocuments() {
		return nInvalidDocs_;
	}
	
}
