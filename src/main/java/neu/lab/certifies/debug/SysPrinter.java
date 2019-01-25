package neu.lab.certifies.debug;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.ClassVO;
import neu.lab.certifies.vo.FieldVO;
import neu.lab.certifies.vo.JarVO;
import neu.lab.certifies.vo.MethodVO;
import neu.lab.certifies.vo.PackageVO;
import neu.lab.certifies.vo.Relation;
import soot.Scene;
import soot.SootClass;

public class SysPrinter {
	private static Logger logger = Logger.getRootLogger();
	private SysInfo sysInfo;

	public SysPrinter(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
	}


	public void printInner() {
		JarVO jarVO = sysInfo.getJar(SysCons.MY_JAR_NAME);
		logger.debug("==================================jar:" + jarVO.getJarSig());
		Set<String> pcks = jarVO.getPcks();
		for (String pckSig : pcks) {
			printPck(pckSig);
		}

	}

	public void printAll() {
		for (String jarSig : sysInfo.getAllJar()) {
			JarVO jarVO = sysInfo.getJar(jarSig);
			logger.debug("==================================jar:" + jarVO.getJarSig());
			Set<String> pcks = jarVO.getPcks();
			for (String pckSig : pcks) {
				printPck(pckSig);
			}
		}
	}

	public void printPck(String pckSig) {
		logger.debug("=============================pck:" + pckSig);
		PackageVO pck = sysInfo.getPck(pckSig);
		if (null != pck) {
			Set<String> clses = pck.getClses();
			for (String clsSig : clses) {
				printCls(clsSig);
			}
		} else {
			logger.warn("not found package:" + pckSig);
		}

	}

	public void printCls(String clsSig) {
		logger.debug("++++++++++++++++class:" + clsSig);
		ClassVO clsVO = sysInfo.getCls(clsSig);
		if (null != clsVO) {
			Set<String> flds = clsVO.getFields();
			for (String fldSig : flds) {
				printFld(fldSig);
			}
			Set<String> mthds = clsVO.getMethods();
			for (String mthdSig : mthds) {
				printMthd(mthdSig);
			}
		} else {
			logger.warn("not found class:" + clsSig);
		}

	}

	public void printFld(String fldSig) {
		FieldVO fldVO = sysInfo.getFld(fldSig);
		if (null != fldVO) {
			logger.debug("---------f:" + fldVO.getFldSig() + "(" + fldVO.getType() + ")");
		} else {
			logger.warn("not found fld:" + fldSig);
		}
	}

	public void printMthd(String mthdSig) {

		MethodVO mthdVO = sysInfo.getMthd(mthdSig);
		if (null != mthdVO) {
			logger.debug("---------m:" + mthdSig);
			Set<String> locTypes = mthdVO.getLocTypes();
			if (locTypes != null) {
				for (String loc : locTypes) {
					// logger.debug("------------------locType:" + loc);
				}
			}
			Set<String> outMthds = mthdVO.getOutMthds();
			if (outMthds != null) {
				for (String out : outMthds) {
					logger.debug("------------------outMthd:" + out);
				}
			}
//			Set<String> inMthds = mthdVO.getInMthds();
//			if (inMthds != null) {
//				for (String in : inMthds) {
//					logger.debug("------------------inMthd:" + in);
//				}
//			}
		} else {
			logger.warn("not found mthd:" + mthdSig);
		}

	}
}
