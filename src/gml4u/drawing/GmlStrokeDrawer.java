package gml4u.drawing;

import gml4u.model.GmlStroke;
import processing.core.PGraphics;

public abstract class GmlStrokeDrawer {
	
	
	private String id;
	
	/**
	 * GmlStrokeDrawer constructor
	 * @param id - String
	 */
	public GmlStrokeDrawer(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the GmlStrokeDrawer ID
	 * @return id - String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the GmlStrokeDrawer ID
	 * @param id - String
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Draws the stroke by scaling the points using the given scale 
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 */
	public final void draw(PGraphics g, GmlStroke  stroke, float scale) {
		draw(g, stroke, scale, Float.MIN_VALUE, Float.MAX_VALUE);
	}
	
	/**
	 * Draws the stroke by scaling the points using the given scale 
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param time - float
	 */
	public final void draw(PGraphics g, GmlStroke  stroke, float scale, float time) {
		draw(g, stroke, scale, Float.MIN_VALUE, time);
	}
	
	/**
	 * Draws the stroke by scaling the points using the given scale 
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param minTime - float
	 * @param maxTime - float
	 */	
	public abstract void draw(PGraphics g, GmlStroke stroke, float scale, float minTime, float maxTime);
}
