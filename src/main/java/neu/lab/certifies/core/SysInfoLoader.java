package neu.lab.certifies.core;

import java.util.List;

import org.apache.log4j.Logger;

import neu.lab.certifies.PtTransformer;
import neu.lab.certifies.util.ArgUtil;
import soot.PackManager;
import soot.Transform;

/**
 * 加载形成一个SysInfo类
 * 
 * @author asus
 *
 */
public class SysInfoLoader {
	private static Logger logger = Logger.getRootLogger();

	/**
	 * 从指定路径加载SysInfo
	 * 
	 * @param path
	 * @return
	 */
	public static SysInfo loadSysInfo(String path) {

		String srcPath = path + "\\src";
		String binPath = path + "\\bin";

		List<String> argsList = ArgUtil.getArgs(binPath);
		SysInfo sysInfo = new SysInfo();
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new PtTransformer(sysInfo)));

		String[] args = argsList.toArray(new String[0]);
		Long startTime = System.currentTimeMillis();
		soot.Main.main(args);// 解析系统中存在的node以及node之间的关系
		soot.G.reset();
		logger.info("soot runTime:" + ((System.currentTimeMillis() - startTime) / 1000));

		new BorderBuilder(sysInfo).geneBorder(srcPath, binPath);// 计算系统的边界
		new MthdRltMt(sysInfo).mtMthdRlt();// 对方法的调用关系进行维护

		logger.info("pck size:" + sysInfo.getAllPcks().size() + ";cls Size:" + sysInfo.getAllClses().size()
				+ ";mth size" + sysInfo.getAllMthd().size() + ";rlt size:" + sysInfo.getAllMthdRlt().size()
				+ ";clsRltSize:" + sysInfo.getAllClsRlt().size());
		logger.info("hostMthd size:" + sysInfo.getHostMthds().size());
		// new DualPrinter().printDual();
		// new SysPrinter(sysInfo).printSys();

		return sysInfo;
	}
}
