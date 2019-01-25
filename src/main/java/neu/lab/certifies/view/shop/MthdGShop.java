package neu.lab.certifies.view.shop;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.MethodVO;

public class MthdGShop extends GraphShop{
	public MthdGShop(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void addNdRow(String sig) {
		MethodVO mthdVO = sysInfo.getMthd(sig);
		if(null!=mthdVO) {
			nodes.addRow();
			nodes.set(ndIdCnt, "id", ndIdCnt);
			nodes.set(ndIdCnt, "sig", mthdVO.getMethodSig());
			String jarSig =  mthdVO.getJar();
			nodes.set(ndIdCnt, "jarSig", jarSig);
			
			this.addNdMap(sig, Integer.valueOf(ndIdCnt),jarSig);
			ndIdCnt++;
		}
	}

}
