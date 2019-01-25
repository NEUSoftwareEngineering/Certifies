package neu.lab.certifies.view.tab.cls;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class PckCheckBox extends JCheckBox {
	public PckCheckBox(String name, final ClsDisplay clsDisplay) {
		super(name);
		this.setSelected(true);
		Font f = this.getFont();
		this.setFont(f.deriveFont((float) 18.0));
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox box = (JCheckBox) e.getItem();
				clsDisplay.showJar(box.getText(), box.isSelected());
			}
		});
	}
}
