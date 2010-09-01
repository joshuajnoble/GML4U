package gml4u.events;

import gml4u.model.Gml;
import gml4u.model.GmlPoint;
import gml4u.model.GmlStroke;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;


public class GmlDrawingManager {


	private Gml gml;
	private GmlDrawingEventHandler eventHandler;
	private boolean start = true;
	private boolean ended = false;


	/**
	 * Constructor for the drawing manager
	 */
	public GmlDrawingManager() {
		this.eventHandler = new GmlDrawingEventHandler();		
	}

	/**
	 * Constructor for the drawing manager with a GML object passed as an argument
	 * @param gml
	 */
	public GmlDrawingManager(Gml gml) {
		this.eventHandler = new GmlDrawingEventHandler();		
		setGml(gml);
	}


	/**
	 * Sets the GML object to be used by the drawing manager
	 * Resets the drawing manager in the meantime
	 * @param gml
	 */
	public void setGml(Gml gml) {
		this.gml = gml;
		reset();
	}

	/**
	 * Registers a listener to receive GmlEvents for the given layer
	 * Note: the Object passed must implement a public gmlEvent(GmlEvent event) method
	 * @param listener
	 * @param layer
	 */
	public void register(Object listener, int layer) {
		eventHandler.addListener(listener, layer);
	}

	/**
	 * Unregisters a listener from the given layer
	 * @param listener
	 * @param layer
	 */
	public void unregister(Object listener, int layer) {
		eventHandler.removeListener(listener, layer);
	}

	/**
	 * Returns the number of layers in the current GML file
	 * @return
	 */
	public int getNbLayers() {
		return gml.getNbLayers();
	}

	/**
	 * Resets the drawing manager
	 */
	public void reset() {
		start = true;
		ended = false;
	}

	/**
	 * Filters a linked list of points to only keep the points drawn up to the given time  
	 * @param source
	 * @param time
	 * @return
	 */
	private LinkedList<GmlPoint> getPoints(LinkedList<GmlPoint> source, float time) {
		LinkedList<GmlPoint> points = new LinkedList<GmlPoint>();
		Iterator<GmlPoint> i = source.iterator();

		while(i.hasNext()) {
			GmlPoint point = i.next();
			if (point.time <= time) {
				points.add(point);
			}
			else {
				//points.add(point);
				return points;
			}
		}
		return points;
	}

	/**
	 * Pulses the drawing manager which will sent the matching GmlEvents to its
	 * listeners (start, drawing info, stop) in return.
	 * @param time
	 */
	public void pulse(float time) {

		if (null != gml) {
			if(start) {
				GmlDrawingStart event = new GmlDrawingStart(gml.environment, gml.getCentroid());
				eventHandler.fireNewEvent(event);
				start = false;
				ended = false; // useless?
			}

			// Get registered (and sorted) layers list
			TreeSet<Integer> layerSet = new TreeSet<Integer>();
			layerSet.addAll(eventHandler.getLayersWithListeners());

			// Loop through each layer
			for (Integer layer: layerSet) {

				ArrayList<GmlStroke> strokes = new ArrayList<GmlStroke>();
				strokes = gml.layers.get(layer);

				if (null != strokes) {
					for (int i=0; i<strokes.size(); i++) {
						GmlStroke stroke = strokes.get(i);
						LinkedList<GmlPoint> points = getPoints(stroke.points, time);
						
						if (points.size() > 0) {
							GmlDrawingEvent event = new GmlDrawingEvent(layer, i, points);
							eventHandler.fireNewEvent(event);
						}
					}
				}
			}

			if (time > gml.getDuration() && !ended) {
				ended = true;
				GmlDrawingEnd event = new GmlDrawingEnd();
				eventHandler.fireNewEvent(event);
			}
		}
	}
}
