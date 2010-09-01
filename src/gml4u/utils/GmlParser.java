package gml4u.utils;

import gml4u.events.GmlEvent;
import gml4u.events.GmlParsingEvent;
import gml4u.model.Gml;

import java.lang.reflect.Method;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GmlParser extends Thread {

	private static final Logger LOGGER = Logger.getLogger("gml4u.utils.GmlParser");
	
	private boolean running;           // Is the thread running?  Yes or no?
	private int wait;                  // How many milliseconds should we wait in between executions?
	private String id;                 // Thread name
	private boolean available;

	public String location;
	public Gml result;
	
	private Object parent;
	private Method callback;
	

	public GmlParser (int wait, String id, Object parent){
		try {
			// Looking for a method called "gmlEvent", with one argument of GmlEvent type
			callback = parent.getClass().getMethod("gmlEvent", new Class[] { GmlEvent.class });
		}
		catch (Exception e) {
			LOGGER.log(Level.WARNING, "Method not in parent class? " + e);
		}

		this.parent = parent;
		this.wait = wait;
		this.running = false;
		this.id = id;
	}

	public void start () {
		running = true;
		available = true;
		LOGGER.log(Level.FINEST, "Starting thread");
		// Do whatever start does in Thread, don't forget this!
		super.start();
	}

	// Triggered by start()
	public void run () {
		while (running){
			try {
				if (!available && !(location.equals(""))) {
					LOGGER.log(Level.FINEST, "Start parsing: "+location);
					result = GmlParsingHelper.getGml(location);
					location = "";
					
					LOGGER.log(Level.FINEST, "Finished parsing");
					if (result != null && callback != null) {
						try {
							// Call the method with this object as the argument!
							LOGGER.log(Level.FINEST, "Invoking callback");
							callback.invoke(parent, new GmlParsingEvent(result) );
						}
						catch (Exception e) {
							LOGGER.log(Level.WARNING, "I couldn't invoke that method for some reason. "+e.getMessage());
						}
						result = null;
					}
					LOGGER.log(Level.FINEST, "Reseting");
					//location = "";
					available = true;
				}
				sleep((long)(wait));	
			}
			catch (Exception e) {
				LOGGER.log(Level.FINEST, e.getMessage());
				location = "";
				available = true;				
			}
			
		}
		LOGGER.log(Level.FINEST, id + " thread is done!");  // The thread is done when we get to the end of run()
		quit();
	}

	public void parse(String location) {
		LOGGER.log(Level.FINEST, location);
		this.location = location;
		this.available = false;
	}
	
	public boolean isAvailable() {
		return available;
	}


	// Our method that quits the thread
	public void quit() {
		LOGGER.log(Level.FINEST, "Quitting.");
		running = false;
		interrupt(); // in case the thread is waiting. . .
	}


}

