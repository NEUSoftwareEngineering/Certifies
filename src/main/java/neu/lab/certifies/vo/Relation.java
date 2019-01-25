package neu.lab.certifies.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author asus 两个java元素的关联method->method
 */
public class Relation {
	private String src;
	private String target;

	public Relation(String src, String target) {
		this.src = src;
		this.target = target;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Relation) {
			Relation rlt = (Relation) obj;
			return src.equals(rlt.getSrc()) && target.equals(rlt.getTgt());
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return src.hashCode() * 31 + target.hashCode();
	}

	public String getSrc() {
		return src;
	}

	public String getTgt() {
		return target;
	}

	@Override
	public String toString() {
		return src + " to " + target;
	}


}
