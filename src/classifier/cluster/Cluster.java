package classifier.cluster;

import java.util.ArrayList;
import java.util.Arrays;

import model.Document;
import classifier.Classifier;
import classifier.DataPoint;
import classifier.proxmeasure.EuclideanDistance;
import classifier.proxmeasure.ProximityMeasure;

/**
 * This abstract class represents a cluster. A cluster can
 * be partitional or hierarchical.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public abstract class Cluster {
	
	/** List of data points that are members of this cluster. */
	protected ArrayList<DataPoint> dpoints_;

	/** Centroid of this cluster. */
	private float[] centroid_;

	private float[][] proxMat;

	
	/**
	 * Returns the centroid of this cluster. If necessary, calculates
	 * the centroid first.
	 *
	 * @return	Centroid of this cluster.
	 */
	public float[] getCentroid() {
		if (centroid_ == null) {
			calculateCentroid();
		}
		return centroid_;
	}

	/**
	 * Calculates the centroid of this cluster.
	 */
	public void calculateCentroid() {
		int n = Document.vectorSize();
		centroid_ = new float[n];
		Arrays.fill(centroid_, 0);

		ArrayList<DataPoint> dpoints = dpoints_;
		for (DataPoint dp: dpoints) {
			float v[] = dp.getVector();
			for (int i = 0; i < n; i++) {
				centroid_[i] += v[i];
			}
		}

		for (int i = 0; i < n; i++) {
			centroid_[i] /= dpoints_.size();
		}
	}
	
	/**
	 * Sets the centroid of this cluster.
	 *
	 * @param	centroid	New centroid for this cluster.
	 */
	public void setCentroid(float[] centroid) {
		centroid_ = centroid;
	}

	/**
	 * Calculates the Sum of Squared Error(SSE) for this cluster.
	 * 
	 * @return	Sum of Squared Error.
	 */
	public float getSSE() {
		if (dpoints_.size() == 0 || dpoints_.size() == 1) {
			return 0;
		}
		float sum = 0;
		ProximityMeasure eucDist = new EuclideanDistance();
		for (DataPoint doc: dpoints_) {
			sum = sum + eucDist.getProximity(getCentroid(), doc.getVector());
		}
		return sum / dpoints_.size();
	}

	/**
	 * Returns the worst proximity of the proximities between the data points
	 * of this cluster and this cluster centroid.
	 * 
	 * @return	Worst proximity.
	 */
	public float getWorstProximity() {
		ProximityMeasure pm = Classifier.getProximityMeasure();
		float worstProx = pm.bestProximity();
		for (DataPoint dp: dpoints_) {
			float prox = pm.getProximity(dp.getVector(), getCentroid());
			if (pm.isBetter(worstProx, prox)) {
				worstProx = prox;
			}
		}
		return worstProx;
	}
	
	/**
	 * Prints the data points of this cluster.
	 */
	public void printDataPoints() {
		System.err.print("[");
		if (dpoints_.size() == 0) {
			System.err.println("]");
			return;
		}
		for (int i = 0; i < dpoints_.size() - 1; i++) {
			System.err.print(dpoints_.get(i) + ", ");
		}
		System.err.println(dpoints_.get(dpoints_.size() - 1) + "]");	
	}
	
	/** 
	 * Prints the centroid of this cluster.
	 */
	public void printCentroid() {
		System.err.print("[");
		for (int i = 0; i < centroid_.length - 1; i++) {
			System.err.print(centroid_[i] + ", ");
		}
		System.err.println(centroid_[centroid_.length - 1] + "]");	
	}
	
	/**
	 * Removes all data points of this cluster.
	 */
	public void removeAllDataPoints() {
		dpoints_  = new ArrayList<DataPoint>();
	}
	
	/**
	 * Returns a list of the data points of this cluster.
	 * 
	 * @return	List of the data points of this cluster.
	 */
	public ArrayList<DataPoint> getDataPoints() {
		return dpoints_;
	}
	
	/**
	 * Returns the number of data points of this cluster.
	 * 
	 * @return	Number of data points of this cluster.
	 */
	public int size() {
		return dpoints_.size();
	}
	
	/**
	 * Returns the proximity matrix for the data points of this cluster.
	 *
	 * @return	Proximity matrix.
	 */
	public float[][] getProximityMatrix() {
		EuclideanDistance eucDist = new classifier.proxmeasure.EuclideanDistance();
		proxMat = new float[dpoints_.size()][dpoints_.size()];
		for (int i = 0; i < dpoints_.size()-1; i++) {
			for (int j = i+1; j < dpoints_.size(); j++) {
				float prox = eucDist.getProximity(dpoints_.get(i).getVector(),
						dpoints_.get(j).getVector());
				proxMat[i][j] = prox;
				proxMat[j][i] = prox;
			}
		}
		return proxMat;
	}

	/**
	 * Returns the proximity matrix for the data points of this cluster.
	 * The proximities are converted do doubles.
	 *
	 * @return	Proximity matrix.
	 */
	public double[][] getDoubleProximityMatrix() {
		this.getProximityMatrix();
		double[][] proxMatDouble = new double[dpoints_.size()][dpoints_.size()];
		for(int i = 0; i < proxMat.length; i++) {
			for(int k = 0; k < proxMat.length; k++) {
				proxMatDouble[i][k] = proxMat[i][k];
				proxMatDouble[k][i] = proxMat[k][i];
			}
		}
		return proxMatDouble;
	}

	/**
	 * Returns a list with the labels for all data points of this cluster.
	 * 
	 * @return	List with the labels for all data points of this cluster.
	 */
	public String[] getLabelList() {
		String[] labels = new String[getDataPoints().size()];
		for (int i = 0; i < getDataPoints().size(); i++) {
			labels[i] = getDataPoints().get(i).getLabel();
		}
		return labels;
	}
}
