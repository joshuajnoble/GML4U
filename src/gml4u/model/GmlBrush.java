package gml4u.model;

import gml4u.model.exceptions.UnknownFieldException;

import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.color.NamedColor;
import toxi.color.TColor;
import toxi.geom.Vec3D;

public class GmlBrush {

	private static final Logger LOGGER = Logger.getLogger("gml4u.model.GmlBrush");
	
	public int layerRelative = 0;
	public int layerAbsolute = Integer.MIN_VALUE;
	public TColor color;
	public String mode;
	public String uniqueStyleID;
	public String spec;
	public int width = -1;
	public float speedToWidthRatio;
	public float dripAmnt;
	public float dripSpeed;
	public Vec3D dripVecRelativeToUp;

	
	public GmlBrush() {
	
	}
	
	public void setDefault() {
		layerAbsolute = 0;
		color = new TColor(NamedColor.WHITE);

		mode = "0";
		uniqueStyleID = "01010101default";
		spec = "";
		width = 10;
		speedToWidthRatio = 1.5f;
		dripAmnt = 1.0f;
		dripSpeed = 1.0f;
		dripVecRelativeToUp = new Vec3D(0, 1, 0);		
	}
	
	public void merge(GmlBrush previousBrush) {
		// If relative, set based on previous
		if (layerRelative != 0) layerAbsolute = previousBrush.layerAbsolute + layerRelative;
		// Else if not defined, set to previous one
		else if (Integer.MIN_VALUE == layerAbsolute) {
			layerAbsolute = previousBrush.layerAbsolute;
		}
		if (null == mode) mode = previousBrush.mode;
		if (null == uniqueStyleID) uniqueStyleID = previousBrush.uniqueStyleID;
		if (null == spec) spec = previousBrush.spec;
		if (-1 == speedToWidthRatio) speedToWidthRatio = previousBrush.speedToWidthRatio;
		if (-1 == dripAmnt) dripAmnt = previousBrush.dripAmnt;
		if (-1 == dripSpeed) dripSpeed = previousBrush.dripSpeed;
		if (null == dripVecRelativeToUp) dripVecRelativeToUp = previousBrush.dripVecRelativeToUp;
	}

	public void set(String name, String value) throws NumberFormatException, UnknownFieldException {
		if (name.equalsIgnoreCase("layerAbsolute"))				this.layerAbsolute = Integer.parseInt(value);
		else if (name.equalsIgnoreCase("layerRelative"))		this.layerRelative = Integer.parseInt(value);
		else if (name.equalsIgnoreCase("mode")) 				this.mode = value;
		else if (name.equalsIgnoreCase("uniqueStyleID")) 		this.uniqueStyleID = value;
		else if (name.equalsIgnoreCase("spec")) 				this.spec = value;
		else if (name.equalsIgnoreCase("width"))				this.width = Integer.parseInt(value);
		else if (name.equalsIgnoreCase("speedToWidthRatio")) 	this.speedToWidthRatio = Float.parseFloat(value);
		else if (name.equalsIgnoreCase("dripAmnt")) 			this.dripAmnt = Float.parseFloat(value);
		else if (name.equalsIgnoreCase("dripSpeed")) 			this.dripSpeed = Float.parseFloat(value);
		else throw new UnknownFieldException("GmlBrush doesn't have a \""+name+"\" field");
	}
	
	public void set(String name, Vec3D v) throws UnknownFieldException {
		if (name.equalsIgnoreCase("dripVecRelativeToUp")) 	this.dripVecRelativeToUp = v;
		else throw new UnknownFieldException("GmlBrush doesn't have a \""+name+"\" field");		
	}

	public void set(String name, TColor c) throws UnknownFieldException {
		if (name.equalsIgnoreCase("color")) 	this.color = c;
		else throw new UnknownFieldException("GmlBrush doesn't have a \""+name+"\" field");	
	}


	public void getInfo() {
		LOGGER.log(Level.FINEST, "layerAbsolute; "+ layerAbsolute);
		LOGGER.log(Level.FINEST, "layerRelative; "+ layerRelative);
		LOGGER.log(Level.FINEST, "color; "+ color.toString());
		LOGGER.log(Level.FINEST, "mode: "+ mode);
		LOGGER.log(Level.FINEST, "uniqueStyleID: "+ uniqueStyleID);
		LOGGER.log(Level.FINEST, "spec: "+ spec);
		LOGGER.log(Level.FINEST, "width: "+width);
		LOGGER.log(Level.FINEST, "speedToWidthRatio: "+speedToWidthRatio);
		LOGGER.log(Level.FINEST, "dripAmnt: "+dripAmnt);
		LOGGER.log(Level.FINEST, "dripSpeed: "+dripSpeed);
		LOGGER.log(Level.FINEST, "dripVecRelativeToUp: "+ dripVecRelativeToUp.toString());
	}
	
}
