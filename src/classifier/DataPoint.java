package classifier;

import classifier.cluster.PartitionalCluster;

/**
 * This is an abstract class that represents an object that can be clustered.
 * An object to be clustered just have to implement the
 * methods  {@link #getVector() getVector} and {@link #getLabel() getLabel}.
 * 
 * @author  Tiago Gomes
 * @version 1.0
 */
public abstract class DataPoint {

	/** Cluster of this data point */
	private PartitionalCluster cluster_;

	/** Sum of the coordinates of this data point */
	private float sum_;

	/** Squared sum of the coordinates of this data point */
	private float sqSum_;

	/** Index where this data point is located in the data point list. */
	private int index_;

	/**
	 * Creates a new intance of this class.
	 */
	public DataPoint() {
	
	}

	/**
	 * Returns the index where this data point is located in the data point list.
	 * 
	 * @return	Index where this data point is located.
	 */
	public int getIndex() {
		return index_;
	}

	/**
	 * Sets the index where this data point is located in the data point list.
	 * 
	 * @param	index	Index where this data point is located in the data point list.
	 */
	public void setIndex(int index) {
		index_ = index;
	}

	/**
	 * Returns the vector of this datapoint.
	 *
	 * @return	Vector of this data point.
	 */
	public abstract float[] getVector();

	/**
	 * Sets the vector of this data point.
	 * 
	 * @param	vector	The new vector for this data point.
	 */
	public abstract void setVector(float[] vector);

	/**
	 * Returns a unique string identifying this data point.
	 *
	 * @return	Unique string identifying this data point.
	 */
	public abstract String getLabel();

	/**
	 * Returns the cluster of this data point.
	 * 
	 * @return	Cluster of this data point.
	 */
	PartitionalCluster getCluster() {
		return cluster_;
	}

	/**
	 * Sets the cluster of this data point
	 * 
	 * @param	cluster	The new cluster for this data point.
	 */
	void setCluster(PartitionalCluster cluster) {
		cluster_ = cluster;
	}

	/**
	 * Returns the sum of the coordinates of this data point.
	 * 
	 * @return	Sum of the coordinates of this data point.
	 */
	float getSum() {
		return sum_;
	}

	/**
	 * Returns the squared sum of the coordinates of this data point.
	 * 
	 * @return	Squared sum of the coordinates of this data point.
	 */
	float getSqSum() {
		return sqSum_;
	}

	/**
	 * Sets the sum and squared sum of the coordinates of this data point.
	 * 
	 * @param	sum		Sum of the coordinates of this data point.
	 * @param	sqSum	Squared sum of the coordinates of this data point.
	 */
	void setValues(float sum, float sqSum) {
		sum_   = sum;
		sqSum_ = sqSum;
	}

	/**
	 * Returns a for label for this data point
	 * 
	 * @return	A label for this data point.
	 */
	public String toString() {
		return getLabel();
	}
	
	/**
	 * Prints a textual representation of the vector of this data point.
	 */
	public void printVector() {
		float[] vector = getVector(); 
		System.err.print("[");
		for (int i = 0; i < vector.length - 1; i++) {
			System.err.print(vector[i] + ", ");
		}
		System.err.println(vector[vector.length - 1] + "]");
	}

}
