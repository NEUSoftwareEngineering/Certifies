package neu.lab.certifies.debug;

import java.util.ArrayList;
import java.util.List;

/**
 * 输出宿主的函数的特征x的csv
 * 
 * @author asus
 *
 */
public class CsvWriter {
	public List<String> head;
	public List<Row> rows;

	public CsvWriter() {
		head = new ArrayList<String>();
		rows = new ArrayList<Row>();
		// head.add("method");
		//
		// head.add("hub");
		// head.add("auth");
		//
		// head.add("highHubCall");
		// head.add("highAuthCall");

	}

	public void addCol(String head, List<String> col) {

	}
}

class Row {
	private List<String> dataCon;

	public Row() {
		dataCon = new ArrayList<>();
	}
	public void append(String unit) {
		dataCon.add(unit);
	}
}
