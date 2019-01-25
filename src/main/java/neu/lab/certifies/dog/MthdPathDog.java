package neu.lab.certifies.dog;

import java.util.List;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.dog.book.Book;
import neu.lab.certifies.dog.book.MthdPathBook;

public class MthdPathDog extends Dog {

	public MthdPathDog(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected Book buyNodeBook(String nodeName) {
		return new MthdPathBook(nodeName);
	}

	@Override
	protected void printResult() {
		// TODO Auto-generated method stub

	}

	/* 参考jarPathDog的注释
	 * 
	 */
	@Override
	protected void dealLoopNd(String donePos) {
		MthdPathBook doneBook = (MthdPathBook)this.books.get(donePos);
		List<String> circle = this.circleMap.get(donePos);
		for (String node : circle) {//
			List<String> preList = circle.subList(circle.indexOf(node), circle.size());
			Book doingBook = buyNodeBook(node);
			doingBook.copy(doneBook);
			for (int index = preList.size() - 1; index >= 0; index--) {// 将前缀加入
				String mthdNd = preList.get(index);
				doingBook.addNdToAll(mthdNd);
			}
			this.books.put(node, doingBook);
		}

	}

}
