package neu.lab.certifies.risk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.sta.HostMSta;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.vo.MethodVO;


public class Penetrability extends RiskCounter {

	public Penetrability(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void initType() {
		type = "Penetrability";
	}

	@Override
	public void calRisk() throws IOException {
		HostMSta hostmSta = new HostMSta(Screen.i().sysInfo);
		for (String hostM : hostmSta.hostMs) {
			Set<String> outMthds = sysInfo.getMthd(hostM).getOutMthds();
			Set<String> dirLib = new HashSet<String>();
			for (String outMthd : outMthds) {
				String jar = sysInfo.getMthd(outMthd).getJar();
				if(!jar.equals(SysCons.MY_JAR_NAME))
					dirLib.add(jar);
			}	
			
			Set<String> acsMthds = (Set<String>) sysInfo.getBook(hostM).getRecords();
			Set<String> penetrability = new HashSet<String>();
			for (String acsMthd : acsMthds) {
				if (sysInfo.isLibMthd(acsMthd)&&!dirLib.contains(sysInfo.getMthd(acsMthd).getJar()))
					penetrability.add(acsMthd);
			}
			nums.add("" + penetrability.size());
			//riskMthds.add(hostM);
			riskGraphData.addNum(hostM, penetrability.size()); 
		}
	}


}
