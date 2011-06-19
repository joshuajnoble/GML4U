package gml4u.drawing;

import gml4u.brushes.BoxesDemo;
import gml4u.brushes.CurvesDemo;
import gml4u.brushes.MeshDemo;
import gml4u.model.Gml;
import gml4u.model.GmlStroke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import processing.core.PGraphics;

public class GmlBrushManager {

	private static final Logger LOGGER = Logger.getLogger(GmlBrushManager.class.getName());

	public final static String BRUSH_DEFAULT 		= "GML4U_STYLE_DEFAULT";
	public final static String BRUSH_MESH0000 		= "GML4U_STYLE_MESH0000";	
	public final static String BRUSH_CURVES0000 	= "GML4U_STYLE_CURVES0000";
	public final static String BRUSH_BOXES0000 		= "GML4U_STYLE_BOXES0000";
	
	
	// TODO singleton ?
	//private	static StyleManager styleMgr;
	private static final String DEFAULT = "DEFAULT";
	private Map<String, GmlStrokeDrawer> drawers = new HashMap<String, GmlStrokeDrawer>();
	
	/**
	 * StyleManager constructor
	 */
	public GmlBrushManager() {
		init();
	}
	
	// TODO Singleton ?
	/*
	 * *
	 * Returns a Style manager Singleton 
	 * @return StyleManager
	 *
	public static StyleManager getInstance() {
		if (null == styleMgr) {
			synchronized(StyleManager.class) {
				if (null == styleMgr) {
					styleMgr = new StyleManager();
				}
			}
		}
		return styleMgr;
	}
	*/
	
	
	/**
	 * Init with default styles and sets defaultStyle
	 */
	private void init() {
		drawers.put(DEFAULT, new CurvesDemo());
		
		drawers.put(BRUSH_MESH0000, new MeshDemo());
		drawers.put(BRUSH_CURVES0000, new CurvesDemo());
		drawers.put(BRUSH_BOXES0000, new BoxesDemo());
	}
	
	/**
	 * Sets the default stroke drawer
	 * @param drawer - GmlStrokeDrawer
	 */
	public void setDefault(GmlStrokeDrawer drawer) {
		if (!drawers.containsValue(drawer)) {
			drawers.put(DEFAULT, drawer);
		}
	}
	
	/**
	 * Sets the default stroke drawer based on his styleID
	 * @param styleId - String
	 */
	public void setDefault(String styleId) {
		if (drawers.containsKey(styleId)) {
			drawers.put(DEFAULT, drawers.get(styleId));
		}
		else {
			LOGGER.warn("Style not found, default brush wasn't changed");
		}
	}

	/**
	 * Gets all drawers' Ids as a Collection
	 * @return Collection<String>
	 */
	public Collection<String> getStyles() {
		Collection<String> styles = new ArrayList<String>();
		styles.addAll(this.drawers.keySet());
		return styles;
	}
	
	/**
	 * Returns the amount of drawers registered
	 * @return int
	 */
	public int size() {
		return drawers.size();
	}
	
	/**
	 * Gets a drawer from its index
	 * @param index - int
	 * @return GmlStrokeDrawer
	 */
	public GmlStrokeDrawer get(int index) {
		if (null == drawers.get(index)) {
			LOGGER.warn("Style not found, returning default");
			return drawers.get(DEFAULT);
		}
		return drawers.get(index);
	}
	
	/**
	 * Gets a drawer from its name
	 * @param styleId - String
	 * @return GmlStrokeDrawer
	 */
	public GmlStrokeDrawer get(String styleId) {
		if (null == drawers.get(styleId)) {
			LOGGER.warn("Style not found, returning default");
			return drawers.get(DEFAULT);
		}
		return drawers.get(styleId);
	}
	
	/**
	 * Gets a drawer id from its index
	 * @param index - int
	 * @return String
	 */
	public String getID(int index) {
		if (index < 0 || index > drawers.size()-1) {
			LOGGER.warn("Drawer not found, returning default");
			return DEFAULT;
		}
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(drawers.keySet());
		return keys.get(index);
	}
	
	/**
	 * Adds a new stroke drawer
	 * @param drawer - GmlStrokeDrawer
	 */
	public void add(String id, GmlStrokeDrawer drawer) {
		drawers.put(id, drawer);
	}
	
	/**
	 * Removes a stroke drawer
	 * @param styleId - String
	 */
	public void remove(String styleId) {
		if (drawers.containsKey(styleId)) {
			drawers.remove(styleId);
		}
		else {
			LOGGER.warn(styleId + " style wasn't removed (not found)");
		}
	}
	
	/**
	 * Draws each stroke according to its brush type
	 * @param g - PGraphics
	 * @param gml - Gml
	 * @param scale - float
	 */
	public void draw(PGraphics g, Gml gml, float scale) {
		draw(g, gml, scale, 0, Float.MAX_VALUE);
	}
	
	/**
	 * Draws each stroke according to its brush type and current time
	 * @param g - PGraphics
	 * @param gml - Gml
	 * @param scale - float
	 * @param time - float
	 */
	public void draw(PGraphics g, Gml gml, float scale, float time) {
		draw(g, gml, scale, 0, time);
	}

	/**
	 * Draws each stroke according to its brush type and current time
	 * @param g - PGraphics
	 * @param gml - Gml
	 * @param scale - float
	 * @param timeStart - float
	 * @param timeEnd - float
	 */
	public void draw(PGraphics g, Gml gml, float scale, float timeStart, float timeEnd) {
		if (null == gml) {
			LOGGER.warn("Doing nothing. Reason: gml is null");
		}
		for (GmlStroke currentStroke : gml.getStrokes()) {
			draw(g, currentStroke, scale, timeStart, timeEnd);
		}
	}
	
	/**
	 * Draws the whole stroke according to its brush type
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 */
	public void draw(PGraphics g, GmlStroke stroke, float scale) {
		draw(g, stroke, scale, 0, Float.MAX_VALUE);
	}

	/**
	 * Draws a stroke according to its brush type and current time
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param time - float
	 */
	public void draw(PGraphics g, GmlStroke stroke, float scale, float time) {
		draw(g, stroke, scale, 0, time);
	}
	
	/**
	 * Draws the stroke given a time interval and according to its brush type
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param timeStart - float
	 * @param timeEnd - float
	 */
	public void draw(PGraphics g, GmlStroke stroke, float scale, float timeStart, float timeEnd) {
		if (null != stroke) {
			String style = "";
			if (null == stroke.getBrush()) {
				LOGGER.warn("GmlStroke has no GmlBrush");
			}
			else {
				style = stroke.getBrush().getStyleID();
			}
			draw(g, stroke, scale, timeStart, timeEnd, style);

		}
		else {
			LOGGER.warn("Skipping, GmlStroke is null");
		}
	}
	
	/**
	 * Draws the whole stroke using a given drawer, bypassing its inner drawer id
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param drawer - drawer id
	 */
	public void draw(PGraphics g, GmlStroke stroke, float scale, String drawer) {
		draw(g, stroke, scale, 0, Float.MAX_VALUE, drawer);
	}

	/**
	 * Draws the stroke up given a time and drawer, bypassing its inner brush style
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param time - float
	 * @param drawer - drawer id
	 */
	public void draw(PGraphics g, GmlStroke stroke, float scale, float time, String drawer) {
		draw(g, stroke, scale, 0, time, drawer);
	}
	
	/**
	 * Draws the stroke given a time interval and drawer, bypassing its inner brush style
	 * @param g - PGraphics
	 * @param stroke - GmlStroke
	 * @param scale - float
	 * @param timeStart - float
	 * @param timeEnd - float
	 * @param drawer - drawer id
	 */
	public void draw(PGraphics g, GmlStroke stroke, float scale, float timeStart, float timeEnd, String drawer) {
		
		if (null == drawers.get(drawer)) {
			LOGGER.warn("Unknow drawer or no drawer found, using default instead");
			drawer = DEFAULT;
		}
		g.pushStyle();
		drawers.get(drawer).draw(g, stroke, scale, timeStart, timeEnd);
		g.popStyle();
	}	
}
