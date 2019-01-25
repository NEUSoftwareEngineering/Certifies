package neu.lab.certifies;

import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysInfoLoader;
import neu.lab.certifies.debug.RltTester;
import neu.lab.certifies.debug.SqlWriter;
import neu.lab.certifies.debug.SysPrinter;
import neu.lab.certifies.risk.RiskWriter;
import neu.lab.certifies.view.Screen;
import soot.G;

public class CodeAnaMain {
	private static Logger logger = Logger.getRootLogger();
	public static String proPath = "D:\\Project\\fop";
	 
	
	public static void main(String[] args) throws IOException {
		long startT = System.currentTimeMillis();
		 //wrtSys();
		 //rltFlt();
	     //wrtRisk();
		 //wrtDb();
		 //G.reset();
		 //wrtLib();
		   ui();
		   
		long runtime = (System.currentTimeMillis() - startT) / 1000;
		logger.info("all runtime:" + runtime);
	}

	private static void wrtSys() {
		SysInfo sysInfo = SysInfoLoader.loadSysInfo(CodeAnaMain.proPath);
		new SysPrinter(sysInfo).printAll();
	}

	private static void wrtLib() {
		SysInfo sysInfo = SysInfoLoader.loadSysInfo(CodeAnaMain.proPath);
		for (String jar : sysInfo.getAllJar()) {
			System.out.println(jar);
		}
	}

	private static void rltFlt() {
		SysInfo sysInfo = SysInfoLoader.loadSysInfo(CodeAnaMain.proPath);
		new RltTester().wrtAllRlt(sysInfo, "d:/j2j/sparkRlt.txt");
		// new RltTester().writeJ2j(sysInfo);

	}

	private static void wrtDb() {
		SysInfo sysInfo = SysInfoLoader.loadSysInfo(CodeAnaMain.proPath);
		new SqlWriter(sysInfo).wrtSysInfo();
	}

	private static void wrtRisk() {
		SysInfo sysInfo = SysInfoLoader.loadSysInfo(CodeAnaMain.proPath);
		new RiskWriter().write(sysInfo);
	}

	private static void ui() {
		Screen.i().show();
/*		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// …Ë÷√Õ‚π€
					UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
					Screen.i().show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});*/
	}
}
