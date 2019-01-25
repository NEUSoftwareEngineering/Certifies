package neu.lab.certifies.sta;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.ClassVO;
import neu.lab.certifies.vo.MethodVO;
import neu.lab.certifies.vo.Relation;

import java.util.Set;

public class SysJarSta {
	private SysInfo sysInfo;

	public HostSta hostSta;
	public Map<String, LibSta> libStas;

	/**
	 * @return host class Number
	 */
	public int hClsNum() {
		return hostSta.getClsCnt();
	}

	/**
	 * @return host method Number
	 */
	public int hMthdNum() {
		return hostSta.getMthdCnt();
	}

	public SysJarSta(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
		libStas = new HashMap<String, LibSta>();
		hostSta = new HostSta(sysInfo);
		Set<String> allJar = sysInfo.getAllJar();
		for (String jarSig : allJar) {
			if (!SysCons.MY_JAR_NAME.equals(jarSig))
				libStas.put(jarSig, new LibSta(sysInfo, jarSig));
		}
		count();
	}

	public HostSta getHostSta() {
		return this.hostSta;
	}

	public void count() {
		countMthd();
		countCls();

		countDir();
		countAcs();

		countPassInfo();

		// print();
	}

	private void countPassInfo() {
		for (Entry<String, LibSta> entry : this.libStas.entrySet()) {
			if (!SysCons.MY_JAR_NAME.equals(entry.getKey()))
				entry.getValue().calPassInfo();
		}
	}

	private void countMthd() {// 计算每个lib中的方法的个数
		Set<String> allMthds = sysInfo.getAllMthd();
		for (String mthd : allMthds) {
			MethodVO mthdVO = sysInfo.getMthd(mthd);
			String jarSig = mthdVO.getJar();
			if(jarSig==null) {
				System.out.println(mthd);
			}
			if (SysCons.MY_JAR_NAME.equals(jarSig)) {
				hostSta.incMthdCnt();
			} else {
//				System.out.println(jarSig);
				libStas.get(jarSig).incMthdCnt();
			}

		}
	}

	private void countCls() {// 计算每个lib中的方法的个数
		Set<String> allClses = sysInfo.getAllClses();
		for (String clsSig : allClses) {
			ClassVO clsVO = sysInfo.getCls(clsSig);
			String jarSig = clsVO.getJar();
			if (SysCons.MY_JAR_NAME.equals(jarSig)) {
				hostSta.incClsCnt();
			} else {
				libStas.get(clsVO.getJar()).incClsCnt();
			}
		}
	}

	private void countDir() {// 直接访问信息
		Set<Relation> allRlt = sysInfo.getAllMthdRlt();
		for (Relation rlt : allRlt) {
			String srcSig = rlt.getSrc();
			String tgtSig = rlt.getTgt();
			if (sysInfo.isHostMthd(srcSig) && !sysInfo.isHostMthd(tgtSig)) {// 一条由host指向第三方的边
				LibSta jarSta = this.libStas.get(sysInfo.getMthd(tgtSig).getJar());
				jarSta.addDirInfo(srcSig, tgtSig);
				hostSta.addDirInfo(srcSig, tgtSig);
			}
		}

	}

	private void countAcs() {
		for (String cHostM : sysInfo.getAllBookedMthd()) {
			if (sysInfo.isHostMthd(cHostM)) {
				Set<String> records = (Set<String>) sysInfo.getBook(cHostM).getRecords();
				for (String cJarM : records) {
					if (!sysInfo.isHostMthd(cJarM)) {
						LibSta jarSta = this.libStas.get(sysInfo.getMthd(cJarM).getJar());
						jarSta.addAcsInfo(cHostM, cJarM);
						hostSta.addAcsInfo(cHostM, cJarM);
					}
				}
			}
		}
	}

	private void print() {
		for (String jar : this.libStas.keySet()) {
			this.libStas.get(jar).print();
		}
	}

}
