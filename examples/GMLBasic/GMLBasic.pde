import gml4u.utils.*;
import gml4u.events.*;
import gml4u.model.exceptions.*;
import gml4u.model.*;

import toxi.geom.Vec3D;
import toxi.geom.Vec2D;

GmlParser gp;
Gml gml;
GmlDrawingManager gdm;

LineDrawer drawer;

void setup() {
  size(400, 300);
  frameRate(24);
  
  // Init the drawer, with screen size and
  // how much of this space the tag will take
  drawer = new LineDrawer(width, height, 20, .8f);
  
  // Initialize the drawing manager and register the drawer to
  // receive drawing events (start, points list, end)
  gdm = new GmlDrawingManager();
  gdm.register(drawer, 0);

  // Initialize the parser with a sleep time (ms), name and
  // use "this" for callbacks
  gp = new GmlParser(500, "myParser", this);
  // Start the parser (as a thread)
  gp.start();
  // Request the parsing of a gml file
  // nb : could be a url as well
  gp.parse(sketchPath+"/19518.gml.xml");
}

void draw() {
  background(255);

  // Wait until the Gml file is parsed
  if(null == gml) {
    frameCount = 0;
  }
  // When parsed, draw
  else {
    gdm.pulse(frameCount/frameRate);
    drawer.draw(g, new Vec3D(width/2, height/2, 0));
    
    if (drawer.finishedDrawing) {
      frameCount = 0;
      gdm.reset();
    }
  }
}


/*  You must implement this method which
    will be called by the gml parser thread
    once the parsing is finished.
*/
public void gmlEvent(GmlEvent event) {
  // Check that the even is of type GmlParsingEvent and if it contains a gml object
  if (event instanceof GmlParsingEvent && null != ((GmlParsingEvent) event).gml) {
    // Get the parsed gml
    gml = ((GmlParsingEvent) event).gml;
    // Uncomment this line if you want to timebox the drawing
    //gml.timeBox(15.f, true);
    // Call the normalize method to make sure the drawing fits into the 0-1 range
    gml.normalize();
    // Tell the drawing manager to use this gml instead
    gdm.setGml(gml);
  }
}

