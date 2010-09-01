package gml4u.model;

import gml4u.model.exceptions.UnknownFieldException;

import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.Vec3D;

public class GmlEnvironment {

	private static final Logger LOGGER = Logger.getLogger("gml4u.model.GmlEnvironment");
	
	public Vec3D offset;
	public Vec3D rotation;
	public Vec3D up;
	public Vec3D screenBounds;
	public Vec3D origin;
	public Vec3D realScale;
	public String realScaleUnit;
		
	public GmlEnvironment() {
		// Use default values
		offset = new Vec3D(0,0,0);
		rotation = new Vec3D(0,0,0);
		up = new Vec3D(1, 0, 0);
		screenBounds = new Vec3D(1024, 768, 0);
		origin = new Vec3D(0, 0, 0);
		realScale = new Vec3D(1024, 768, 0);
		realScaleUnit = "cm";
	}
	

	// TODO set(String name, Object value) + tests instanceof
	public void set(String name, Vec3D v) throws UnknownFieldException {
		if (name.equalsIgnoreCase("offest")) 			this.offset = v;
		else if (name.equalsIgnoreCase("rotation")) 	this.rotation = v;
		else if (name.equalsIgnoreCase("up")) 			this.up = v;
		else if (name.equalsIgnoreCase("screenBounds")) this.screenBounds = v;
		else if (name.equalsIgnoreCase("origin"))		this.origin = v;
		else if (name.equalsIgnoreCase("realScale")) 	this.origin = v;
		else throw new UnknownFieldException("GmlEnvironment doesn't have a \""+name+"\" field");
	}

	public void set(String name, String value) throws UnknownFieldException {
		if (name.equalsIgnoreCase("realScaleUnit")) this.realScaleUnit = value;
		else throw new UnknownFieldException("GmlEnvironment doesn't have a \""+name+"\" field");
	}
	
	public void getInfo() {
		LOGGER.log(Level.FINEST, "offset: "+offset.toString());
		LOGGER.log(Level.FINEST, "rotation: "+rotation.toString());
		LOGGER.log(Level.FINEST, "up: "+up.toString());
		LOGGER.log(Level.FINEST, "screenBounds: "+screenBounds.toString());
		LOGGER.log(Level.FINEST, "origin: "+origin.toString());
		LOGGER.log(Level.FINEST, "realScale: "+ realScale.toString());
		LOGGER.log(Level.FINEST, "realScaleUnit: "+realScaleUnit);
	}
}
