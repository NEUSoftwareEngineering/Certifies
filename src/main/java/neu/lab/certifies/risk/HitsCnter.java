package neu.lab.certifies.risk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.SubSys;

public class HitsCnter extends RiskCounter {
	public HitsCnter(SysInfo sysInfo) {
		super(sysInfo);
	}

	private static Logger logger = Logger.getRootLogger(); 
	String hitsDir = dirPath + "jarHits/";
	Map<String, Float> hubMap;
	Map<String, Float> authMap;

	@Override
	protected void initType() {
		type = "hits";
	}

	@Override
	protected void calRisk() throws IOException {
		new File(hitsDir).mkdirs();
		hubMap = new HashMap<String, Float>();
		authMap = new HashMap<String, Float>();
		for (String jarName : sysInfo.getAllJar()) {
			logger.debug("hits:" + jarName);
			JarHits hits = new JarHits(SubSys.getJarSys(sysInfo,jarName));
			wrtJarHits(jarName, hits);
			hubMap.putAll(hits.getHubMap());
			authMap.putAll(hits.getAuthMap());
		}
		print();
	}

	private void wrtJarHits(String jarName, JarHits hits) {
		try {
			TreeMap<Float, Set<String>> sortedHub = hits.getSortedHub();
			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(hitsDir + jarName + ".txt")));
			printer.println(jarName);
			for (Float f : sortedHub.descendingKeySet()) {
				for(String mthd:sortedHub.get(f)) {
					printer.println(String.format("%.4f", f) + "-" + mthd);
				}
			}
			printer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void print() {
		TreeSet<Float> tree = new TreeSet<Float>();
		tree.addAll(hubMap.values());
		int i = 0;
		for (Float f : tree.descendingSet()) {
			if (i > 10)
				break;
			for (String mthd : hubMap.keySet()) {
				if (hubMap.get(mthd) == f)
					logger.debug(mthd + ":" + f);
			}
			i++;
		}
	}

	public static void main(String[] args) throws IOException {
		// new HitsWriter().calRisk();
		TreeSet<Float> tree = new TreeSet<Float>();
		tree.add(new Float(1.1));
		tree.add(new Float(3.3));
		tree.add(new Float(2.2));
		for (Float f : tree.descendingSet()) {
		}
	}
}
