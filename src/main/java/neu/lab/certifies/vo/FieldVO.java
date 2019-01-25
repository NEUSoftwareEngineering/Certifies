package neu.lab.certifies.vo;

public class FieldVO {
	private String fldSig;//field signature
	private String cls;//class
	private String type;

	public FieldVO(String fldSig) {
		this.fldSig = fldSig;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldVO) {
			FieldVO field = (FieldVO) obj;
			return fldSig.equals(field.getFldSig());
		} else {
			return false;
		}

	}

	public String getFldSig() {
		return fldSig;
	}

	public void setFldSig(String fldSig) {
		this.fldSig = fldSig;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		return this.fldSig.hashCode();
	}
}
