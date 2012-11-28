package util;

import java.util.Comparator;
import java.util.Map;


public class WeightTableComparator implements Comparator<Object> {

	@SuppressWarnings("unchecked")
	public int compare(Object obj1, Object obj2) {
		Map.Entry<String, Float> e1 = (Map.Entry<String, Float>) obj1 ;
		Map.Entry<String, Float> e2 = (Map.Entry<String, Float>) obj2 ;
		String word1 = e1.getKey();
		String word2 = e2.getKey();
		Float value1 = e1.getValue();
		Float value2 = e2.getValue();
		// if the values are the same, sort in alphabetical order
		if (value1.compareTo(value2) == 0) {
			return word1.compareTo(word2);
		}
		return value2.compareTo(value1);
	}

}
