package neu.lab.certifies.dog.book;

import java.util.HashSet;
import java.util.Set;

public class MthdPathBook extends Book {
	private Set<MthdPath> records;

	public MthdPathBook(String nodeName) {
		super(nodeName);
		this.records = new HashSet<MthdPath>();
	}

	@Override
	public void addChild(Book childBook) {
		Set<MthdPath> childRecords = (Set<MthdPath>) childBook.getRecords();
		for (MthdPath path : childRecords) {
			if (!records.contains(path)) {
				MthdPath copyPath = new MthdPath();
				copyPath.addAll(path);
				this.records.add(path);
			}
		}

	}

	@Override
	public void addSelf() {
		if (records.isEmpty()) {
			MthdPath path = new MthdPath();
			path.add(nodeName);
			records.add(path);
		} else {
			addNdToAll(nodeName);
		}

	}

	@Override
	public void addNdToAll(String node) {
		for (MthdPath mthdPath : records) {
			mthdPath.add(node);
		}

	}

	@Override
	public Object getRecords() {
		return this.records;
	}

	@Override
	public void copy(Book book) {
		Set<MthdPath> templet = (Set<MthdPath>) book.getRecords();
		for (MthdPath path : templet) {
				MthdPath copyPath = new MthdPath();
				copyPath.addAll(path);
				this.records.add(path);
		}

	}

}
