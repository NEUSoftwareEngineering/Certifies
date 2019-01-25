package neu.lab.certifies.debug;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.vo.MethodVO;
import neu.lab.certifies.vo.Relation;

public class SqlWriter {
	public static final String ADD_M = "INSERT INTO METHOD(mthd_id,mthd_sig,cls_sig,pck_sig,jar_sig) VALUES(?,?,?,?,?)";
	public static final String ADD_R = "INSERT INTO MTHD_RLT(src_id,tgt_id) VALUES(?,?)";
	private Connection con;
	private SysInfo sysInfo;
	private Map<String, Integer> sig2id;

	public SqlWriter(SysInfo sysInfo) {
		con = getConnection();
		this.sysInfo = sysInfo;
		sig2id = new HashMap<String, Integer>();
	}

	public Connection getConnection() {
		con = null;

		String url = "jdbc:mysql://localhost:3306/sys";
		String user = "root";
		String password = "123456";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Succeeded connecting to the Database!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public void wrtSysInfo() {
		// INSERT INTO METHOD(mthd_sig,cls_sig,pck_sig,jar_sig) VALUES("a","a","a","a");

		try {
			// 添加方法关系表
			wtrMthd();
			wrtRlt();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void wtrMthd() throws SQLException {
		long startT = System.currentTimeMillis();
		PreparedStatement ps = con.prepareStatement(ADD_M);
		final int batchSize = 1000;
		// 添加方法表
		int count = 0;
		for (String mthdSig : sysInfo.getAllMthd()) {
			MethodVO mthd = sysInfo.getMthd(mthdSig);
			sig2id.put(mthdSig, new Integer(count));
			ps.setInt(1, count);
			ps.setString(2, mthd.getMethodSig());
			ps.setString(3, mthd.getCls());
			ps.setString(4, mthd.getPck());
			ps.setString(5, mthd.getJar());
			ps.addBatch();
			count = count+1;
			if (count % batchSize == 0) {
				ps.executeBatch();
			}
		}
		ps.executeBatch(); // insert remaining records
		ps.close();
		long runtime = (System.currentTimeMillis() - startT) / 1000;
		System.out.println("add method time:" + runtime);
	}

	private void wrtRlt() throws SQLException {
		long startT = System.currentTimeMillis();
		PreparedStatement ps = con.prepareStatement(ADD_R);
		final int batchSize = 1000;
		// 添加方法表
		int count = 0;
		for (Relation rlt : sysInfo.getAllMthdRlt()) {
			int src = sig2id.get(rlt.getSrc());
			int tgt = sig2id.get(rlt.getTgt());
			ps.setInt(1, src);
			ps.setInt(2, tgt);
			ps.addBatch();
			count = count+1;
			if (count % batchSize == 0) {
				ps.executeBatch();
			}
		}
		ps.executeBatch(); // insert remaining records
		ps.close();
		long runtime = (System.currentTimeMillis() - startT) / 1000;
		System.out.println("add relation time:" + runtime);
	}
}
