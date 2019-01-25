package neu.lab.certifies.risk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.sta.HostMSta;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.vo.MethodVO;


public class Touching_area extends RiskCounter {

	public Touching_area(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void initType() {
		type = "Touching-area";
	}

	@Override
	public void calRisk() throws IOException {
		HostMSta hostmSta = new HostMSta(Screen.i().sysInfo);
		for (String hostM : hostmSta.hostMs) {
			Set<String> outMthds = sysInfo.getMthd(hostM).getOutMthds();
			Set<String> touching_area = new HashSet<String>();
			for (String outMthd : outMthds) {
				if (sysInfo.isLibMthd(outMthd))
					touching_area.add(outMthd);
			}	
			nums.add("" + touching_area.size());
			//riskMthds.add(hostM);
			riskGraphData.addNum(hostM, touching_area.size());
		}
	}
}
