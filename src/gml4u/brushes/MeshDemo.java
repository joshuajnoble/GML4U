package gml4u.brushes;

import gml4u.drawing.GmlStrokeDrawer;
import gml4u.model.GmlPoint;
import gml4u.model.GmlStroke;

import java.util.Iterator;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.geom.mesh.TriangleMesh;

public class MeshDemo extends GmlStrokeDrawer {

	public void draw(PGraphics g, GmlStroke stroke, float scale, float minTime, float maxTime) {

		TriangleMesh mesh = buildMesh(stroke, maxTime);
		mesh.scale(scale);

		g.pushMatrix();
		g.pushStyle();

		// Style
		g.noStroke();
		g.fill(255);

		g.beginShape(PConstants.TRIANGLES);
		// iterate over all faces/triangles of the mesh
		for(Iterator<TriangleMesh.Face> i = mesh.faces.iterator(); i.hasNext();) {
			TriangleMesh.Face f=(TriangleMesh.Face)i.next();

			g.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
			g.vertex(f.a.x, f.a.y, f.a.z);
			g.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
			g.vertex(f.b.x, f.b.y, f.b.z);
			g.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
			g.vertex(f.c.x, f.c.y, f.c.z);
		}
		g.endShape();

		g.popStyle();
		g.popMatrix();

	}

	// TODO move that to an Helper
	private static TriangleMesh buildMesh(GmlStroke stroke, float time) {
		TriangleMesh mesh = new TriangleMesh("");

		if (stroke.getPoints().size() > 0) {	

			GmlPoint prev = new GmlPoint();
			GmlPoint pos = new GmlPoint();
			Vec3D a = new Vec3D();
			Vec3D b = new Vec3D();
			Vec3D p = new Vec3D();
			Vec3D q = new Vec3D();
			float weight = 0;

			prev.set(stroke.getPoints().get(0));

			for (GmlPoint point: stroke.getPoints()) {
				if (point.time > time) break;
				pos.set(point);

				// use distance to previous point as target stroke weight
				weight += (pos.distanceTo(prev)*4-weight)*0.1;

				// define offset points for the triangle strip
				a.set(pos);
				b.set(pos);
				a.addSelf(0, 0, weight);
				b.addSelf(0, 0, -weight);

				// add 2 faces to the mesh
				mesh.addFace(p,b,q);
				mesh.addFace(p, a, b);

				// store current points for next iteration
				prev.set(pos);
				p.set(a);
				q.set(b);
			}	
		}
		return mesh;
	}
}