package neu.lab.certifies;

public class SysConf {
	public static final boolean FLT_M_Rlt = false;// filte method relation�Ƿ�Ҫ�Է����Ĺ�ϵ���й���
	
	public static final int SAFE_NUM = 1;//outMthdsС�ڵ��ڴ���ֵ�Ĳ�����й���
	public static final int DANGER_NUM = 1000;//outMthds���ڵ��ڴ���ֵ��һ������й���
	public static final boolean FLT_OBJ = true;//�Ƿ���˶�Object�ิд�ķ���
	public static final boolean FLT_SET = true;//�Ƿ���˸�дjava�����෽���ķ���

	// ������յ���ֵ
	public static final int LIB_NUM_T = 0;// һ��hostMthd���õ�����lib����
	public static final int MC_PL_T = 0;// call per lib��ÿ��hostMthd��ÿ��lib���Ե��õ����ķ����ĸ���
	public static final int IN_HM_T = 0;// in method��һ��lib method��౻���ٸ�hostMethod����
	public static final int DIR_LM_T = 0;// direct lib method��host method������ֱ�ӵ��ö��ٸ����������
	public static final int ACS_LM_T = 0;// accessible lib method��host Method�����Կɴ���ٸ����������
	public static final int K_CORE_T = 0;//���õ�������

	
	
//	public static final int LIB_NUM_T = 2;// һ��hostMthd���õ�����lib����
//	public static final int MC_PL_T = 1000;// call per lib��ÿ��hostMthd��ÿ��lib���Ե��õ����ķ����ĸ���
//	public static final int IN_HM_T = 1000;// in method��һ��lib method��౻���ٸ�hostMethod����
//	public static final int DIR_LM_T = 15;// direct lib method��host method������ֱ�ӵ��ö��ٸ����������
//	public static final int ACS_LM_T = 1000;// accessible lib method��host Method�����Կɴ���ٸ����������
	
//	public static final int LIB_NUM_T = 1000;// һ��hostMthd���õ�����lib����
//	public static final int MC_PL_T = 1000;// call per lib��ÿ��hostMthd��ÿ��lib���Ե��õ����ķ����ĸ���
//	public static final int IN_HM_T = 1000;// in method��һ��lib method��౻���ٸ�hostMethod����
//	public static final int DIR_LM_T = 1000;// direct lib method��host method������ֱ�ӵ��ö��ٸ����������
//	public static final int ACS_LM_T = 1000;// accessible lib method��host Method�����Կɴ���ٸ����������

	public static final String riskOutPath = "risk/";
	public static final String staDir = riskOutPath + "Sta/";
	
	public static final String CG_TYPE = "cha";//����cg�ķ�ʽ��cha/spark
	
}
