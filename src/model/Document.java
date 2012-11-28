package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import parser.Parser;
import classifier.DataPoint;

/**
 * This class represents a document of text to classifying purposes. 
 * Instances of this class can be classified once this class extends
 * {@link classifier.DataPoint DataPoint}.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
public class Document extends DataPoint {

	/** Number of dimensions of the vector space. */
	private static int vectorSize_;

	/** Full path of the location of this document on file system. */
	private String fullPath_;

	/** A frequency table of the words. */
	private HashMap<String, Integer> ft_;

	/** Vector of this document. */
	private float[] vector_;

	/** Indicates if this document is valid or not. */
	private boolean isValid_;


	/**
	 * Creates a new document instance.
	 *
	 * @param	file	File instance of the document.
	 * @param	wdb		Word data base.
	 */
	public Document(File file, WordDataBase wdb) {
		readDocument(file, wdb, null);
	}

	/**
	 * Creates a new document instance with dictionary support.
	 *
	 * @param	file	A file instance of this document.
	 * @param	wdb		A word data base.
	 * @param	dict	A dictionary.
	 */
	public Document(File file, WordDataBase wdb, Dictionary dict) {
		readDocument(file, wdb, dict);
	}

	/**
	 * Constructs the frequency table and updates the word data base in use. 
	 * 
	 * @param	file	A file instance of this document.
	 * @param	wdb		Word data base.
	 * @param	dict	A dictionary. If the system is not using a dictionary this is null.
	 */
	private void readDocument(File file, WordDataBase wdb, Dictionary dict) {
		try {
			fullPath_ = file.getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ft_ = new HashMap<String, Integer>();

		try {
			Parser parser = new Parser(file.getPath());
			long time = System.currentTimeMillis();

			while (parser.hasNext()) {
				String word = parser.next();
				if (ft_.containsKey(word) == false) {
					wdb.incDocumentFreqOf(word);
					ft_.put(word, 1);
				}
				else {
					ft_.put(word, ft_.get(word) + 1);
				}
				wdb.incCollectionFreqOf(word);
				if (dict != null && dict.isDecayEnabled() == true) {
					dict.updateAges(word);
				}
			}

			System.out.println("        " + fullPath_  + " readed in "
					+ (System.currentTimeMillis()-time) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the vector of the document.
	 *
	 * @param	weightScheme	Scheme used to determine the weight of each word.
	 * @param	termVocabulary	List of the words that compose the vocabulary of terms.
	 * @param	wdb				A word data base.
	 * @param	nDocuments		Number of documents readed.
	 */
	public void calculateVector(int weightScheme, ArrayList<String> termVocabulary,
			WordDataBase wdb, int nDocuments) {
		
		vectorSize_ = termVocabulary.size();
		vector_     = new float[vectorSize_];
		isValid_ 	= false;

		for (int i = 0; i < vectorSize_; i++) {
			String term = termVocabulary.get(i);
			int tf = getTermFrequencyOf(term);
			float weight = 0;

			switch (weightScheme) {
			case SaidModel.WS_TF:
				weight = tf;
				break;
			case SaidModel.WS_TFIDF:
				float idf;
				if (wdb.constainsWord(term)) {
					idf = (float) Math.log((float)nDocuments / wdb.getDocumentFreqOf(term));
				} else {
					idf = 1;
				}
				weight = tf * idf;
				break;
			}

			vector_[i] = weight;
			if (vector_[i] != 0) {
				isValid_ = true;
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see	classifier.DataPoint#getVector()
	 */
	public float[] getVector() {
		return vector_;
	}

	/**
	 * Sets the vector of this document.
	 * 
	 * @param	vector	A new vector for this document.
	 */
	public void setVector(float[] vector) {
		vector_     = vector;
		vectorSize_ = vector.length;
	}

	/**
	 * (non-Javadoc)
	 * @see	classifier.DataPoint#getLabel()
	 */
	public String getLabel() {
		int sep = fullPath_.lastIndexOf(File.separator);
		return fullPath_.substring(sep + 1);
	}

	/**
	 * Returns the name of this document in the file system.
	 *
	 * @return  Name of this document.
	 */
	public String getName() {
		int sep = fullPath_.lastIndexOf(File.separator);
		return fullPath_.substring(sep + 1);
	}

	/**
	 * Returns the path of this document in the file system.
	 *
	 * @return  Path of this document.
	 */
	public String getPath() {
		int sep = fullPath_.lastIndexOf(File.separator);
		return fullPath_.substring(0, sep);
	}

	/**
	 * Returns the frequency of a given word.
	 * 
	 * @param	word	The word to get the frequency.
	 * @return	Frequency of the word.
	 */
	public int getTermFrequencyOf(String word) {
		if (ft_.containsKey(word)) {
			return ft_.get(word);
		}
		else {
			return 0;
		}
	}

	/**
	 * Returns a list of words contained on this document.
	 * 
	 * @return	List of words contained on this document.
	 */
	public ArrayList<String> getWords() {
		return new ArrayList<String>(ft_.keySet());
	}

	/**
	 * Checks if this document is valid or not.
	 * A document is valid if contains at least one word contained on the term vocabulary.
	 * 
	 * @return	True if the document is valid. False otherwise.
	 */
	public boolean isValid() {
		return isValid_;
	}

	/**
	 * Returns the number of dimensions of vector space.
	 * This is equal to the size of the term vocabulary list.
	 *
	 * @return	Number of dimensions of the vector space.
	 */
	public static int vectorSize() {
		return vectorSize_;
	}

	/**
	 * Prints a textual representation of the frequency table of this document.
	 */
	@SuppressWarnings("unchecked")
	public void printFrequencyTable() {
		Iterator<?> itr = ft_.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Entry) itr.next();
			System.err.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	/**
	 * Returns a string with the text of this document.
	 * 
	 * @return	String with the text of this document.
	 */
	public String getText() {
		return Parser.getText(fullPath_);
	}

}
