package neu.lab.certifies.vo;

import java.util.HashSet;
import java.util.Set;

public class PackageVO {
	public static String getSig(String jarName, String pckName) {
		return jarName + ":" + pckName;
	}

	private String pckSig;// jar的名字加上pckName
	private String pckName;

	private Set<String> classes;
	private Set<String> subPcks;
	private String jarSig;

	public String getPckName() {
		return pckName;
	}

	public void setPckName(String pckName) {
		this.pckName = pckName;
	}

	public String getJarSig() {
		return jarSig;
	}

	public void setJarSig(String jarSig) {
		this.jarSig = jarSig;
	}

	public PackageVO(String pckSig) {
		this.pckSig = pckSig;
		int splitI = pckSig.indexOf(":");
		if (-1 != splitI)
			this.pckName = pckSig.substring(splitI + 1);
		classes = new HashSet<String>();
		subPcks = new HashSet<String>();
	}

	/**
	 * pck是否为该对象的子包
	 * 
	 * @param pck
	 *            被检测的包的包名
	 * @return
	 */
	public boolean addClass(String clsSig) {
		return classes.add(clsSig);
	}

	public boolean isSub(PackageVO pck) {
		return false;
	}

	public String getPckSig() {
		return pckSig;
	}

	public void setPckSig(String pckSig) {
		this.pckSig = pckSig;
	}

	public Set<String> getClses() {
		return classes;
	}

	public Set<String> getSubPcks() {
		return subPcks;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PackageVO) {
			PackageVO pck = (PackageVO) obj;
			return pckSig.equals(pck.getPckSig());
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return this.pckSig.hashCode();
	}
}
