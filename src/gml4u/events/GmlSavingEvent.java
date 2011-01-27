package gml4u.events;

public class GmlSavingEvent extends GmlEvent {
	
	public String location;
	
	/**
	 * Creates a new GmlSavingEvent using the given location
	 * @param location - String
	 */
	public GmlSavingEvent(String location) {
		// TODO success/fail info
		this.location = location;
	}
}