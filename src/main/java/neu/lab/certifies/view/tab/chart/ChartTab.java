package neu.lab.certifies.view.tab.chart;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.DefaultXYDataset;

import neu.lab.certifies.risk.RiskCnterChain;
import neu.lab.certifies.risk.RiskGraphData;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.view.ViewCons;

public class ChartTab extends JPanel {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getRootLogger();
	private static String firstRisk = "Size";
	public static String[] riskTypes = { firstRisk, "Touching-area","Reachability","Multi-source","Multi-source transitivity","Penetrability","Hub Degree" };
	private ChartPanel chartP;
	private JPanel radioP;
	private Map<String, RiskGraphData> risk2graphData;

	public ChartTab() {
		this.setLayout(new BorderLayout());

		risk2graphData = new RiskCnterChain(Screen.i().sysInfo).getRisk2graphData();

		initChartPanel();
		this.add(chartP, BorderLayout.CENTER);

		initRadioP();
		this.add(radioP, BorderLayout.EAST);
	}

	private void initRadioP() {
		ButtonGroup bg = new ButtonGroup();
		radioP = new JPanel();
		radioP.setLayout(new BorderLayout());
		Box yBox = Box.createVerticalBox();
		for (String riskType : riskTypes) {
			JRadioButton radio = new JRadioButton(riskType);
			radio.setFont(radio.getFont().deriveFont((float) 18.0));
			if (firstRisk.equals(riskType))
				radio.setSelected(true);
			radio.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					JRadioButton radio = (JRadioButton) e.getItem();
					if (radio.isSelected()) {
						showChart(radio.getText());
					}
				}
			});

			bg.add(radio);
			yBox.add(radio);
		}
		radioP.add(yBox);
		radioP.setPreferredSize(new Dimension(ViewCons.RISK_SEL_W, 0));
	}

	private void showChart(String riskType) {
		chartP.setChart(getChart(riskType));
	}

	private void initChartPanel() {
		// 初始化panel
		chartP = new ChartPanel(getChart(firstRisk));
		chartP.setMouseWheelEnabled(true);
		chartP.setLayout(new BorderLayout());
	}

	private JFreeChart getChart(String riskType) {
		return getHistChart(riskType);
	}

	private JFreeChart getLineChart(String riskType) {
		// 获取数据
		DefaultXYDataset xydataset = new DefaultXYDataset();
		xydataset.addSeries(riskType, risk2graphData.get(riskType).getLineData());
		// 初始化chart
		JFreeChart localJFreeChart = ChartFactory.createXYLineChart("Line Chart", "X", "Y", xydataset,
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();
		localXYPlot.setDomainPannable(true);
		localXYPlot.setRangePannable(true);
		XYLineAndShapeRenderer localXYLineAndShapeRenderer = (XYLineAndShapeRenderer) localXYPlot.getRenderer();
		localXYLineAndShapeRenderer.setBaseShapesVisible(true);
		localXYLineAndShapeRenderer.setBaseShapesFilled(true);
		NumberAxis localNumberAxis = (NumberAxis) localXYPlot.getRangeAxis();
		localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return localJFreeChart;
	}

	private JFreeChart getHistChart(String riskType) {
		int intervalNum = 20;
		// 获取数据
		HistogramDataset dataset = new HistogramDataset();
		dataset.addSeries(riskType, risk2graphData.get(riskType).getHistData(), intervalNum);
		// 初始化chart
		JFreeChart localJFreeChart = ChartFactory.createHistogram(null, null, null, dataset,
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
		return localJFreeChart;
	}

}
