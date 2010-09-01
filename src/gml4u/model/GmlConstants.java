package gml4u.model;

public class GmlConstants {

	public final static String HEADER_ROOT = "/gml/tag/header";
	
	public final static String CLIENT_ROOT = HEADER_ROOT+"/client/child::*"; // child::*
	public final static String CLIENT_NAME = CLIENT_ROOT+"/name"; 
	public final static String CLIENT_VERSION = CLIENT_ROOT+"/version";
	public final static String CLIENT_USERNAME = CLIENT_ROOT+"/username";
	public final static String CLIENT_PERMALINK = CLIENT_ROOT+"/permalink";
	public final static String CLIENT_KEYWORDS = CLIENT_ROOT+"/keywords";
	public final static String CLIENT_UNIQUEKEY = CLIENT_ROOT+"/uniqueKey";
	public final static String CLIENT_IP = CLIENT_ROOT+"/ip";
	public final static String CLIENT_TIME = CLIENT_ROOT+"/time";
	public final static String CLIENT_LOCATION_LON = CLIENT_ROOT+"/location/lon";
	public final static String CLIENT_LOCATION_LAT = CLIENT_ROOT+"/location/lat";
	
	public final static String ENVIRONMENT_ROOT = HEADER_ROOT+"/environment";
	public final static String ENVIRONMENT_OFFSET = ENVIRONMENT_ROOT+"/offset";
	public final static String ENVIRONMENT_ROTATION = ENVIRONMENT_ROOT+"/rotation";
	public final static String ENVIRONMENT_UP = ENVIRONMENT_ROOT+"/up";
	public final static String ENVIRONMENT_SCREENBOOUNDS = ENVIRONMENT_ROOT+"/screenBounds";
	public final static String ENVIRONMENT_ORIGIN = ENVIRONMENT_ROOT+"/origin";
	public final static String ENVIRONMENT_REALSCALE = ENVIRONMENT_ROOT+"/realscale";
		
	public final static String ENVIRONMENT_NODES = "/gml/tag/header/environment";

}
