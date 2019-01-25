package neu.lab.certifies.sta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.dog.book.Book;

/**
 * ��Ҫͳ�Ƶ�����һ���ɷ�ʽ(dir/acs)������(M/C)�����(Host/jar)������Ϻ������������
 * 
 * @author asus
 *
 */
public class LibSta extends Sta {
	private static Logger logger = Logger.getRootLogger();
	String jarSig;

	private Set<String> passedMes;
	private Set<String> passMes;

	// private Map<String, Integer> hostPass;//
	// <libName,count>:host�ķ�������:(ͨ��thisLib����thirdLib)
	// private Map<String, Integer> hostPassed;//
	// <libName,count>:thirdLib�ķ�������:(��hostͨ��thisLib����)

	public LibSta(SysInfo sysInfo, String jarName) {
		super(sysInfo);
		this.jarSig = jarName;

		dirHostMes = new HashSet<String>();
		acsHostMes = new HashSet<String>();

		dirHostCes = new HashSet<String>();
		acsHostCes = new HashSet<String>();

		dirJarMes = new HashSet<String>();
		acsJarMes = new HashSet<String>();

		dirJarCes = new HashSet<String>();
		acsJarCes = new HashSet<String>();

		passedMes = new HashSet<String>();
		passMes = new HashSet<String>();
	}

	public void incMthdCnt() {
		mthdCnt++;
	}

	public void incClsCnt() {
		clsCnt++;
	}

	public void addDirInfo(String srcSig, String tgtSig) {
		dirHostMes.add(srcSig);
		dirJarMes.add(tgtSig);

		String srcCls = sysInfo.getMthd(srcSig).getCls();
		String tgtCls = sysInfo.getMthd(tgtSig).getCls();
		dirHostCes.add(srcCls);
		dirJarCes.add(tgtCls);
	}

	public void addAcsInfo(String srcSig, String tgtSig) {
		this.acsHostMes.add(srcSig);
		this.acsJarMes.add(tgtSig);

		String srcCls = sysInfo.getMthd(srcSig).getCls();
		String tgtCls = sysInfo.getMthd(tgtSig).getCls();
		this.acsHostCes.add(srcCls);
		this.acsJarCes.add(tgtCls);

	}

	public void calPassInfo() {
		// ����jar���еĴ̴�������acsJarMes�п��Ե�����jar���ķ���
		for (String acsMthd : this.acsJarMes) {
			@SuppressWarnings("unchecked")
			Set<String> outMthds = (Set<String>) sysInfo.getBook(acsMthd).getRecords();
			for (String outMthd : outMthds) {
				String outJar = sysInfo.getMthd(outMthd).getJar();
				if (!this.jarSig.equals(outJar)) {
					this.passedMes.add(acsMthd);
					break;
				}
			}
		}
		// ����host����Щ����������jar���еĴ̴�����
		for (String hostMthd : sysInfo.getHostMthds()) {
			@SuppressWarnings("unchecked")
			Set<String> outMthds = (Set<String>) sysInfo.getBook(hostMthd).getRecords();
			for (String outMthd : outMthds) {
				if (this.passedMes.contains(outMthd)) {
					this.passMes.add(hostMthd);
					break;
				}
			}
		}
	}

	public void print() {
		logger.debug("================" + jarSig);

		logger.debug("mthdCnt:" + mthdCnt);
		logger.debug("clsCnt:" + clsCnt);

		logger.debug("dirHostMes:" + this.dirHostMes.size());
		logger.debug("acsHostMes:" + this.acsHostMes.size());

		logger.debug("dirHostCes:" + this.dirHostCes.size());
		logger.debug("acsHostCes:" + this.acsHostCes.size());

		logger.debug("dirJarMes:" + this.dirJarMes.size());
		logger.debug("acsJarMes:" + this.acsJarMes.size());

		logger.debug("dirJarCes:" + this.dirJarCes.size());
		logger.debug("acsJarCes:" + this.acsJarCes.size());
	}

	public Set<String> getPassMes() {
		return passMes;
	}

	@Override
	protected Integer getPassM() {
		return this.passMes.size();
	}

	@Override
	protected Integer getPassedM() {
		return this.passedMes.size();
	}

	@Override
	protected String getSig() {
		return this.jarSig;
	}

}
