package gml4u.utils;

import gml4u.model.GmlEnvironment;

import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.Vec3D;

public class GmlDrawingHelper {

	private static final Logger LOGGER = Logger.getLogger("gml4u.utils.GmlDrawingHelper");

	
	/**
	 * Returns the best graff scale, based on screen dimensions and ratio 
	 * @param screen
	 * @param environment
	 * @param screenGraffRatio
	 * @return
	 */
	public static Vec3D getGraffScale(Vec3D screen, GmlEnvironment environment, float screenGraffRatio) {

		// TODO refactor and optimize
		
		Vec3D graffScale = new Vec3D(1, 1, 1);

		Vec3D up = environment.up;		
		Vec3D screenBounds = environment.screenBounds;
		
		float screenRatio = screen.x/screen.y;
		
		// Remap the graff to the screen
		if (Math.abs(up.x) == 1) {
			// Get the graff ratio based on its original screen size
			float graffRatio = screenBounds.y/screenBounds.x;

			// Same orientation
			if((graffRatio >= 1 && screenRatio >= 1) || (graffRatio < 1 && screenRatio < 1)) {
				if (graffRatio > screenRatio) {
					graffScale.y = screen.x*screenGraffRatio;
					graffScale.x = graffScale.y/graffRatio;
					graffScale.z = screen.z;
				}
				else {
					graffScale.x = screen.y*screenGraffRatio;
					graffScale.y = graffRatio*graffScale.x;
					graffScale.z = screen.z;				
				}
			}
			// Landscape graff vs Portrait screen
			else if (graffRatio >= 1 && screenRatio < 1) {
				graffScale.y = screen.x*screenGraffRatio;
				graffScale.x = graffScale.y/graffRatio;
				graffScale.z = screen.z;
			}
			// Portrait graff vs Lanscape screen
			else if (graffRatio < 1 && screenRatio >= 1) {
				graffScale.x = screen.y*screenGraffRatio;
				graffScale.y = graffRatio*graffScale.x;
				graffScale.z = screen.z;												
			}
		}
		else if (Math.abs(up.y) == 1) {

			// Get the graff ratio based on its original screen size
			float graffRatio = screenBounds.x/screenBounds.y;

			// Same orientation
			if((graffRatio >= 1 && screenRatio >= 1) || (graffRatio < 1 && screenRatio < 1)) {
				if (graffRatio > screenRatio) {
					graffScale.x = screen.x*screenGraffRatio;
					graffScale.y = graffScale.x/graffRatio;
					graffScale.z = screen.z;
				}
				else {
					graffScale.y = screen.y*screenGraffRatio;
					graffScale.x = graffScale.y*graffRatio; // TODO check
					graffScale.z = screen.z;				
				}
			}
			// Landscape graff vs Portrait screen
			else if (graffRatio >= 1 && screenRatio < 1) {
				graffScale.x = screen.x*screenGraffRatio;
				graffScale.y = graffScale.x/graffRatio;
				graffScale.z = screen.z;
			}
			// Portrait graff vs Lanscape screen
			else if (graffRatio < 1 && screenRatio >= 1) {
				graffScale.y = screen.y*screenGraffRatio;
				graffScale.x = graffRatio/graffScale.y;
				graffScale.z = screen.z;												
			}
		}
		else  if (Math.abs(up.z) == 1) {
			// TODO
		}

		return graffScale;

	}

}
