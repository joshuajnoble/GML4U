
public class LineDrawer {
	
	HashMap<Integer, LinkedList<GmlPoint>> strokes = new HashMap<Integer, LinkedList<GmlPoint>>();

	GmlEnvironment environment;
	GmlClient client;
	float rotation = 0;
	Vec3D screen;
        float ratio;
	Vec3D graffScale = new Vec3D(1, 1, 1);
	Vec3D centroid = new Vec3D();
	
	public boolean finishedDrawing = false;
	public boolean finished = false;

	
	LineDrawer(int width, int height, int depth, float ratio) {
	    screen = new Vec3D(width, height, depth);
            this.ratio = ratio;
	}

        public void draw(PGraphics p, Vec3D position) {
            drawLines(g, (Vec2D) position.to2DXY());
        }

	private void drawLines(PGraphics g, Vec2D position) {

		g.pushMatrix();
		g.pushStyle();
		g.translate(position.x, position.y);

		if (environment.up.x == 1) {
			g.rotate((float)-Math.PI/2);

		}
		else if (environment.up.y == 1) {
			// Tested OK
		}
		else if (environment.up.z == 1) {
			// TODO z up rotation
		}

		g.translate(-centroid.x, -centroid.y);

		for (LinkedList<GmlPoint> points : strokes.values()) {

			if (points.size() > 0) {
				g.smooth();
				g.stroke(0);
				g.strokeWeight(1);

				// iterate over all points
				GmlPoint prev = new GmlPoint();
				GmlPoint cur = new GmlPoint();
				prev.set(points.getFirst().scale(graffScale));
				for(GmlPoint point: points) {
					cur.set(point.scale(graffScale));
					line(g, prev, cur);
					prev.set(cur);
				}
			}
		}
		g.popStyle();
		g.popMatrix();

	}

	
	private void line(PGraphics g, Vec3D a, Vec3D b) {
		g.line(a.x, a.y, b.x, b.y);
	}


	public void gmlEvent(GmlEvent event) {

		if (event instanceof GmlDrawingStart) {
                        strokes.clear();
			environment = ((GmlDrawingStart) event).environment;

			graffScale = GmlDrawingHelper.getGraffScale(screen, environment, ratio);
			centroid = ((GmlDrawingStart) event).centroid.scale(graffScale);
			finishedDrawing = false;
		}

		else if (event instanceof GmlDrawingEvent) {
			if (((GmlDrawingEvent) event).points.size() > 0) {
				
				if (((GmlDrawingEvent)event).points.size() > 0) {
					this.strokes.put(((GmlDrawingEvent) event).strokeId, ((GmlDrawingEvent) event).points);
				}
			}
		}

		else if (event instanceof GmlDrawingEnd) {
			finishedDrawing = true;
		}
	}
}

