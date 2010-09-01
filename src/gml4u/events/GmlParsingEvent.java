package gml4u.events;

import gml4u.model.Gml;

public class GmlParsingEvent extends GmlEvent {

	public Gml gml;
	
	public GmlParsingEvent(Gml gml) {
		this.gml = gml;
	}
}
