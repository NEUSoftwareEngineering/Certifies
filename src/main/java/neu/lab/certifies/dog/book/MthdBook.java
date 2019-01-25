package neu.lab.certifies.dog.book;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class MthdBook extends Book {
	private static Logger logger = Logger.getRootLogger();
	Set<String> records;// 所有可以到达的节点

	public MthdBook(String nodeName) {
		super(nodeName);
		records = new HashSet<String>();
	}

	@Override
	public void addChild(Book childBook) {
		records.addAll((Set<String>) childBook.getRecords());
	}

	@Override
	public void addSelf() {
		records.add(nodeName);
	}

	@Override
	public void addNdToAll(String node) {
		records.add(node);
	}

	@Override
	public Object getRecords() {
		return records;
	}

	@Override
	public void copy(Book book) {
		records.addAll((Set<String>) book.getRecords());
	}
	
	public void addRecord(String record) {
		records.add(record);
	}

	public void print() {
		logger.debug("rch size:" + records.size());
		for (String mthd : records) {
			logger.debug("***" + mthd);
		}
		logger.debug("\n");

	}

}
