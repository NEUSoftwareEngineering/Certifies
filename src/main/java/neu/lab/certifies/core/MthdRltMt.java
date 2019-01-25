package neu.lab.certifies.core;

import java.util.Set;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.vo.MethodVO;

/**
 * method-relation-maintain ϵͳ�����ĸ��ط����漰������֮��Ĺ�ϵ 1.MethodVO��outMthds
 * 2.MethodVO��inMthds 3.SysInfo�е�m2ms 4.ϵͳ�е�c2cs
 * ��ִ����soot�ĳ����call-graph�ᱻ��¼��outMthds�У���Ҫ����outMthds�е���Ϣ��ʣ�����ֵ���Ϣ����
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
