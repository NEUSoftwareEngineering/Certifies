package neu.lab.certifies.debug;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.view.ViewCons;
import neu.lab.certifies.vo.MethodVO;
import neu.lab.certifies.vo.Relation;

/**
 * 关于方法过滤的测试
 * 
 * @author asus
 *
 */
public class RltTester {
	private static Logger logger = Logger.getLogger("dualCall");

	/**
	 * 打印某个方法的调用中重名的方法
	 */
	public void printDual(SysInfo sysInfo) {
		for (String mthd : sysInfo.getAllMthd()) {
			StringBuilder sb = new StringBuilder("");
			MethodVO mthdVO = sysInfo.getMthd(mthd);
			Map<String, Integer> nameCnts = mthdVO.calNameCnt();
			for (String name : nameCnts.keySet()) {
				if (nameCnts.get(name) > 1) {
					for (String outMthd : mthdVO.getOutMthds()) {
						if (outMthd.contains(name)) {
							sb.append(outMthd + "\n");
						}
					}
					sb.append("\n");
				}
			}
			String result = sb.toString();
			if (!"".equals(result)) {
				logger.debug("=================" + mthd);
				logger.debug(result);
			}
		}

	}

	/**
	 * 统计：方法的调用中，所有的nameCnt的分布情况
	 */
	public void wrtDualSta(SysInfo sysInfo) {
		List<String> cnts = new ArrayList<String>();
		for (String mthd : sysInfo.getAllMthd()) {
			MethodVO mthdVO = sysInfo.getMthd(mthd);
			Map<String, Integer> nameCnts = mthdVO.calNameCnt();
			for (String name : nameCnts.keySet()) {
				cnts.add("" + nameCnts.get(name));
			}
		}
		for (String cnt : cnts) {
			logger.debug(cnt);
		}
	}

	/**
	 * 写出所有method-》method对应的jar-》jar
	 */
	public void writeJ2j(SysInfo sysInfo) {
		Set<Relation> result = new HashSet<Relation>();
		for (Relation rlt : sysInfo.getAllMthdRlt()) {
			String srcJar = sysInfo.getMthd(rlt.getSrc()).getJar();
			String tgtJar = sysInfo.getMthd(rlt.getTgt()).getJar();
			if (!srcJar.equals(tgtJar))
				result.add(new Relation(srcJar, tgtJar));
		}
		try {
			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter("d:/j2j/j2j_cg.txt")));
			for (Relation rlt : result) {
				printer.println(getName(rlt.getSrc()));
				printer.println("-" + getName(rlt.getTgt()));
			}
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * plexus-utils-3.1.0.jar 转化为plexus-utils
	 * 
	 * @return
	 */
	private String getName(String jarSig) {
		Pattern pattern = Pattern.compile(".*-\\d");
		Matcher m = pattern.matcher(jarSig);
		if (!m.find())//
			return jarSig;
		String s = m.group();// plexus-utils-3
		return s.substring(0, s.length() - 2);
	}

	private List<String> readFile(String path) {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			while (line != null) {
				if (!"".equals(line))
					lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	public void wrtAllRlt(SysInfo sysInfo,String putPath) {
		try {
			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(putPath)));
			for(Relation rlt:sysInfo.getAllMthdRlt()) {
				printer.println(rlt.getSrc());
				printer.println("-"+rlt.getTgt());
			}
			printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	
	}

	public static void main(String[] args) {
		new RltTester().draw();

	}

	private void draw() {
		List<String> lines = readFile("d:/j2j/dualCnt.csv");
		double[] nums = new double[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			nums[i] = Double.valueOf(lines.get(i));
		}
		double sum = 0;
		for (double d : nums) {
			sum = sum + d;
		}
		System.out.println("sum:" + sum);
		int intervalNum = 100;
		// 获取数据
		HistogramDataset dataset = new HistogramDataset();
		dataset.addSeries("dualCnt", nums, intervalNum);
		// 初始化chart
		JFreeChart localJFreeChart = ChartFactory.createHistogram("Histogram Demo 1", null, null, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();
		localXYPlot.setDomainPannable(true);
		localXYPlot.setRangePannable(true);
		localXYPlot.setForegroundAlpha(0.85F);
		NumberAxis localNumberAxis = (NumberAxis) localXYPlot.getRangeAxis();
		localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		XYBarRenderer localXYBarRenderer = (XYBarRenderer) localXYPlot.getRenderer();
		localXYBarRenderer.setDrawBarOutline(false);
		localXYBarRenderer.setBarPainter(new StandardXYBarPainter());
		localXYBarRenderer.setShadowVisible(false);
		JPanel localChartPanel = new ChartPanel(localJFreeChart);

		JFrame frame = new JFrame("Code Analysis");
		frame.setPreferredSize(new Dimension(ViewCons.FRAME_W, ViewCons.FRAME_H));
		frame.setLocation(ViewCons.FRAME_X, ViewCons.FRAME_Y);// 设定窗口出现位置

		frame.setContentPane(localChartPanel);

		frame.pack();
		frame.setVisible(true); // show the window
	}

}
