package com.geo_tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

	private static double Constrain(double value, double min, double max) {
		value = Math.max(value, min);
		value = Math.min(value, max);
		return value;
	}

	public static double Normalize(double value, double min, double max) {
		value = Constrain(value, min, max);
		return (value - min) / (max - min);

	}

	public static double angleDiff(double a, double b) {
		double dif = Math.IEEEremainder(b - a + 180, 360);
		if (dif < 0)
			dif += 360;
		return dif - 180;
	}

	public static double constrainAngle(double x) {
		x = Math.IEEEremainder(x, 360);
		if (x < 0)
			x += 360;
		return x;
	}

	public static double bisectAngle(double a, double b, double alpha) {
		return constrainAngle(a + angleDiff(a, b) * alpha);
	}

	public static double hypot(double altDelta, double distDelta) {
		return Math.hypot(altDelta, distDelta);
	}

	public static double round(double value, int amountAfterDot) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(amountAfterDot, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
