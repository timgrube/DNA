package dynamicGraphs.util;

import java.util.Arrays;
import java.util.Set;

public class ArrayUtils {
	public static int[] incr(int[] values, int index) {
		try {
			values[index]++;
			return values;
		} catch (ArrayIndexOutOfBoundsException e) {
			int[] valuesNew = new int[index + 1];
			System.arraycopy(values, 0, valuesNew, 0, values.length);
			valuesNew[index] = 1;
			return valuesNew;
		}
	}

	public static int[] decr(int[] values, int index) {
		try {
			values[index]--;
			return values;
		} catch (ArrayIndexOutOfBoundsException e) {
			int[] valuesNew = new int[index + 1];
			System.arraycopy(values, 0, valuesNew, 0, values.length);
			valuesNew[index] = -1;
			return valuesNew;
		}
	}

	public static int[] truncate(int[] values, int value) {
		if (values[values.length - 1] != value) {
			return values;
		}
		int index = values.length - 1;
		for (int i = values.length - 1; i >= 0; i--) {
			if (values[i] != value) {
				break;
			}
			index--;
		}
		int[] valuesNew = new int[index + 1];
		System.arraycopy(values, 0, valuesNew, 0, index + 1);
		return valuesNew;
	}

	public static int sum(int[] values) {
		int sum = 0;
		for (int v : values) {
			sum += v;
		}
		return sum;
	}

	public static double sum(double[] values) {
		double sum = 0;
		for (double v : values) {
			sum += v;
		}
		return sum;
	}

	public static String toString(int[] values) {
		StringBuffer buff = new StringBuffer();
		for (int v : values) {
			buff.append(v + " ");
		}
		return buff.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String toString(Set set) {
		StringBuffer buff = new StringBuffer();
		for (Object obj : set) {
			buff.append(obj.toString() + " ");
		}
		return buff.toString();
	}

	public static boolean equals(int[] v1, int[] v2) {
		if (v1.length != v2.length) {
			return false;
		}
		for (int i = 0; i < v1.length; i++) {
			if (v1[i] != v2[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean equals(double[] v1, double[] v2) {
		if (v1.length != v2.length) {
			return false;
		}
		for (int i = 0; i < v1.length; i++) {
			if (v1[i] != v2[i]) {
				return false;
			}
		}
		return true;
	}

	public static double avg(double[] values) {
		double avg = 0;
		for (double v : values) {
			avg += v;
		}
		return avg / values.length;
	}

	public static double med(double[] values) {
		Arrays.sort(values);
		return values[values.length / 2];
	}
}
