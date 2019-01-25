package neu.lab.certifies.view.tab.cls;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.view.ViewCons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JList;

public class ClsTab extends JPanel {
	ClsDisplay clsDisplay;
	JScrollPane pckCheckPanel;

	/**
	 * Create the panel.
	 */
	public ClsTab() {
		super();
		this.setLayout(new BorderLayout());
		clsDisplay = new ClsDisplay();
		this.add(clsDisplay, BorderLayout.CENTER);

		pckCheckPanel = new JScrollPane();
		pckCheckPanel.setPreferredSize(new Dimension(ViewCons.PCK_SEL_W, 0));
		this.setPckCheckBox();
		this.add(pckCheckPanel, BorderLayout.EAST);
	}

	public void initView() {

	}

	private void setPckCheckBox() {
		Set<String> names = Screen.i().sysInfo.getAllJar();
		JPanel innerP = new JPanel();
		Box b = Box.createVerticalBox();
		for (String name : names) {
			b.add(Box.createVerticalStrut(10));
			b.add(new PckCheckBox(name, clsDisplay));
		}
		innerP.add(b);
		pckCheckPanel.setViewportView(innerP);
	}

}
