package neu.lab.certifies.vo;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.core.SysInfo;

/**
 * ����һ������ϵͳ��ͼ����ͼ�� ��ͼ�е�����class�ڵ����methodCall��ϵ�Ǳպϵ�
 * ĳ��class����ͼΪ��class�е�methodֱ����ػ��߼����ص�����method
 * 
 * @author asus
 *
 */
public class ClosureSys extends SubSys{
	private static Logger logger = Logger.getRootLogger(); 
	private ClosureSys() {

	}

	public void print() {
		for (String node : nodes) {
			logger.debug("node:" + node);
		}
		for(Relation rlt:edges) {
			logger.debug(rlt.getSrc()+" to " + rlt.getTgt());
		}

	}

	public static ClosureSys geneClourseSys(SysInfo sysInfo,String clsSig) {
		ClosureSys result = new ClosureSys();
		result.nodes = new HashSet<String>();
		result.edges = new HashSet<Relation>();
		ClassVO clsVO = sysInfo.getCls(clsSig);
		if (null != clsVO) {
			Set<String> mthdSigs = clsVO.getMethods();
			if (null != mthdSigs) {// ������ڲ��ķ���
				for (String mthdSig : mthdSigs) {
					result.nodes.add(mthdSig);
				}
			}
		}
		findClosure(sysInfo,result);
		return result;
	}

	private static void findClosure(SysInfo sysInfo,ClosureSys subSys) {
		Set<String> oldNodes = new HashSet<String>(subSys.nodes);
		while (oldNodes.size() != 0) {
			oldNodes = propagate(subSys, sysInfo.getAllMthdRlt(), oldNodes);
		}
	}

	/**
	 * ���ݾɽڵ㷱�ܳ��±ߺ��½ڵ㣬�������½ڵ�
	 * 
	 * @param allRlt
	 * @param oldNodes
	 */
	private static Set<String> propagate(ClosureSys subSys, Set<Relation> allRlt, Set<String> oldNodes) {
		Set<String> newNds = new HashSet<String>();
		for (String oldNode : oldNodes) {
			for (Relation rlt : allRlt) {
				String src = rlt.getSrc();
				if (src.equals(oldNode)) {
					boolean isOldEdge = subSys.edges.contains(rlt);
					if (!isOldEdge) {// �±�
						subSys.edges.add(rlt);
						String tgt = rlt.getTgt();
						if (!subSys.nodes.contains(tgt)) {// �±ߵ�tgtΪ��node
							subSys.nodes.add(tgt);
							newNds.add(tgt);
						}
					}
				}
			}
		}
		return newNds;
	}

}
