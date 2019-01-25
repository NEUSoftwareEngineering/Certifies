package neu.lab.certifies.view.tab.host;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

public class StaTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	// ��ӱ�ͷ���������ͣ�����toolTip�����÷�������
	public static String[] tHead = {"HostM"};
	public static Map<String, String> head2tip;// ��ͷ��ע��
	static {
		head2tip = new HashMap<String, String>();
		head2tip.put("HostM", "boundary node");
	}

	public StaTableModel(Object[][] rows) {
		super(rows,tHead);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}
	
}
