package neu.lab.certifies.risk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.core.SysInfo;

public class RiskCnterChain {
	private Map<String, RiskGraphData> risk2graphData = new HashMap<String, RiskGraphData>();
	private List<RiskCounter> writers;

	public RiskCnterChain(SysInfo sysInfo) {
		writers = new ArrayList<RiskCounter>();
		writers.add(new Size(sysInfo));
		writers.add(new Touching_area(sysInfo));
		writers.add(new Reachability(sysInfo));
		writers.add(new Multi_source(sysInfo));
		writers.add(new Multi_source_transitivity(sysInfo));
		writers.add(new Penetrability(sysInfo));
		writers.add(new HubDegree(sysInfo));
		
		invokeAllWrt();
	}

	public void invokeAllWrt() {
		try {
			delFile(new File(SysConf.riskOutPath));
			new File(SysConf.staDir).mkdirs();
			for (RiskCounter writer : writers) {
				writer.run();
			}
			for (RiskCounter writer : writers) {
				RiskGraphData riskGraphData = writer.getRiskGraphData();
				risk2graphData.put(riskGraphData.getRiskType(), riskGraphData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, RiskGraphData> getRisk2graphData() {
		return risk2graphData;
	}

	public void delFile(File path) {
		if (!path.exists())
			return;
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			delFile(files[i]);
		}
		path.delete();
	}
}
