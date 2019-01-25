package neu.lab.certifies.risk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;

public class RiskWriter {
	public void write(SysInfo sysInfo) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("d:/fop_lib.txt")));
			// for (String mthd : sysInfo.getAllMthd()) {
			// if (!SysCons.MY_JAR_NAME.equals(sysInfo.getMthd(mthd).getJar()))
			// writer.println(format(mthd));
			// }
			for (String mthd : sysInfo.getHostMthds()) {
				writer.println(format(mthd));
				Set<String> outs = sysInfo.getMthd(mthd).getOutMthds();
				if (outs != null) {
					for (String out : outs) {
						if (sysInfo.isLibMthd(out))
							writer.println("-" + format(out));
					}
				}

			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String format(String mthd) {
		int i = mthd.indexOf(":");
		String cClsName = mthd.substring(0, i);// <org.apache.maven.repository.legacy.DefaultWagonManager
		String clsName = sig2name(cClsName) + ".java";
		String leftStr = mthd.substring(i);

		i = leftStr.indexOf(" ", 2);
		String cRetType = leftStr.substring(2, i);
		String retType = sig2name(cRetType);
		leftStr = leftStr.substring(i);

		i = leftStr.indexOf("(");
		String mthdName = leftStr.substring(1, i);
		leftStr = leftStr.substring(i);

		String argsStr = leftStr.substring(1, leftStr.length() - 2);
		String[] args = argsStr.split(",");
		argsStr = "";
		for (String arg : args) {
			argsStr = argsStr + "," + sig2name(arg);
		}
		argsStr = argsStr.substring(1);
		return clsName + "  " + retType + " " + mthdName + "  " + "(" + argsStr + ")";
	}

	public String sig2name(String sig) {
		int i = sig.lastIndexOf(".");
		if (i == -1)
			return sig;
		return sig.substring(i + 1);
	}

	// public static void main(String[] args) {
	// String m = "<org.apache.maven.wagon.proxy.ProxyInfo: void
	// setPassword(java.lang.String)>";
	// String s = new RiskWriter().format(m);
	// System.out.println(s);
	// }
}
