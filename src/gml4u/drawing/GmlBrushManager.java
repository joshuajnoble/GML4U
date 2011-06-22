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
		
	public static final String BRUSH_DEFAULT = CurvesDemo.ID;
	
	private String defaultId;
	private Map<String, GmlStrokeDrawer> drawers = new HashMap<String, GmlStrokeDrawer>();
	
	/**
	 * GmlBrushManager constructor
	 */
	public GmlBrushManager() {
		init();
	}
	
	
	/**
	 * Init with default styles and sets defaultStyle
	 */
	private void init() {

		GmlStrokeDrawer curve = new CurvesDemo();
		add(curve);
		GmlStrokeDrawer mesh = new MeshDemo();
		add(mesh);
		GmlStrokeDrawer boxes = new BoxesDemo();
		add(boxes);
		
		defaultId = curve.getId();
	}
	
	/**
	 * Sets the default stroke drawer
	 * @param drawer - GmlStrokeDrawer
	 */
	public void setDefault(GmlStrokeDrawer drawer) {
		add(drawer);
		setDefault(drawer.getId());
	}
	
	/**
	 * Sets the default stroke drawer based on his styleID (if already exists)
	 * @param styleId - String
	 */
	public void setDefault(String styleId) {
		if (drawers.containsKey(styleId)) {
			defaultId = styleId;
		}
		else {
			LOGGER.warn("Style not found, default style wasn't changed");
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
			return drawers.get(defaultId);
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
			return drawers.get(defaultId);
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
			return defaultId;
		}
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(drawers.keySet());
		return keys.get(index);
	}
	
	
	/**
	 * Adds a new stroke drawer.
	 * If another drawer with the same name exists, it will be replaced.
	 * @param drawer - GmlStrokeDrawer
	 */
	public void add(GmlStrokeDrawer drawer) {
		if (null != drawers.get(drawer.getId())) {
			LOGGER.warn("Replacing existing drawer with the same name: "+ drawer.getId());
		}
		drawers.put(drawer.getId(), drawer);		
	}
	
	/**
	 * Adds a new stroke drawer and changes its ID in the same time
	 * @param id - String
	 * @param drawer - GmlStrokeDrawer
	 */
	public void add(String id, GmlStrokeDrawer drawer) {
		drawer.setId(id);
		add(drawer);
	}
	
	/**
	 * Removes a stroke drawer based on its id
	 * If this style is the default one, it won't be removed and you'll need to set another default one
	 * @param styleId - String
	 */
	public void remove(String styleId) {
		if (drawers.containsKey(styleId)) {
			if (!defaultId.equals(styleId)) {
				drawers.remove(styleId);
			}
			else {
				LOGGER.warn(styleId + " wasn't removed (currently used as default style)");
			}
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
			drawer = defaultId;
		}
		g.pushStyle();
		drawers.get(drawer).draw(g, stroke, scale, timeStart, timeEnd);
		g.popStyle();
	}	
}
