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
	private int count;// һ���ж���������
	private int recT;// recommendation threshold �Ƽ���ֵ
	private TreeMap<Integer, Set<String>> num2sig;

	// /**
	// * @return ֱ��ͼ���������
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
	 * ��ȡ�߷�����ֵ:����ֵ���ڵ���recT��riskMethod�ĸ���С�ڵ���maxRiskNum
	 * (��num2sig�����Integer��Ӧ��set��СҲ����maxRiskNumʱ��riskNum����set��С������maxRiskNum)
	 * 
	 * @return �Ƽ��ĸ߷�����ֵ
	 */
	public int getRecT() {
		if (recT == -1) {// �Ƽ���ֵ��δ���й�����
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
			if (recT == -1) {// num2sig�����Integer��Ӧ��set��С����maxRiskNum
				recT = num2sig.descendingKeySet().first();
			}
		}
		return recT;
	}

	/**
	 * ������ͼ������
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
	 * ��ɢ��ͼ������
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
	 * ��ֱ��ͼ������
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
