package util;

public class Round {
	
	public static float getRoundedValue(float value, int precision) {
		if (value == 0) {
			return 0;
		}

		int exp = (int) Math.pow(10, precision);
		if (value < 5 / Math.pow(10, precision +  1)) {
			value = 1F / exp;
		} else {
			// round the value
			value = value * exp;
			value = Math.round(value);
			value = value / exp;
		}
		
		return value;
	}

}
