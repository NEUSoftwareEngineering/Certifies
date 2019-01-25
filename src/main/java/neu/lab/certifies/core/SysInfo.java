package neu.lab.certifies.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import neu.lab.certifies.SysCons;
import neu.lab.certifies.dog.MthdDog;
import neu.lab.certifies.dog.book.Book;
import neu.lab.certifies.vo.ClassVO;
import neu.lab.certifies.vo.FieldVO;
import neu.lab.certifies.vo.JarVO;
import neu.lab.certifies.vo.MethodVO;
import neu.lab.certifies.vo.PackageVO;
import neu.lab.certifies.vo.Relation;

import java.util.Set;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * @author asus 类中保存着被分析的系统中的所有的信息
 */
public class SysInfo {

	private int phantomRef = 0;
	private Map<String, JarVO> jarTb = new HashMap<String, JarVO>();// 存储二进制项目中的出现的jar包
	private Map<String, PackageVO> pckTb = new HashMap<String, PackageVO>();
	private Map<String, ClassVO> clsTb = new HashMap<String, ClassVO>();
	private Map<String, MethodVO> mthdTb = new HashMap<String, MethodVO>();
	private Map<String, FieldVO> fldTb = new HashMap<String, FieldVO>();

	private Set<Relation> m2ms = new HashSet<Relation>();// methodRelations method与method的调用关系 method to method
	private Set<Relation> c2cs = new HashSet<Relation>();// 如果两个类中的method有关联，则两个Class有关联
	private Map<String, Book> books;// mthd的accessMthd

	private Map<String, Book> getBooks() {
		if (null == books) {
			System.out.println("set dog to get book ");
			books = new MthdDog(this).findRlt();
		}
		return books;
	}
	
	public void setBooks(Map<String, Book> books) {
		this.books = books;
	}
	public Set<String> getAllBookedMthd() {
		return getBooks().keySet();
	}

	public Book getBook(String mthd) {
		return getBooks().get(mthd);
	}

	/////// package的操作
	public void delPck(String pckSig) {
		pckTb.remove(pckSig);
	}

	public Set<String> getAllPcks() {
		return pckTb.keySet();
	}

	public PackageVO getPck(String pckSig) {
		return pckTb.get(pckSig);
	}

	public boolean addPck(String pckSig) {
		if (null == pckTb.get(pckSig)) {
			PackageVO packageVO = new PackageVO(pckSig);
			pckTb.put(pckSig, packageVO);
			return true;
		} else {
			return false;
		}
	}

	/////////// jar包的操作
	public void delJar(String jarSig) {
		jarTb.remove(jarSig);
	}

	public Iterator<Entry<String, JarVO>> jarIte() {
		return jarTb.entrySet().iterator();
	}

	public Set<String> getAllJar() {
		return jarTb.keySet();
	}

	public int getJarNum() {
		return jarTb.size();
	}

	public JarVO getJar(String jarSig) {
		return jarTb.get(jarSig);
	}

	public boolean addJar(String jarSig, JarVO jarVO) {
		JarVO oldJar = jarTb.get(jarSig);
		if (null == oldJar) {
			jarTb.put(jarSig, jarVO);
			return true;
		}
		return false;
	}

	////////////// 类
	public Set<String> getAllClses() {
		return clsTb.keySet();
	}

	public ClassVO getCls(String clsSig) {
		return clsTb.get(clsSig);
	}

	public boolean isSysCls(String clsSig) {
		if (clsTb.get(clsSig) != null) {
			return true;
		}
		return false;
	}

	public boolean addClass(SootClass sootClass) {
		String className = sootClass.getName();
		if (clsTb.get(className) == null) {
			ClassVO classVO = new ClassVO(this, className);
			if (sootClass.hasSuperclass()) {
				classVO.setFathClsSig(sootClass.getSuperclass().getName());
			}
			clsTb.put(className, classVO);
			return true;
		} else {
			return false;
		}
	}

	///////////////// 方法
	public Set<String> getAllMthd() {
		return mthdTb.keySet();
	}

	public MethodVO getMthd(String mthdSig) {
		return mthdTb.get(mthdSig);
	}

	public boolean isHostMthd(String mthdSig) {
		MethodVO methodVO = this.getMthd(mthdSig);
		if (null != methodVO) {
			if (SysCons.MY_JAR_NAME.equals(methodVO.getJar()))
				return true;
		}
		return false;
	}

	public boolean isLibMthd(String mthdSig) {
		return !isHostMthd(mthdSig);
	}

	public boolean isJarLibMthd(String libName,String mthdSig) {
		MethodVO methodVO = this.getMthd(mthdSig);
		if (null != methodVO) {
			if (libName.equals(methodVO.getJar()))
				return true;
		}
		return false;
	}
	
	public boolean addMethod(SootMethod sootMethod) {
		String mthdSig = sootMethod.getSignature();
		if (mthdTb.get(mthdSig) == null) {
			MethodVO methodVO = new MethodVO(this, mthdSig);
			mthdTb.put(mthdSig, methodVO);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 添加方法的局部变量
	 * 
	 * @param src
	 * @param locTypeSet
	 * @return
	 */
	public boolean addLocRlt(String src, Set<String> locTypeSet) {
		MethodVO methodVO = mthdTb.get(src);
		if (methodVO != null) {
			methodVO.setLocTypes(locTypeSet);
			return true;
		}
		return false;
	}

	//////////////// 属性
	public boolean addField(SootField sootField) {
		String fldSig = sootField.getSignature();
		if (fldTb.get(fldSig) == null) {
			FieldVO fieldVO = new FieldVO(fldSig);
			fldTb.put(fldSig, fieldVO);
			return true;
		} else {
			return false;
		}
	}

	public FieldVO getFld(String fldSig) {
		return fldTb.get(fldSig);
	}

	////////////// 关系的操作
	public boolean addM2m(String src, String target) {
		return m2ms.add(new Relation(src, target));
	}

	public boolean addC2c(String src, String tgt) {
		return c2cs.add(new Relation(src, tgt));
	}

	public Set<Relation> getAllClsRlt() {
		return c2cs;
	}

	public Set<Relation> getAllMthdRlt() {
		return m2ms;
	}

	public boolean addFldRlt(String src, String target) {
		FieldVO fieldVO = fldTb.get(src);
		if (fieldVO != null) {
			fieldVO.setType(target);
			return true;
		}
		return false;
	}

	public Set<String> getHostMthds() {// 系统内部的方法
		Set<String> hostMthds = new HashSet<String>();
		for (String mthdSig : getAllMthd()) {
			MethodVO mthdVO = getMthd(mthdSig);
			if (null != mthdVO) {
				String jarSig = mthdVO.getJar();
				if (SysCons.MY_JAR_NAME.equals(jarSig)) {
					hostMthds.add(mthdSig);
				}
			}
		}
		return hostMthds;
	}
	
	public Set<String> getLibMthds() { //所有lib方法
		Set<String> libMthds = new HashSet<String>();
		for (String mthdSig : getAllMthd()) {
			MethodVO mthdVO = getMthd(mthdSig);
			if (null != mthdVO) {
				String jarSig = mthdVO.getJar();
				if (!SysCons.MY_JAR_NAME.equals(jarSig)) {
					libMthds.add(mthdSig);
				}
			}
		}
		return libMthds;
	}

	
	public Set<String> getLibMthds(String libName) {// lib内部的方法
		Set<String> libMthds = new HashSet<String>();
		for (String mthdSig : getAllMthd()) {
			MethodVO mthdVO = getMthd(mthdSig);
			if (null != mthdVO) {
				String jarSig = mthdVO.getJar();
				if (libName.equals(jarSig)) {
					libMthds.add(mthdSig);
				}
			}
		}
		return libMthds;
	}
	
	
	public void cls2pck(String clsSig, String pckSig) {
		ClassVO cls = this.getCls(clsSig);
		PackageVO pck = this.getPck(pckSig);
		if (null != pck && null != cls) {
			pck.addClass(clsSig);
			cls.setPck(pckSig);
		}
	}

	public void mthd2cls(String mthdSig, String clsSig) {
		MethodVO mthd = this.getMthd(mthdSig);
		ClassVO cls = this.getCls(clsSig);
		if (null != cls && mthd != null) {
			cls.addMethod(mthdSig);
			mthd.setCls(clsSig);
		}

	}

	public void fld2cls(String fldSig, String clsSig) {
		FieldVO fieldVO = this.getFld(fldSig);
		ClassVO cls = this.getCls(clsSig);
		if (null != cls && null != fieldVO) {
			cls.addField(fldSig);
			fieldVO.setCls(clsSig);
		}
	}

	public void pck2jar(String pckSig, JarVO jarVO) {
		PackageVO pckVO = this.getPck(pckSig);
		if (null != pckVO && null != jarVO) {
			jarVO.addPck(pckSig);
			pckVO.setJarSig(jarVO.getJarSig());
		}
	}

	public void pcks2jar(Set<String> pcks, JarVO jarVO) {
		jarVO.setPcks(pcks);
		for (String pckSig : pcks) {
			PackageVO pckVO = this.getPck(pckSig);
			if (null != pckVO) {
				pckVO.setJarSig(jarVO.getJarSig());
			}
		}
	}

}
