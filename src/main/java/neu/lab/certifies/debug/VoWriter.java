package neu.lab.certifies.debug;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.Relation;

public class VoWriter {
	Map<String, Integer> mthdIdMap = new HashMap<String, Integer>();
	int idCount = 0;

	public void write(SysInfo sysInfo) {
		// 这是写入CSV的代码
		try {
			writeMthd(sysInfo);
			writeMthdRlt(sysInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeMthdRlt(SysInfo sysInfo) throws IOException {
		final String[] header = { "Source", "Target" };
		CSVFormat format = CSVFormat.DEFAULT.withHeader(header);
		String filePath = "edges.csv";
		CSVPrinter printer = new CSVPrinter(new FileWriter(filePath), format);
		Set<Relation> rlts = sysInfo.getAllMthdRlt();
		for (Relation rlt : rlts) {
			Integer srcId = mthdIdMap.get(rlt.getSrc());
			Integer tgtId = mthdIdMap.get(rlt.getTgt());
			if(null!=srcId&&null!=tgtId) {
				List<String> record = new ArrayList<String>();
				record.add("" + srcId);
				record.add("" + tgtId);
				printer.printRecord(record);
			}
			
		}
		printer.close();
	}

	private void writeMthd(SysInfo sysInfo) throws IOException {
		final String[] header = { "Id", "Name" };
		CSVFormat format = CSVFormat.DEFAULT.withHeader(header);
		String filePath = "nodes.csv";

		Writer out = new FileWriter(filePath);
		CSVPrinter printer = new CSVPrinter(out, format);
		Set<String> mthds = sysInfo.getAllMthd();
		for (String mthd : mthds) {
			List<String> records = new ArrayList<String>();
			records.add("" + idCount);
			records.add(mthd);
			printer.printRecord(records);
			mthdIdMap.put(mthd, Integer.valueOf(idCount));

			idCount++;
		}
		printer.close();
		out.close();
	}
}
