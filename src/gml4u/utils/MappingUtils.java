package gml4u.utils;

public class MappingUtils {

	/**
	 * Convenience function used to map a variable from one
	 * coordinate space to another.
	 * 
	 * Equivalent to the Processing PApplet map function
	 * Used to remove dependency to Processing core.jar
	 * 
	 * @param x
	 * @param minS
	 * @param maxS
	 * @param minT
	 * @param maxT
	 * @return
	 */
	public static float map(float x, float minS, float maxS, float minT, float maxT) {
		
		return minT + ((x-minS) / (maxS-minS)) * (maxT - minT);
		
	}
	
}
