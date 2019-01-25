package neu.lab.certifies.util;

import org.apache.log4j.Logger;

/**
 * ����·��������������·����������ת��
 * 
 * @author asus
 *
 */
public class NameManager {
	private static Logger logger = Logger.getRootLogger(); 
	/**
	 * ��������·������ȡ����
	 * 
	 * @param clsPath
	 *            (D:\cTestSrc\apache-ant-1.10.1\src\main\org\apache\tools\ant\ClassName.java)
	 * @return ClassName
	 */
	public static String path2cls(String clsPath) {
		return clsPath.substring(clsPath.lastIndexOf("\\") + 1, clsPath.length() - 5);
	}

	/**
	 * ��������·������ȡ����
	 * 
	 * @param clsPath
	 *            (D:\cTestSrc\apache-ant-1.10.1\src\main\org\apache\tools\ant\ClassName.java)
	 * @return org.apache.tools.ant
	 */
	public static String path2pck(String clsPath) {
		String pckPath = clsPath.substring(0, clsPath.lastIndexOf("\\"));
		// D:\cTestSrc\apache-ant-1.10.1\src\main\org\apache\tools\ant
		// D:\cTestSrc\apache-maven-3.5.2\maven-core\src\main\java\org\apache\maven
		// D:\cTestSrc\apache-tomcat-7.0.82-src\java\org\apache
		// D:\cTestSrc\commons-email-1.5-src\src\main\java
		if (pckPath.contains("\\org\\")) {// ·���д���\org\
			int index = pckPath.lastIndexOf("\\org\\");
			return pckPath.substring(index + 1).replaceAll("\\\\", ".");

		} else if (pckPath.contains("\\com\\")) {// ·���д���\com\
			int index = pckPath.lastIndexOf("\\com\\");
			return pckPath.substring(index + 1).replaceAll("\\\\", ".");

		} else if (pckPath.contains("\\src\\main\\java\\")) {// maven�����Ľṹ\src\main\java
			int index = pckPath.lastIndexOf("\\src\\main\\java\\");
			return pckPath.substring(index + 15).replaceAll("\\\\", ".");

		} else if (pckPath.contains("\\src\\main\\")) {//·���д���\src\main\
			int index = pckPath.lastIndexOf("\\src\\main\\");
			return pckPath.substring(index + 10).replaceAll("\\\\", ".");

		} else if (pckPath.contains("\\src\\test\\")) {// maven��д���Դ���İ�
			return null;
		} else if(pckPath.contains("\\src\\")){
			int index = pckPath.lastIndexOf("\\src\\");
			return pckPath.substring(index + 5).replaceAll("\\\\", ".");
		}else {
			logger.warn("not sure how to short path:" + pckPath);
			return null;
		}
	}

	/**
	 * @param javax/activation/MimeTypeParameterList.class
	 * @return javax.activation
	 */
	public static String entry2pck(String entryName) {
		return entryName.substring(0, entryName.lastIndexOf("/")).replace("/", ".");
	}

	/**
	 * @param javax/activation/MimeTypeParameterList.class
	 * @return javax.activation
	 */
	public static String entry2cls(String entryName) {
		return entryName.substring(entryName.lastIndexOf("/") + 1, entryName.length() - 6);
	}

}
