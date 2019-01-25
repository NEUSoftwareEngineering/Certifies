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
		// 将寻找的jar包添加到参数中
		Set<String> jarPaths = Detective.findJarPath(new File(path));

		for (String jarPath : jarPaths) {
			argsList.add("-process-dir");
			argsList.add(jarPath);
		}
		return argsList;
	}

	/**
	 * 得到通用的参数设置
	 * 
	 * @return
	 */
	private static List<String> getGenArg() {
		List<String> argsList = new ArrayList<String>();
		argsList.add("-pp");// 将soot的classPath中的类用于解析
		argsList.add("-ire");// 忽略classPath中的无效实体
		argsList.add("-app");// 所有的类都将作为appClass
		argsList.add("-allow-phantom-refs");// 允许无效的类型解析
		argsList.add("-w");// 整个项目解析

		addCgArg(argsList);

		// 关闭自带的callGraph生成环节
		// argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "off", }));//
		// argsList.addAll(Arrays.asList(new String[] { "-p", "wjop", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "wjap", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "jtp", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "jop", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "jap", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "bb", "off", }));
		// argsList.addAll(Arrays.asList(new String[] { "-p", "tag", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-f", "n", }));// 关闭文件的输出
		// argsList.add("-outjar");//整个项目输出一个.jar

		return argsList;
	}

	private static void addCgArg(List<String> argsList) {
		if (SysConf.CG_TYPE.equals("cha")) {
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "all-reachable:true", }));// 对所有的appclass进行调用分析
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.cha", "apponly:true", }));
		} else if (SysConf.CG_TYPE.equals("spark")) {
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.cha", "off", }));
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.spark", "on", }));
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "all-reachable:true", }));// 对所有的appclass进行调用分析
			argsList.addAll(Arrays.asList(new String[] { "-p", "cg.spark", "apponly:true", }));
		} else {
			logger.error("cg type error");
		}

	}
}
// //设置classPath
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

// 不分析Classloader等类
// argsList.addAll(Arrays.asList(new String[] {
// "-p", "cg.cha", "apponly:true"
// }));

// argsList.addAll(Arrays.asList(new String[] { "-p", "jb.tr",
// "use-older-type-assigner:true", }));
