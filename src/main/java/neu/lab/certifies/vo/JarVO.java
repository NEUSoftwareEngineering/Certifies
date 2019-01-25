package neu.lab.certifies.vo;

import java.util.HashSet;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;

public class JarVO {
	private String jarPath;
	private String jarSig;
	private Set<String> pcks;
	private SysInfo sysInfo;
	public JarVO(SysInfo sysInfo,String jarSig, String jarPath) {
		this.sysInfo = sysInfo;
		this.jarPath = jarPath;
		this.jarSig = jarSig;
		pcks = new HashSet<String>();
	}

	public boolean hasPck(String pckSig) {
		return pcks.contains(pckSig);
	}

	public boolean addPck(String pckSig) {
		return pcks.add(pckSig);
	}

	public boolean delPck(String pckSig) {
		return pcks.remove(pckSig);
	}

	public String getJarPath() {
		return jarPath;
	}

	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	public String getJarSig() {
		return jarSig;
	}

	public void setJarSig(String jarSig) {
		this.jarSig = jarSig;
	}

	public Set<String> getPcks() {
		return pcks;
	}

	public void setPcks(Set<String> pcks) {
		this.pcks = pcks;
	}

	@Override
	public int hashCode() {
		return this.jarSig.hashCode();
	}

	public Set<String> getClses() {// 获得jar包下的所有类
		Set<String> allCls = new HashSet<String>();
		for (String pckSig : this.pcks) {
			PackageVO pckVO = sysInfo.getPck(pckSig);
			Set<String> pckClses = pckVO.getClses();
			if (null != pckClses)
				allCls.addAll(pckClses);
		}
		return allCls;
	}

	public Set<String> getMthds() {// 获得jar包下的所有方法
		Set<String> allMthds = new HashSet<String>();
		Set<String> allCls = this.getClses();
		for (String clsSig : allCls) {
			ClassVO clsVO = sysInfo.getCls(clsSig);
			Set<String> clsMthds = clsVO.getMethods();
			if (null != clsMthds)
				allMthds.addAll(clsMthds);
		}
		return allMthds;
	}
}
