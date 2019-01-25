package neu.lab.certifies.dog;

import java.util.List;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.dog.book.Book;
import neu.lab.certifies.dog.book.MthdBook;

public class MthdDog extends Dog {

	public MthdDog(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected Book buyNodeBook(String nodeName) {
		return new MthdBook(nodeName);
	}

	@Override
	protected void printResult() {
		for(String mthd:sysInfo.getHostMthds()) {
			MthdBook book = (MthdBook)this.books.get(mthd);
			book.print();
		}

	}

	@Override
	protected void dealLoopNd(String donePos) {
		MthdBook doneBook = (MthdBook)this.books.get(donePos);
		List<String> circle = this.circleMap.get(donePos);
		for (String node : circle) {//
			Book doingBook = buyNodeBook(node);
			doingBook.copy(doneBook);
			this.books.put(node, doingBook);
		}
	}
}
