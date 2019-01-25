package neu.lab.certifies.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.vo.JarVO;

public class ArgUtil {
	private static Logger logger = Logger.getRootLogger();

	public static List<String> getArgs(String path) {
		List<String> argsList = ArgUtil.getGenArg();
		// argsList.add("-process-dir");
		// argsList.add("D:\\bin");
		// ��Ѱ�ҵ�jar����ӵ�������
		Set<String> jarPaths = Detective.findJarPath(new File(path));

		for (String jarPath : jarPaths) {
			argsList.add("-process-dir");
			argsList.add(jarPath);
		}
		return argsList;
	}

	/**
	 * �õ�ͨ�õĲ�������
	 * 
	 * @return
	 */
	private static List<String> getGenArg() {
		List<String> argsList = new ArrayList<String>();
		argsList.add("-pp");// ��soot��classPath�е������ڽ���
		argsList.add("-ire");// ����classPath�е���Чʵ��
		argsList.add("-app");// ���е��඼����ΪappClass
		argsList.add("-allow-phantom-refs");// ������Ч�����ͽ���
		argsList.add("-w");// ������Ŀ����

		addCgArg(argsList);

		// �ر��Դ���callGraph���ɻ���
		// argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "off", }));//
		// argsList.addAll(Arrays.asList(new String[] { "-p", "wjop", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "wjap", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "jtp", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "jop", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "jap", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "bb", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "tag", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-f", "n", }));// �ر��ļ������
		// argsList.add("-outjar");//������Ŀ���һ��.jar

		return argsList;
	}

	private static void addCgArg(List<String> argsList) {
		if (SysConf.CG_TYPE.equals("cha")) {
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "all-reachable:true", }));// �����е�appclass���е��÷���
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.cha", "apponly:true", }));
		} else if (SysConf.CG_TYPE.equals("spark")) {
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.cha", "off", }));
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.spark", "on", }));
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "all-reachable:true", }));// �����е�appclass���е��÷���
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.spark", "apponly:true", }));
		} else {
			logger.error("cg type error");
		}

	}
}
// //����classPath
// argsList.addAll(Arrays.asList(new String[] {
// "-cp", classPath
// }));
// argsList.addAll(Arrays.asList(new String[] {
// "-p", "bb", "off",
// }));
// argsList.addAll(Arrays.asList(new String[] {
// "-p", "jop", "off",
// }));
// argsList.addAll(Arrays.asList(new String[] {
// "-p", "db", "off",
// }));

// argsList.addAll(Arrays.asList(new String[] {
// "-p", "cg", "all-reachable:true",
// }));

// ������Classloader����
// argsList.addAll(Arrays.asList(new String[] {
// "-p", "cg.cha", "apponly:true"
// }));

// argsList.addAll(Arrays.asList(new String[] { "-p", "jb.tr",
// "use-older-type-assigner:true", }));
