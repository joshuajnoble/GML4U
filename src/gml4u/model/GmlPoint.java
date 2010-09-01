package gml4u.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import toxi.geom.Vec3D;

public class GmlPoint extends Vec3D {

	private static final Logger LOGGER = Logger.getLogger("gml4u.model.GmlPoint");
	
	public float time;
	
	public GmlPoint() {
		// Defaults to 0
		time = 0;
	}
	
	public void set(GmlPoint point) {
		super.set(point);
		this.time = point.time;
	}
	
	public void getInfo() {
		LOGGER.log(Level.FINEST, super.toString()+ "time: "+time);
	}
	
	public String toXML() {
		String xml = "";
		
		xml+="<pt>\n";
		xml+="<x>"+x+"</x>\n";
		xml+="<y>"+y+"</y>\n";
		xml+="<z>"+z+"</z>\n";
		xml+="<time>"+time+"</time>\n";
		xml+="</pt>";
		
		return xml;
	}
}
