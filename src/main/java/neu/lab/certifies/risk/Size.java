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


public class Size extends RiskCounter {

	public Size(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void initType() {
		type = "Size";
	}

	@Override
	public void calRisk() throws IOException {
		HostMSta hostmSta = new HostMSta(Screen.i().sysInfo);
		for (String hostM : hostmSta.hostMs) {
			Set<String> acsMthds = (Set<String>) sysInfo.getBook(hostM).getRecords();
			Set<String> size_stub = new HashSet<String>();
			for (String acsMthd : acsMthds) 
				 size_stub.add(acsMthd);
			
			
			Set<String> size_driver = new HashSet<String>();
			for (String hostMthd : sysInfo.getHostMthds()) {
				Set<String> hostMDrivers = (Set<String>) sysInfo.getBook(hostMthd).getRecords();
				if(hostMDrivers.contains(hostM))
				   size_driver.add(hostMthd);
			}
			
			
			Set<String> outMthds = sysInfo.getMthd(hostM).getOutMthds();
			for (String outMthd : outMthds) {
				if (sysInfo.isLibMthd(outMthd)) {
					String jar = sysInfo.getMthd(outMthd).getJar();
					 for (String libMthd : sysInfo.getLibMthds(jar)) {
						    if(sysInfo.getBook(libMthd)!=null) {
						    	Set<String> libMDrivers = (Set<String>) sysInfo.getBook(libMthd).getRecords();
						    	if(libMDrivers.contains(outMthd)) 
						    		size_driver.add(libMthd);
						    }
						}
				   }	
			}
			nums.add("" + size_stub.size()+size_driver.size());
			//riskMthds.add(hostM);
			riskGraphData.addNum(hostM, size_stub.size()+size_driver.size());
		}
	}
	
	
	
}
				
			
			
