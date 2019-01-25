package neu.lab.certifies.core;

import java.util.List;

import org.apache.log4j.Logger;

import neu.lab.certifies.PtTransformer;
import neu.lab.certifies.util.ArgUtil;
import soot.PackManager;
import soot.Transform;

/**
 * �����γ�һ��SysInfo��
 * 
 * @author asus
 *
 */
public class SysInfoLoader {
	private static Logger logger = Logger.getRootLogger();

	/**
	 * ��ָ��·������SysInfo
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
		soot.Main.main(args);// ����ϵͳ�д��ڵ�node�Լ�node֮��Ĺ�ϵ
		soot.G.reset();
		logger.info("soot runTime:" + ((System.currentTimeMillis() - startTime) / 1000));

		new BorderBuilder(sysInfo).geneBorder(srcPath, binPath);// ����ϵͳ�ı߽�
		new MthdRltMt(sysInfo).mtMthdRlt();// �Է����ĵ��ù�ϵ����ά��

		logger.info("pck size:" + sysInfo.getAllPcks().size() + ";cls Size:" + sysInfo.getAllClses().size()
				+ ";mth size" + sysInfo.getAllMthd().size() + ";rlt size:" + sysInfo.getAllMthdRlt().size()
				+ ";clsRltSize:" + sysInfo.getAllClsRlt().size());
		logger.info("hostMthd size:" + sysInfo.getHostMthds().size());
		// new DualPrinter().printDual();
		// new SysPrinter(sysInfo).printSys();

		return sysInfo;
	}
}
