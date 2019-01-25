package neu.lab.certifies.risk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import neu.lab.certifies.vo.Relation;
import neu.lab.certifies.vo.SubSys;

public class JarHits {
	private static Logger logger = Logger.getRootLogger(); 
	private final int MAX_ITE = 200;
	private int pageNum;// 网页个数

	private float[] auths;// 网页Authority权威值
	private float[] hubs;// 网页hub中心值
	private int[][] linkMatrix;// 链接矩阵关系

	private List<String> nodes;// 网页种类

	public JarHits(SubSys subsys) {
		this.nodes = new ArrayList<String>();
		nodes.addAll(subsys.nodes);
		pageNum = nodes.size();

		int i = 0;
		int j = 0;

		linkMatrix = new int[pageNum][pageNum];
		auths = new float[pageNum];
		hubs = new float[pageNum];

		// 初始时默认权威值和中心值都为1
		for (int k = 0; k < pageNum; k++) {
			auths[k] = 1;
			hubs[k] = 1;
		}

		for (Relation rlt : subsys.edges) {
			i = nodes.indexOf(rlt.getSrc());
			j = nodes.indexOf(rlt.getTgt());
			linkMatrix[i][j] = 1;// 设置linkMatrix[i][j]为1代表i网页包含指向j网页的链接
		}
		calHits();
	}

	public Map<String, Float> getHubMap() {
		Map<String, Float> hubMap = new HashMap<String, Float>();
		for (int i = 0; i < nodes.size(); i++) {
			hubMap.put(nodes.get(i), new Float(hubs[i]));
		}
		return hubMap;
	}

	public Map<String, Float> getAuthMap() {
		Map<String, Float> authMap = new HashMap<String, Float>();
		for (int i = 0; i < nodes.size(); i++) {
			authMap.put(nodes.get(i), new Float(auths[i]));
		}
		return authMap;
	}

	/**
	 * 返回hub值为f的节点
	 * 
	 * @param f
	 * @return
	 */
	public TreeMap<Float, Set<String>> getSortedHub() {
		TreeMap<Float, Set<String>> sortedHub = new TreeMap<Float, Set<String>>();
		for (int i = 1; i < hubs.length; i++) {
			Float f = new Float(hubs[i]);
			Set<String> nds = sortedHub.get(f);
			if (null == nds) {
				nds = new HashSet<String>();
				sortedHub.put(f, nds);
			}
			nds.add(nodes.get(i));
		}
		return sortedHub;
	}

	public void calHits() {
		int ite = 0;
		// 最大Hub和Authority值，用于后面的归一化计算
		float maxHub = 0;
		float maxAuthority = 0;

		// 误差值，用于收敛判断
		float error = Integer.MAX_VALUE;
		float[] newHub = new float[pageNum];
		float[] newAuthority = new float[pageNum];

		while (error > 0.01 * pageNum && ite < MAX_ITE) {
			ite++;
			// 重置新值
			for (int k = 0; k < pageNum; k++) {
				newHub[k] = 0;
				newAuthority[k] = 0;
			}
			// hub和authority值的更新计算
			for (int i = 0; i < pageNum; i++) {
				for (int j = 0; j < pageNum; j++) {
					if (linkMatrix[i][j] == 1) {
						newHub[i] += auths[j];
						newAuthority[j] += hubs[i];
					}
				}
			}
			// 寻找最大的hub，auth以便进行归一化
			maxHub = 0;
			maxAuthority = 0;
			for (int k = 0; k < pageNum; k++) {
				if (newHub[k] > maxHub) {
					maxHub = newHub[k];
				}
				if (newAuthority[k] > maxAuthority) {
					maxAuthority = newAuthority[k];
				}

			}

			// 归一化处理
			error = 0;
			for (int k = 0; k < pageNum; k++) {
				newHub[k] /= maxHub;
				newAuthority[k] /= maxAuthority;
				error += Math.abs(newHub[k] - hubs[k]);
				hubs[k] = newHub[k];
				auths[k] = newAuthority[k];
			}

		}
		logger.debug("iteNum:" + ite);

	}

}