package gml4u.utils;

import gml4u.model.Gml;
import gml4u.model.GmlBrush;
import gml4u.model.GmlClient;
import gml4u.model.GmlEnvironment;
import gml4u.model.GmlInfo;
import gml4u.model.GmlPoint;
import gml4u.model.GmlStroke;
import gml4u.model.exceptions.UnknownFieldException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.*;

import toxi.color.NamedColor;
import toxi.color.TColor;
import toxi.geom.Vec3D;

public class GmlParsingHelper {

	private static final Logger LOGGER = Logger.getLogger("gml4u.utils.GmlParsingHelper");


	/*
	 * GML parser version 0.1b
	 * GML instead of gml
	 * Evironment in /GML/tag/environment
	 * 
	 * GML parser version 0.1c
	 */
	
	// TODO pass a default GmlBrush from the starting getGml method ?
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static Gml getGml(String file) {

		Gml gml = null;

		// TODO xsd validation
		
		// Get document
		Document document = JDomParsingUtils.buildDocument(file);
		String root = "/*[name()='GML' or name()='gml']";
		
		// Get version
		Element rootNode = (Element) JDomParsingUtils.selectSingleNode(document, root);
		String version = rootNode.getAttributeValue("spec");
		
		// TODO parser factory
		
		String expression = root+"/tag/header/client/child::*";
		List elements = JDomParsingUtils.selectNodes(document, expression);

		gml = new Gml();
		
		// Get Client
		GmlClient client = getGmlClient(elements);
		gml.client = client;

		// Get Environment
		if (null == version || version.equalsIgnoreCase("0.1a") || version.equalsIgnoreCase("0.1b")) {
			expression = root+"/tag/environment/child::*";
		}
		else {
			expression = root+"/tag/header/environment/child::*";
		}
		
		elements = JDomParsingUtils.selectNodes(document, expression);
		GmlEnvironment environment = getGmlEnvironment(elements);
		gml.environment = environment;
		
		// Get Drawing
		expression = root+"/tag/drawing/child::*";
		elements = JDomParsingUtils.selectNodes(document, expression);
		
		ArrayList<GmlStroke> gmlStrokes = new ArrayList<GmlStroke>();
		gmlStrokes.addAll(getGmlDrawing(elements));
		gml.addStrokes(gmlStrokes);

		return gml;
	}

	/**
	 * Returns a GmlClient based on the provided nodes (subnodes of /gml/tag/header/client/)
	 * @param elements
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static GmlClient getGmlClient(List<Element> elements) {

		GmlClient client = new GmlClient();
		for (Element e: elements) {
			String name = e.getName();
			String value = e.getValue();
			
			if (e.getChildren().size() > 0) {
				List<Element> subElements = e.getChildren();
				for (Element sube : subElements) {
					String subname = sube.getName();
					String subvalue = sube.getValue();
					
					try {
						client.set(subname, subvalue);
					}
					catch (NumberFormatException nfe) {
						LOGGER.log(Level.WARNING, nfe.getMessage());
					}
					catch (UnknownFieldException ufe) {
						LOGGER.log(Level.WARNING, ufe.getMessage());
					}
					
					client.map.put(name+"/"+subname, subvalue);
					LOGGER.log(Level.FINEST, name+"/"+subname +"="+ subvalue);
				}
			}
			else {
				try {
					client.set(name, value);
				}
				catch (NumberFormatException nfe) {
					LOGGER.log(Level.WARNING, nfe.getMessage());
				}
				catch (UnknownFieldException ufe) {
					LOGGER.log(Level.WARNING, ufe.getMessage());
				}

				LOGGER.log(Level.FINEST, name+"="+ value);
			}
		}
		return client;
	}


	/**
	 * Returns a GmlEnvironment based on the provided nodes (subnodes of /gml/tag/header/environment)
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GmlEnvironment getGmlEnvironment(List<Element> elements) {

		GmlEnvironment environment = new GmlEnvironment();

		// loop through environment nodes
		for (Element e: elements) {
			String name = e.getName();

			// loop through sub nodes to create vectors
			if (e.getChildren().size() > 0) {

				Vec3D v = getGmlVec3D(e.getChildren());

				try {
					environment.set(name, v);
				}
				catch (UnknownFieldException ufe) {
					LOGGER.log(Level.WARNING, ufe.getMessage());
				}

				LOGGER.log(Level.FINEST, name+"="+ v.toString());				

				// Specific case for realScale "unit" subnode
				if (e.getName().equalsIgnoreCase("realScale")) {
					String realScaleUnit;
					try {
						realScaleUnit = e.getChild("unit").getValue();
					}
					catch (Exception ex) {
						realScaleUnit = "";
					}

					try {
						environment.set("realScaleUnit", realScaleUnit);
					}
					catch (UnknownFieldException ufe) {
						LOGGER.log(Level.WARNING, ufe.getMessage());
					}

					LOGGER.log(Level.FINEST, "realScaleUnit"+"="+ realScaleUnit);				
				}
			}
		}
		
		// Force a up Vector if not defined (ie: x,y,z = 0,0,0)
		// TODO check consistency (at least one 1 and 0 for others)
		if (environment.up.x == 0 && environment.up.y == 0 && environment.up.z == 0) {
			environment.up.x = 1;
		}
		
		return environment;
	}
	
	
	/**
	 * Returns a list of GmlStrokes given the provided stroke nodes
	 * @param elements
	 * @return
	 */
	public static ArrayList<GmlStroke> getGmlDrawing(List<Element> elements) {
		
		ArrayList<GmlStroke> list = new ArrayList<GmlStroke>();
		
		for (Element e: elements) {
			// Get the previous brush if exists
			GmlBrush previousBrush;
			if (list.size() > 0 && null != list.get(list.size()-1).brush) {
				previousBrush = list.get(list.size()-1).brush;
			}
			// If not, create one with default values
			else {
				previousBrush = new GmlBrush();
				previousBrush.setDefault();
			}
			
			// Get the GmlStroke
			GmlStroke stroke = getGmlStroke(e, previousBrush);
			list.add(stroke);
		}
		return list;
	}

	/**
	 * 
	 * @param nodes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GmlStroke getGmlStroke(Element element, GmlBrush previousBrush) {
 		
		GmlStroke gmlStroke = new GmlStroke();
		
		// Get isDrawing info
		try {
			String isDrawing =  element.getAttributeValue("isDrawing");
			if (isDrawing.equalsIgnoreCase("false")) {
				gmlStroke.isDrawing = false;
			}
		}
		catch (Exception ex) {
		}
		
		// Get info
		GmlInfo gmlInfo = null;
		Element info = element.getChild("info");
		
		if (null != info) {
			gmlInfo = getGmlInfo(info.getChildren());
		}
		gmlStroke.info = gmlInfo;
		
		// Get Brush
		GmlBrush gmlBrush = null;
		Element brush = element.getChild("brush");

		if (null != brush) {
			gmlBrush = getGmlBrush(brush.getChildren());
			// Merge with previousBrush
			gmlBrush.merge(previousBrush);
		}
		else {
			gmlBrush = previousBrush;
		}
		gmlStroke.brush = gmlBrush;
		
		// Get points
		List<Element> points = element.getChildren("pt");
		LinkedList<GmlPoint> gmlPoints = getGmlPoints(points);
		
		gmlStroke.points = gmlPoints;
		return gmlStroke;
	}

	
	public static GmlInfo getGmlInfo(List<Element> elements) {
		GmlInfo gmlInfo = new GmlInfo();
		
		for(Element e: elements) {
			gmlInfo.map.put(e.getName(), e.getValue());
		}
		return gmlInfo;
	}
	
	
	/**
	 * 
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GmlBrush getGmlBrush(List<Element> elements) {
			    
		GmlBrush brush = new GmlBrush();

		// loop through brush subnodes
		for (Element e: elements) {
			String name = e.getName();
			String value = e.getValue();
			
			// TODO check each name
			
			if (e.getChildren().size() == 0) {
				LOGGER.log(Level.FINEST, name+": "+value);
				try {
					brush.set(name, value);
				}
				catch (UnknownFieldException ufe) {
					LOGGER.log(Level.WARNING, ufe.getMessage());
				}
				catch (NumberFormatException nfe) {
					LOGGER.log(Level.WARNING, nfe.getMessage());
				}
				
			}
			else {
				List<Element> subNodes = e.getChildren();
				if (name.equalsIgnoreCase("color")) {
					TColor color = getGmlColor(subNodes);
					LOGGER.log(Level.FINEST, "color: "+color);
					try {
						brush.set(name, color);
					}
					catch (UnknownFieldException ufe) {
						LOGGER.log(Level.WARNING, ufe.getMessage());
					}
					
				}
				else if (name.equalsIgnoreCase("dripVecRelativeToUp")) {
					Vec3D v = getGmlVec3D(subNodes);
					try {
						brush.set(name, v);
					}
					catch (UnknownFieldException ufe) {
						LOGGER.log(Level.WARNING, ufe.getMessage());
					}
				}
			}
		}

		return brush;
	}

	/**
	 * 
	 * @param nodes
	 * @return
	 */
	public static LinkedList<GmlPoint> getGmlPoints(List<Element> elements) {

		LinkedList<GmlPoint> pointsList = new LinkedList<GmlPoint>();
		for (Element point: elements) {
			GmlPoint gmlPoint = new GmlPoint();
			gmlPoint = getGmlPoint(point);
			pointsList.add(gmlPoint);
		}

		return pointsList;
	}

	/**
	 * Returns a GmlPoint from the given pt element
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GmlPoint getGmlPoint(Element element) {
		GmlPoint point = new GmlPoint();

		Vec3D v = getGmlVec3D(element.getChildren());
		point.set(v);

		String timeVal = "0";
		// As gml v0.1b and v0.1c "time" element name differs ...
		try {
			Element time = element.getChild("t");
			if (null == time) {
				time = element.getChild("time");
			}
			timeVal = time.getValue();
		}
		catch (Exception ex) {
		}
		float time = Float.parseFloat(timeVal);
		point.time = time;
		
		return point;
	}


	/**
	 * Gets the xyz values from the provided elements and returns the corresponding Vec3D
	 * @param elements
	 * @return
	 */
	public static Vec3D getGmlVec3D(List<Element> elements) {
		Vec3D v = new Vec3D();
		for (Element e : elements) {
			String name = e.getName();
			String value = e.getValue();

			if (name == "x") {
				v.x = Float.parseFloat(value);
			}
			else if (name == "y") {
				v.y = Float.parseFloat(value);
			}
			else if (name == "z") {
				v.z = Float.parseFloat(value);
			}
		}
		return v;
	}
	
	
	/**
	 * Gets the color values from the provided rgb(a) elements and returns the corresponding TColor
	 * @param elements
	 * @return
	 */
	public static TColor getGmlColor(List<Element> elements) {
		TColor color = new TColor(NamedColor.WHITE);
		for (Element e : elements) {
			String name = e.getName();
			String value = e.getValue();

			if (name == "r") {
				color.setRed(Float.parseFloat(value)/255);
			}
			else if (name == "g") {
				color.setGreen(Float.parseFloat(value)/255);
			}
			else if (name == "b") {
				color.setBlue(Float.parseFloat(value)/255);
			}
			else if (name == "a") {
				color.setAlpha(Float.parseFloat(value)/255);
			}
		}
		return color;
	}
}
