package neu.lab.certifies.sta;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;

public class HostMSta {

	private SysInfo sysInfo;
	public Set<String> hostMs = new HashSet<String>();

	public HostMSta(SysInfo sysInfo) {
		super();
		this.sysInfo = sysInfo;
		for (String hostM : sysInfo.getHostMthds()) {
			int flag = 0;
			Set<String> outMthds = sysInfo.getMthd(hostM).getOutMthds();
			if (null != outMthds) {
				for (String outMthd : outMthds) {
					if (sysInfo.isLibMthd(outMthd)) {
						flag=1;
						break;
					}
				}
			}
			
		   if(flag==1) {
			   hostMs.add(hostM);
			}
		}
		
	}

	
	


}
