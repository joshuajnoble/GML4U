package gml4u.model;

import java.util.HashMap;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GmlInfo {

	private static final Logger LOGGER = Logger.getLogger("gml4u.model.GmlInfo");
	
	public HashMap<String, String> map;
	
	public GmlInfo() {
		map = new HashMap<String, String>();
	}
	
	public float getFloat(String key) {
		return Float.parseFloat(map.get(key));
	}
	
	public int getInt(String key) {
		return Integer.parseInt(map.get(key));
	}
	
	public String getString(String key) {
		return map.get(key);
	}
	
	
	public void getInfo() {
		Set<String> keys = map.keySet();
		for (String key: keys) {
			LOGGER.log(Level.FINEST, key+": "+map.get(key));
		}
	}
}
