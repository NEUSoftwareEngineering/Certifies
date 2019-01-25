package neu.lab.certifies.view.tab.sta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

public class StaTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	// 添加表头，设置类型，设置toolTip，设置返回数据
	public static String[] tHead = { "jarSig", "mthdCnt", "clsCnt", "dirJarM", "rDJM", "acsJarM", "rAJM", "dirJarC",
			"rDJC", "acsJarC", "rAJC", "dirHostM", "rDHM", "acsHostM", "rAHM", "dirHostC", "rDHC", "acsHostC", "rAHC",
			"passedM", "rPDM", "passM", "rPM" };
	public static Map<String, String> head2tip;// 表头的注释
	public static Set<String> iCols;// Integer类型的列
	public static Set<String> fCols;// float类型的列
	static {
		fCols = new HashSet<String>();
		fCols.add("rDJM");
		fCols.add("rAJM");
		fCols.add("rDJC");
		fCols.add("rAJC");

		fCols.add("rDHM");
		fCols.add("rAHM");
		fCols.add("rDHC");
		fCols.add("rAHC");
		fCols.add("rPDM");
		fCols.add("rPM");

		iCols = new HashSet<String>();
		iCols.add("mthdCnt");
		iCols.add("clsCnt");

		iCols.add("dirJarM");
		iCols.add("acsJarM");
		iCols.add("dirJarC");
		iCols.add("acsJarC");

		iCols.add("dirHostM");
		iCols.add("acsHostM");
		iCols.add("dirHostC");
		iCols.add("acsHostC");
		iCols.add("passedM");
		iCols.add("passM");
		// public static String[] tHead = { "jarSig", "mthdCnt", "clsCnt", "dirJarM",
		// "rDJM", "acsJarM", "rAJM", "dirJarC",
		// "rDJC", "acsJarC", "rAJC", "dirHostM", "rDHM", "acsHostM", "rAHM",
		// "dirHostC", "rDHC", "acsHostC", "rAHC",
		// "passedM", "rPDM", "passM", "rPM" };
		head2tip = new HashMap<String, String>();
		head2tip.put("jarSig", "lib.name");
		head2tip.put("mthdCnt", "lib.methodNumber(all)");
		head2tip.put("clsCnt", "lib.classNumber(all)");
		
		head2tip.put("dirJarM", "lib.methodNumber(host project directly call)");
		head2tip.put("rDJM", "lib.methodNumber(host project directly call)/lib.methodNumber(all)");
		head2tip.put("acsJarM", "lib.methodNumber(host project directly or indirectly call)");
		head2tip.put("rAJM", "lib.methodNumber(host project directly or indirectly call)/lib.methodNumber(all)");

		head2tip.put("dirJarC", "lib.classNumber(host project directly call)");
		head2tip.put("rDJC", "lib.classNumber(host project directly call)/lib.classNumber(all)");
		head2tip.put("acsJarC", "lib.classNumber(host project directly or indirectly call)");
		head2tip.put("rAJC", "lib.classNumber(host project directly or indirectly call)/lib.classNumber(all)");
		
		head2tip.put("dirHostM", "host.methodNumber(directly call lib)");
		head2tip.put("rDHM", "host.methodNumber(directly call lib)/host.methodNumber(all)");
		head2tip.put("acsHostM", "host.methodNumber(directly or indirectly call lib)");
		head2tip.put("rAHM", "host.methodNumber(directly or indirectly call lib)/host.methodNumber(all)");
		
		head2tip.put("dirHostC", "host.classNumber(directly call lib)");
		head2tip.put("rDHC", "host.classNumber(directly call lib)/host.classNumber(all)");
		head2tip.put("acsHostC", "host.classNumber(directly or indirectly call lib)");
		head2tip.put("rAHC", "host.classNumber(directly or indirectly call lib)/host.classNumber(all)");
		
		head2tip.put("passedM", "lib.methodNumber(directly or indirectly call otherLib)");
		head2tip.put("rPDM", "lib.methodNumber(directly or indirectly call otherLib)/lib.methodNumber(all)");
		head2tip.put("passM", "host.methodNumber(call otherLib through lib)");
		head2tip.put("rPM", "host.methodNumber(call otherLib through lib)/host.methodNumber(all)");

	}

	public StaTableModel(Object[][] rows) {
		super(rows, tHead);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (iCols.contains(this.getColumnName(columnIndex)))
			return Integer.class;
		if (fCols.contains(this.getColumnName(columnIndex)))
			return Float.class;
		return String.class;
	}
}
