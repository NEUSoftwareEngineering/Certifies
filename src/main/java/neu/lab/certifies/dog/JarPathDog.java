package neu.lab.certifies.dog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.dog.book.Book;
import neu.lab.certifies.dog.book.JarPath;
import neu.lab.certifies.dog.book.JarPathBook;
import neu.lab.certifies.vo.MethodVO;

/**
 * Ѱ�����е�jar������˳��
 * 
 * @author asus
 *
 */
public class JarPathDog extends Dog {

	public JarPathDog(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected Book buyNodeBook(String nodeName) {
		return new JarPathBook(sysInfo,nodeName);
	}

	@Override
	protected void printResult() {
		Set<JarPath> result = new HashSet<JarPath>();
		for (String mthd : sysInfo.getHostMthds()) {
			JarPathBook book = (JarPathBook) this.books.get(mthd);
			Set<JarPath> paths = (Set<JarPath>)book.getRecords();
			for (JarPath path : paths) {
				result.add(path);
			}
		}
		for (JarPath path : result) {
			path.print();
		}

	}

	@Override
	protected void dealLoopNd(String donePos) {
		JarPathBook doneBook = (JarPathBook)this.books.get(donePos);
		List<String> circle = this.circleMap.get(donePos);
		for (String node : circle) {//
			// A->B->C->Aʱ��B��jar���õ���B.jar+C.jar+A��jar���е��ã�C��jar���õ���C.jar+A��jar���е��ã�
			List<String> preList = circle.subList(circle.indexOf(node), circle.size());
			Book doingBook = buyNodeBook(node);
			doingBook.copy(doneBook);
			for (int index = preList.size() - 1; index >= 0; index--) {// ��ǰ׺����
				String mthdNd = preList.get(index);
				doingBook.addNdToAll(mthdNd);
			}
			this.books.put(node, doingBook);
		}
	}
}
