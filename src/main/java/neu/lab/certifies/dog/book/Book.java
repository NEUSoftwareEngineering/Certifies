package neu.lab.certifies.dog.book;

import java.util.Set;

/**
 * �������ݽṹ���ڵ�Ĺ�����¼����¼dog��ÿ���ڵ��Ϲ����Ľ��
 * 
 * @author asus
 *
 */
public abstract class Book {
	protected String nodeName;
	public Book(String nodeName) {
		this.nodeName = nodeName;
	}
	public abstract void addChild(Book childBook);//���Ӽ�node���ֲ�copy���Լ����ֲ���
	public abstract void addSelf();//���ֲ����ʱ�����Լ��Ľڵ���Ϣ���뵽�ֲ�
	public abstract void addNdToAll(String node);//Ϊbook��ÿ��record���node;
	public abstract Object getRecords();//�õ�book�е�ÿ����¼
	public abstract void copy(Book book);//�õ�book�е�ÿ����¼
}
