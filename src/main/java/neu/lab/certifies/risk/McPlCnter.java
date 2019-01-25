package neu.lab.certifies.risk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.MethodVO;

/**
 * method call per-lib writer������host method ��ÿһ�������������ĺ����ĸ���
 * 
 * @author asus
 *
 */
public class McPlCnter extends RiskCounter {

	public McPlCnter(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void initType() {
		type = "mc_pl";
	}

	@Override
	protected void calRisk() throws IOException {
		for (String hostM : sysInfo.getHostMthds()) {
			Set<String> outMthds = sysInfo.getMthd(hostM).getOutMthds();
			Map<String, Set<String>> lib2Mthds = new HashMap<String, Set<String>>();
			if (null != outMthds) {// �����ⲿ����
				for (String outMthd : outMthds) {// ����hostMthd�ĵ��÷���
					MethodVO methodVO = sysInfo.getMthd(outMthd);
					String jarName = methodVO.getJar();
					if (!SysCons.MY_JAR_NAME.equals(jarName)) {
						// �������ⲿ����
						Set<String> libMthds = lib2Mthds.get(jarName);
						if (null == libMthds) {
							libMthds = new HashSet<String>();
						}
						libMthds.add(outMthd);
						lib2Mthds.put(jarName, libMthds);
					}
				}
			}
			wrtEachM(hostM, lib2Mthds);
		}

	}

	public void wrtEachM(String hostM, Map<String, Set<String>> lib2Mthds) throws IOException {
		PrintWriter printer = null;
		// ��ÿ��lib���õķ����ĸ���
		for (String lib : lib2Mthds.keySet()) {
			Set<String> libMthds = lib2Mthds.get(lib);
			int cgSize = libMthds.size();
			nums.add("" + cgSize);
			if (cgSize > SysConf.MC_PL_T) {// ������ֵ
				if (printer == null)
					printer = new PrintWriter(new BufferedWriter(new FileWriter(getWrtFile(dirPath, hostM))));
				riskMthds.add(hostM);
				printer.println(hostM);
				this.wrtHostCall(printer, hostM);
				printer.println(cgSize + " " + lib + ":");
				for (String libMthd : libMthds) {
					printer.println(libMthd);
				}
			}
		}
		if (null != printer)
			printer.close();
	}

}
