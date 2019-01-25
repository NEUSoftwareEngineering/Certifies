package neu.lab.certifies.view.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.view.ViewCons;
import neu.lab.certifies.vo.Relation;
import prefuse.data.Graph;
import prefuse.data.Table;

/**将节点和边转换成prefuse画图需要的数据结构
 * @author asus
 *
 */
public abstract class GraphShop {
	private static Logger logger = Logger.getRootLogger(); 
	protected int ndIdCnt = 0;
	protected Table nodes = new Table();

	protected int egIdCnt = 0;// edgeIdCounter
	protected Table edges = new Table();

	protected Map<String, Integer> sigIdMap = new HashMap<String, Integer>();
	protected Map<String, String> sigJarMap = new HashMap<String, String>();
	
	protected SysInfo sysInfo;
	public GraphShop(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
	}
	public Graph getGraph(Set<String> mthds, Set<Relation> rlts) {
		// 生成节点
		formNdCols();
		for (String mthdSig : mthds) {
			addNdRow(mthdSig);
		}
		// 生成边
		formEgCols();
		for (Relation rlt : rlts) {
			addEgRow(rlt);
		}
		logger.info("node size:"+nodes.getRowCount()+",edges size:"+edges.getRowCount());
		return new Graph(nodes, edges, false, ViewCons.EG_ID, ViewCons.EG_SRC, ViewCons.EG_TGT);
	}

	protected void formNdCols() {
		nodes.addColumn("id", int.class);
		nodes.addColumn("sig", String.class);
		nodes.addColumn("jarSig", String.class);
	}

	protected void formEgCols() {
		edges.addColumn("id", int.class);
		edges.addColumn("srcId", int.class);
		edges.addColumn("srcJar", String.class);
		edges.addColumn("tgtId", int.class);
		edges.addColumn("tgtJar", String.class);
	}

	protected void addEgRow(Relation rlt) {
		Integer srcId = sigIdMap.get(rlt.getSrc());
		Integer tgtId = sigIdMap.get(rlt.getTgt());
		if (null != srcId && null != tgtId) {
			edges.addRow();
			edges.set(egIdCnt, "id", egIdCnt);
			edges.set(egIdCnt, "srcId", (int) srcId);
			edges.set(egIdCnt, "srcJar", sigJarMap.get(rlt.getSrc()));
			edges.set(egIdCnt, "tgtId", (int) tgtId);
			edges.set(egIdCnt, "tgtJar", sigJarMap.get(rlt.getTgt()));

			egIdCnt++;
		}

	}
	protected void addNdMap(String sig,Integer id,String jarSig) {
		this.sigIdMap.put(sig, id);
		this.sigJarMap.put(sig, jarSig);
	}
	protected abstract void addNdRow(String mthdSig);
}
