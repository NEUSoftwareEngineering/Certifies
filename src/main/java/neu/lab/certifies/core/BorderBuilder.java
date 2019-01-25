package neu.lab.certifies.core;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.util.Detective;
import neu.lab.certifies.util.NameManager;
import neu.lab.certifies.vo.JarVO;
import neu.lab.certifies.vo.PackageVO;

import java.util.Set;

/**
 * 通过jar包定义系统的边界
 * 
 * @author asus
 *
 */
public class BorderBuilder {
	private static Logger logger = Logger.getRootLogger();
	private SysInfo sysInfo;

	public BorderBuilder(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
	}

	public void geneBorder(String srcPath, String binPath) {
		// 处理源代码项目:有源代码的类为myProject
		JarVO myJar = new JarVO(sysInfo, SysCons.MY_JAR_NAME, "");
		sysInfo.addJar(myJar.getJarSig(), myJar);
		Set<String> srcClses = Detective.findDirCls(new File(srcPath), ".java");
		for (String clsPath : srcClses) {

			String pckName = NameManager.path2pck(clsPath);
			String pckSig = PackageVO.getSig(SysCons.MY_JAR_NAME, pckName);
			String clsSig = pckName + "." + NameManager.path2cls(clsPath);

			sysInfo.addPck(pckSig);
			sysInfo.cls2pck(clsSig, pckSig);
			sysInfo.pck2jar(pckSig, myJar);
		}
		// 处理jar包项目
		Set<JarVO> jars = Detective.findJarVO(sysInfo, new File(binPath));// 得到所有的jar包

		for (JarVO jarVO : jars) {
			sysInfo.addJar(jarVO.getJarSig(), jarVO);
			Set<String> pckClses = Detective.findJarCls(jarVO.getJarPath());
			for (String clsPath : pckClses) {
                try {
				String pckName = NameManager.entry2pck(clsPath);
				String pckSig = PackageVO.getSig(jarVO.getJarSig(), pckName);
				String clsSig = pckName + "." + NameManager.entry2cls(clsPath);

				sysInfo.addPck(pckSig);

				sysInfo.cls2pck(clsSig, pckSig);
				sysInfo.pck2jar(pckSig, jarVO);
                }catch(Exception e) {
                	System.out.println("cant't extra package from:"+clsPath);
                }
			}
		}
		// 处理src代码和jar包重叠的包
		this.meltPck();
	}

	/**
	 * src中的package和jar文件中的package可能指向的是同一个所以应该进行合并
	 * 判断两个package为同一个的思路：1.packageName相同 2.有重叠的Class
	 */
	private void meltPck() {
		for (String jarSig : sysInfo.getAllJar()) {
			if (!SysCons.MY_JAR_NAME.equals(jarSig)) {
				JarVO jarVO = sysInfo.getJar(jarSig);
				Iterator<String> ite = jarVO.getPcks().iterator();
				while (ite.hasNext()) {
					String libPckSig = ite.next();
					PackageVO libPckVO = sysInfo.getPck(libPckSig);
					String inPckSig = PackageVO.getSig(SysCons.MY_JAR_NAME, libPckVO.getPckName());
					PackageVO inPck = sysInfo.getPck(inPckSig);
					if (inPck == null) {// src源码中没有相同包名
						continue;
					} else {
						Set<String> inClses = inPck.getClses();
						Set<String> libClses = libPckVO.getClses();
						boolean isSame = false;
						for (String cls : inClses) {
							if (libClses.contains(cls)) {// 真正的第三方中不会存在src中的类，所以只要有一个重叠就可以视为一样
								isSame = true;
								break;
							}
						}
						if (isSame) {
							// 将libPck的信息复制到inPck
							for (String clsSig : libClses) {
								sysInfo.cls2pck(clsSig, inPckSig);
							}
							// 从jarVO移除pck
							ite.remove();
							sysInfo.delPck(libPckSig);
						}
					}
				}
			}
		}
		delEmptyJar();
		// checkJarPck();
	}

	/**
	 * 删除melt之后不包含任何pck的jar包从系统中删除
	 */
	private void delEmptyJar() {
		Iterator<Entry<String, JarVO>> jarIte = sysInfo.jarIte();
		while (jarIte.hasNext()) {
			Entry<String, JarVO> entry = jarIte.next();
			JarVO jarVO = entry.getValue();
			if (jarVO.getPcks().isEmpty() || jarVO.getClses().isEmpty()) {
				jarIte.remove();
			}
		}
	}

	/**
	 * 对于一个jar文件中的所有package可能有三种情况
	 * 1.所有package都出现在源代码中-》此jar文件完全由源代码生成，应该将从SysInfo.jarTb中移除该jar包
	 * 2.所有package都未出现在源代码中-》此jar文件是一个标准的第三方,不需要进行特殊处理
	 * 3.jar文件中的package可能有一部分出现在源代码中-》混合情况
	 */
	private void checkJarPck() {
		for (String jarSig : sysInfo.getAllJar()) {
			JarVO jarVO = sysInfo.getJar(jarSig);
			Set<String> srcPck = new HashSet<String>();
			Set<String> libPck = new HashSet<String>();
			for (String pckSig : jarVO.getPcks()) {
				PackageVO pckVO = sysInfo.getPck(pckSig);
				if (pckVO == null) {
					logger.warn(jarSig + ":no my pck " + pckSig);
				} else {
					if (SysCons.MY_JAR_NAME.equals(pckVO.getJarSig()))
						srcPck.add(pckSig);
					else
						libPck.add(pckSig);
				}
			}
			logger.debug("============================" + jarSig);
			if (srcPck.isEmpty() && libPck.isEmpty()) {// 空jar
				logger.debug(" is emptyJar\n");
			} else if (!srcPck.isEmpty() && !libPck.isEmpty()) {// 混合jar
				logger.debug(" is mixJar\n");
				logger.debug("++++++srcPck");
				for (String src : srcPck) {
					logger.debug(src + "+++++++");
				}
				logger.debug("\n");
				logger.debug("++++++libPck");
				for (String lib : libPck) {
					logger.debug(lib + "+++++++");
				}
				logger.debug("\n");
			} else {
				if (srcPck.isEmpty()) {
					logger.debug(" is libJar\n");
				}
				if (libPck.isEmpty()) {
					logger.debug(" is srcJar\n");
				}
			}

		}
	}

}
