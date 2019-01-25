package neu.lab.certifies.view.tab.host;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

public class StaTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	// 添加表头，设置类型，设置toolTip，设置返回数据
	public static String[] tHead = {"HostM"};
	public static Map<String, String> head2tip;// 表头的注释
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
