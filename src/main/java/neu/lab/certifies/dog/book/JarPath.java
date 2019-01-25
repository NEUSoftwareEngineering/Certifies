package neu.lab.certifies.dog.book;

import java.util.LinkedList;
import java.util.UUID;

import org.apache.log4j.Logger;

import neu.lab.certifies.core.SysInfo;

public class JarPath extends LinkedList<String> {// �����ݽṹ��֤���ڵ�������Ԫ���ǲ�һ����
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
			if (first.equals(jar))// ���������ӵ�Ԫ�غ����һ��Ԫ����ͬ�����
				return;
		}
		super.addFirst(jar);
	}

	public void addTail(String mthd) {
		// mthds.addLast(mthd);

		String jar = getJar(mthd);
		String last = this.peekLast();
		if (last != null) {
			if (last.equals(jar))// ���������ӵ�Ԫ�غ����һ��Ԫ����ͬ�����
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
