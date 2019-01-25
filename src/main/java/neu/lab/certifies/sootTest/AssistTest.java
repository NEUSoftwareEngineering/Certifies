package neu.lab.certifies.sootTest;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class AssistTest {
	public static void main(String[] args) throws Exception {
		
		ClassPool pool = new ClassPool();
		pool.appendClassPath("D:\\cWS\\eclipse1\\testGrammar\\target\\testGrammar-1.0.jar");
		CtClass clazz = pool.get("pck1.RefTest");
		System.out.println(clazz);
		
		for(Object obj:clazz.getRefClasses()) {
			System.out.println(obj);
		}
	}
}
