package gml4u.utils;


import gml4u.events.GmlEvent;
import gml4u.events.GmlMultiParsingEvent;
import gml4u.model.Gml;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GmlMultiParser extends Thread {

	private static final Logger LOGGER = Logger.getLogger(GmlMultiParser.class.getName());
	
	private boolean running;           // Is the thread running?  Yes or no?
	private int wait;                  // How many milliseconds should we wait in between executions?
	private String threadId;           // Thread name
	private boolean normalize;
	private List<String> fileList = new ArrayList<String>();
	private Object parent;
	private Method callback;

	/**
	 * Creates a new GmlMultiParser thread
	 * The parent object must implement a <i>public void gmlEvent(GmlEvent event)</i> method.
	 * @param wait - int (waiting time in ms)
	 * @param id - String (thread id)
	 * @param parent - Object
	 */
	public GmlMultiParser (int wait, String id, Object parent){
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
				if (fileList.size() > 0) {
					LOGGER.debug("Start parsing: "+fileList.size()+ "files");
					
					Map<String, Gml> result = new HashMap<String, Gml>();
					
					for (String fileName : fileList) {
						Gml gml = GmlParsingHelper.getGml(fileName, normalize);
						if (null != gml) {
							result.put(fileName, gml);
						}
					}
					
					LOGGER.debug("Finished parsing");
					if (result.size() > 0 && null != callback) {
						try {
							// Call the method with this object as the argument!
							LOGGER.debug("Invoking callback");
							callback.invoke(parent, new GmlMultiParsingEvent(result) );
						}
						catch (Exception e) {
							LOGGER.warn("Couldn't invoke the callback method for some reason. "+e.getMessage());
						}
					}
				}
				fileList.clear();							

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
	 * Parses GML files found at the given location.<br/>
	 * Must be local files or a http resource as well.<br/>
	 * Note that you might need a local proxy to access extenal http resources when running inside an unsigned Applet 
	 * @param fileList - String
	 */
	public void parse(final List<String> fileList) {
		LOGGER.debug(fileList + " to be parsed");
		this.fileList.addAll(fileList);
		this.normalize = false;
	}
	
	/**
	 * Parses a GML file using the given location and normalizes it.<br/>
	 * Can be a local file or a http resource as well.<br/>
	 * Note that you might need a local proxy to access extenal http resources when running inside an unsigned Applet 
	 * @param fileList - String
	 * @param normalize - boolean
	 */
	public void parse(final List<String> fileList, boolean normalize) {
		LOGGER.debug(fileList + " to be parsed");
		this.fileList.addAll(fileList);
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