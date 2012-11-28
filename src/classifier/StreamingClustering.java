package classifier;

import java.util.ArrayList;

import classifier.cluster.PartitionalCluster;
import classifier.proxmeasure.ProximityMeasure;

/**
 * Class used to run the streaming clustering algorithm.
 * 
 * In this type of clustering, the first set of documents
 * is classified using the kmeans algorithm. The following
 * sets of documents are classified incrementaly, one-by-one.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class StreamingClustering {

	/** List of data points. */
	private ArrayList<DataPoint> dpoints_;

	/** List of clusters. */
	private ArrayList<PartitionalCluster> clusters_;

	/** Proximity measure in use. */
	private ProximityMeasure proxMeasure_;

	/** Worst proximity between a data point and the centroid of its cluster. */
	private float worstProx_;

	
	/**
	 * Creates a new StreamingClustering instance.
	 * 
	 * @param	dpoints	List of data points to run the streaming clustering algorithm.
	 */
	public StreamingClustering(ArrayList<DataPoint> dpoints) {
		dpoints_ = dpoints;
	}

	/**
	 * Initiates the streaming clustering algorithm.
	 * 
	 * @param	startingK	The initial number of clusters.
	 */
	public void start(int startingK) {
		if (startingK > dpoints_.size()) {
			startingK = dpoints_.size();
		}
		
		Kmeans kmeans = new Kmeans(dpoints_, false);
		kmeans.run(startingK);
		clusters_  = kmeans.getClusters();
		
		proxMeasure_ = Classifier.getProximityMeasure();
		calculateWorstProximity();
		Classifier.setLastAgorithmUsed(Classifier.STREAMING_CLUST);
		System.out.println("\nRunning streaming clustering: startingK=" + startingK);
	}
	
	/**
	 * Calculates the worst proximity of the proximities between
	 * a data point and the centroid of its cluster.
	 */
	private void calculateWorstProximity() {
		worstProx_  = proxMeasure_.bestProximity();
		for (PartitionalCluster c: clusters_) {
			float prox = c.getWorstProximity();
			if (proxMeasure_.isBetter(worstProx_, prox)) {
				worstProx_ = prox;
			}
		}
	}
	
	/**
	 * Assigns a data point to its nearest cluster, if the proximity between the data point
	 * and the centroid of the nearest cluster is better than the worst proximity. Otherwise, 
	 * the data point is assigned to a new cluster.
	 *
	 * @param	dp	The data point to be assigned.
	 */
	public void assignDataPoint(DataPoint dp) {
		PartitionalCluster nearestCluster = null;
		float bestProx = proxMeasure_.worstProximity();
		for (PartitionalCluster c: clusters_) {
			float prox = proxMeasure_.getProximity(dp.getVector(), c.getCentroid());
			if (proxMeasure_.isBetter(prox, bestProx)) {
				bestProx = prox;
				nearestCluster = c;
			}
		}

		float prox = proxMeasure_.getProximity(dp.getVector(), nearestCluster.getCentroid());
		if (prox == worstProx_ || proxMeasure_.isBetter(prox, worstProx_)) {
			dp.setCluster(nearestCluster);
			nearestCluster.addDataPoint(dp);
			nearestCluster.updateCentroid();
			float clusterWorst = nearestCluster.getWorstProximity();
			if (proxMeasure_.isBetter(worstProx_, clusterWorst)) {
				worstProx_ = clusterWorst;
			}
		}
		else {
			PartitionalCluster newCluster = new PartitionalCluster(dp);
			dp.setCluster(newCluster);
			clusters_.add(newCluster);
			
			// if a data point of the nearest cluster of the data point to
			// assign before the data point being assigned to a new cluster,  
			// is closer to the centroid of the new cluster than the centroid
			// of its cluster, then move the data point to the new cluster
			float[] nearestCentroid = nearestCluster.getCentroid();
			float[] newestCentroid  = newCluster.getCentroid();
			ArrayList<DataPoint> nearestDataPoints = nearestCluster.getDataPoints(); 
			int index = 0;
			while (index < nearestDataPoints.size()) {
				DataPoint dpoint = nearestDataPoints.get(index);
				float prox1 = proxMeasure_.getProximity(dpoint.getVector(), nearestCentroid);
				float prox2 = proxMeasure_.getProximity(dpoint.getVector(), newestCentroid);
				if (proxMeasure_.isBetter(prox2, prox1)) {
					nearestCluster.removeDataPoint(dpoint);
					newCluster.addDataPoint(dpoint);
					dpoint.setCluster(newCluster);
					index--;
				}
				index++;
			}
			nearestCluster.updateCentroid();
			newCluster.updateCentroid();
		}
	}

	/**
	 * Returns a list of the created clusters.
	 * 
	 * @return	A list of the created clusters.
	 */
	ArrayList<PartitionalCluster> getClusters() {
		return clusters_;
	}

}
