package neu.lab.certifies.util.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.vo.MethodVO;

public class FilterChain {
	/**
	 * method��outMthds��ÿ���������ֳ��ּ���
	 */
	Map<String, Integer> nameCnts;
	private List<Filter> filters;

	public FilterChain(MethodVO mthd) {
		filters = new ArrayList<Filter>();
		nameCnts = mthd.calNameCnt();
	}

	public void addFilter(Filter f) {
		filters.add(f);
	}

	public boolean shdFltM(String mthdSig) {
		if (isSafe(mthdSig))
			return false;
		if (isDanger(mthdSig))
			return true;
		for (Filter f : filters) {
			if (f.shdFltM(mthdSig))
				return true;
		}
		return false;

	}

	public boolean isDanger(String mthdSig) {
		String mthdName = SysUtil.mthdSig2Name(mthdSig);
		Integer cnt = nameCnts.get(mthdName);
		if (cnt >= SysConf.DANGER_NUM) {// ��������̫�࣬���й���
			return true;
		} else {
			return false;
		}
	}

	public boolean isSafe(String mthdSig) {
		String mthdName = SysUtil.mthdSig2Name(mthdSig);
		Integer cnt = nameCnts.get(mthdName);
		if (cnt <= SysConf.SAFE_NUM) {// ��������̫�࣬���й���
			return true;
		} else {
			return false;
		}
	}
}
