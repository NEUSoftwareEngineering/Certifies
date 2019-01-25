package neu.lab.certifies.view;

import java.awt.Toolkit;

public class ViewCons {
	// Frame���������
	public static final int FRAME_W = 1300;
	public static final int FRAME_H = 800;
	public static final int FRAME_X = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - ViewCons.FRAME_W)
			/ 2);//λ����Ļ���м�
	public static final int FRAME_Y = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()
			- ViewCons.FRAME_H) / 5);//λ����Ļ���м�
	// ���·�����ʾ��
	public static final int JLABEL_W = 320;
	public static final int JLABEL_H = 30;

	////////////// class-tab 
	public static final int PCK_SEL_W = 300;//�Ҳ��jar����ѡ��

	public static final String CLS_G = "clsG";
	public static final String CLS_NDS = "clsG.nodes";
	public static final String CLS_EGS = "clsG.edges";

	public static final String MTHD_G = "mthdG";
	public static final String MTHD_NDS = "mthdG.nodes";
	public static final String MTHD_EGS = "mthdG.edges";

	public static final String EG_ID = "id";
	public static final String EG_SRC = "srcId";
	public static final String EG_TGT = "tgtId";

	//////////////jarsta-tab
	public static final int TAB_ROW_H = 32;// ���ÿ�еĸ߶�
	
	/////////////chart-tab
	public static final int RISK_SEL_W = 200;//�Ҳ�ķ�������ѡ��
	
	// ������������
	public static final int DIALOG_W = 320;
	public static final int DIALOG_H = 80;
	public static final int DIALOG_X = FRAME_X + (FRAME_W - DIALOG_W) / 2;
	public static final int DIALOG_Y = FRAME_Y + (FRAME_H - DIALOG_H) / 2;
}
