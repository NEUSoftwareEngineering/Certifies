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


public class Multi_source_transitivity extends RiskCounter {

	public Multi_source_transitivity(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void initType() {
		type = "Multi-source transitivity";
	}

	@Override
	public void calRisk() throws IOException {
		HostMSta hostmSta = new HostMSta(Screen.i().sysInfo);
		for (String hostM : hostmSta.hostMs) {
			Set<String> acsMthds = (Set<String>) sysInfo.getBook(hostM).getRecords();
			Set<String> multi_source_transitivity = new HashSet<String>();
			for (String acsMthd : acsMthds) {
				if (sysInfo.isLibMthd(acsMthd)) {
					Set<String> multi_Nodes = sysInfo.getMthd(acsMthd).getOutMthds();
					if(null!=multi_Nodes) 
						for(String multi_Node : multi_Nodes) 
							if(sysInfo.isLibMthd(multi_Node)) 
                                if(!sysInfo.getMthd(multi_Node).getJar().equals(sysInfo.getMthd(acsMthd).getJar())) 
                                	multi_source_transitivity.add(multi_Node);	
				}		
			}
			nums.add("" + multi_source_transitivity.size());
			//riskMthds.add(hostM);
			riskGraphData.addNum(hostM, multi_source_transitivity.size());
		}
	}


}
