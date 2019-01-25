package neu.lab.certifies.dog.book;

import java.util.HashSet;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;

public class JarPathBook extends Book {

	private Set<JarPath> records;
	private SysInfo sysInfo;

	public JarPathBook(SysInfo sysInfo, String nodeName) {
		super(nodeName);
		this.sysInfo = sysInfo;
		records = new HashSet<JarPath>();
	}

	@Override
	public void addChild(Book childBook) {
		Set<JarPath> childRecords = (Set<JarPath>) childBook.getRecords();
		for (JarPath path : childRecords) {
			if (!this.records.contains(path)) {
				//TODO �˴���contains������
				JarPath pathCopy = new JarPath(sysInfo);
				pathCopy.addAll(path);
				this.records.add(pathCopy);
			}
		}
	}

	@Override
	public Object getRecords() {
		return this.records;
	}

	@Override
	public void addSelf() {
		if (records.isEmpty()) {// �ýڵ�û���κεĵ���
			JarPath path = new JarPath(sysInfo);
			path.addHead(nodeName);
			records.add(path);
		} else {// �ýڵ��е��ã����Լ���jar����ӵ�ÿһ������·������ǰ��
			addNdToAll(nodeName);
		}

	}

	@Override
	public void addNdToAll(String node) {
		for (JarPath path : records) {
			path.addHead(node);
		}
	}

	@Override
	public void copy(Book book) {
		Set<JarPath> templet = (Set<JarPath>) book.getRecords();
		for (JarPath path : templet) {
			JarPath pathCopy = new JarPath(sysInfo);
			pathCopy.addAll(path);
			this.records.add(pathCopy);
		}
	}
}
