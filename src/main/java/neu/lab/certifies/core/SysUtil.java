package neu.lab.certifies.core;

import java.util.HashSet;
import java.util.Set;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.vo.MethodVO;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.util.Chain;

public class SysUtil {

	public static boolean isJdkCls(String sig) {
		if (sig.startsWith("java.") || sig.startsWith("sun.") || sig.startsWith("org.ietf.jgss.")
				|| sig.startsWith("com.sun.") || sig.startsWith("org.omg.") || sig.startsWith("org.xml.")
				|| sig.startsWith("org.w3c.dom"))
			return true;
		return false;
	}

	public static String type2string(Type type) {
		// ��testers.A[]�����е������ʶȥ��
		String typeString = type.toString();
		return typeString.replace("[]", "");
	}

	/**
	 * @param mthdSig
	 *            eg.:<org.slf4j.event.SubstituteLoggingEvent: org.slf4j.event.Level
	 *            getLevel()>
	 * @return eg.: org.slf4j.event.Level getLevel();
	 */
	public static String mthdSig2Name(String mthdSig) {
		return mthdSig.substring(mthdSig.indexOf(":") + 1, mthdSig.indexOf(")") + 1);
	}

	public static String mthdSig2FileName(String mthdSig) {
		// mthdSig.lastIndexOf(" ");
		// �������Ϊ�˷�ֹ�������ֵ��ظ�
		return mthdSig.substring(mthdSig.lastIndexOf(" ") + 1, mthdSig.indexOf("(")).replace("<", "").replace(">", "")
				+ (int) (Math.random() * 900) + ".txt";
		// return mthdSig.replace("<", "").replace(">", "").replace(":", "-")+".txt";
	}

	/**
	 * ����ͳ�Ʊ���е�ĳһ����Ԫ
	 * 
	 * @param up
	 *            ����
	 * @param down
	 *            ��ĸ
	 * @return
	 */
	public static Float getUnit(int up, int down) {
		return Float.parseFloat((String.format("%.3f", (float) up / down)));
	}

	public static void getSuper(SootClass cls, Set<String> result) {
		Set<SootClass> allSuper = new HashSet<SootClass>();

		if (cls.hasSuperclass()) {
			allSuper.add(cls.getSuperclass());
		}

		Chain<SootClass> superInters = cls.getInterfaces();
		if (null != superInters) {
			for (SootClass superInter : superInters) {
				allSuper.add(superInter);
			}
		}
		if (!allSuper.isEmpty()) {
			for (SootClass superC : allSuper) {
				getSuper(superC, result);
			}
		}
		result.add(cls.getName());
	}

}
