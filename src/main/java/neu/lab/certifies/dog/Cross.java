package neu.lab.certifies.dog;

import java.util.Iterator;
import java.util.Set;

import neu.lab.certifies.vo.MethodVO;

/**
 * Ê®×ÖÂ·¿Ú
 * 
 * @author asus
 *
 */
public class Cross {
	Iterator<String> cross;

	Cross(MethodVO mthdVO) {
		Set<String> outMthds = mthdVO.getOutMthds();
		if (null != outMthds) {
			this.cross = outMthds.iterator();
		} else {
			this.cross = null;
		}
	}

	boolean hasBranch() {
		if (null == cross)
			return false;
		return cross.hasNext();
	}

	String getBranch() {
		return cross.next();
	}
}
