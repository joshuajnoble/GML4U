package gml4u.test;

import gml4u.utils.Vec3DUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import toxi.geom.Vec3D;

public class Vec3DUtilsTest extends TestCase {
	
	private static final Logger LOGGER = Logger.getLogger(Vec3DUtilsTest.class.getName());
	
	@BeforeClass
	public void init() {
		LOGGER.setLevel(Level.FINEST);
	}

	/* 
	 * Smallest axis size tests 
	 * 
	 */
	
	@Test
	public void testGetSmallestAxisSizeX() {
		float size = Vec3DUtils.getSmallestAxisSize(new Vec3D(.1f, .2f, .3f));
		assertTrue("x is the smallest", size == .1f);
	}

	@Test
	public void testGetSmallestAxisSizeY() {
		float size = Vec3DUtils.getSmallestAxisSize(new Vec3D(.2f, .1f, .3f));
		assertTrue("y is the smallest", size == .1f);
	}

	@Test
	public void testGetSmallestAxisSizeZ() {
		float size = Vec3DUtils.getSmallestAxisSize(new Vec3D(.2f, .3f, .1f));
		assertTrue("z is the smallest", size == .1f);
	}

	/* 
	 * Longest axis size tests 
	 * 
	 */
	@Test
	public void testGetLongestAxisSizeX() {
		float size = Vec3DUtils.getLongestAxisSize(new Vec3D(.3f, .2f, .1f));
		assertTrue("x is the longest", size == .3f);
	}

	@Test
	public void testGetLongestAxisSizeY() {
		float size = Vec3DUtils.getLongestAxisSize(new Vec3D(.1f, .3f, .2f));
		assertTrue("y is the longest", size == .3f);
	}

	@Test
	public void testGetLongestAxisSizeZ() {
		float size = Vec3DUtils.getLongestAxisSize(new Vec3D(.1f, .2f, .3f));
		assertTrue("z is the longest", size == .3f);
	}

	
	/*
	 * 
	 * Scaling tests
	 * 
	 * 
	 */
	
	@Test
	public void testGetScalingCoefX() {
		Vec3D v = new Vec3D(.3f, .2f, .1f);
		Vec3D scalingCoef = Vec3DUtils.getScalingCoef(v);
		assertTrue("scale on x", scalingCoef.equals(new Vec3D(.3f, .3f, .3f)));
	}
	
	@Test
	public void testGetScalingCoefY() {
		Vec3D v = new Vec3D(.2f, .3f, .1f);
		Vec3D scalingCoef = Vec3DUtils.getScalingCoef(v);
		assertTrue("scale on y", scalingCoef.equals(new Vec3D(.3f, .3f, .3f)));
	}
	
	@Test
	public void testGetScalingCoefZ() {
		Vec3D v = new Vec3D(.1f, .2f, .3f);
		Vec3D scalingCoef = Vec3DUtils.getScalingCoef(v);
		assertTrue("scale on z", scalingCoef.equals(new Vec3D(.3f, .3f, .3f)));
	}
	
	
	/*
	 * 
	 * Normalizer test
	 * 
	 * 
	 */
	
	
	@Test
	public void testIsNormalizedTrue() {
		Vec3D v = new Vec3D(.5f, .2f, .7f);
		assertTrue("normalized", Vec3DUtils.isNormalized(v));		
	}
	
	@Test
	public void testIsNormalizedTrueLimit() {
		Vec3D v = new Vec3D(.5f, .2f, 1.0000001f);
		assertTrue("normalized limit", Vec3DUtils.isNormalized(v));		
	}
	
	@Test
	public void testIsNormalizedFalseX() {
		Vec3D v = new Vec3D(1.1f, .5f, .2f);
		assertFalse("not normalized X", Vec3DUtils.isNormalized(v));		
	}
	
	@Test
	public void testIsNormalizedFalseY() {
		Vec3D v = new Vec3D(.5f, 1.1f, .2f);
		assertFalse("not normalized Y", Vec3DUtils.isNormalized(v));		
	}
	
	@Test
	public void testIsNormalizedFalseZ() {
		Vec3D v = new Vec3D(.2f, .5f, 1.1f);
		assertFalse("not normalized Z", Vec3DUtils.isNormalized(v));		
	}
	
	/*
	 * 
	 * Normalizer test
	 * 
	 * 
	 */
	
	
	@Test
	public void testGetNormalizerX() {
		Vec3D v = new Vec3D(.5f, .2f, .1f);
		Vec3D normalizer = Vec3DUtils.getNormalizer(v);
		assertTrue("normalizer", normalizer.equals(new Vec3D(2, 2, 2)));		
	}
	
	@Test
	public void testGetNormalizerY() {
		Vec3D v = new Vec3D(.1f, .5f, .2f);
		Vec3D normalizer = Vec3DUtils.getNormalizer(v);
		assertTrue("normalizer", normalizer.equals(new Vec3D(2, 2, 2)));		
	}
	
	@Test
	public void testGetNormalizerZ() {
		Vec3D v = new Vec3D(.1f, .2f, .5f);
		Vec3D normalizer = Vec3DUtils.getNormalizer(v);
		assertTrue("normalizer", normalizer.equals(new Vec3D(2, 2, 2)));		
	}
	
	
	/*
	 * Normalized test
	 * 
	 */
	
	@Test
	public void testGetNormalizedX() {
		Vec3D v = new Vec3D(.5f, .4f, .3f);
		v = Vec3DUtils.getNormalized(v);
		assertTrue("normalized x=1", v.equalsWithTolerance(new Vec3D(1f, .8f, .6f), 0.000001f));
	}

	@Test
	public void testGetNormalizedY() {
		Vec3D v = new Vec3D(.4f, .5f, .3f);
		v = Vec3DUtils.getNormalized(v);
		assertTrue("normalized y=1", v.equalsWithTolerance(new Vec3D(.8f, 1f, .6f), 0.000001f));
	}

	@Test
	public void testGetNormalizedZ() {
		Vec3D v = new Vec3D(.3f, .4f, .5f);
		v = Vec3DUtils.getNormalized(v);
		assertTrue("normalized z=1", v.equalsWithTolerance(new Vec3D(.6f, .8f, 1f), 0.000001f));
	}


	
	/*
	 * 
	 * Reorient
	 * 
	 */
	
	// TODO

	@Test
	public void testReorientUpNull() {
		
	}
	
	
	/*
	 * 
	 * Transpose
	 * 
	 */
	
	@Test
	public void testTransposeXAxis(){
		Vec3D v = new Vec3D(.75f, .5f, .25f);
		Vec3D shift = new Vec3D(.5f, .5f, .5f);
		Vec3DUtils.transpose(v, shift, Vec3D.X_AXIS, (float) Math.PI/2);
		assertTrue("transpose on x", v.equalsWithTolerance(new Vec3D(.75f, .75f, .5f), .0000001f));
	}

	
	@Test
	public void testTransposeYAxis(){
		Vec3D v = new Vec3D(.75f, .5f, .25f);
		Vec3D shift = new Vec3D(.5f, .5f, .5f);
		Vec3DUtils.transpose(v, shift, Vec3D.Y_AXIS, (float) Math.PI/2);
		assertTrue("transpose on y", v.equalsWithTolerance(new Vec3D(.25f, .5f, .25f), .0000001f));
	}

	
	@Test
	public void testTransposeZAxis(){
		Vec3D v = new Vec3D(.75f, .5f, .25f);
		Vec3D shift = new Vec3D(.5f, .5f, .5f);
		Vec3DUtils.transpose(v, shift, Vec3D.Z_AXIS, (float) Math.PI/2);
		assertTrue("transpose on z", v.equalsWithTolerance(new Vec3D(.5f, .75f, .25f), .0000001f));
	}

	
	/*
	 * Swap tests
	 * 
	 * 
	 * 
	 */
	
	@Test
	public void testSwapPlusX() {
		Vec3D v = new Vec3D(.5f, 1, .25f);
		Vec3D up = new Vec3D(1, 0, 0);
		Vec3DUtils.swap(v, up);
		assertTrue("", v.equalsWithTolerance(new Vec3D(1, .5f, .25f), .0000001f));
	}

	@Test
	public void testSwapMinusX() {
		Vec3D v = new Vec3D(.5f, 1, .25f);
		Vec3D up = new Vec3D(-1, 0, 0);
		Vec3DUtils.swap(v, up);
		assertTrue("", v.equalsWithTolerance(new Vec3D(1, .5f, .25f), .0000001f));
	}


	@Test
	public void testSwapPlusY() {
		Vec3D v = new Vec3D(.5f, 1, .25f);
		Vec3D up = new Vec3D(0, 1, 0);
		Vec3DUtils.swap(v, up);
		assertTrue("", v.equalsWithTolerance(new Vec3D(.5f, 1, .25f), .0000001f));	
	}

	@Test
	public void testSwapMinusY() {
		Vec3D v = new Vec3D(.5f, 1, .25f);
		Vec3D up = new Vec3D(0, -1, 0);
		Vec3DUtils.swap(v, up);
		assertTrue("", v.equalsWithTolerance(new Vec3D(.5f, 1, .25f), .0000001f));	
	}


	@Test
	public void testSwapPlusZ() {
		Vec3D v = new Vec3D(.5f, 1, .25f);
		Vec3D up = new Vec3D(0, 0, 1);
		Vec3DUtils.swap(v, up);
		assertTrue("", v.equalsWithTolerance(new Vec3D(.5f, .25f, 1), .0000001f));	
	}

	
	@Test
	public void testSwapMinusZ() {
		Vec3D v = new Vec3D(.5f, 1, .25f);
		Vec3D up = new Vec3D(0, 0, -1);
		Vec3DUtils.swap(v, up);
		assertTrue("", v.equalsWithTolerance(new Vec3D(.5f, .25f, 1), .0000001f));	
	}

	
	/*
	 * Tests getFitInside2DXY
	 * 
	 * 
	 */
	
	@Test
	public void testGetFitInside2DXYLandscapeXLarger() {
		Vec3D container = new Vec3D(200, 100, 100);
		Vec3D aspectRatio = new Vec3D(1, .3f, .5f);
		float ratio = .5f;
		Vec3D scaler = Vec3DUtils.getFitInside2DXY(container, aspectRatio, ratio);
		assertTrue("scale on x: ", scaler.equalsWithTolerance(new Vec3D(100, 30, 50), .000002f));
	}

	@Test
	public void testGetFitInside2DXYLandscapeXSmaller() {
		Vec3D container = new Vec3D(200, 50, 100);
		Vec3D aspectRatio = new Vec3D(1, .5f, .5f);
		float ratio = .5f;
		Vec3D scaler = Vec3DUtils.getFitInside2DXY(container, aspectRatio, ratio);
		assertTrue("scale on x/ aspectRatio.x: ", scaler.equalsWithTolerance(new Vec3D(50, 25, 25), .0000001f));
	}

	
	@Test
	public void testGetFitInside2DXYPortraitYLarger() {
		Vec3D container = new Vec3D(100, 200, 100);
		Vec3D aspectRatio = new Vec3D(.3f, 1, .5f);
		float ratio = .5f;
		Vec3D scaler = Vec3DUtils.getFitInside2DXY(container, aspectRatio, ratio);
		assertTrue("scale on y: ", scaler.equalsWithTolerance(new Vec3D(30, 100, 50), .000002f));
		
	}

	@Test
	public void testGetFitInside2DXYPortraitYSmaller() {
		Vec3D container = new Vec3D(50, 200, 100);
		Vec3D aspectRatio = new Vec3D(.5f, 1, .5f);
		float ratio = .5f;
		Vec3D scaler = Vec3DUtils.getFitInside2DXY(container, aspectRatio, ratio);
		assertTrue("scale on y/ aspectRatio.y: ", scaler.equalsWithTolerance(new Vec3D(25, 50, 25), .0000001f));		
	}

	@Test
	public void testGetFitInside2DXYDifferentXLarger() {
		Vec3D container = new Vec3D(200, 100, 100);
		Vec3D aspectRatio = new Vec3D(.3f, 1, .5f);
		float ratio = .5f;
		Vec3D scaler = Vec3DUtils.getFitInside2DXY(container, aspectRatio, ratio);
		assertTrue("scale on y : ", scaler.equalsWithTolerance(new Vec3D(15f, 50, 25), .000001f));				
	}

	@Test
	public void testGetFitInside2DXYDifferentYLarger() {
		Vec3D container = new Vec3D(100, 200, 100);
		Vec3D aspectRatio = new Vec3D(1, .3f, .5f);
		float ratio = .5f;
		Vec3D scaler = Vec3DUtils.getFitInside2DXY(container, aspectRatio, ratio);
		assertTrue("scale on y : ", scaler.equalsWithTolerance(new Vec3D(50, 15, 25), .000001f));						
	}
	
}
