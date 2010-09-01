package gml4u.model;

import java.util.ArrayList;
import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.PointCloud;
import toxi.geom.Vec3D;

public class GmlStroke {
	
	private static final Logger LOGGER = Logger.getLogger("gml4u.model.GmlStroke");

	public boolean isDrawing;
	public GmlInfo info;
	public GmlBrush brush;
	public LinkedList<GmlPoint> points;
	
	public GmlStroke() {
		isDrawing = true;
		info = new GmlInfo();
		brush = new GmlBrush();
		points = new LinkedList<GmlPoint>();
	}
	
	public void getInfo() {
		LOGGER.log(Level.FINEST, "is drawing: "+isDrawing);
		info.getInfo();
		brush.getInfo();
		LOGGER.log(Level.FINEST, "nb points: "+points.size());
		for (GmlPoint point: points) {
			point.getInfo();
		}
	}
	
	
	public AABB getBoundingBox() {
		PointCloud pointCloud = new PointCloud();
		ArrayList<Vec3D> list = new ArrayList<Vec3D>();
		list.addAll(points);
		pointCloud.addAll(list);
		return pointCloud.getBoundingBox();
	}
		
	
	public float duration() {
		if (points.size() > 0) {
			return points.getLast().time;
		}
		else {
			return 0;
		}
	}

}
