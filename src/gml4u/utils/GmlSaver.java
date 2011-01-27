package gml4u.utils;

import gml4u.events.GmlEvent;
import gml4u.events.GmlSavingEvent;
import gml4u.model.Gml;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class GmlSaver extends Thread {

	private static final Logger LOGGER = Logger.getLogger(GmlSaver.class.getName());
	
	private boolean running;           // Is the thread running?  Yes or no?
	private int wait;                  // How many milliseconds should we wait in between executions?
	private String threadId;           // Thread name
	private Gml gml;
	private String location;
	private Object parent;
	private Method callback;

	/**
	 * Constructor for GmlSaver thread
	 * Checks for a callback in the meantime (gmlEvent with GmlEvent parameter)
	 * @param wait
	 * @param id
	 * @param parent
	 */
	public GmlSaver (int wait, String id, Object parent){
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
				if (null != gml && !location.equals("")) {

					LOGGER.debug("Start saving: "+location);
					GmlSavingHelper.save(gml, location);
					
					LOGGER.debug("Finished saving");
					if (callback != null) {
						try {
							// Call the method with this object as the argument!
							LOGGER.debug("Invoking callback");
							callback.invoke(parent, new GmlSavingEvent(location) );
						}
						catch (Exception e) {
							LOGGER.warn("I couldn't invoke that method for some reason. "+e.getMessage());
						}
					}
				}
				location = "";
				gml = null;						

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
	 * Saves a GML file given its location
	 * @param location
	 */
	public void save(final Gml gml, final String location) {
		LOGGER.debug(location + " to be saved");
		this.gml = gml;
		this.location = location;
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