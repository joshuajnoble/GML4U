package gml4u.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CallbackUtils {

	private static final Logger LOGGER = Logger.getLogger("gml4u.utils.CallbackUtils");

	/**
	 * Checks if the provided Object implements the given method and argument
	 * @param object
	 * @param methodName
	 * @param classe
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasRequiredCallback(Object object, String methodName, Class classe) {
		
		try {
			object.getClass().getMethod(methodName, classe);
		} catch (SecurityException e) {
			LOGGER.log(Level.WARNING, "Cannot access methods of "+object.getClass().getName());
			return false;
		} catch (NoSuchMethodException e) {
			LOGGER.log(Level.WARNING, object.getClass().getName()+" must implement this method: public void "+methodName+"("+classe.getCanonicalName()+")");
			return false;
		}
		return true;
	}
}
