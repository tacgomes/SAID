package classifier.proxmeasure;

/**
 * This class implements the {@link classifier.proxmeasure.ProximityMeasure ProximityMeasure}
 * interface for Euclidean Distance.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class EuclideanDistance implements ProximityMeasure {

	/**
	 * Returns the minimum distance between two multidimensional points.
	 * 
	 * @return	Minimum distance between two multidimensional points.
	 */
	public float bestProximity() {
		return 0;
	}

	/**
	 * Returns the maximum distance between two multidimensional points.
	 * 
	 * @return	Maximum distance between two multidimensional points.
	 */
	public float worstProximity() {
		return Float.POSITIVE_INFINITY;
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
		return x < y ? true : false;
	}

	/**
	 * Calculates the euclidean distance between two multidimensional points.
	 *
	 * @param	x	Array representing the first multidimensional point.
	 * @param	y	Array representing the second multidimensional point.
	 * 
	 * @return	Euclidean distance between x and y.
	 */
	public float getProximity(float[] x, float[] y) {
		float sum = 0;
		for (int i = 0; i < x.length; i++) {
			float diff = x[i] - y[i];
			sum += diff * diff ;
		}
		return (float) Math.sqrt(sum);
	}
	
	/**
	 * Returns a string with the name of this proximity measure.
	 * 
	 * @return	String with the name of this proximity measure.
	 */
	public String toString() {
		return "EucDist";
	}
}
