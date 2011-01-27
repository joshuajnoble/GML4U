package gml4u.test;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
		
public class Gml4uTestSuite  extends TestCase {
		
	public Gml4uTestSuite(String method) {
		super(method);
	}
	
	static public Test suite() {
		TestSuite suite = new TestSuite(); 
		// Grab everything: 
		suite.addTestSuite(GmlUtilsTest.class);
		suite.addTestSuite(Vec2DUtilsTest.class);
		suite.addTestSuite(Vec3DUtilsTest.class);

		return suite;
	}
}
