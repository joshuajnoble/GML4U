package gml4u.events;

import gml4u.events.GmlDrawingEvent;
import gml4u.events.GmlDrawingEventHandler;
import gml4u.utils.CallbackUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


import java.util.logging.Level;
import java.util.logging.Logger;

public class GmlDrawingEventHandler {

	private static final Logger LOGGER = Logger.getLogger("gml4u.events.GmlDrawingEventHandler");

	private HashMap<Integer, ArrayList<Object>> listenersByLayer = new HashMap<Integer, ArrayList<Object>>();
	private final String callbackMethod = "gmlEvent";
	@SuppressWarnings("unchecked")
	private final Class callbackParameterClass = GmlEvent.class; 

	/**
	 * Constructor for GmlDrawingEventHandler
	 */
	public GmlDrawingEventHandler() {
	}

	
	/**
	 * Adds a new listener
	 * @param Object (implementing a gmlEvent(GmlEvent) method)
	 * @param int (layer)
	 */
	public void addListener(Object listener, int layer) {

		// Check callback methods
		if (!CallbackUtils.hasRequiredCallback(listener, "gmlEvent", GmlEvent.class)) {
			LOGGER.log(Level.WARNING, listener.getClass()+" shall implement \"public void gmlEvent(GmlEvent event)\" to be able to receive GmlEvent");
		}
		else {
			// If layer doesn't exist, then add it
			if(null == listenersByLayer.get(layer)) {
				listenersByLayer.put(layer, new ArrayList<Object>());
			}
			// Add the listener to the layer
			listenersByLayer.get(layer).add(listener);
		}
	}


	/**
	 * Removes the given listener
	 * @param Object (implementing a gmlEvent(GmlEvent) method)
	 * @param int (layer)
	 */
	public void removeListener(Object listener, int layer) { 
		listenersByLayer.get(layer).remove(listener);

		// Remove the layer entry if it contains no more listeners
		if(listenersByLayer.get(layer).size() == 0) {
			listenersByLayer.remove(layer);
		}
	}


	/**
	 * Gets the list of layers having at least one listener
	 */
	public Set<Integer> getLayersWithListeners() {
		return listenersByLayer.keySet();
	}
	
	/**
	 * Fires a new Event 
	 * @param event
	 */
	public void fireNewEvent(GmlEvent event) {

		// Fire events by layer only
		if (event instanceof GmlDrawingEvent) {
			int layer = ((GmlDrawingEvent) event).layer;
			callBack(listenersByLayer.get(layer), event);
		}
		// Fire events for all layers
		else {
			for (ArrayList<Object> listeners : listenersByLayer.values()) {
				callBack(listeners, event);
			}
		}
	}
	
	
	/**
	 * Call the callback method for an array of Objects
	 * @param listeners
	 * @param event
	 */
	private void callBack(ArrayList<Object> listeners, GmlEvent event) {
		for (Object listener : listeners) {
			try {
				listener.getClass().getMethod(callbackMethod, callbackParameterClass).invoke(listener, event);
			} catch (IllegalArgumentException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			} catch (SecurityException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			} catch (IllegalAccessException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			} catch (InvocationTargetException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			} catch (NoSuchMethodException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			}
		}
	}
}
