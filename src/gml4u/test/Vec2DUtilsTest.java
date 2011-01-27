package gml4u.test;

import gml4u.utils.Vec2DUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import toxi.geom.Vec2D;

public class Vec2DUtilsTest extends TestCase {
	
	private static final Logger LOGGER = Logger.getLogger("gml4u.test.Vec2DUtilsTest");
	
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
		float size = Vec2DUtils.getSmallestAxisSize(new Vec2D(.1f, .2f));
		assertTrue("x is the smallest", size == .1f);
	}

	@Test
	public void testGetSmallestAxisSizeY() {
		float size = Vec2DUtils.getSmallestAxisSize(new Vec2D(.2f, .1f));
		assertTrue("y is the smallest", size == .1f);
	}

	
	/* 
	 * Longest axis size tests 
	 * 
	 */
	
	@Test
	public void testGetLongestAxisSizeX() {
		float size = Vec2DUtils.getLongestAxisSize(new Vec2D(.3f, .2f));
		assertTrue("x is the longest", size == .3f);
	}

	@Test
	public void testGetLongestAxisSizeY() {
		float size = Vec2DUtils.getLongestAxisSize(new Vec2D(.1f, .3f));
		assertTrue("y is the longest", size == .3f);
	}


	/*
	 * Normalized test
	 * 
	 */
	
	@Test
	public void testGetNormalizedX() {
		Vec2D v = new Vec2D(.5f, .4f);
		v = Vec2DUtils.getNormalized(v);

		assertTrue("normalized x=1", v.equalsWithTolerance(new Vec2D(1f, .8f), 0.000001f));
	}

	@Test
	public void testGetNormalizedY() {
		Vec2D v = new Vec2D(.4f, .5f);
		v = Vec2DUtils.getNormalized(v);

		assertTrue("normalized y=1", v.equalsWithTolerance(new Vec2D(.8f, 1f), 0.000001f));
	}
}
