package classifier;

import java.util.ArrayList;

import classifier.cluster.Cluster;
import classifier.cluster.PartitionalCluster;
import classifier.proxmeasure.CosineSimilarity;
import classifier.proxmeasure.EuclideanDistance;
import classifier.proxmeasure.ManhattanDistance;
import classifier.proxmeasure.ProximityMeasure;

/**
 * This class acts as a proxy to access functions provided by the classes
 * Kmeans, Hac, BisectingKmeans and StreamingClustering. It contains also
 * some methods to perform cluster related operations.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
public class Classifier {

	// types of clustering algorithms
	public static final int KMEANS    		= 1;
	public static final int BS_KMEANS 		= 2;
	public static final int HAC 	  		= 3;
	public static final int STREAMING_CLUST = 4;

	// types of proximity measures
	public static final int COSINE_SIMILARITY  = 1;
	public static final int EUCLIDEAN_DISTANCE = 2;
	public static final int MANHATTAN_DISTANCE = 3;
	
	/** Maximum number of iterations to try to normalize the size of the clusters. */
	private static final int MAX_NORM_ITERS_ = 30;

	/** Clustering algorithm that was used last time. */
	private static int lastAlgorithmUsed_;

	/** Proximity measure in use. */
	private static ProximityMeasure proxMeasure_;

	/** List of data points. */
	private ArrayList<DataPoint> dpoints_;
	
	/** Proximity matrix for all data points. */
	private static float[][] proxMat_;

	/** Kmeans clustering instance. */
	private Kmeans kmeans_;

	/** Bisecting kmeans clustering instance. */
	private BisectingKmeans bsKmeans_;

	/** Hierarchical agglomerative clustering instance. */
	private Hac hac_;

	/** Streaming clustering instance.*/
	private StreamingClustering sc_;

	/** Indicates if all clusters were successful normalized or not. */
	private boolean successfulNormalized_;

	/** List of clusters. */
	private ArrayList<PartitionalCluster> clusters_;

	/** Indicates if the sum and squared sum were already pre-processed. */
	private static boolean preprocessed_;


	/**
	 * Creates a new Classifier instance.
	 */
	public Classifier() {
		dpoints_     = new ArrayList<DataPoint>();
		proxMeasure_ = new CosineSimilarity();
	}

	/**
	 * Creates a new Classifier instance with an initial list of data points.
	 *
	 * @param	dpoints	The list of data points to perform clustering operations.
	 */
	public Classifier(ArrayList<DataPoint> dpoints) {
		dpoints_ 	 = dpoints;
		proxMeasure_ = new CosineSimilarity();
	}
	
	/**
	 * Adds a data point to the list of data points.
	 * 
	 * @param	dp	The data point to add.
	 */
	public void addDataPoint(DataPoint dp) {
		dpoints_.add(dp);
		proxMat_ = null;
	}
	
	/**
	 * Adds data points to the list of data points.
	 * 
	 * @param	dpoints	The data points to add.
	 */
	public void setDataPoints(ArrayList<DataPoint> dpoints) {
		dpoints_ = dpoints;
		proxMat_ = null;
	}

	/**
	 * Provides access to kmeans related operations.
	 * 
	 * @return	An instance of Kmeans.
	 */
	public Kmeans kmeans() {
		if (kmeans_ == null) {
			kmeans_ = new Kmeans(dpoints_);
		}
		return kmeans_;
	}

	/**
	 * Provides access to bisecting kmeans related operations.
	 * 
	 * @return	An instance of BisectingKmeans.
	 */
	public BisectingKmeans bisectingKmeans() {
		if (bsKmeans_ == null) {
			bsKmeans_ = new BisectingKmeans(dpoints_);
		}
		return bsKmeans_;
	}

	/**
	 * Provides access to hierarchical agglomerative clustering related operations.
	 * 
	 * @return	An instance of Hac.
	 */
	public Hac hac() {
		if (hac_ == null) {
			hac_ = new Hac(dpoints_, getProximityMatrix());
		}
		return hac_;
	}

	/**
	 * Provides access to streaming clustering related operations.
	 * 
	 * @return	An instance of HierarchicalAgglomerative.
	 */
	public StreamingClustering streamingClustering() {
		if (sc_ == null) {
			sc_ = new StreamingClustering(dpoints_);
		}
		return sc_;
	}

	/**
	 * Normalizes the size of the clusters.
	 * 
	 * @param	minSize	Minimum number of data points per cluster.
	 * @param	maxSize	Maximum number of data points per cluster.
	 */
	public void normalizeClusterSize(int minSize, int maxSize) {
		System.out.println("\nNormalizing clusters: min size=" + minSize 
				+ ", max size=" + maxSize);

		if (lastAlgorithmUsed_ == KMEANS) {
			clusters_ = kmeans_.getClusters();
		}
		else if (lastAlgorithmUsed_ == BS_KMEANS) {
			clusters_ = bsKmeans_.getClusters();
		}

		int nNormIters = 0;
		do {
			// we cannot use foreach because the list of
			// clusters will be modified on the fly
			int index = 0;
			while (index < clusters_.size()) {
				PartitionalCluster cluster = clusters_.get(index);
				if (minSize != 1 && cluster.size() < minSize) {
					mergeCluster(cluster, maxSize);
				}
				else if (maxSize != 0 && cluster.size() > maxSize) {
					splitCluster(cluster);
				}
				index++;
			}

			nNormIters++;

			if (isNormalized(minSize, maxSize)) {
				successfulNormalized_ = true;
				System.out.println("        Normalization ended in " + nNormIters + " iterations");
				return;
			}

		} while (nNormIters < MAX_NORM_ITERS_);

		successfulNormalized_ = false;
		System.out.println("        Normalization failed: it was not possible to normalize all clusters\n");
	}

	/**
	 * Checks the size of the clusters.
	 * 
	 * @param	minSize	Minimum number of data points per cluster.
	 * @param	maxSize	Maximum number of data points per cluster.
	 * 
	 * @return	True if the size of all clusters respect the size criterion. False otherwise.
	 */
	private boolean isNormalized(int minSize, int maxSize) {
		boolean normalized = true;
		for (PartitionalCluster c: clusters_) {
			if (minSize != 0 && c.size() < minSize
			||  maxSize != 0 && c.size() > maxSize) {
				normalized = false;
			}
		}
		return normalized;
	}

	/**
	 * Splits a given cluster into two smallest clusters. A cluster is splitted in
	 * two by running kmeans with k=2 over the data points of the cluster.
	 * 
	 * @param	c	The cluster to split in two.
	 */
	private void splitCluster(PartitionalCluster c) {
		clusters_.remove(c);
		Kmeans kmeans = new Kmeans(c.getDataPoints(), false);
		kmeans.run(2);
		clusters_.add(kmeans.getClusters().get(0));
		clusters_.add(kmeans.getClusters().get(1));
	}

	/**
	 * Tries to merge a given cluster with the nearest cluster. 
	 *  
	 * @param 	c		Cluster to merge.
	 * @param	maxSize	Maximum number of data points per cluster.
	 * 
	 * @return	True if this cluster was successful merged with another cluster. False otherwise.
	 */
	private boolean mergeCluster(PartitionalCluster c, int maxSize) {
		// remove from the system the cluster to normalize
		clusters_.remove(c);

		// if the cluster has no members, nothing happens
		if (c.size() == 0) {
			return true;
		}

		if (maxSize == 0) {
			maxSize = Integer.MAX_VALUE;
		}

		// looks up for the closest cluster from the cluster to normalize
		// the total number of data points of the merged cluster must not exceed maxSize
		PartitionalCluster bestCandidate = null;
		float bestProx = proxMeasure_.worstProximity();
		for (PartitionalCluster candidate: clusters_) {
			float prox = proxMeasure_.getProximity(c.getCentroid(), candidate.getCentroid());
			if (proxMeasure_.isBetter(prox, bestProx)
					&& c.size() + candidate.size() <= maxSize) {
				bestProx = prox;
				bestCandidate = candidate;
			}
		}

		if (bestCandidate != null) {
			// it was found a cluster that satisfy the conditions
			for (DataPoint dp : c.getDataPoints()) {
				dp.setCluster(bestCandidate);
				bestCandidate.addDataPoint(dp);
			}
			bestCandidate.updateCentroid();
			return true;
		} else {
			// no cluster was found that satisfied the conditions
			// put the cluster back to the system
			//System.out.println("YOU SHOULD NOT BE HERE");
			clusters_.add(c);
			return false;
		}
	}

	/**
	 * Checks if all clusters were normalized successful.
	 * 
	 * @return	True if all clusters were successful normalized. False otherwise.
	 */
	public boolean wasSuccessfulNormalized() {
		return successfulNormalized_;
	}

	/**
	 * Returns the cluster with the highest sum of squared error from a given cluster list.
	 * 
	 * @param	clusters	The cluster list.
	 * 
	 * @return	Cluster with the highest sum of squared error.
	 */
	static PartitionalCluster getClusterWithBiggestSSE(ArrayList<PartitionalCluster> clusters) {
		PartitionalCluster biggestC = null;
		float maxDispersion = -1;
		for (PartitionalCluster c: clusters) {
			float disp = c.getSSE();
			if (disp > maxDispersion) {
				maxDispersion = disp;
				biggestC = c;
			}
		}

		return biggestC;
	}

	/**
	 * Returns the cluster with highest number of data points from a given cluster list.
	 * 
	 * @param	clusters	The cluster list.
	 * 
	 * @return	Cluster with highest number of data points.
	 */
	static PartitionalCluster getClusterWithBiggestSize(ArrayList<PartitionalCluster> clusters) {
		PartitionalCluster biggestC = null;
		int maxSize = -1;
		for (PartitionalCluster c: clusters) {
			int size = c.size();
			if (size > maxSize) {
				maxSize = size;
				biggestC = c;
			}
		}

		return biggestC;
	}

	/**
	 * Calculates the silhouette Coefficients for all possible K.
	 * Uses the method described in "An Efficient Approach for
	 * Computing Silhouette Coefficients" by Moh'd Belal Al- Zoubi
	 * and Mohammad al Rawi for fast calculate the coefficient.
	 * 
	 * @return	An array containing the silhouette coefficientes.
	 */
	public float[] getSilhouetteCoefficients() {
		// only executes the pre processed step one time
		if (preprocessed_ == false) {
			doPreprocessingStep();
			preprocessed_ = true;
		}

		float silhCoefs[] = new float[dpoints_.size() - 2];
		ProximityMeasure savedProxMeasure = proxMeasure_;
		proxMeasure_ = new EuclideanDistance();
		Kmeans kmeans = new Kmeans(dpoints_, false);

		for (int k = 2; k < dpoints_.size(); k++) {
			kmeans.run(k);
			silhCoefs[k-2] = calcSilhouetteCoefficient(kmeans.getClusters());
		}

		proxMeasure_ = savedProxMeasure;
		return silhCoefs;
	}

	/**
	 * Pre-processing step for speed up the calculation of silhouette coefficients.
	 */
	private void doPreprocessingStep() {
		for (DataPoint dp: dpoints_) {
			float sum = 0;
			float sqSum = 0;
			float v[] = dp.getVector();
			for (int i = 0; i < v.length; i++) {
				sum = sum + v[i];
				sqSum = sqSum + v[i] * v[i];
			}
			dp.setValues(sum, sqSum);
		}
	}

	/**
	 * Calculates the silhouette coefficient for a given list of clusters.
	 * 
	 * @param	clusters	The list of clusters.
	 * 
	 * @return	Silhouette coefficiente.
	 */
	private float calcSilhouetteCoefficient(ArrayList<PartitionalCluster> clusters) {
		float silhouetteCoeff = 0;
		//for each data point
		for (DataPoint dp: dpoints_) {
			float dpAvgDist  = calcAverageDistance(dp, dp.getCluster());
			float minAvgDist = Float.MAX_VALUE;

			for (PartitionalCluster c: clusters) {
				if (dp.getCluster() != c) {
					float currAvgDist = calcAverageDistance(dp, c);
					if (currAvgDist < minAvgDist) {
						minAvgDist = currAvgDist;
					}
				}
			}

			float max = dpAvgDist > minAvgDist ? dpAvgDist : minAvgDist;
			silhouetteCoeff = silhouetteCoeff + (minAvgDist - dpAvgDist) / max;
		}

		return silhouetteCoeff / dpoints_.size();
	}

	/**
	 * Returns the average distance between a given data point and the
	 * data points of a given cluster.
	 * 
	 * @param	dp	The data point.
	 * @param	c	The cluster.
	 * 
	 * @return 	Average distance.
	 */
	private float calcAverageDistance(DataPoint dp, PartitionalCluster c) {
		if (dp.getCluster() == c && c.size() == 1) {
			return 0;
		}

		float avgDist = 0;
		ArrayList<DataPoint> clusterDataPoints = c.getDataPoints();

		for (DataPoint cdp: clusterDataPoints) {
			if (dp != cdp) {
				avgDist = avgDist + dp.getSqSum();
				avgDist = avgDist + 2 * dp.getSum() * cdp.getSum();
				avgDist = avgDist + cdp.getSqSum();
			}
		}

		int size = dp.getCluster() == c ? c.size() - 1 : c.size();
		return avgDist / size;
	}

	/**
	 * Returns the proximity matrix for all data points.
	 * 
	 * @return	Proximity matrix.
	 */
	public float[][] getProximityMatrix() {
		if (proxMat_ == null) {
			calcProximityMatrix();
		}
		return proxMat_;
	}

	/**
	 * Calculates the proximity matrix for all data points
	 */
	private void calcProximityMatrix() {
		proxMat_ = new float[dpoints_.size()][dpoints_.size()];
		for (int i = 0; i < dpoints_.size() - 1; i++) {
			for (int j = i + 1; j < dpoints_.size(); j++) {
				if (i == j) {
					proxMat_[i][j] = 0;
				}
				else {
					float prox = proxMeasure_.getProximity(
							dpoints_.get(i).getVector(), dpoints_.get(j).getVector());
					proxMat_[i][j] = prox;
					proxMat_[j][i] = prox;
				}
			}
		}
	}
	
	/**
	 * Returns the proximity matrix for all data points.
	 * The proximities are converted do doubles.
	 * 
	 * @return	Proximity matrix.
	 */
	public double[][] getDoubleProximityMatrix() {
		double[][] proxDoubleMat_ = new double[dpoints_.size()][dpoints_.size()];
		for (int i = 0; i < dpoints_.size(); i++) {
			for (int j = 0; j < dpoints_.size(); j++) {
				if (i == j) {
					proxDoubleMat_[i][j] = 0;
				}
				else {
					proxDoubleMat_[i][j] = proxMeasure_.getProximity(
							dpoints_.get(i).getVector(), dpoints_.get(j).getVector());
				}
			}
		}
		return proxDoubleMat_;
	}
	
	/**
	 * Returns the number of data points.
	 * 
	 * @return	Number of data points
	 */
	public int getNumDataPoints() {
		return dpoints_.size();
	}

	/**
	 * Returns a list with the labels of all data points.
	 * 
	 * @return	List with the labels of all data points.
	 */	
	public String[] getLabelList() {
		String[] labels = new String[dpoints_.size()];
		for (int i = 0; i < dpoints_.size(); i++) {
			labels[i] = dpoints_.get(i).getLabel();
		}
		return labels;
	}	

	/**
	 * Returns a list of the created clusters by the execution of the last clustering algorithm.
	 * 
	 * @return	List of created clusters.
	 */
	public ArrayList<Cluster> getClusters() {
		switch (lastAlgorithmUsed_) {
			case KMEANS:
				return new ArrayList<Cluster>(kmeans_.getClusters());
			case BS_KMEANS:
				return new ArrayList<Cluster>(bsKmeans_.getClusters());
			case HAC:
				return new ArrayList<Cluster>(hac_.getClusters());
			case STREAMING_CLUST:
				return new ArrayList<Cluster>(sc_.getClusters());
		}
		return null;
	}

	/**
	 * Returns the number of clusters (K).
	 * 
	 * @return	Number of clusters.
	 */
	public int getFinalK() {
		return getClusters().size();
	}
	
	/**
	 * Returns the average dispersion of the clusters created by the 
	 * execution of the last clustering algorithm.
	 * 
	 * @return	Average dispersion.
	 */
	public float getAverageDispersion() {
		float sum = 0;
		for (Cluster c: getClusters()) {
			sum += c.getSSE();
		}
		return sum / getFinalK();
	}

	/**
	 * Sets which clustering algorithm that was used last time.
	 * 
	 * @param	lastAlgorithmUsed	The last algorithm used.
	 */
	static void setLastAgorithmUsed(int lastAlgorithmUsed) {
		lastAlgorithmUsed_ = lastAlgorithmUsed;
	}

	/**
	 * Returns the clustering algorithm that was used last time.
	 * 
	 * @return	Last algorithm used.
	 */
	public int getLastAlgorithmUsed() {
		return lastAlgorithmUsed_;
	}
	
	/**
	 * Returns the proximity measure currently in use.
	 * 
	 * @return	Proximity measure.
	 */
	public static ProximityMeasure getProximityMeasure() {
		return proxMeasure_;
	}
	
	/**
	 * Sets the proximity measure to use.
	 * 
	 * @param	proxMeasure	The proximity measure.
	 */
	public void setProximityMeasure(ProximityMeasure proxMeasure) {
		proxMeasure_ = proxMeasure;
		proxMat_     = null;
		hac_         = null;
	}

	/**
	 * Sets the proximity measure to use.
	 * 
	 * @param	proxMeasure	The proximity measure.
	 */
	public void setProximityMeasure(int proxMeasure) {
		switch (proxMeasure) {
			case COSINE_SIMILARITY:  proxMeasure_ = new CosineSimilarity();  break;
			case EUCLIDEAN_DISTANCE: proxMeasure_ = new EuclideanDistance(); break;
			case MANHATTAN_DISTANCE: proxMeasure_ = new ManhattanDistance(); break;
		}
		proxMat_ = null;
		hac_     = null;
	}
	
}
