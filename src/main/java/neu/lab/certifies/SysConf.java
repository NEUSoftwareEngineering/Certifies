package neu.lab.certifies;

public class SysConf {
	public static final boolean FLT_M_Rlt = false;// filte method relation是否要对方法的关系进行过滤
	
	public static final int SAFE_NUM = 1;//outMthds小于等于此数值的不会进行过滤
	public static final int DANGER_NUM = 1000;//outMthds大于等于此数值的一定会进行过滤
	public static final boolean FLT_OBJ = true;//是否过滤对Object类复写的方法
	public static final boolean FLT_SET = true;//是否过滤复写java集合类方法的方法

	// 计算风险的阈值
	public static final int LIB_NUM_T = 0;// 一个hostMthd调用的最多的lib个数
	public static final int MC_PL_T = 0;// call per lib：每个hostMthd对每个lib可以调用的最多的方法的个数
	public static final int IN_HM_T = 0;// in method：一个lib method最多被多少个hostMethod调用
	public static final int DIR_LM_T = 0;// direct lib method：host method最多可以直接调用多少个第三方软件
	public static final int ACS_LM_T = 0;// accessible lib method：host Method最多可以可达多少个第三方软件
	public static final int K_CORE_T = 0;//调用的最大核数

	
	
//	public static final int LIB_NUM_T = 2;// 一个hostMthd调用的最多的lib个数
//	public static final int MC_PL_T = 1000;// call per lib：每个hostMthd对每个lib可以调用的最多的方法的个数
//	public static final int IN_HM_T = 1000;// in method：一个lib method最多被多少个hostMethod调用
//	public static final int DIR_LM_T = 15;// direct lib method：host method最多可以直接调用多少个第三方软件
//	public static final int ACS_LM_T = 1000;// accessible lib method：host Method最多可以可达多少个第三方软件
	
//	public static final int LIB_NUM_T = 1000;// 一个hostMthd调用的最多的lib个数
//	public static final int MC_PL_T = 1000;// call per lib：每个hostMthd对每个lib可以调用的最多的方法的个数
//	public static final int IN_HM_T = 1000;// in method：一个lib method最多被多少个hostMethod调用
//	public static final int DIR_LM_T = 1000;// direct lib method：host method最多可以直接调用多少个第三方软件
//	public static final int ACS_LM_T = 1000;// accessible lib method：host Method最多可以可达多少个第三方软件

	public static final String riskOutPath = "risk/";
	public static final String staDir = riskOutPath + "Sta/";
	
	public static final String CG_TYPE = "cha";//计算cg的方式：cha/spark
	
}
