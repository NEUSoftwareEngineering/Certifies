package neu.lab.certifies.util.filter;

import java.util.HashSet;
import java.util.Set;

import neu.lab.certifies.core.SysUtil;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

/**
 * 删除对java类库中set类型的类的复写的方法
 * 
 * @author asus
 *
 */
public class SetFilter implements Filter {
	private static SetFilter instance;
	private Set<String> blackClses;

	private SetFilter() {
		blackClses = new HashSet<String>();
		blackClses.add("java.util.Collection");

		blackClses.add("java.util.Iterator");

		blackClses.add("java.util.Map");
		blackClses.add("java.util.Map$Entry");

	}

	public static SetFilter i() {
		if (instance == null)
			instance = new SetFilter();
		return instance;
	}

	@Override
	public boolean shdFltM(String mthdSig) {
		SootMethod sm = Scene.v().getMethod(mthdSig);
		return hasBlackSuper(sm.getDeclaringClass());
	}

	private boolean hasBlackSuper(SootClass st) {
		Set<String> supers = new HashSet<String>();
		SysUtil.getSuper(st, supers);
		for (String superCls : supers) {
			if (isBlackCls(superCls))
				return true;
		}
		return false;
	}

	private boolean isBlackCls(String clsSig) {
		return blackClses.contains(clsSig);
	}
}
