package neu.lab.certifies.sootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.util.Detective;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.options.Options;
import soot.util.Chain;
import soot.util.queue.QueueReader;

public class ConflictTest {
	private static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) {
		String path = "D:\\cEnvironment\\repository\\neu\\lab\\plug.testcase.homemade.host\\1.0";
		String binPath = path + "\\plug.testcase.homemade.host-1.0.jar";

		List<String> argsList = getArgs(binPath);
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new TestTransformer()));

		args = argsList.toArray(new String[0]);
		Long startTime = System.currentTimeMillis();
		soot.Main.main(args);// 解析系统中存在的node以及node之间的关系

		logger.info("soot runTime:" + ((System.currentTimeMillis() - startTime) / 1000));

	}

	private static List<String> getArgs(String path) {
		// String[] jarFilePaths = Detective.findJarPath(new File(path)).toArray(new
		// String[0]);
		// String[] jarFilePaths = { "D:\\cTestWs\\jxpath\\main\\commons-jxpath-1.3.jar"
		// };
		List<String> argsList = new ArrayList<String>();
		argsList.add("-process-dir");
		argsList.add(path);
		addGenArg(argsList);
		addCgArgs(argsList);
		addIgrArgs(argsList);

		return argsList;
	}

	protected static void addCgArgs(List<String> argsList) {
		argsList.addAll(Arrays.asList(new String[] { "-p", "cg", "off", }));

		// argsList.addAll(Arrays.asList(new String[] { "-p", "cg",
		// "all-reachable:true", }));// 对所有的appclass进行调用分析
		// argsList.addAll(Arrays.asList(new String[] { "-p", "cg.cha", "apponly:true",
		// }));
	}

	protected static void addClassPath(List<String> argsList, String[] jarFilePaths) {
		for (String jarFilePath : jarFilePaths) {
			argsList.add("-process-dir");
			argsList.add(jarFilePath);
		}

	}

	protected static void addGenArg(List<String> argsList) {

		// argsList.add("-pp");// 将soot的classPath中的类用于解析
		argsList.add("-ire");// 忽略classPath中的无效实体
		argsList.add("-app");// 所有的类都将作为appClass
		argsList.add("-allow-phantom-refs");// 允许无效的类型解析
		argsList.add("-w");// 整个项目解析

	}

	protected static void addIgrArgs(List<String> argsList) {
		argsList.addAll(Arrays.asList(new String[] { "-p", "wjop", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-p", "wjap", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-p", "jtp", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-p", "jop", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-p", "jap", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-p", "bb", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-p", "tag", "off", }));
		argsList.addAll(Arrays.asList(new String[] { "-f", "n", }));// 关闭文件的输出
	}
}

//class MthdGetter extends SceneTransformer {
//
//	@Override
//	protected void internalTransform(String phaseName, Map<String, String> options) {
//		// TODO Auto-generated method stub
//
//	}
//
//}

class TestTransformer extends SceneTransformer {
	private static Logger logger = Logger.getRootLogger();

	public TestTransformer() {
		super();
	}

	@Override
	protected void internalTransform(String phaseName, Map option) {
		Map<String, String> cgMap = new HashMap<String, String>();
		cgMap.put("enabled", "true");
		cgMap.put("apponly", "true");
		cgMap.put("all-reachable", "true");
//		List<SootMethod> entryMthds = new ArrayList<SootMethod>();
		for (SootClass sootClass : Scene.v().getApplicationClasses()) {
			System.out.println(sootClass.getName());
//			if (entryClses.contains(sootClass.getName())) {// entry class
//				for (SootMethod method : sootClass.getMethods()) {
//					entryMthds.add(method);
//				}
//			}
		}
//		Scene.v().setEntryPoints(entryMthds);
		CHATransformer.v().transform("wjtp", cgMap);
		CallGraph cg = Scene.v().getCallGraph();

		Iterator<Edge> ite = cg.iterator();
		while (ite.hasNext()) {
			Edge edge = ite.next();
			System.out.println(edge.src().getSignature()+"->"+edge.tgt().getSignature());
		}
	}

	private Set<String> getJarCls(String jarPath) {
		Set<String> jarClses = new HashSet<String>();
		for (String cls : SourceLocator.v().getClassesUnder(jarPath)) {
			jarClses.add(cls);
		}
		System.out.println("entry class size:" + jarClses.size());
		return jarClses;
	}
}
// logger.debug("call from PtTransformer");
// Set<String> entryClses = getJarCls(
// "D:\\cWsFile\\projectLib\\smallTest\\bin\\plug.testcase.homemade.host\\1.0\\plug.testcase.homemade.host-1.0.jar");
// Map<String, String> cgMap = new HashMap<String, String>();
// cgMap.put("enabled", "true");
// cgMap.put("apponly", "true");
// List<SootMethod> entryMthds = new ArrayList<SootMethod>();
// for (SootClass sootClass : Scene.v().getApplicationClasses()) {
// if (entryClses.contains(sootClass.getName())) {
// for (SootMethod method : sootClass.getMethods()) {
// entryMthds.add(method);
// }
//
// }
// Scene.v().setEntryPoints(entryMthds);
// CHATransformer.v().transform("wjtp", cgMap);
// CallGraph cg = Scene.v().getCallGraph();
// Iterator<Edge> ite = cg.iterator();
// while (ite.hasNext()) {
// Edge edge = ite.next();
// logger.info(edge);
// }
// QueueReader<MethodOrMethodContext> entryRchMthds =
// Scene.v().getReachableMethods().listener();
// while(entryRchMthds.hasNext()) {
// SootMethod method = entryRchMthds.next().method();
// logger.info(method);
// }
// logger.info("-------------");
// logger.info(Options.v().soot_classpath());
// logger.info(Scene.v().defaultClassPath());
//
// }
