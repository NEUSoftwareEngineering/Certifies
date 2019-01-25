package neu.lab.certifies.util.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * ���˵���Object���еĺ������и�д�ĺ���
 * 
 * @author asus
 *
 */
public class ObjFilter implements Filter {
	private static ObjFilter instance;
	private Set<String> blackMthds;

	private ObjFilter() {
		blackMthds = new HashSet<String>();
		blackMthds.add("java.lang.Object clone()");
		blackMthds.add("boolean equals(java.lang.Object)");
		blackMthds.add("int hashCode()");
		blackMthds.add("java.lang.String toString()");
	}

	public static ObjFilter i() {
		if (instance == null)
			instance = new ObjFilter();
		return instance;
	}

	/*
	 * mthdSig:<org.apache.maven.artifact.repository.metadata.Plugin:
	 * java.lang.Object clone()> blackMthd:java.lang.Object clone()
	 * ���߲���equals�Ĺ�ϵ����contains�Ĺ�ϵ
	 */
	@Override
	public boolean shdFltM(String mthdSig) {
		for (String blackMthd : blackMthds) {
			if (mthdSig.contains(blackMthd))
				return true;
		}
		return false;
	}

}
