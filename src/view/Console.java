package view;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import model.SaidModel;
import parser.Parser;
import util.MemoryInformation;
import util.Round;
import classifier.BisectingKmeans;
import classifier.Classifier;
import classifier.DataPoint;
import classifier.Hac;
import classifier.cluster.Cluster;

public class Console {

	private SaidModel said_;

	public Console() {
		said_ = new SaidModel("consoleLog.txt");
		runCycle();
	}

	private void runCycle() {
		Scanner sc = new Scanner(System.in);
		System.err.print("command: ");
		while (true) {
			String line = sc.nextLine().trim();
			if (line.length() == 0) {
				continue;
			}
			if (line.equals("exit") || line.equals("quit")) {
				break;
			}
			processLine(line);
			System.err.print("\ncommand: ");
		}
		sc.close();
	}

	private void processLine(String line) {
		int index = line.indexOf(" ");
		String cmd;
		if (index > 0) {
			cmd = line.substring(0, index);
		}
		else {
			cmd = line;
		}

		String args[] = line.substring(index + 1).split("--");

		if (cmd.equals("do-analyse")) {
			doAnalyseCmd(args);
		}
		else if (cmd.equals("run-kmeans")) {
			runKmeansCmd(args);
		}
		else if (cmd.equals("run-bisecting-kmeans")) {
			runBisectingKmeansCmd(args);
		}
		else if (cmd.equals("run-hac")) {
			runHacCmd(args);
		}
		else if (cmd.equals("print-clusters")) {
			printClusters();
		}
		else if (cmd.equals("calculate-silhcoefs")) {
			calculateSilhCoefs();
		}
		else {
			System.err.println("error: invalid command");
			return;
		}

	}

	private void doAnalyseCmd(String args[]) {
		int max = Integer.MAX_VALUE;
		boolean mandatoryArgsPresent = false;

		for (int i = 1; i < args.length; i++) {
			String param = args[i].split("=")[0].trim();
			String value = args[i].split("=")[1].trim();

			if (param.equals("dir")) {
				mandatoryArgsPresent = true;
				said_.setDocumentsDirectory(value);
			}
			else if (param.equals("dict")) {
				said_.setDictionary(value);
			}
			else if (param.equals("decay")) {
				said_.setDecayTime(Integer.parseInt(value));
			}
			else if (param.equals("lsi")) {
				said_.setLatentSemanticIndexing(Double.parseDouble(value));
			}
			else if (param.equals("weighting")) {
				if (value.equals("tf")) {
					said_.setWeightScheme(SaidModel.WS_TF);
				}
				else if (value.equals("tfidf")) {
					said_.setWeightScheme(SaidModel.WS_TFIDF);
				}
				else {
					System.err.println("error: invalid option");
					return;
				}
			}
			else if (param.equals("max")) {
				max = Integer.parseInt(value);
			}
			else {
				System.err.println("error: invalid parameter");
				return;
			}
		}

		if (mandatoryArgsPresent == false) {
			System.err.println("error: parameter 'dir' is mandatory!");
			return;
		}

		long start = Calendar.getInstance().getTimeInMillis();

		said_.prepareNewAnalysis();
		ArrayList<File> fileList = said_.getSortedFileList();

		if (said_.isDictionaryEnabled()) {
			said_.readDictionary();
		}

		int nFiles = fileList.size();
		System.err.print("reading documents: ");
		for (int i = 0; i < nFiles; i++) {
			if (i >= max) {
				break;
			}
			if (fileList.get(i).isFile() && Parser.checkExtension(fileList.get(i).getPath())) {
				System.err.print(i + ", ");
				said_.addDocument(fileList.get(i));
			}
		}

		if (said_.isDictionaryEnabled() && said_.dictionary().isDecayEnabled()) {
			said_.dictionary().replaceDeadWords(said_.getSystemWeightTable());
		}

		said_.calculateVectors();

		if (said_.isLatentSemanticIndexingEnabled()) {
			said_.applyLatentSemanticIndexing();
		}

		long end = Calendar.getInstance().getTimeInMillis();

		System.err.println("\ntime: " + Round.getRoundedValue((float) ((end - start)/1000.0), 1)
				+ "s, memory: " + MemoryInformation.getUsedMemory());

	}
	
	private void calculateSilhCoefs() {
		long start   = Calendar.getInstance().getTimeInMillis();
		said_.classifier().getSilhouetteCoefficients();
		long end = Calendar.getInstance().getTimeInMillis();
		System.err.println("\ntime to calculate silhcoefs: " 
				+ Round.getRoundedValue((float) ((end - start)/1000.0), 1));

	}

	private void runKmeansCmd(String[] args) {
		int k = -1;
		int maxIters = -1;
		int minSize = 1;
		int maxSize = said_.getNumDocuments();

		for (int i = 1; i < args.length; i++) {
			String param = args[i].split("=")[0].trim();
			String value  = args[i].split("=")[1].trim();

			if (param.equals("k")) {
				k = Integer.parseInt(value);
			}
			else if (param.equals("prox-measure")) {
				if (value.equals("cosine")) {
					said_.classifier().setProximityMeasure(Classifier.COSINE_SIMILARITY);
				}
				else if (value.equals("euclidean")) {
					said_.classifier().setProximityMeasure(Classifier.EUCLIDEAN_DISTANCE);
				}
				else if (value.equals("manhattan")) {
					said_.classifier().setProximityMeasure(Classifier.MANHATTAN_DISTANCE);
				}
			}
			else if (param.equals("seeding-heuristic")) {
				if (value.equals("uniform")) {
					said_.classifier().kmeans().setSeedingHeuristic(1);
				}
				else if (value.equals("space-distributed")) {
					said_.classifier().kmeans().setSeedingHeuristic(2);
				}
				else if (value.equals("kmeans++")) {
					said_.classifier().kmeans().setSeedingHeuristic(3);
				}
			}
			else if (param.equals("empty-clusters-treatement")) {
				if (value.equals("just-remove")) {
					said_.classifier().kmeans().setEmptyClusterHandleMode(1);
				}
				else if (value.equals("most-dispersed")) {
					said_.classifier().kmeans().setEmptyClusterHandleMode(2);
				}
				else if (value.equals("biggest-cluster")) {
					said_.classifier().kmeans().setEmptyClusterHandleMode(3);
				}
			}
			else if (param.equals("min-size")) {
				minSize = Integer.parseInt(value);
			}
			else if (param.equals("max-size")) {
				maxSize = Integer.parseInt(value);	
			}
			else if (param.equals("max-iterations")) {
				maxIters = Integer.parseInt(value);
			}
			else {
				System.err.println("error: invalid parameter");
				return;
			}
		}

		if (k == -1) {
			System.err.println("error: parameter 'k' is mandatory");
			return;
		}

		long start = Calendar.getInstance().getTimeInMillis();

		if (maxIters == -1) {
			said_.classifier().kmeans().run(k);
		}
		else {
			said_.classifier().kmeans().run(k, maxIters);
		}
		
		if (minSize != 1 || maxSize != said_.getNumDocuments()) {
			said_.classifier().normalizeClusterSize(minSize, maxSize);
		}

		long end = Calendar.getInstance().getTimeInMillis();
		System.err.println("kmeans execution time: " + (end - start) + "ms");
	}

	private void runBisectingKmeansCmd(String[] args) {
		int splitCriterium = BisectingKmeans.SPLIT_BY_DISPERSION;
		int stopCriterium  = BisectingKmeans.STOP_BY_NUM_OF_CLUSTERS;
		int stopValue      = -1;
		int nTrials        = 1;

		for (int i = 1; i < args.length; i++) {
			String param = args[i].split("=")[0].trim();
			String value = args[i].split("=")[1].trim();

			if (param.equals("prox-measure")) {
				if (value.equals("cosine")) {
					said_.classifier().setProximityMeasure(Classifier.COSINE_SIMILARITY);
				}
				else if (value.equals("euclidean")) {
					said_.classifier().setProximityMeasure(Classifier.EUCLIDEAN_DISTANCE);
				}
				else if (value.equals("manhattan")) {
					said_.classifier().setProximityMeasure(Classifier.MANHATTAN_DISTANCE);
				}
			}
			else if (param.equals("split-criterium")) {
				if (value.equals("dispersion")) {
					splitCriterium = BisectingKmeans.SPLIT_BY_DISPERSION;
				}
				else if (value.equals("size")) {
					splitCriterium = BisectingKmeans.SPLIT_BY_SIZE;
				}
			}
			else if (param.equals("stop-criterium")) {
				if (value.equals("dispersion")) {
					stopCriterium = BisectingKmeans.STOP_BY_DISPERSION;
				}
				else if (value.equals("size")) {
					stopCriterium = BisectingKmeans.STOP_BY_SIZE;
				}
				else if (value.equals("number-clusters")) {
					stopCriterium = BisectingKmeans.STOP_BY_NUM_OF_CLUSTERS;
				}
			}
			else if (param.equals("stop-value")) {
				stopValue = Integer.parseInt(value);
			}
			else if (param.equals("trials")) {
				nTrials = Integer.parseInt(value);
			}
			else {
				System.err.println("error: invalid parameter");
				return;
			}
		}

		if (stopValue == -1) {
			System.err.println("error: parameter 'stop-value' is mandatory");
			return;
		}

		long start = Calendar.getInstance().getTimeInMillis();

		said_.classifier().bisectingKmeans().run(splitCriterium, stopCriterium, stopValue, nTrials);

		long end = Calendar.getInstance().getTimeInMillis();
		System.err.println("bisecting-kmeans execution time: " + (end - start) + "ms");
	}

	private void runHacCmd(String[] args) {
		int k = -1;
		int clusterSim = Hac.SINGLE_LINKAGE;

		for (int i = 1; i < args.length; i++) {
			String param = args[i].split("=")[0].trim();
			String value  = args[i].split("=")[1].trim();

			if (param.equals("k")) {
				k = Integer.parseInt(value);
			}
			else if (param.equals("prox-measure")) {
				if (value.equals("cosine")) {
					said_.classifier().setProximityMeasure(Classifier.COSINE_SIMILARITY);
				}
				else if (value.equals("euclidean")) {
					said_.classifier().setProximityMeasure(Classifier.EUCLIDEAN_DISTANCE);
				}
				else if (value.equals("manhattan")) {
					said_.classifier().setProximityMeasure(Classifier.MANHATTAN_DISTANCE);
				}
			}
			else if (param.equals("cluster-sim")) {
				if (value.equals("single-linkage")) {
					clusterSim = Hac.SINGLE_LINKAGE;
				}
				else if (value.equals("complete-linkage")) {
					clusterSim = Hac.COMPLETE_LINKAGE;
				}
				else if (value.equals("group-average")) {
					clusterSim = Hac.GROUP_AVERAGE;
				}
				else if (value.equals("centroid-proximity")) {
					clusterSim = Hac.CENTROID_PROXIMITY;
				}
				else if (value.equals("wards-method")) {
					clusterSim = Hac.WARDS_METHOD;
				}
			}
			else {
				System.err.println("error: invalid parameter");
				return;
			}
		}

		if (k == -1) {
			System.err.println("error: parameter 'k' is mandatory");
			return;
		}

		long start = Calendar.getInstance().getTimeInMillis();

		said_.classifier().hac().run(k, clusterSim);

		long end = Calendar.getInstance().getTimeInMillis();
		System.err.println("hac execution time: " + (end - start) + "ms");
	}

	private void printClusters() {
		int clusterNumber = 1;
		for (Cluster c: said_.classifier().getClusters()) {
			System.err.println("Cluster " + clusterNumber + " (" + c.size() + " documents):");
			ArrayList<DataPoint> docsCluster = c.getDataPoints();
			for (int i = 0; i < docsCluster.size(); i++) {
				System.err.println("    " + docsCluster.get(i).getLabel() );
			}
			System.err.println();
			clusterNumber++;
		}
		System.err.println("final k:            " + said_.classifier().getFinalK());
		System.err.println("average dispersion: " + said_.classifier().getAverageDispersion() + "\n");
	}

	public static void main(String[] args) {
		new Console();
		
		/*
		examples of commands :
		
		do-analyse --dir=/home/user/Docs
		run-kmeans --k=10
		run-bisecting-kmeans --k=10 --stop-value=10
		run-hac --k = 10 --cluster-sim=single_linkage
		print-clusters
		run-hac --k=20 --cluster-sim=single-linkage
		run-hac --k=20 --cluster-sim=complete-linkage
		run-hac --k=20 --cluster-sim=group-average
		run-hac --k=20 --cluster-sim=centroid-proximity
		run-hac --k=20 --cluster-sim=wards-method
		calculate-silhcoefs
		*/
	}

}
