package classifier;

import java.util.ArrayList;

import classifier.cluster.PartitionalCluster;

/**
 * Class that implements the bisecting k-means algorithm:
 * <pre>
 * 	1. For I=1 to k-1 do
 * 	2.   Pick a leaf cluster C to split
 * 	3.   For nt=1 to nTrials do
 * 	4.   	Use K-means to split C into two sub-clusters, c1 and c2
 * 	5.   Choose the best of the above splits and make it permanent
 * </pre>
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class BisectingKmeans {

	// split criteriums
	public static final int SPLIT_BY_DISPERSION     = 1;
	public static final int SPLIT_BY_SIZE 		    = 2;

	// stop criteriums
	public static final int STOP_BY_DISPERSION 		= 1;
	public static final int STOP_BY_SIZE	   		= 2;
	public static final int STOP_BY_NUM_OF_CLUSTERS = 3;

	/** List of data points. */
	private ArrayList<DataPoint> dpoints_;

	/** List of clusters. */
	private ArrayList<PartitionalCluster> clusters_;

	/** Criterium used to know wich cluster will be splited next. */
	private int splitCriterium_;

	/** Number of trials to choose the best split. */
	private int nTrials_;


	/**
	 * Creates a new BisectingKmeans instance.
	 * 
	 * @param dpoints	The list of datapoints to run the bisecting kmeans algorithm.
	 */
	BisectingKmeans(ArrayList<DataPoint> dpoints) {
		dpoints_ = dpoints;
	}

	/**
	 * Runs the bisecting kmeans algorithm.
	 * 
	 * @param	splitCriterium	Criterium used to know wich cluster will be splited next.
	 * @param	stopCriterium	Criterium used to know when stop splitting.
	 * @param	stopValue		The meaning of this value depends on the stopCriterium.
	 * @param	nBsKmTrials		Number of trials to choose the best split.
	 */
	public void run(int splitCriterium, int stopCriterium, int stopValue, int nBsKmTrials) {
		System.out.println("\nRunning Bisecting Kmeans: split criterium=" + splitCriterium
				+ ", stop criterium=" + stopCriterium + ", stop value=" + stopValue + ", #trials=" + nBsKmTrials);

		splitCriterium_ = splitCriterium;
		nTrials_ 	    = nBsKmTrials;
		clusters_ 		= new ArrayList<PartitionalCluster>();

		Kmeans kmeans;
		PartitionalCluster c1 = null;
		PartitionalCluster c2 = null;

		float minSSE = Float.MAX_VALUE;

		for (int nt = 0; nt < nTrials_; nt++) {
			kmeans = new Kmeans(dpoints_, false);
			kmeans.run(2);
			ArrayList<PartitionalCluster> newClusters = kmeans.getClusters();
			float c1SSE = newClusters.get(0).getSSE();
			float c2SSE = newClusters.get(1).getSSE();
			if (c1SSE + c2SSE < minSSE) {
				minSSE = c1SSE + c2SSE;
				c1 = newClusters.get(0);
				c2 = newClusters.get(1);
			}
		}
		clusters_.add(c1);
		clusters_.add(c2);


		switch(stopCriterium) {
		// stop when each cluster dispersion is less than stopValue
		case STOP_BY_DISPERSION:
			while (true) {
				PartitionalCluster c =  Classifier.getClusterWithBiggestSSE(clusters_);;
				if (c.getSSE() < stopValue) {
					break;
				}
				runOneBisectingKmeansIteration();
			}
			break;

			// stop when each cluster size is less than stopValue
		case STOP_BY_SIZE:
			while (true) {
				PartitionalCluster c = Classifier.getClusterWithBiggestSize(clusters_);
				if (c.size() < stopValue) {
					break;
				}
				runOneBisectingKmeansIteration();
			}
			break;

			// stop when we have stopValue clusters
		case STOP_BY_NUM_OF_CLUSTERS:
			for (int i = 2; i < stopValue; i++) {
				runOneBisectingKmeansIteration();
			}
			break;
		}

		Classifier.setLastAgorithmUsed(Classifier.BS_KMEANS);
	}
	
	public void runOneBisectingKmeansIteration() {
		runOneBisectingKmeansIteration_();
		Classifier.setLastAgorithmUsed(Classifier.BS_KMEANS);
	}

	/**
	 * Runs one iteration of bisecting kmeans.
	 */
	private void runOneBisectingKmeansIteration_() {
		PartitionalCluster c1 = null;
		PartitionalCluster c2 = null;
		PartitionalCluster worstCluster;
		if (splitCriterium_ == SPLIT_BY_DISPERSION) {
			worstCluster = Classifier.getClusterWithBiggestSSE(clusters_);
		}
		else {
			worstCluster = Classifier.getClusterWithBiggestSize(clusters_);
		}

		Kmeans kmeans;
		float minSSE = Float.MAX_VALUE;
		for (int nt = 0; nt < nTrials_; nt++) {
			kmeans = new Kmeans(worstCluster.getDataPoints(), false);
			kmeans.run(2);
			ArrayList<PartitionalCluster> newClusters = kmeans.getClusters();
			float c1SSE = newClusters.get(0).getSSE();
			float c2SSE = newClusters.get(1).getSSE();
			if (c1SSE + c2SSE < minSSE) {
				minSSE = c1SSE + c2SSE;
				c1 = newClusters.get(0);
				c2 = newClusters.get(1);
			}
		}

		System.out.println("        splitting cluster " + (clusters_.indexOf(worstCluster)+1) + " in two");
		clusters_.remove(worstCluster);
		clusters_.add(c1);
		clusters_.add(c2);
	}

	/**
	 * Return a list of the created clusters.
	 * 
	 * @return	List of the created clusters.
	 */
	ArrayList<PartitionalCluster> getClusters() {
		return clusters_;
	}

}
