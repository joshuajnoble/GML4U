package gml4u.events;

import gml4u.model.GmlEnvironment;
import toxi.geom.Vec3D;


public class GmlDrawingStart extends GmlEvent {

	public GmlEnvironment environment;
	public Vec3D centroid;
	
	public GmlDrawingStart(GmlEnvironment environment, Vec3D centroid) {
		this.environment = environment;
		this.centroid = centroid;
	}
}
