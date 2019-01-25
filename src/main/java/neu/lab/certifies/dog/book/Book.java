package neu.lab.certifies.dog.book;

import java.util.Set;

/**
 * 抽象数据结构：节点的工作记录，记录dog在每个节点上工作的结果
 * 
 * @author asus
 *
 */
public abstract class Book {
	protected String nodeName;
	public Book(String nodeName) {
		this.nodeName = nodeName;
	}
	public abstract void addChild(Book childBook);//将子级node的手册copy到自己的手册中
	public abstract void addSelf();//在手册完成时，将自己的节点信息加入到手册
	public abstract void addNdToAll(String node);//为book的每条record添加node;
	public abstract Object getRecords();//得到book中的每条记录
	public abstract void copy(Book book);//得到book中的每条记录
}
