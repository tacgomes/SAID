package classifier.cluster;

import java.util.ArrayList;

import classifier.DataPoint;

/**
 * This class represents a cluster to use with partitional algorithms like 
 * {@link classifier.Kmeans Kmeans} and {@link classifier.BisectingKmeans BisectingKmeans}
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class PartitionalCluster extends Cluster {

	/** Indicates if any data point was added or removed in the last iteration. */
	private boolean changed_;


	/**
	 * Creates a new PartitionalCluster instance given an initial centroid.
	 *
	 * @param	centroid	The initial centroid for this cluster.
	 */
	public PartitionalCluster(float[] centroid) {
		dpoints_ = new ArrayList<DataPoint>();
		changed_ = false;
		super.setCentroid(centroid.clone());
	}

	/**
	 * Creates a new instance of a partitional cluster given a first data point.
	 * 
	 * @param dp	The first data point that will become a member of this cluster.
	 */
	public PartitionalCluster(DataPoint dp) {
		dpoints_ = new ArrayList<DataPoint>();
		dpoints_.add(dp);
		changed_ = false;
		super.setCentroid(dp.getVector().clone());
	}

	/**
	 * Creates a new instance of a partitional cluster given a list of data points.
	 * 
	 * @param	dpoints		List of data points that will become members of this cluster.
	 */
	public PartitionalCluster(ArrayList<DataPoint> dpoints) {
		dpoints_ = dpoints;
		changed_ = false;
		super.calculateCentroid();
	}

	/**
	 * Recalculates the centroid if any data point was added or removed
	 * to this cluster in the last iteration of kmeans.
	 */
	public void updateCentroid() {
		if (changed_ == false || dpoints_.size() == 0) {
			return;
		}
		super.calculateCentroid();
		changed_ = false;
	}

	/**
	 * Adds a new data point to this Cluster and sets the cluster status to "changed".
	 * 
	 * @param	dp	The data point to be added.
	 */
	public void addDataPoint(DataPoint dp) {
		dpoints_.add(dp);
		changed_ = true;
	}

	/**
	 * Removes a data point of this Cluster and sets the cluster status to "changed".
	 * 
	 * @param	dp	The data point to be removed.
	 */
	public void removeDataPoint(DataPoint dp) {
		dpoints_.remove(dp);
		changed_ = true;
	}

}
