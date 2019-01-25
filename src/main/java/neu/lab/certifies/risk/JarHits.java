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
	private int pageNum;// ��ҳ����

	private float[] auths;// ��ҳAuthorityȨ��ֵ
	private float[] hubs;// ��ҳhub����ֵ
	private int[][] linkMatrix;// ���Ӿ����ϵ

	private List<String> nodes;// ��ҳ����

	public JarHits(SubSys subsys) {
		this.nodes = new ArrayList<String>();
		nodes.addAll(subsys.nodes);
		pageNum = nodes.size();

		int i = 0;
		int j = 0;

		linkMatrix = new int[pageNum][pageNum];
		auths = new float[pageNum];
		hubs = new float[pageNum];

		// ��ʼʱĬ��Ȩ��ֵ������ֵ��Ϊ1
		for (int k = 0; k < pageNum; k++) {
			auths[k] = 1;
			hubs[k] = 1;
		}

		for (Relation rlt : subsys.edges) {
			i = nodes.indexOf(rlt.getSrc());
			j = nodes.indexOf(rlt.getTgt());
			linkMatrix[i][j] = 1;// ����linkMatrix[i][j]Ϊ1����i��ҳ����ָ��j��ҳ������
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
	 * ����hubֵΪf�Ľڵ�
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
		// ���Hub��Authorityֵ�����ں���Ĺ�һ������
		float maxHub = 0;
		float maxAuthority = 0;

		// ���ֵ�����������ж�
		float error = Integer.MAX_VALUE;
		float[] newHub = new float[pageNum];
		float[] newAuthority = new float[pageNum];

		while (error > 0.01 * pageNum && ite < MAX_ITE) {
			ite++;
			// ������ֵ
			for (int k = 0; k < pageNum; k++) {
				newHub[k] = 0;
				newAuthority[k] = 0;
			}
			// hub��authorityֵ�ĸ��¼���
			for (int i = 0; i < pageNum; i++) {
				for (int j = 0; j < pageNum; j++) {
					if (linkMatrix[i][j] == 1) {
						newHub[i] += auths[j];
						newAuthority[j] += hubs[i];
					}
				}
			}
			// Ѱ������hub��auth�Ա���й�һ��
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

			// ��һ������
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