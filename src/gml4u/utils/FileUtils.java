package gml4u.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

public class FileUtils {

	private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

	/**
	 * Lists and filters files inside the given folder using a regular expression
	 * If the regex is badly formatted, empty or null, then the result will not be filtered
	 * @param folder
	 * @param regex
	 * @return List<String>
	 */
	public static List<String> scanFolder(String folder, String regex) {
		if (null == regex || regex.length() == 0) {
			LOGGER.warn("Regex is empty of null, not filter will be used");
			regex = ".*";
		}
		else {
			try {
	            Pattern.compile(regex);
	        } catch (PatternSyntaxException exception) {
				LOGGER.warn("Regex is invalid, not filter will be used "+exception.getMessage());
				regex = ".*";	        	
	        }
		}
		File dir = new File(folder);
		String[] fileList = dir.list();
		List<String> filteredList = new ArrayList<String>(); 
		if (null != fileList) {
			for (int i=0; i<fileList.length; i++) {
				if (fileList[i].matches(regex)) {
					filteredList.add(folder+"/"+fileList[i]);
				}
			}
		}
		return filteredList;
	}
}
