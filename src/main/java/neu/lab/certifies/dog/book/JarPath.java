package neu.lab.certifies.dog.book;

import java.util.LinkedList;
import java.util.UUID;

import org.apache.log4j.Logger;

import neu.lab.certifies.core.SysInfo;

public class JarPath extends LinkedList<String> {// 该数据结构保证相邻的两个的元素是不一样的
	private static Logger logger = Logger.getRootLogger();
	private static final long serialVersionUID = 1L;
	// public LinkedList<String> mthds = new LinkedList<String>();
	SysInfo sysInfo;
	public JarPath(SysInfo sysInfo) {
		super();
		this.sysInfo = sysInfo;
	}
	public void addHead(String mthd) {
		// mthds.addFirst(mthd);

		String jar = getJar(mthd);
		String first = this.peekFirst();
		if (first != null) {
			if (first.equals(jar))// 如果即将添加的元素和最后一个元素相同则不添加
				return;
		}
		super.addFirst(jar);
	}

	public void addTail(String mthd) {
		// mthds.addLast(mthd);

		String jar = getJar(mthd);
		String last = this.peekLast();
		if (last != null) {
			if (last.equals(jar))// 如果即将添加的元素和最后一个元素相同则不添加
				return;
		}
		super.addLast(jar);
	}


	private String getJar(String mthd) {
		return sysInfo.getMthd(mthd).getJar();
	}

	public void print() {
		for (String jar : this) {
			logger.debug(jar + "->");
		}
		logger.debug("\n");
	}

}
