package gml4u.model;

import gml4u.utils.MappingUtils;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.PointCloud;
import toxi.geom.Vec3D;

public class Gml {

	private static final Logger LOGGER = Logger.getLogger("gml4u.model.Gml");


	public GmlEnvironment environment;
	public GmlClient client;

	private float storedDuration = 0;

	// Key = layer, ArrayList for strokes
	public HashMap<Integer, ArrayList<GmlStroke>> layers = new HashMap<Integer, ArrayList<GmlStroke>>();

	public void getInfo() {
		client.getInfo();
		LOGGER.log(Level.FINEST, "Nb layers: "+layers.size());
		// TODO
		LOGGER.log(Level.FINEST, "Nb strokes: ");

		for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
			for (GmlStroke stroke : tmpStrokes) {
				stroke.getInfo();
			}
		}
	}

	/**
	 * Checks if the given layer exists and adds it if needed 
	 * @param layer
	 */
	public void addLayer(int layer) {
		if (!layers.containsKey(layer)) {
			ArrayList<GmlStroke> strokeList = new ArrayList<GmlStroke>();
			layers.put(layer, strokeList);
		}
	}

	/**
	 * Adds the provided strokes to the appropriate layers
	 * @param newStrokes
	 */
	public void addStrokes(ArrayList<GmlStroke> newStrokes) {
		for(GmlStroke tmpStroke : newStrokes) {
			int layer = tmpStroke.brush.layerAbsolute;
			addLayer(layer);
			layers.get(layer).add(tmpStroke);
		}
	}

	/**
	 * Gets the centroid of the graffiti (includes all strokes)
	 * @return
	 */
	public Vec3D getCentroid() {
		AABB boundingBox = getBoundingBox();
		return boundingBox.getMin().add(boundingBox.getExtent());
	}

	/**
	 * Gets the bounding box of the graffiti (including all strokes);
	 * @return
	 */
	public AABB getBoundingBox() {
		// TODO handle empty stroke map

		PointCloud pointCloud = new PointCloud();
		ArrayList<Vec3D> list = new ArrayList<Vec3D>();

		for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
			for (GmlStroke stroke : tmpStrokes) {
				AABB bounds = stroke.getBoundingBox();
				list.add(bounds.getMin());
				list.add(bounds.getMax());
			}
		}

		pointCloud.addAll(list);
		return pointCloud.getBoundingBox();
	}


	/**
	 * Calculates the duration
	 * return float
	 */
	private float calculateDuration() {

		float duration = 0;
		for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
			for (GmlStroke stroke : tmpStrokes) {
				float tmpDuration = stroke.duration();
				if (tmpDuration > duration) duration = tmpDuration;
			}
		}
		return duration;
	}


	/**
	 * Gets the duration of the graffiti
	 * @return
	 */
	public float getDuration() {
		// TODO change to storedDuration
		return calculateDuration();
	}


	// TODO
	// public void reorient

	/**
	 * Normalizes the graffiti to fit within 0-1
	 * Fixes some GML records issues
	 */
	public void normalize() {
		// Reference bounding box
		AABB refBox = AABB.fromMinMax(new Vec3D(0, 0, 0), new Vec3D(1, 1, 1));

		// Get BoundingBox
		AABB boundingBox = getBoundingBox();
		Vec3D min = boundingBox.getMin();
		Vec3D max = boundingBox.getMax();
		Vec3D diff = max.sub(min);

		// TODO Does it work if min is not in refBox?

		// If within refBox or bigger than refBox
		float coef;
		if(diff.x > diff.y) coef = diff.x;
		else coef = diff.y;

		for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
			for (GmlStroke stroke : tmpStrokes) {
				for(GmlPoint point: stroke.points) {
					point.x = (point.x-min.x)/coef;
					point.y = (point.y-min.y)/coef;
					point.z = MappingUtils.map(point.z, min.z, max.z, 0, 1);
					
					if (point.z != point.z) { // In case of NaN
						point.z = 0;
					}
				}
			}
		}
	}


	/**
	 * Returns the total number of strokes including all layers
	 * @return int
	 */
	public int nbStrokes() {
		int nbStrokes = 0;

		for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
			nbStrokes += tmpStrokes.size();
		}
		return nbStrokes;
	}


	/**
	 * Returns the total number of strokes in the specified layer
	 * @return int
	 */
	public int nbStrokes(int layer) {
		return layers.get(layer).size();
	}



	/**
	 * Returns the total number of points including all layers
	 * @return int
	 */
	public int nbPoints() {
		int totalNbPoints = 0;

		for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
			for (GmlStroke stroke : tmpStrokes) {
				totalNbPoints += stroke.points.size();
			}
		}
		return totalNbPoints;
	}


	/**
	 * Returns the total number of points in the specified layer
	 * @return int
	 */
	public int totalPoints(int layer) {
		int totalNbPoints = 0;
		for (GmlStroke stroke : layers.get(layer)) {
			totalNbPoints += stroke.points.size();
		}
		return totalNbPoints;
	}


	/**
	 * Makes the Gml fit into a certain duration.<br>
	 * If no time information was found, the <i>force</i> parameter tells
	 * whether the GML points should be left as is or if time should 
	 * be equally split across all layers, strokes and points.
	 * If time information is available then it is rescaled to the new
	 * duration, unless force is true, in which case, the time will be
	 * equally split across all available layers, strokes and points.
	 * @param float
	 */

	public void timeBox(float duration, boolean force) {

		float currentDuration = getDuration();
		// TODO check if no conflict between storedDuration and calculateDuration		

		if (force) {
			int nbPoints = nbPoints();
			float step = duration/nbPoints;
			float currentTime = step;

			// Loop through all layers/strokes/points
			for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
				for (GmlStroke stroke : tmpStrokes) {
					for(GmlPoint point: stroke.points) {
						point.time = currentTime;
						currentTime += step;
					}
				}
			}
		}

		else if (currentDuration > 0) {
			for (ArrayList<GmlStroke> tmpStrokes : layers.values()) {
				for (GmlStroke stroke : tmpStrokes) {
					for(GmlPoint point: stroke.points) {
						point.time = MappingUtils.map(point.time, 0, currentDuration, 0, duration);						
						
						if(point.time != point.time) {
							point.time = 0;
						}
					}
				}
			}
		}
		
		// else leave as is

		storedDuration = currentDuration;

	}


	/**
	 * Gets the nb of layers
	 * @return
	 */
	public int getNbLayers() {
		return layers.size();
	}


	/**
	 * Returns the list of named layers
	 * @return
	 */
	public String getLayers() {

		String layerList = "";
		for (Integer key : layers.keySet()) {
			layerList +=" "+key;
		}
		return layerList;
	}
}
