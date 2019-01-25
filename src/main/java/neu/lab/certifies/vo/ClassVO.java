package neu.lab.certifies.vo;

import java.util.HashSet;
import java.util.Set;

import neu.lab.certifies.core.SysInfo;

public class ClassVO {
	private SysInfo sysInfo;
	private String clsSig;// class signature
	private String fathClsSig;
	private Set<String> methods;
	private Set<String> fields;
	private String pck;
	
	private Set<String> outCls;// 调用的外部方法

	public String getPck() {
		return pck;
	}

	public void setPck(String pck) {
		this.pck = pck;
	}

	public ClassVO(SysInfo sysInfo,String clsSig) {
		this.sysInfo = sysInfo;
		this.clsSig = clsSig;
		methods = new HashSet<String>();
		fields = new HashSet<String>();
	}

	public String getClsSig() {
		return clsSig;
	}

	public void setClsSig(String clsSig) {
		this.clsSig = clsSig;
	}

	public Set<String> getMethods() {
		return methods;
	}

	
	//=------------------------
	
	public Set<String> getOutCls() {
		return outCls;
	}
	
	//----------------------
	
	public Set<String> getFields() {
		return fields;
	}

	public String getFathClsSig() {
		return fathClsSig;
	}

	public void setFathClsSig(String fathClsSig) {
		this.fathClsSig = fathClsSig;
	}

	/**
	 * @param methodVO
	 * @return
	 */
	public boolean addMethod(String mthd) {
		return methods.add(mthd);
	}

	public boolean addField(String fldSig) {
		return fields.add(fldSig);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClassVO) {
			ClassVO classVO = (ClassVO) obj;
			return clsSig.equals(classVO.getName());
		} else {
			return false;
		}

	}
	@Override
	public int hashCode() {
		return this.clsSig.hashCode();
	}
	public String getName() {
		return clsSig;
	}
	// public void print() {
	// for (String fldSig : fields) {
	// FieldVO fieldVO = SysInfo.getFld(fldSig);
	// if (null != fieldVO)
	// fieldVO.print();
	// }
	// for (String mthdSig : methods) {
	// MethodVO methodVO = SysInfo.getMthd(mthdSig);
	// if (null != methodVO) {
	// methodVO.print();
	// }
	// }
	// }

	public String getJar() {
		PackageVO pckVO = sysInfo.getPck(pck);
		if (null != pckVO) {
			return pckVO.getJarSig();
		}
		return null;
	}
}
