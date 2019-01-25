package neu.lab.certifies.core;

import java.util.Set;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.vo.MethodVO;

/**
 * method-relation-maintain 系统中有四个地方会涉及到方法之间的关系 1.MethodVO的outMthds
 * 2.MethodVO的inMthds 3.SysInfo中的m2ms 4.系统中的c2cs
 * 在执行完soot的程序后call-graph会被记录到outMthds中，需要根据outMthds中的信息将剩余三种的信息填补完成
 * 
 * @author asus
 *
 */
public class MthdRltMt {
	private SysInfo sysInfo;
	public MthdRltMt(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
	}
	public void mtMthdRlt() {
		if (SysConf.FLT_M_Rlt) {
			filt();
		}
		Set<String> allMthd = sysInfo.getAllMthd();
		for (String srcSig : allMthd) {
			MethodVO mthdVO = sysInfo.getMthd(srcSig);
			Set<String> outMthds = mthdVO.getOutMthds();
			if (null != outMthds) {
				for (String tgtSig : outMthds) {
					MethodVO tgtVO = sysInfo.getMthd(tgtSig);
					tgtVO.addInMthds(srcSig);// inMthds
					sysInfo.addM2m(srcSig, tgtSig);// m2ms
					sysInfo.addC2c(mthdVO.getCls(), tgtVO.getCls());// c2cs
				}
			}
		}
	}

	private void filt() {
		Set<String> allMthd = sysInfo.getAllMthd();
		for (String srcSig : allMthd) {
			MethodVO mthdVO = sysInfo.getMthd(srcSig);
			mthdVO.filOutM();
		}
	}
}
