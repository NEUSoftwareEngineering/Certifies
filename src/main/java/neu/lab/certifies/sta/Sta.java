package neu.lab.certifies.sta;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;

public abstract class Sta {
	protected SysInfo sysInfo;

	protected int clsCnt = 0;
	protected int mthdCnt = 0;// �����ĸ���

	protected Set<String> dirHostMes;// direct-host-method:host.method(ֱ�ӵ��ô�jar.method)
	protected Set<String> acsHostMes;// access-host-method:host.method(ֱ�ӻ��ӵ��ô�jar.method)

	protected Set<String> dirHostCes;// direct-host-class: host.class(ֱ�ӵ��ô�jar.method)
	protected Set<String> acsHostCes;// access-host-class: host.class(ֱ�ӻ��ӵ��ô�jar.method)

	protected Set<String> dirJarMes;// direct-Jar-method: Jar.method(��host.methodֱ�ӵ���)
	protected Set<String> acsJarMes;// access-Jar-method:Jar.method(��host.methodֱ�ӻ��ӵ���)

	protected Set<String> dirJarCes;// direct-Jar-class: Jar.class(��host.methodֱ�ӵ���)
	protected Set<String> acsJarCes;// access-Jar-class: Jar.class(��host.methodֱ�ӻ��ӵ���)

	public Sta(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
	}
	
	
	public static void writeFile(String file, int conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * @param tHead ��ͷ
	 * @param hClsNum ��������ĸ���
	 * @param hMthdNum �����ķ����ĸ���
	 * @return
	 */
	public Object[] staRow(String[] tHead, int hClsNum, int hMthdNum) {
		List<Object> row = new ArrayList<Object>();
		for (String columnName : tHead) {
			switch (columnName) {
			case "jarSig":
				row.add(getSig());
				break;
			case "mthdCnt":
				row.add(mthdCnt);
				break;
			case "clsCnt":
				row.add(clsCnt);
				break;
			case "dirJarM":
				row.add(dirJarMes.size());
				break;
			case "acsJarM":
				row.add(acsJarMes.size());
				break;
			case "dirJarC":
				row.add(dirJarCes.size());
				break;
			case "acsJarC":
				row.add(acsJarCes.size());
				break;
			case "dirHostM":
				row.add(dirHostMes.size());
				break;
			case "acsHostM":
				row.add(acsHostMes.size());
				break;
			case "dirHostC":
				row.add(dirHostCes.size());
				break;
			case "acsHostC":
				row.add(acsHostCes.size());
				break;
			case "passedM":
				row.add(getPassedM());
				break;
			case "passM":
				row.add(getPassM());
				break;
			case "rDJM":
				row.add(SysUtil.getUnit(dirJarMes.size(), mthdCnt));
				break;
			case "rAJM":
				row.add(SysUtil.getUnit(acsJarMes.size(), mthdCnt));
				break;
			case "rDJC":
				row.add(SysUtil.getUnit(dirJarCes.size(), clsCnt));
				break;
			case "rAJC":
				row.add(SysUtil.getUnit(acsJarCes.size(), clsCnt));
				break;
			case "rDHM":
				row.add(SysUtil.getUnit(dirHostMes.size(), hMthdNum));
				break;
			case "rAHM":
				row.add(SysUtil.getUnit(acsHostMes.size(), hMthdNum));
				break;
			case "rDHC":
				row.add(SysUtil.getUnit(dirHostCes.size(), hClsNum));
				break;
			case "rAHC":
				row.add(SysUtil.getUnit(acsHostCes.size(), hClsNum));
				break;
			case "rPDM":
				row.add(SysUtil.getUnit(getPassedM(), mthdCnt));
				break;
			case "rPM":
				row.add(SysUtil.getUnit(getPassM(), hMthdNum));
				break;
			}

		}
		return row.toArray();
	}



	protected abstract Integer getPassM();

	protected abstract Integer getPassedM();

	protected abstract String getSig();
}
