package gml4u.model;

import gml4u.model.exceptions.UnknownFieldException;

import java.util.HashMap;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;


public class GmlClient {

	private static final Logger LOGGER = Logger.getLogger("gml4u.model.GmlClient");

	public HashMap<String, String> map;
	
	public String name;
	public String version;
	public String username;
	public String permalink;
	public String keywords;
	public String uniqueKey;
	public String ip;
	public long time;
	public float lon;
	public float lat;
	
	
	public GmlClient() {
		map = new HashMap<String, String>();
		
		name = "";
		version = "";
		username = "";
		permalink = "";
		keywords = ""; 
		uniqueKey = "";
		ip = "";
		time = 0;
		lon = 0;
		lat = 0;
	}
	
	public void set(String name, String value) throws NumberFormatException, UnknownFieldException {
		if (name.equalsIgnoreCase("name")) 				this.name = value;
		else if (name.equalsIgnoreCase("version")) 		this.version = value;
		else if (name.equalsIgnoreCase("username")) 	this.username = value;
		else if (name.equalsIgnoreCase("keywords")) 	this.keywords = value;
		else if (name.equalsIgnoreCase("uniqueKey"))	this.uniqueKey = value;
		else if (name.equalsIgnoreCase("ip")) 			this.ip = value;
		else if (name.equalsIgnoreCase("time")) 		this.time = Long.parseLong(value);
		else if (name.equalsIgnoreCase("lon")) 			this.lon = Float.parseFloat(value);
		else if (name.equalsIgnoreCase("lat")) 			this.lat = Float.parseFloat(value);
		else throw new UnknownFieldException("GmlClient doesn't have a \""+name+"\" field");
	}
	

	public String get(String key) {
		return map.get(key);
	}
	
	public void getInfo() {
		Set<String> keys = map.keySet();
		for(String key: keys) {
			LOGGER.log(Level.FINEST, key+":"+map.get(key));
		}
		
	}
}
