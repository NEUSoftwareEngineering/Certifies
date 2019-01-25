package neu.lab.certifies.view;

import java.awt.Dialog;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProBarDialog extends JDialog {
	private JPanel panel;

	public ProBarDialog(JFrame frame) {
		super(frame, true);
		this.setSize(ViewCons.DIALOG_W, ViewCons.DIALOG_H);
		panel = new JPanel();
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		panel.add(new JLabel("系统分析:"));
		panel.add(progressBar);
		this.setContentPane(panel);
		this.setLocation(ViewCons.DIALOG_X, ViewCons.DIALOG_Y);
		// this.setUndecorated(true);//设置无边框
		this.setVisible(true);
	}
}
