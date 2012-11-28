package classifier.proxmeasure;

/**
 * This interface defines which methods have to be implemented by
 * a proximity measure.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public interface ProximityMeasure {
	/**
	 * Returns the value that represents the maximum similarity between
	 * two multidimensional points.
	 * 
	 * @return	Maximum similarity between two multidimensional points.
	 */
	public float bestProximity();

	/**
	 * Returns the value that represents the minimum similarity between
	 * two multidimensional points.
	 * 
	 * @return	Minimum similarity between two multidimensional points.
	 */
	public float worstProximity();

	/**
	 * Returns true if the first argument is more similar than the second argument.
	 * 
	 * @param	x	Array representing the first multidimensional point.
	 * @param	y	Array representing the second multidimensional point.
	 * 
	 * @return	True if and only if x is more similar than y.
	 */
	public boolean isBetter(float x, float y);

	/**
	 * Returns the proximity between two multidimensional points.
	 * 
	 * @param	x	Array representing the first multidimensional point.
	 * @param	y	Array representing the second multidimensional point.
	 * 
	 * @return	Proximity between x and y.
	 */
	public float getProximity(float[] x, float[] y);
	
	/**
	 * Return a string with the name of the proximity measure.
	 * 
	 * @return	String with the name of the proximity measure.
	 */
	public String toString();
}
