package gml4u.events;

import gml4u.model.GmlPoint;

import java.util.LinkedList;



public class GmlDrawingEvent extends GmlEvent {

	public int layer;
	public int strokeId;
	public LinkedList<GmlPoint> points;
	
	public GmlDrawingEvent(int layer, int strokeId, LinkedList<GmlPoint> points) {
		this.layer = layer;
		this.strokeId = strokeId;
		this.points = points;
	}
}
