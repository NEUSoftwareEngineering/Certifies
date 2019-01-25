package neu.lab.certifies.risk;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.dog.book.Book;
import neu.lab.certifies.vo.MethodVO;

/**
 * 缩写解释: hm:host method;lm:lib method;lot:lots of;mc:method call;pl:per
 * lib;wrt:write;dir:direct
 * 
 * @author asus
 *
 */
public abstract class RiskCounter {

	protected String type;
	protected String dirPath;
	protected String numFile;
	protected String riskMthdFile;

	protected RiskGraphData riskGraphData;
	protected List<String> nums = new ArrayList<String>();
	protected Set<String> riskMthds = new HashSet<String>();
	protected SysInfo sysInfo;

	public RiskCounter(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
		initType();
		dirPath = SysConf.riskOutPath + type + "/";
		numFile = SysConf.staDir + "nums_" + type + ".txt";
		riskMthdFile = SysConf.staDir + "HostM"  + ".txt";
		riskGraphData = new RiskGraphData(type);
	}

	public void run() throws IOException {
		//createDir();
		calRisk();
		wrtSta();
	}
	public RiskGraphData getRiskGraphData() {
		return this.riskGraphData;
	}
	

	protected abstract void initType();

	protected abstract void calRisk() throws IOException;

	public void createDir() {
		new File(dirPath).mkdirs();
	}

	public void wrtSta() throws IOException {
		PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(numFile)));
		for (String num : nums) {
			printer.println(num);
		}
		printer.close();
		printer = new PrintWriter(new BufferedWriter(new FileWriter(riskMthdFile)));
		for (String riskMthd : riskMthds) {
			printer.println(riskMthd);
		}
		printer.close();
	}

	public void wrtHostCall(PrintWriter printer, String hostMthd) {
		Set<String> inMthds = sysInfo.getMthd(hostMthd).getInMthds();
		StringBuilder sb = new StringBuilder("call from host:");
		int cnt = 0;// 计数
		if (null != inMthds) {
			for (String inMthd : inMthds) {
				if (SysCons.MY_JAR_NAME.equals(sysInfo.getMthd(inMthd).getJar())) {
					sb.append(inMthd + "  ");
					cnt++;
				}
			}
			sb.insert(0, cnt + " ");
			printer.println(sb.toString());
		}

	}

	protected File getWrtFile(String dir, String mthdSig) {
		File outFile = new File(dir + SysUtil.mthdSig2FileName(mthdSig));
		while (outFile.exists()) {// 防止方法名字的重复
			outFile = new File(dir + SysUtil.mthdSig2FileName(mthdSig));
		}
		return outFile;
	}

}
