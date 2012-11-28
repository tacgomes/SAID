package classifier.cluster;

import java.util.ArrayList;

import classifier.DataPoint;

/**
 * This class represents a cluster to use with hierarchical algorithms such as
 * {@link classifier.Hac Hac}.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class HierarchicalCluster extends Cluster {
	
	/**
	 * Creates a new HierarchicalCluster instance.
	 * 
	 * @param	dp	The first data point that will become a member of this cluster.
	 */
	public HierarchicalCluster(DataPoint dp) {
		dpoints_ = new ArrayList<DataPoint>();
		dpoints_.add(dp);
	}
	
	/**
	 * Creates a new HierarchicalCluster instance given two initial data points.
	 * 
	 * @param	dp1	The first data point that will become a member of this cluster.
	 * @param	dp2	The second data point that will become a member of this cluster.
	 */
	public HierarchicalCluster(DataPoint dp1, DataPoint dp2) {
		dpoints_ = new ArrayList<DataPoint>();
		dpoints_.add(dp1);
		dpoints_.add(dp2);
	}
	
	/**
	 * Creates a new HierarchicalCluster instance with the data points
	 * of two given clusters.
	 * 
	 * @param	c1	The first cluster.
	 * @param	c2	The second cluster.
	 */
	public HierarchicalCluster(HierarchicalCluster c1, HierarchicalCluster c2) {
		dpoints_ = new ArrayList<DataPoint>();
		for (DataPoint dp: c1.getDataPoints()) {
			dpoints_.add(dp);
		}
		for (DataPoint dp: c2.getDataPoints()) {
			dpoints_.add(dp);
		}
		float[] newCentroid = getCentroidOfPoints(c1.getCentroid(), c2.getCentroid());
		super.setCentroid(newCentroid);
	}
	
	/**
	 * Adds more data points to this cluster.
	 * 
	 * @param	dpoints	A list of data points to add.
	 */
	public void addDataPoints(ArrayList<DataPoint> dpoints) {
		for (DataPoint dp: dpoints) {
			dpoints_.add(dp);
		}
	}
	
	/**
	 * Adds more data points to this cluster and calculates the centroid.
	 * 
	 * @param	dpoints		A list of data points to add.
	 * @param	centroid	The centroid of the given data points.
	 */
	public void addDataPoints(ArrayList<DataPoint> dpoints, float[] centroid) {
		float[] newCentroid = getCentroidOfPoints(super.getCentroid(), centroid);
		super.setCentroid(newCentroid);
		for (DataPoint dp: dpoints) {
			dpoints_.add(dp);
		}
	}
	
	/**
	 * Calculates the centroid of two given multidimensional points.
	 * 
	 * @param	p1	The first multidimensional point
	 * @param	p2	The second multidimensional point
	 * 
	 * @return	The centroid of the two points.
	 */
	private float[] getCentroidOfPoints(float[] p1, float[] p2) {
		float centroid[] = new float[p1.length];
		for (int i = 0; i < p1.length; i++) {
			centroid[i] = (p1[i] + p2[i]) / 2;
		}
		return centroid;
	}
	
}
