package neu.lab.certifies.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.JarVO;

/**
 * @author asus ����Ѱ��package
 */
public class Detective {
	/**
	 * �ҳ�ָ���ļ����ڲ�������jar���������ļ��е����ļ��У�
	 * 
	 * @param fathFile
	 * @return
	 */
	public static Set<String> findJarPath(File fathFile) {
		Set<String> result = new HashSet<String>();
		File[] children = fathFile.listFiles();
		for (File child : children) {
			String jarPath = child.getAbsolutePath();
			if (jarPath.endsWith(".jar")) {
				result.add(jarPath);
			}
		}
		for (File child : children) {
			if (child.isDirectory()) {
				result.addAll(findJarPath(child));
			}
		}
		return result;
	}
	/**
	 * �ҳ�ָ���ļ����ڲ�������jar���������ļ��е����ļ��У�
	 * 
	 * @param fathFile
	 * @return
	 */
	public static Set<JarVO> findJarVO(SysInfo sysInfo,File fathFile) {
		Set<JarVO> result = new HashSet<JarVO>();
		File[] children = fathFile.listFiles();
		for (File child : children) {
			String jarPath = child.getAbsolutePath();
			String jarName = child.getName();
			if (jarPath.endsWith(".jar")) {
				result.add(new JarVO(sysInfo,jarName, jarPath));
			}
		}
		for (File child : children) {
			if (child.isDirectory()) {
				result.addAll(findJarVO(sysInfo,child));
			}
		}
		return result;
	}

	/**
	 * �ҳ��ļ����е�����Class�ļ�
	 * 
	 * @param fathFile
	 * @param clsSuf:��Ĵ�����ʽ��.class����.java
	 * @return
	 */
	public static Set<String> findDirCls(File fathFile, String clsSuf) {
		Set<String> result = new HashSet<String>();
		File[] children = fathFile.listFiles();
		for (File child : children) {
			String wholePath = child.getAbsolutePath();
			if (wholePath.endsWith(clsSuf)) {
				result.add(wholePath);
			}
		}
		for (File child : children) {
			if (child.isDirectory()) {
				result.addAll(findDirCls(child, clsSuf));
			}
		}
		return result;
	}

	/**
	 * Ѱ���ļ����е�package
	 * 
	 * @param fathFile
	 * @return
	 */
	private static Set<String> findDirPck(File fathFile) {
		Set<String> result = new HashSet<String>();
		File[] children = fathFile.listFiles();
		for (File pckPath : children) {
			String wholePath = pckPath.getAbsolutePath();
			if (wholePath.endsWith(".java")) {
				String pckName = NameManager.path2pck(wholePath);
				if (null != pckName) {
					result.add(pckName);
				}
				break;
			}
		}
		for (File child : children) {
			if (child.isDirectory()) {
				result.addAll(findDirPck(child));
			}
		}
		return result;
	}
	public static Set<String> findJarCls(String jarPath) {
		Set<String> result = new HashSet<String>();
		JarFile jf = null;
		try {
			jf = new JarFile(jarPath);
			Enumeration<JarEntry> e = jf.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				if (entry.getName().endsWith(".class")) {
					// entry.getName()->javax/activation/MimeTypeParameterList.class
					// ���jar��
					result.add(entry.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			jf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Set<String> findJarPck(String jarPath) {
		Set<String> result = new HashSet<String>();
		JarFile jf = null;
		try {
			jf = new JarFile(jarPath);
			Enumeration<JarEntry> e = jf.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				if (entry.getName().endsWith(".class")) {
					// entry.getName()->javax/activation/MimeTypeParameterList.class
					// ���jar��
					result.add(NameManager.entry2pck(entry.getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			jf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
