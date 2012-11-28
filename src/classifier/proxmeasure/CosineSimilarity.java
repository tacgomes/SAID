package classifier.proxmeasure;

/**
 * This class implements the {@link classifier.proxmeasure.ProximityMeasure ProximityMeasure}
 * interface for Cosine Similarity.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class CosineSimilarity implements ProximityMeasure {

	/**
	 * Returns the maximum similarity between two multidimensional points.
	 * 
	 * @return	Maximum similarity between two multidimensional points.
	 */
	public float bestProximity() {
		return 1;
	}

	/**
	 * Returns the minimum similarity between two multidimensional points.
	 * 
	 * @return	Minimum similarity between two multidimensional points.
	 */
	public float worstProximity() {
		return -1.0F;
	}

	/**
	 * Returns true if the first argument is more similar than the second argument.
	 * 
	 * @param	x	Array representing the first multidimensional point.
	 * @param	y	Array representing the second multidimensional point.
	 * 
	 * @return	True if and only if x is more similar than y.
	 */
	public boolean isBetter(float x, float y) {
		return x > y ? true : false;
	}

	/**
	 * Computes the cosine similarity between two multidimensional points
	 *
	 * @param	x	Array representing the first multidimensional point.
	 * @param	y	Array representing the second multidimensional point.
	 * 
	 * @return	Cosine similarity between x and y.
	 */
	public float getProximity(float[] x, float[] y) {
		float dp = 0;		// dot product
		float xLen = 0; 	// euclidean length of x
		float yLen = 0; 	// euclidean length of y
		for (int i = 0; i < x.length; i++) {
			float xi = x[i];
			float yi = y[i];
			dp      += xi * yi;
			xLen    += xi * xi;
			yLen    += yi * yi;
		}
		xLen = (float) Math.sqrt(xLen);
		yLen = (float) Math.sqrt(yLen);
		return dp / (xLen * yLen);
	}
	
	/**
	 * Returns a string with the name of this proximity measure.
	 * 
	 * @return	String with the name of this proximity measure.
	 */
	public String toString() {
		return "CosineSim";
	}
}
