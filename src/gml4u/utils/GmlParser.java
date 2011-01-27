package gml4u.utils;

import gml4u.events.GmlEvent;
import gml4u.events.GmlParsingEvent;
import gml4u.model.Gml;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class GmlParser extends Thread {

	private static final Logger LOGGER = Logger.getLogger(GmlParser.class.getName());
	
	private boolean running;           // Is the thread running?  Yes or no?
	private int wait;                  // How many milliseconds should we wait in between executions?
	private String threadId;           // Thread name
	private boolean normalize;
	private String location;
	private Gml result;
	private Object parent;
	private Method callback;

	/**
	 * Creates a new GmlParser thread
	 * The parent object must implement a <i>public void gmlEvent(GmlEvent event)</i> method.
	 * @param wait - int (waiting time in ms)
	 * @param id - String (thread id)
	 * @param parent - Object
	 */
	public GmlParser (int wait, String id, Object parent){
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
				if (!location.equals("")) {
					LOGGER.debug("Start parsing: "+location);
					result = GmlParsingHelper.getGml(location, normalize);
					
					LOGGER.debug("Finished parsing");
					if (null != result && null != callback) {
						try {
							// Call the method with this object as the argument!
							LOGGER.debug("Invoking callback");
							callback.invoke(parent, new GmlParsingEvent(result) );
						}
						catch (Exception e) {
							LOGGER.warn("Couldn't invoke the callback method for some reason. "+e.getMessage());
						}
					}
				}
				result = null;
				location = "";							

				sleep((long)(wait));	
			}
			// TODO Lock and loop issue exception when NullPointerExceptions in GmlParsingHelper
			catch (Exception e) {
				//LOGGER.warn(e.getMessage());
			}
		}
		LOGGER.debug(threadId + " thread is done!");  // The thread is done when we get to the end of run()
		quit();
	}

	/**
	 * Parses a GML file using the given location.<br/>
	 * Can be a local file or a http resource as well.<br/>
	 * Note that you might need a local proxy to access extenal http resources when running inside an unsigned Applet 
	 * @param location - String
	 */
	public void parse(final String location) {
		LOGGER.debug(location + " to be parsed");
		this.location = location;
		this.normalize = false;
	}
	
	/**
	 * Parses a GML file using the given location and normalizes it.<br/>
	 * Can be a local file or a http resource as well.<br/>
	 * Note that you might need a local proxy to access extenal http resources when running inside an unsigned Applet 
	 * @param location - String
	 * @param normalize - boolean
	 */
	public void parse(final String location, boolean normalize) {
		LOGGER.debug(location + " to be parsed");
		this.location = location;
		this.normalize = normalize;
	}

	/**
	 * Quits the thread
	 */
	public void quit() {
		LOGGER.debug(threadId + " quitting.");
		running = false;
		interrupt(); // in case the thread is waiting. . .
	}
}