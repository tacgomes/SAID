package classifier;

import java.util.ArrayList;

import classifier.cluster.HierarchicalCluster;
import classifier.proxmeasure.ProximityMeasure;

/**
 * Class that implements the Hierarchical Agglomerative Clustering algorithm (HAC):
 * <pre>
 * 	1. Start with all instances in their own cluster.
 * 	2. Until there is only one cluster:
 * 	3.	 Among the current clusters, determine the two
 * 		 clusters, ci and cj, that are most similar.
 * 	4. Replace ci and cj with a single cluster
 * </pre>
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class Hac {

	public static final int SINGLE_LINKAGE     = 1;
	public static final int COMPLETE_LINKAGE   = 2;
	public static final int GROUP_AVERAGE      = 3;
	public static final int CENTROID_PROXIMITY = 4;
	public static final int WARDS_METHOD       = 5;

	/** List of clusters. */
	private ArrayList<HierarchicalCluster> clusters_;

	/** List of data points. */
	private ArrayList<DataPoint> dpoints_;

	/** Proximity Measure in use. */
	private ProximityMeasure proxMeasure_;

	/** Proximity matrix between data points */
	private final float[][] docMat_;
	
	/** Proximity matrix between clusters */
	private float[][] clusterMat_;

	/** Inter-cluster similiarty method. */
	private int simMethod_;
	
	/** Needed data to make the dendrogram. */
	private String[][] data_;
	
	/** Number of iterations */
	private int nIters_;
	
	/**
	 * Creates a new HierarchicalAgglomerative instance.
	 * 
	 * @param dpoints	List of data points to run the HAC algorithm.
	 * @param proxMat	Proximity matrix of the data points.
	 */
	Hac(ArrayList<DataPoint> dpoints, float[][] proxMat) {
		dpoints_ = dpoints;
		docMat_  = proxMat;
	}

	/**
	 * Runs the HAC algorithm.
	 * 
	 * @param	k			The desired number of clusters to create.
	 * @param	simMethod	Method used to determine cluster similarity.
	 */
	public void run(int k, int simMethod) {
		System.out.println("\nRunning HAC: k=" + k + ", simMethod=" + simMethod);
		Classifier.setLastAgorithmUsed(Classifier.HAC);
		proxMeasure_ = Classifier.getProximityMeasure();
		simMethod_   = simMethod;
		
		data_ = new String[dpoints_.size()][3];
		clusters_ = new ArrayList<HierarchicalCluster>();
		for (int i = 0; i < dpoints_.size(); i++) {
			clusters_.add(new HierarchicalCluster(dpoints_.get(i)));
		}
		
		if (simMethod_ == WARDS_METHOD) {
			clusterMat_ = new float[dpoints_.size()][dpoints_.size()];
			for (int i = 0; i  < clusters_.size() - 1; i++) {
				for (int j = i + 1; j < clusters_.size(); j++) {
					HierarchicalCluster newCluster = 
						new HierarchicalCluster(dpoints_.get(i), dpoints_.get(j));	
					float sse = newCluster.getSSE();
					clusterMat_[i][j] = sse;
					clusterMat_[j][i] = sse;
				}
			}
		}
		else {
			clusterMat_ = docMat_.clone();
		}
	
		for (nIters_ = 0; nIters_ < dpoints_.size() - k; nIters_++) {
			runOneIteration();	
		}

	}
	
	/**
	 * Runs one iteration of HAC.
	 */
	public void runOneIteration() {
		runOneIteration_();
	}
	
	/**
	 * Runs one iteration of HAC.
	 */
	private void runOneIteration_() {
		int clusterIndex1 = 0;
		int clusterIndex2 = 0;
		float bestProx = proxMeasure_.worstProximity();

		for (int i = 0; i  < clusters_.size() - 1; i++) {
			if (clusters_.get(i).size() != 0) {
				for (int j = i + 1; j < clusters_.size(); j++) {
					if (clusters_.get(j).size() != 0) {
						float prox = clusterMat_[i][j];
						if (proxMeasure_.isBetter(prox, bestProx)) {
							bestProx = prox;
							clusterIndex1 = i;
							clusterIndex2 = j;
						}
					}
				}
			}
		}

		System.out.println("        merging cluster " + (clusterIndex1+1) 
				+ " with " + (clusterIndex2+1));
				
		HierarchicalCluster cluster1 = clusters_.get(clusterIndex1);
		HierarchicalCluster cluster2 = clusters_.get(clusterIndex2);
		if (simMethod_ != CENTROID_PROXIMITY) {
			cluster1.addDataPoints(cluster2.getDataPoints());
		}
		else {
			cluster1.addDataPoints(cluster2.getDataPoints(), cluster2.getCentroid());
		}
		cluster2.removeAllDataPoints();
		
		data_[nIters_][0] = "" + (clusterIndex1 + 1);
		data_[nIters_][1] = "" + (clusterIndex2 + 1);
		data_[nIters_][2] = "" + bestProx;
			
		// update the proximity matrix
		for (int index = 0; index < clusters_.size(); index++) {
			if (index != clusterIndex1 && clusters_.get(index).size() != 0) {
				float prox = 0;
					switch (simMethod_) {
					case SINGLE_LINKAGE:
						prox = getSingleLinkageProximity(clusters_.get(index), cluster1);
						break;
					case COMPLETE_LINKAGE:
						prox = getCompleteLinkageProximity(clusters_.get(index), cluster1);
						break;
					case GROUP_AVERAGE:
						prox = getGroupAverageProximity(clusters_.get(index), cluster1);
						break;
					case CENTROID_PROXIMITY:
						prox = getCentroidProximity(clusters_.get(index), cluster1);
						break;
					case WARDS_METHOD:
						HierarchicalCluster newCluster = 
							new HierarchicalCluster(clusters_.get(index), cluster1);
						prox = newCluster.getSSE();
						break;
					}
				
				clusterMat_[clusterIndex1][index] = prox;
				clusterMat_[index][clusterIndex1] = prox;
			}
		}	
	}
	
	/**
	 * Returns the single linkage proximity between two given clusters.
	 * 
	 * @param	c1	The first cluster.
	 * @param	c2	The second cluster.
	 * 
	 * @return	The single linkage proximity.
	 */
	private float getSingleLinkageProximity(HierarchicalCluster c1, HierarchicalCluster c2) {
		float bestProx = proxMeasure_.worstProximity();
		for (DataPoint dp1: c1.getDataPoints()) {
			for (DataPoint dp2: c2.getDataPoints()) {
				int pos1 = dp1.getIndex();
				int pos2 = dp2.getIndex();
				float prox = docMat_[pos1][pos2];
				if (proxMeasure_.isBetter(prox, bestProx)) {
					bestProx = prox;
				}
			}
		}
		return bestProx;
	}

	/**
	 * Returns the complete linkage proximity between two given clusters.
	 * 
	 * @param	c1	The first cluster.
	 * @param	c2	The second cluster.
	 * 
	 * @return	The complete linkage proximity.
	 */
	private float getCompleteLinkageProximity(HierarchicalCluster c1, HierarchicalCluster c2) {
		float worstProx = proxMeasure_.bestProximity();
		for (DataPoint dp1: c1.getDataPoints()) {
			for (DataPoint dp2: c2.getDataPoints()) {
				int pos1 = dp1.getIndex();
				int pos2 = dp2.getIndex();
				float prox = docMat_[pos1][pos2];
				if (proxMeasure_.isBetter(prox, worstProx) == false) {
					worstProx = prox;
				}
			}
		}
		return worstProx;
	}

	/**
	 * Returns the group average proximity between two given clusters.
	 * 
	 * @param	c1	The first cluster.
	 * @param	c2	The second cluster.
	 * 
	 * @return	The group average proximity.
	 */
	private float getGroupAverageProximity(HierarchicalCluster c1, HierarchicalCluster c2) {
		float sumProximities = 0;
		for (DataPoint dp1: c1.getDataPoints()) {
			for (DataPoint dp2: c2.getDataPoints()) {
				int pos1 = dp1.getIndex();
				int pos2 = dp2.getIndex();
				sumProximities = docMat_[pos1][pos2];
			}
		}
		return sumProximities / (c1.size() * c2.size());
	}

	/**
	 * Returns the proximity between the centroids of two given clusters.
	 * 
	 * @param	c1	The first cluster.
	 * @param	c2	The second cluster.
	 * 
	 * @return	The proximity between the centroids.
	 */
	private float getCentroidProximity(HierarchicalCluster c1, HierarchicalCluster c2) {
		float prox = proxMeasure_.getProximity(c1.getCentroid(), c2.getCentroid());
		return prox;
	}
	
	/**
	 * Returns the necessary data to make the dendrogram.
	 * 
	 * @return	Necessary data to make the dendrogram.
	 */
	public String[][] getDendrogramData() {
		return data_;
	}

	/**
	 * Return a list of the created clusters.
	 * 
	 * @return	List of the created clusters.
	 */
	ArrayList<HierarchicalCluster> getClusters() {
		ArrayList<HierarchicalCluster> clusters = new ArrayList<HierarchicalCluster>();
		// don't return clusters that were merged with some cluster
		for (HierarchicalCluster c: clusters_) {
			if (c.size() != 0) {
				clusters.add(c);
			}	
		}	
		return clusters;
	}

}
