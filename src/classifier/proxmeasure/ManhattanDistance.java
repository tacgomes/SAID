package classifier.proxmeasure;

/**
 * This class implements the {@link classifier.proxmeasure.ProximityMeasure ProximityMeasure}
 * interface for Manhattan Distance.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class ManhattanDistance implements ProximityMeasure {

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
	 * Computes the manhattan distance between two multidimensional points.
	 *
	 * @param	x	Array representing the first multidimensional point.
	 * @param	y	Array representing the second multidimensional point.
	 * 
	 * @return	Manhattan distance between the x and y.
	 */
	public float getProximity(float[] x, float[] y) {
		float sum = 0;
		for (int i = 0; i < x.length; i++) {
			sum += Math.abs(x[i] - y[i]);
		}
		return sum;
	}
	
	/**
	 * Returns a string with the name of this proximity measure.
	 * 
	 * @return	String with the name of this proximity measure.
	 */
	public String toString() {
		return "ManhatDist";
	}
	
}
