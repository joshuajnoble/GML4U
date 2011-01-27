package gml4u.test;

import gml4u.model.Gml;
import gml4u.model.GmlPoint;
import gml4u.model.GmlStroke;
import gml4u.utils.GmlUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import toxi.geom.AABB;
import toxi.geom.Vec3D;

public class GmlUtilsTest extends TestCase {

	private static final Logger LOGGER = Logger.getLogger("gml4u.test.GmlUtilsTest");
	
	private Gml gml;

	@BeforeClass
	public void init() {
		LOGGER.setLevel(Level.FINEST);
	}
	
	//TODO @Before
	public Gml createGml() {
		gml = new Gml(new Vec3D(480, 240, 60));
		
		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(0.1f, 0.35f, 0.25f), 0.01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(0.85f, 0.35f, 0.25f), 0.02f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(0.85f, 0.95f, 0.70f), 0.03f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(0.10f, 0.95f, 0.70f), 0.04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);
		return gml;
	}
	
	//TODO @After
	public void clear() {
		gml = null;
	}
	
	// Rotation from up 1,0,0 to up 0,-1,0
	@Test
	public void testReorientPlusX2MinusY() {
		gml = createGml();
		gml.environment.up = new Vec3D(1, 0, 0);

		AABB aabb = AABB.fromMinMax(new Vec3D(0.35f, 0.15f, 0.25F), new Vec3D(0.95f, 0.9f, 0.7f));
		
		GmlUtils.reorient(gml, true);
		LOGGER.log(Level.FINEST, "testPlusX2MinusY "+gml.getBoundingBox().getMin()+ " / " +gml.getBoundingBox().getMax());
		LOGGER.log(Level.FINEST, "Expected :"+aabb.getMin()+ " / "+ aabb.getMax());
		
		// Check min-max bounding box
		assertTrue("x=1 to y=-1", aabb.equalsWithTolerance(gml.getBoundingBox(), 0.0000001f));
		assertTrue("up vector", gml.environment.up.equals(new Vec3D(0, -1, 0)));
	}

	// Rotation from up -1,0,0 to up 0,-1,0
	@Test
	public void testReorientMinusX2MinusY() {
		Gml gml = createGml();
		gml.environment.up = new Vec3D(-1, 0, 0);

		AABB aabb = AABB.fromMinMax(new Vec3D(0.05f, 0.1f, 0.25f), new Vec3D(0.65f, 0.85f, 0.7f));

		GmlUtils.reorient(gml, true);
		LOGGER.log(Level.FINEST, "testMinusX2MinusY "+gml.getBoundingBox().getMin()+ " / " +gml.getBoundingBox().getMax());
		LOGGER.log(Level.FINEST, "Expected :"+aabb.getMin()+ " / "+ aabb.getMax());

		// Check min-max bounding box
		assertTrue("x=-1 to y=-1", aabb.equalsWithTolerance(gml.getBoundingBox(), 0.0000001f));
		assertTrue("up vector", gml.environment.up.equals(new Vec3D(0, -1, 0)));
	}

	// Rotation from up 0,1,0 to up 0,-1,0
	@Test
	public void testReorientPlusY2MinusY() {
		Gml gml = createGml();
		gml.environment.up = new Vec3D(0, 1, 0);

		AABB aabb = AABB.fromMinMax(new Vec3D(0.15f, 0.05f, 0.7f), new Vec3D(0.9f, 0.65f, 0.25f));
		
		GmlUtils.reorient(gml, true);
		LOGGER.log(Level.FINEST, "testPlusY2MinusY "+gml.getBoundingBox().getMin()+ " / " +gml.getBoundingBox().getMax());
		LOGGER.log(Level.FINEST, "Expected :"+aabb.getMin()+ " / "+ aabb.getMax());
		
		// Check min-max bounding box
		assertTrue("y=1 to y=-1", aabb.equalsWithTolerance(gml.getBoundingBox(), 0.0000001f));
		assertTrue("up vector", gml.environment.up.equals(new Vec3D(0, -1, 0)));
	}

	// Rotation from up 0,-1,0 to up 0,-1,0
	@Test
	public void testReorientMinusY2MinusY() {
		Gml gml = createGml();
		gml.environment.up = new Vec3D(0, -1, 0);

		AABB aabb = AABB.fromMinMax(new Vec3D(0.1f, 0.35f, 0.25f), new Vec3D(0.85f, 0.95f, 0.7f));

		GmlUtils.reorient(gml, true);
		LOGGER.log(Level.FINEST, "testMinusY2MinusY "+gml.getBoundingBox().getMin()+ " / " +gml.getBoundingBox().getMax());
		LOGGER.log(Level.FINEST, "Expected :"+aabb.getMin()+ " / "+ aabb.getMax());
		
		// Check min-max bounding box
		assertTrue("y=-1 to y=-1", aabb.equalsWithTolerance(gml.getBoundingBox(), 0.0000001f));
		assertTrue("up vector", gml.environment.up.equals(new Vec3D(0, -1, 0)));
	}	

	// Rotation from up 0,0,1 to up 0,-1,0
	@Test
	public void testReorientPlusZ2MinusY() {
		Gml gml = createGml();
		gml.environment.up = new Vec3D(0, 0, 1);

		AABB aabb = AABB.fromMinMax(new Vec3D(0.1f, 0.3f, 0.35f), new Vec3D(0.85f, 0.75f, 0.95f));

		GmlUtils.reorient(gml, true);
		LOGGER.log(Level.FINEST, "plusZ2MinusY "+gml.getBoundingBox().getMin()+ " / " +gml.getBoundingBox().getMax());
		LOGGER.log(Level.FINEST, "Expected :"+aabb.getMin()+ " / "+ aabb.getMax());

		// Check min-max bounding box
		assertTrue("z=1 to y=-1", aabb.equalsWithTolerance(gml.getBoundingBox(), 0.0000001f));
		assertTrue("up vector", gml.environment.up.equals(new Vec3D(0, -1, 0)));
	}

	// Rotation from up 0,0,-1 to up 0,-1,0
	@Test
	public void testReorientMinusZ2MinusY() {
		Gml gml = createGml();
		gml.environment.up = new Vec3D(0, 0, -1);

		AABB aabb = AABB.fromMinMax(new Vec3D(0.15f, 0.25f, 0.35f), new Vec3D(0.9f, 0.7f, 0.95f));

		GmlUtils.reorient(gml, true);
		LOGGER.log(Level.FINEST, "testMinusZ2MinusY "+gml.getBoundingBox().getMin()+ " / " +gml.getBoundingBox().getMax());
		LOGGER.log(Level.FINEST, "Expected :"+aabb.getMin()+ " / "+ aabb.getMax());

		// Check min-max bounding box
		assertTrue("z=-1 to y=-1", aabb.equalsWithTolerance(gml.getBoundingBox(), 0.0000001f));
		assertTrue("up vector", gml.environment.up.equals(new Vec3D(0, -1, 0)));
	}
	
	/*
	 * isUpValid tests
	 *  
	 */
	
	@Test
	public void testIsUpValidTwoPlusOnes() {
		Vec3D v = new Vec3D(1, 1, 0);
		boolean result = false;
		assertTrue("invalid up", result == GmlUtils.isUpValid(v));		
	}

	public void testIsUpValidTwoMinusOnes() {
		Vec3D v = new Vec3D(0, -1, -1);
		boolean result = false;
		assertTrue("invalid up", result == GmlUtils.isUpValid(v));		
	}

	public void testIsUpValidTwoMinusOnePlusOne() {
		Vec3D v = new Vec3D(0, -1, 1);
		boolean result = false;
		assertTrue("invalid up", result == GmlUtils.isUpValid(v));		
	}

	@Test
	public void testIsUpValidNoOnes() {
		Vec3D v = new Vec3D(0, 0, 0);
		boolean result = false;
		assertTrue("invalid up", result == GmlUtils.isUpValid(v));				
	}

	@Test
	public void testIsUpValidPlusOne() {
		Vec3D v = new Vec3D(1, 0, 0);
		boolean result = true;
		assertTrue("valid up", result == GmlUtils.isUpValid(v));				
	}

	@Test
	public void testIsUpValidMinusOne() {
		Vec3D v = new Vec3D(0, -1, 0);
		boolean result = true;
		assertTrue("valid up", result == GmlUtils.isUpValid(v));				
	}

	/*
	 * isNormalized tests
	 *  
	 */
	
	@Test
	public void testIsNormalizedLarger() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(-0.1f, -0.35f, -0.25f), 0.01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(1.10f, 1.95f, 1.70f), 0.04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);

		assertTrue("not normalized (larger)", !GmlUtils.isNormalized(gml));
	}
	
	@Test
	public void testIsNormalizedSmaller() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(0.1f, 0.35f, .25f), 0.01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.9f, .95f, .70f), 0.04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);

		assertTrue("not normalized (smaller)", !GmlUtils.isNormalized(gml));
	}

	@Test
	public void testIsNormalized() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(0.0000001f, 0.f, 0.f), 0.01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(1f, 1f, 1.0000001f), 0.04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);

		assertTrue("normalized", GmlUtils.isNormalized(gml));
	}

	
	/*
	 * 
	 * normalizeAll tests
	 * 
	 */

	public void testNormalizeAllLarger() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(-.2f, -.1f, -.5f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.6f, .2f, .0f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(1.4f, 1.2f, 1.1f), .04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);
		
		GmlUtils.normalize(gml);
		

		assertTrue("bounds", GmlUtils.isNormalized(gml));
		assertTrue("origin", gml.environment.originalOriginShift.equalsWithTolerance(new Vec3D(-.2f, -.1f, -.5f), 0.0000001f));
		assertTrue("aspect ratio", gml.environment.originalAspectRatio.equalsWithTolerance(new Vec3D(1.6000001f, 1.3000001f, 1.6f), .0000001f));
		
		assertTrue("normalized origin", gml.environment.normalizedOriginShift.equalsWithTolerance(new Vec3D(-0.12499998f, -0.06250001f, -0.31249997f), .000001f));
		assertTrue("normalized aspect ratio", gml.environment.normalizedAspectRatio.equalsWithTolerance(new Vec3D(1f, 0.81249994f, 1f), .0000001f));
	}
	
	public void testNormalizeAllSmaller() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(.2f, .1f, .25f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.6f, .2f, .3f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.9f, .8f, .8f), .04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);
		
		GmlUtils.normalize(gml);

		assertTrue("bounds", GmlUtils.isNormalized(gml));
		assertTrue("origin", gml.environment.originalOriginShift.equalsWithTolerance(new Vec3D(.2f, .1f, .25f), 0.0000001f));
		assertTrue("aspect ratio", gml.environment.originalAspectRatio.equalsWithTolerance(new Vec3D(.7f, 0.7f, .55f), .0000001f));
		
		assertTrue("normalized origin", gml.environment.normalizedOriginShift.equalsWithTolerance(new Vec3D(0.2857143f, 0.14285715f, 0.3571429f), .000001f));
		assertTrue("normalized aspect ratio", gml.environment.normalizedAspectRatio.equalsWithTolerance(new Vec3D(1f, 1f, 0.7857143f), .0000001f));
	}
		
	public void testNormalizeAllShiftedAhead() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(.2f, .1f, .25f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.6f, .2f, .3f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(1.3f, 1.25f, 1.8f), .04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);
		
		GmlUtils.normalize(gml);
		
		assertTrue("bounds", GmlUtils.isNormalized(gml));
		assertTrue("origin", gml.environment.originalOriginShift.equalsWithTolerance(new Vec3D(0.19999999f, 0.100000024f, .25f), 0.000001f));
		assertTrue("aspect ratio", gml.environment.originalAspectRatio.equalsWithTolerance(new Vec3D(1.0999999f, 1.15f, 1.55f), .0000001f));
		
		assertTrue("normalized origin", gml.environment.normalizedOriginShift.equalsWithTolerance(new Vec3D(0.12903225f, 0.06451615f, 0.16129033f), .000001f));
		assertTrue("normalized aspect ratio", gml.environment.normalizedAspectRatio.equalsWithTolerance(new Vec3D(0.7096774f, 0.7419355f, 1f), .0000001f));
	}
	
	public void testNormalizeAllShiftedBefore() {
		Gml gml = new Gml();

		GmlStroke stroke = new GmlStroke();
		stroke.addPoint(new GmlPoint(new Vec3D(-.2f, -.1f, -.25f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.6f, .2f, .3f), .01f, 1, new Vec3D(), new Vec3D()));
		stroke.addPoint(new GmlPoint(new Vec3D(.7f, .7f, .6f), .04f, 1, new Vec3D(), new Vec3D()));
		stroke.setLayer(0);
		gml.addStroke(stroke);
		
		GmlUtils.normalize(gml);
		
		assertTrue("bounds", GmlUtils.isNormalized(gml));
		assertTrue("origin", gml.environment.originalOriginShift.equalsWithTolerance(new Vec3D(-.2f, -.1f, -.25f), 0.0000001f));
		assertTrue("aspect ratio", gml.environment.originalAspectRatio.equalsWithTolerance(new Vec3D(.9f, 0.8000001f, .85f), .0000001f));
		
		assertTrue("normalized origin", gml.environment.normalizedOriginShift.equalsWithTolerance(new Vec3D(-0.22222224f, -0.11111111f, -0.2777778f), .000001f));
		assertTrue("normalized aspect ratio", gml.environment.normalizedAspectRatio.equalsWithTolerance(new Vec3D(1f, 0.888889f, .94444454f), .0000001f));
	}
}
