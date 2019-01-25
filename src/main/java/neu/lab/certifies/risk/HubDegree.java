package neu.lab.certifies.risk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import neu.lab.certifies.SysConf;
import neu.lab.certifies.SysCons;
import neu.lab.certifies.core.SysInfo;
import neu.lab.certifies.core.SysUtil;
import neu.lab.certifies.sta.HostMSta;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.vo.MethodVO;


public class HubDegree extends RiskCounter {
	
	Map<String, Integer> mthd2core_host;
	Set<String> leftMthds_host;
	Set<String> delMthds_host;
	int maxlevel_host;
	
	Map<String, Integer> mthd2core_lib = new HashMap<String, Integer>();
	Set<String> leftMthds_lib;
	Set<String> delMthds_lib;

	public HubDegree(SysInfo sysInfo) {
		super(sysInfo);
	}

	@Override
	protected void initType() {
		type = "Hub Degree";
	}

	@Override
	public void calRisk() throws IOException {
		calCoreMapHost();
		Map<String, Integer> libMaxlevel = new HashMap<String, Integer>();
		Iterator<String> it = sysInfo.getAllJar().iterator();
		while(it.hasNext()) {
			String jar = it.next();
			if(!SysCons.MY_JAR_NAME.equals(jar)) {
				libMaxlevel.put(jar, calCoreMapLib(jar));
			}
		}
		HostMSta hostmSta = new HostMSta(Screen.i().sysInfo);
		for (String hostM : hostmSta.hostMs) {
			double hostHub=(double)mthd2core_host.get(hostM)/maxlevel_host;
			double maxLibHub = 0;
			Set<String> outMthds = sysInfo.getMthd(hostM).getOutMthds();
			for (String outMthd : outMthds) {
				if (sysInfo.isLibMthd(outMthd)) {
					double libHub=(double)mthd2core_lib.get(outMthd)/libMaxlevel.get(sysInfo.getMthd(outMthd).getJar());
					if(libHub>maxLibHub)
						maxLibHub=libHub;
				}
			}
			
			nums.add("" + (hostHub+maxLibHub));
			riskMthds.add(hostM);
			riskGraphData.addNum(hostM, (int)(hostHub+maxLibHub)); 
			
			}		
     }
		
	
	
	
	/**
	 * 计算宿主中每个函数的kcore值
	 */
	public void calCoreMapHost() {
		mthd2core_host = new HashMap<String, Integer>();
		leftMthds_host = new HashSet<String>();
		leftMthds_host.addAll(sysInfo.getHostMthds());
		delMthds_host = new HashSet<String>();
		int core = 0;
		while (!leftMthds_host.isEmpty()) {
			calCoreMthds_host(core);//从度为0的节点开始++
			core++;
		}
	}
	
	/**
	 * 计算核数是core的方法
	 * 
	 * @param core
	 * @param leftMthds
	 * @param delMthds
	 * @return
	 */
	private void calCoreMthds_host(int core) {
        
		int delNum;
		maxlevel_host=0;//最大层数
		
		do {
			delNum = 0;
			Iterator<String> ite = leftMthds_host.iterator();
			
			while(ite.hasNext()) {
				String leftM = ite.next();
				int validDegree = 0;// 记录函数有效的出度（一部分函数会在计算的过程中删除掉）
				Set<String> outMthds = getEdge_host(leftM);
				if (null != outMthds) {
					for (String outMthd : outMthds) {
						if (!delMthds_host.contains(outMthd)) {
							validDegree++;
						}	
					}
				}
				if (validDegree < core) {// 方法的度太小，执行删除
					ite.remove();
					delMthds_host.add(leftM);
					//在计算k-core子图时删除掉的节点的核数为k-1
					int mthdCore = core-1;
					if(mthdCore>maxlevel_host)
						maxlevel_host=mthdCore;
					mthd2core_host.put(leftM, new Integer(mthdCore));
					delNum++;
				}
			}
		} while (delNum > 0);
	}
	
	
	private Set<String> getEdge_host(String mthd){
		Set<String> edges = new HashSet<String>();
		Set<String> outMthds = sysInfo.getMthd(mthd).getOutMthds();
		if(null!=outMthds) {
			for (String outMthd : outMthds) {
				if (sysInfo.isHostMthd(outMthd)) {
					edges.addAll(outMthds);			    
				}
			}
		}
		return edges;
	}
	
	
	
	
	/**
	 * 计算lib中每个函数的kcore值
	 */
	public int calCoreMapLib(String jar) {
		int maxlevel_lib=0;
        leftMthds_lib = new HashSet<String>();
		leftMthds_lib.addAll(sysInfo.getLibMthds(jar));
		delMthds_lib = new HashSet<String>();
		int core = 0;
		while (!leftMthds_lib.isEmpty()) {
			int delNum;
			do {
				delNum = 0;
				Iterator<String> ite = leftMthds_lib.iterator();
				while(ite.hasNext()) {
					String leftM = ite.next();
					int validDegree = 0;// 记录函数有效的出度（一部分函数会在计算的过程中删除掉）
					Set<String> outMthds = getEdge_lib(leftM,jar);
					if (null != outMthds) {
						for (String outMthd : outMthds) {
							if (!delMthds_lib.contains(outMthd)) {
								validDegree++;
							}	
						}
					}
					if (validDegree < core) {// 方法的度太小，执行删除
						ite.remove();
						delMthds_lib.add(leftM);
						//在计算k-core子图时删除掉的节点的核数为k-1
						int mthdCore = core-1;
						if(mthdCore>maxlevel_lib)
							maxlevel_lib=mthdCore;
						mthd2core_lib.put(leftM, new Integer(mthdCore));
						delNum++;
					}
				}
			} while (delNum > 0);
			core++;
		}
		return maxlevel_lib;
	}

	
	private Set<String> getEdge_lib(String mthd,String jar){
		Set<String> edges = new HashSet<String>();
		Set<String> outMthds = sysInfo.getMthd(mthd).getOutMthds();
		if(null!=outMthds) {
			for (String outMthd : outMthds) {
				if(sysInfo.getMthd(outMthd).getJar()!=null) {
					if (sysInfo.getMthd(outMthd).getJar().equals(jar)) {
						edges.addAll(outMthds);			    
					}
				}
			}
			
		}
		return edges;
	}
	
	
	
	
}
				
			
			
