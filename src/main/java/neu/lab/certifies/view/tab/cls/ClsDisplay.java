package neu.lab.certifies.view.tab.cls;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.view.ViewCons;
import neu.lab.certifies.view.ViewUtil;
import neu.lab.certifies.view.shop.ClsJarGShop;
import neu.lab.certifies.vo.SubSys;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
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
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class ClsDisplay extends Display {
	private Graph clsG;

	public ClsDisplay() {
		super(new Visualization());

		this.setMinimumSize(new Dimension(ViewCons.FRAME_W - ViewCons.PCK_SEL_W, 0));
		// this.pan((Cons.WIDTH - Cons.PCK_SEL_W)/2, Cons.HEIGHT/2);
		this.pan(400, 350);//设置图的中心

		initClsG();
		m_vis.add(ViewCons.CLS_G, this.clsG);

		m_vis.setInteractive(ViewCons.CLS_EGS, null, false);
		addListeners();
		addRender();
		m_vis.setValue(ViewCons.CLS_NDS, null, VisualItem.SHAPE, new Integer(Constants.SHAPE_ELLIPSE));

		m_vis.putAction("clsColor", getClsColorActs());
		m_vis.putAction("clsLayout", getClsLayoutActs());
		m_vis.putAction("placeJar", getPlaceJarAct());
		m_vis.run("clsColor");
		m_vis.run("clsLayout");
		m_vis.run("placeJar");

	}

	private void addRender() {
		Renderer nodeR = new ShapeRenderer(5);
		DefaultRendererFactory drf = new DefaultRendererFactory();
		drf.setDefaultRenderer(nodeR);
		m_vis.setRendererFactory(drf);
	}

	private void addListeners() {
		addControlListener(new DragControl()); // drag items around
		addControlListener(new PanControl()); // pan with background left-drag
		addControlListener(new ZoomControl()); // zoom with vertical right-drag
		addControlListener(new ZoomToFitControl());
		addControlListener(new FocusControl(1));
		addControlListener(new WheelZoomControl());
		addControlListener(new NeighborHighlightControl());
		m_vis.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(new TupleSetListener() {
			public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
				if (add.length == 1) {
					Screen.i().updateMthdTab(add[0].getString("sig"));
					
				}
			}
		});
		addControlListener(new ControlAdapter() {
			public void itemEntered(VisualItem item, MouseEvent e) {
				if (item.canGetString("sig")) {
					Screen.i().updateLabel(item.getString("sig"));
				}
			}

			public void itemExited(VisualItem item, MouseEvent e) {
				Screen.i().updateLabel(null);
			}
		});
	}

	private void initClsG() {
		SubSys innerCls2Jar = SubSys.getInnerCls2Lib(Screen.i().sysInfo);
		clsG = new ClsJarGShop(Screen.i().sysInfo).getGraph(innerCls2Jar.nodes, innerCls2Jar.edges);
	}

	private ActionList getClsLayoutActs() {
		ActionList layoutActs = new ActionList();
		ForceDirectedLayout force = new ForceDirectedLayout(ViewCons.CLS_G, false, true);
		layoutActs.add(force);
		return layoutActs;
	}

	private ActionList getClsColorActs() {
		ActionList colorActs = new ActionList(Activity.INFINITY);

		// int[] palette = DisplayUtil.getColors(SysInfo.getJarNum());
		DataColorAction colorNode = new DataColorAction(ViewCons.CLS_NDS, "jarSig", Constants.NOMINAL,
				VisualItem.FILLCOLOR, ColorLib.getCategoryPalette(Screen.i().sysInfo.getJarNum()));
		colorNode.add(VisualItem.FIXED, ColorLib.rgb(255, 100, 100));
		colorNode.add(VisualItem.HIGHLIGHT, ColorLib.rgb(145, 2, 100));

		ColorAction colorEdge = new ColorAction(ViewCons.CLS_EGS, VisualItem.STROKECOLOR, ColorLib.gray(200));

		colorActs.add(colorNode);
		colorActs.add(colorEdge);
		colorActs.add(new ColorAction(ViewCons.CLS_EGS, VisualItem.FILLCOLOR, ColorLib.gray(200)));
		colorActs.add(new RepaintAction());

		return colorActs;
	}

	private Action getPlaceJarAct() {
		return new RepaintAction() {
			@Override
			public void run(double frac) {
				Iterator<VisualItem> vIte = m_vis.visibleItems(ViewCons.CLS_NDS);
				while (vIte.hasNext()) {
					VisualItem vItem = vIte.next();
					Tuple t = vItem.getSourceTuple();
					String sig = (String) t.get("jarSig");
					// if (sig.startsWith("cmp")) {
					if (!SysCons.MY_JAR_NAME.equals(sig)) {
						vItem.setSize(3);
						vItem.setX(vItem.getX() + ViewUtil.getRdX());
						vItem.setY(vItem.getY() + 450 + ViewUtil.getRdY());
					}
				}
				m_vis.repaint();
			}
		};
	}

	/**
	 * 将与jar包相关的节点显示(isShow==true)或者不显示(isShow==false)
	 * 
	 * @param jar
	 * @param isShow
	 */
	public void showJar(String jar, boolean isShow) {
		// 改变相关节点
		Predicate ndP = (Predicate) ExpressionParser.parse("jarSig == '" + jar + "'");
		m_vis.setVisible(ViewCons.CLS_NDS, ndP, isShow);
		// 改变相关边
		Predicate egP = (Predicate) ExpressionParser.parse("srcJar =='" + jar + "'||tgtJar == '" + jar + "'");
		m_vis.setVisible(ViewCons.CLS_EGS, egP, isShow);
	}
}
