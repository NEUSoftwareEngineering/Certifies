package neu.lab.certifies.risk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RiskGraphData {
	// private static Map<String, RiskGraphData> riskDataMap = new HashMap<String,
	// RiskGraphData>();

	// public static void addRiskData(String riskType, RiskGraphData data) {
	// RiskGraphData.riskDataMap.put(riskType, data);
	// }
	//
	// public static RiskGraphData getRiskData(String riskType) {
	// return riskDataMap.get(riskType);
	// }

	private static int maxRiskNum = 100;
	private String riskType;
	private int count;// 一共有多少条数据
	private int recT;// recommendation threshold 推荐阈值
	private TreeMap<Integer, Set<String>> num2sig;

	// /**
	// * @return 直方图的区间个数
	// */
	// public int intervalNum() {
	// return num2sig.size();
	// }
	public String getRiskType() {
		return riskType;
	}

	public RiskGraphData(String riskType) {
		this.riskType = riskType;
		count = 0;
		num2sig = new TreeMap<Integer, Set<String>>();
		recT = -1;
	}

	public void addNum(String sig, Integer num) {
		count++;
		Set<String> sigs = num2sig.get(num);
		if (null == sigs) {
			sigs = new HashSet<String>();
			num2sig.put(num, sigs);
		}
		sigs.add(sig);
	}

	/**
	 * 获取高风险阈值:风险值大于等于recT的riskMethod的个数小于等于maxRiskNum
	 * (当num2sig的最大Integer对应的set大小也超过maxRiskNum时，riskNum等于set大小，超过maxRiskNum)
	 * 
	 * @return 推荐的高风险阈值
	 */
	public int getRecT() {
		if (recT == -1) {// 推荐阈值从未进行过计算
			int riskMthdNum = 0;
			Iterator<Integer> ite = num2sig.descendingKeySet().iterator();
			while (ite.hasNext()) {
				Integer t = ite.next();
				riskMthdNum = riskMthdNum + num2sig.get(t).size();
				if (riskMthdNum > maxRiskNum) {
					break;
				}
				recT = t;
			}
			if (recT == -1) {// num2sig的最大Integer对应的set大小超过maxRiskNum
				recT = num2sig.descendingKeySet().first();
			}
		}
		return recT;
	}

	/**
	 * 画折线图的数据
	 * 
	 * @return
	 */
	public double[][] getLineData() {
		double[][] result = new double[2][num2sig.size()];
		int index = 0;
		for (Integer x : num2sig.keySet()) {
			result[0][index] = (double) x;// x
			result[1][index] = (double) num2sig.get(x).size();// y
			index++;
		}
		return result;
	}

	/**
	 * 画散点图的数据
	 * 
	 * @return
	 */
	public double[][] getScatterData() {
		List<Integer> riskNums = new ArrayList<Integer>();
		for (Integer riskNum : num2sig.keySet()) {
			for (String sig : num2sig.get(riskNum)) {
				riskNums.add(riskNum);
			}
		}
		double[][] result = new double[2][riskNums.size()];
		for (int i = 0; i < riskNums.size(); i++) {
			result[0][i] = (double) i;// x
			result[1][i] = (double) riskNums.get(i);// y
		}
		return result;
	}

	/**
	 * 画直方图的数据
	 * 
	 * @return
	 */
	public double[] getHistData() {
		List<Integer> riskNums = new ArrayList<Integer>();
		for (Integer riskNum : num2sig.keySet()) {
			for (String sig : num2sig.get(riskNum)) {
				riskNums.add(riskNum);
			}
		}
		double[] result = new double[riskNums.size()];
		for (int i = 0; i < riskNums.size(); i++) {
			result[i] = (double) riskNums.get(i);// y
		}
		return result;
	}

}
