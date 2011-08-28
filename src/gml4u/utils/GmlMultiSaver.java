package gml4u.utils;

import gml4u.events.GmlEvent;
import gml4u.events.GmlMultiSavingEvent;
import gml4u.model.Gml;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class GmlMultiSaver extends Thread {

	private static final Logger LOGGER = Logger.getLogger(GmlMultiSaver.class.getName());
	
	private boolean running;           // Is the thread running?  Yes or no?
	private int wait;                  // How many milliseconds should we wait in between executions?
	private String threadId;           // Thread name
	private Map <String, Gml> gmlLocations = new HashMap<String, Gml>();
	private Object parent;
	private Method callback;

	/**
	 * Constructor for GmlSaver thread
	 * Checks for a callback in the meantime (gmlEvent with GmlEvent parameter)
	 * @param wait
	 * @param id
	 * @param parent
	 */
	public GmlMultiSaver (int wait, String id, Object parent){
		try {
			// Looking for a method called "gmlEvent", with one argument of GmlEvent type
			callback = parent.getClass().getMethod("gmlEvent", new Class[] { GmlEvent.class });
		}
		catch (Exception e) {
			LOGGER.warn(parent.getClass()+" shall implement a \"public void gmlEvent(GmlEvent event)\" method to be able to receive GmlEvent");
		}
		
		this.parent = parent;
		this.wait = wait;
		this.running = false;
		this.threadId = id;
	}

	/**
	 * Starts the thread
	 */
	public void start () {
		LOGGER.debug("Starting thread");
		running = true;
		super.start();
	}

	/**
	 * Run method triggered by start()
	 */
	public void run () {
		while (running){
			try {
				if (gmlLocations.size() > 0) {

					Map<String, Boolean> results = new HashMap<String, Boolean>();
					
					LOGGER.debug("Start saving: "+ gmlLocations.size() +" file(s)");
					for (String location : gmlLocations.keySet()) {
						boolean successful = GmlSavingHelper.save(gmlLocations.get(location), location);
						results.put(location, successful);
					}
					
					LOGGER.debug("Finished saving");
					if (callback != null) {
						try {
							// Call the method with this object as the argument!
							LOGGER.debug("Invoking callback");
							callback.invoke(parent, new GmlMultiSavingEvent(results) );
						}
						catch (Exception e) {
							LOGGER.warn("I couldn't invoke that method for some reason. "+e.getMessage());
						}
					}
				}
				gmlLocations.clear();						

				sleep((long)(wait));	
			}
			catch (Exception e) {
				LOGGER.warn(e.getMessage());
			}
		}
		LOGGER.debug(threadId + " thread is done!");  // The thread is done when we get to the end of run()
		quit();
	}

	/**
	 * Save GML file given a location for each file
	 * @param locations - Map<String, Gml>
	 */
	public void save(Map<String, Gml> gmlLocations) {
		LOGGER.debug(gmlLocations.size() +" location to be saved");
		this.gmlLocations.putAll(gmlLocations);
	}
		
	/**
	 * Quits the thread
	 */
	public void quit() {
		LOGGER.debug(threadId + "Quitting.");
		running = false;
		interrupt(); // in case the thread is waiting. . .
	}
}