package neu.lab.certifies.view.tab.mthd;

import java.awt.event.MouseEvent;

import javax.swing.border.EtchedBorder;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.view.ViewCons;
import neu.lab.certifies.view.shop.MthdGShop;
import neu.lab.certifies.vo.ClosureSys;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class MthdDisplay extends Display {
	public Graph mthdG;
	Screen screen;

	public MthdDisplay(Screen screen) {
		super(new Visualization());
		this.screen = screen;
		setSize(ViewCons.FRAME_W, 0);
		addListeners();
		Renderer nodeR = new ShapeRenderer(10);// 内部小圆圈的大小
		DefaultRendererFactory drf = new DefaultRendererFactory();
		drf.setDefaultRenderer(nodeR);
		m_vis.setRendererFactory(drf);
		m_vis.putAction("mthdColor", getMthdColorActs());
		m_vis.putAction("mthdLayout", getMthdLayoutActs());
		this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
	}

	public void updateView(String clsSig) {
		initMthdG(clsSig);
		m_vis.removeGroup(ViewCons.MTHD_G);
		m_vis.add(ViewCons.MTHD_G, mthdG);
		m_vis.run("mthdColor");
		m_vis.run("mthdLayout");
	}

	public void initMthdG(String clsSig) {
		ClosureSys subSys = ClosureSys.geneClourseSys(Screen.i().sysInfo,clsSig);
//		subSys.print();
		this.mthdG = new MthdGShop(Screen.i().sysInfo).getGraph(subSys.nodes, subSys.edges);
	}

	public ActionList getMthdLayoutActs() {
		ActionList layoutActs = new ActionList();
		layoutActs.add(new ForceDirectedLayout(ViewCons.MTHD_G, false, true));
		return layoutActs;
	}

	public ActionList getMthdColorActs() {
		ActionList colorActs = new ActionList();
//		int[] palette = DisplayUtil.getColors(SysInfo.getJarNum());
		DataColorAction fill = new DataColorAction(ViewCons.MTHD_NDS, "jarSig", Constants.NOMINAL, VisualItem.FILLCOLOR,
				ColorLib.getCoolPalette(Screen.i().sysInfo.getJarNum()));
		ColorAction colorEdge = new ColorAction(ViewCons.MTHD_EGS, VisualItem.STROKECOLOR, ColorLib.gray(200));
		colorActs.add(fill);
		colorActs.add(colorEdge);
		return colorActs;
	}

	public void addListeners() {
		addControlListener(new DragControl()); // drag items around
		addControlListener(new PanControl()); // pan with background left-drag
		addControlListener(new ZoomControl()); // zoom with vertical right-drag
		addControlListener(new ZoomToFitControl());
		addControlListener(new FocusControl(1));
		addControlListener(new WheelZoomControl());
		addControlListener(new NeighborHighlightControl());
		addControlListener(new ControlAdapter() {
			public void itemEntered(VisualItem item, MouseEvent e) {
				if (item.canGetString("sig")) {
					screen.updateLabel(item.getString("sig"));
				}
			}

			public void itemExited(VisualItem item, MouseEvent e) {
				screen.updateLabel(null);
			}
		});
	}
}
