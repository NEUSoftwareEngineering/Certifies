package neu.lab.certifies.vo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.util.filter.FilterChain;
import neu.lab.certifies.util.filter.ObjFilter;
import neu.lab.certifies.util.filter.SetFilter;

public class MethodVO {
	private static Logger logger = Logger.getLogger("filted");
	private String methodSig;// method signature
	private String cls;// 方法属于哪个类
	private Set<String> locTypes;// 局部变量的类型
	private Set<String> outMthds;// 调用的外部方法
	private Set<String> inMthds;// 调用本方法的方法

	private SysInfo sysInfo;

	public MethodVO(SysInfo sysInfo,String methodSig) {
		this.sysInfo = sysInfo;
		this.methodSig = methodSig;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getMethodSig() {
		return this.methodSig;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodVO) {
			MethodVO method = (MethodVO) obj;
			return methodSig.equals(method.getMethodSig());
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return this.methodSig.hashCode();
	}

	public Set<String> getLocTypes() {
		return locTypes;
	}

	public void setLocTypes(Set<String> locTypeSet) {
		this.locTypes = locTypeSet;
	}

	public Set<String> getOutMthds() {
		return outMthds;
	}

	public void addOutMthds(String mthdSig) {
		if (null == this.outMthds)
			outMthds = new HashSet<String>();
		outMthds.add(mthdSig);
	}

	public String getJar() {
		ClassVO clsVO = sysInfo.getCls(cls);
		if (null != clsVO) {
			PackageVO pckVO = sysInfo.getPck(clsVO.getPck());
			if (null != pckVO) {
				return pckVO.getJarSig();
			}
		}
		return null;
	}
	public String getPck() {
		ClassVO clsVO = sysInfo.getCls(cls);
		if (null != clsVO) {
			return clsVO.getPck();
		}
		return null;
	}

	/**
	 * 对outMthds进行过滤
	 */
	public void filOutM() {
		if (null != outMthds) {
			logger.debug("==================" + methodSig);
			FilterChain fc = new FilterChain(this);
			if (SysConf.FLT_OBJ)
				fc.addFilter(ObjFilter.i());
			if (SysConf.FLT_SET)
				fc.addFilter(SetFilter.i());
			Iterator<String> ite = outMthds.iterator();
			while (ite.hasNext()) {
				String outMthd = ite.next();
				if (fc.shdFltM(outMthd)) {
					logger.debug(outMthd);
					ite.remove();
				}
			}
		}
	}

	/**
	 * 计算outMthds中每个函数名字出现的次数
	 * 
	 * @param outMthds
	 * @return <k,v> k:函数名字 v:出现的次数
	 */
	public Map<String, Integer> calNameCnt() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (outMthds != null) {
			// 对每一个方法进行计数
			for (String mthdSig : outMthds) {
				String mthdName = SysUtil.mthdSig2Name(mthdSig);
				Integer cnt = result.get(mthdName);
				if (null == cnt) {
					result.put(mthdName, new Integer(1));
				} else {
					result.put(mthdName, cnt + 1);
				}
			}
		}
		return result;
	}

	public Set<String> getInMthds() {
		return inMthds;
	}

	public void addInMthds(String mthdSig) {
		if (null == this.inMthds)
			inMthds = new HashSet<String>();
		inMthds.add(mthdSig);
	}

	public boolean isHostM() {
		return SysCons.MY_JAR_NAME.equals(getJar());
	}
}
