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

public class LibNumCnter extends RiskCounter {
	public LibNumCnter(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	public void initType() {
		type = "Lib_Num";

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
						// ���õ����ⲿ����
						Set<String> libMthds = lib2Mthds.get(jarName);
						if (null == libMthds) {
							libMthds = new HashSet<String>();
						}
						libMthds.add(outMthd);
						lib2Mthds.put(jarName, libMthds);
					}
				}
			}
			// ���õ�lib����
			nums.add("" + lib2Mthds.size());
			if (lib2Mthds.size() > SysConf.LIB_NUM_T) {// �����˶��lib
				riskMthds.add(hostM);
				wrtEachM(hostM, lib2Mthds);
			}
		}
	}

	private void wrtEachM(String hostM, Map<String, Set<String>> lib2Mthds) throws IOException {
		PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(getWrtFile(dirPath, hostM))));
		printer.println(lib2Mthds.size() + "  " + hostM);
		this.wrtHostCall(printer, hostM);
		Set<String> acsMthds = (Set<String>) sysInfo.getBook(hostM).getRecords();
		for (String libSig : lib2Mthds.keySet()) {
			printer.println(libSig + ":");
			// ��ӡֱ�ӵ��õķ���
			Set<String> libMthds = lib2Mthds.get(libSig);
			printer.print(libMthds.size() + " " + "dir:");
			for (String mthd : libMthds) {
				printer.print(mthd + ";");
			}
			printer.println();

			// ��ӡ��ӵ��õķ���
			printer.print(acsMthds.size() + " " + "acs:");
			for (String acsMthd : acsMthds) {
				String jarName = sysInfo.getMthd(acsMthd).getJar();
				if (libSig.equals(jarName)) {
					printer.print(acsMthd + ";");
				}
			}
			printer.println();
			printer.println();
		}
		printer.close();
	}
	

}
