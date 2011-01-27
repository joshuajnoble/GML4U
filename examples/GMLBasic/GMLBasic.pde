import gml4u.brushes.*;
import gml4u.drawing.*;
import gml4u.utils.*;
import gml4u.model.*;

Gml gml;
GmlBrushManager brushes = new GmlBrushManager();

void setup() {
  size(600, 400, P3D);
  gml = GmlParsingHelper.getGml(sketchPath+"/sample.gml.xml", false);
}

void draw() {
    brushes.draw(g, gml, 600);
}
