package neu.lab.certifies.view;

import java.util.ArrayList;
import java.util.List;

import prefuse.util.ColorLib;

public class ViewUtil {
	public static List<RGB> colors = new ArrayList<RGB>();
	
	/**
	 * 返回固定数量的colorRgb
	 * 
	 * @param num
	 * @return
	 */
	public static int[] getColors(int num) {
		int[] result = new int[num];
		for (int i = 0; i < result.length; i++) {
			result[i] = ColorLib.rgb((int) (250 * Math.random()), (int) (250 * Math.random()),
					(int) (250 * Math.random()));
		}
		return result;
	}

	public static double getRdX() {
		int span = 500;
		return span * Math.random() - (span / 2);
	}

	public static double getRdY() {
		int span = 50;
		return span * Math.random() - (span / 2);
	}
}

class RGB {
	int r;
	int g;
	int b;
}