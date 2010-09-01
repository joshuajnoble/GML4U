package gml4u.utils;

import java.io.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.*;
import org.jdom.xpath.XPath;
import org.jdom.input.*;
import java.util.List;

public class JDomParsingUtils {

	private static final Logger LOGGER = Logger.getLogger("gml4u.utils.JDomParsingUtils");

	/**
	 * Builds a document based on the provided path/filename
	 * @param file
	 * @return
	 */


	public static Document buildDocument(String file) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
			Document document = saxBuilder.build(file);
			return document;
		}
		catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		} catch (JDOMException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	/**
	 * Returns a single element of node based on a document and XPath expression
	 * @param document
	 * @param expression
	 * @return
	 */


	public static Object selectSingleNode(Document document, String expression) {
		try {
			Object result = XPath.selectSingleNode(document, expression);
			return result;
		}
		catch (JDOMException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return null;
	}


	/**
	 * Returns a list of element or node base on a document and XPath expression
	 * @param document
	 * @param expression
	 * @return
	 */

	public static List<?> selectNodes(Document document, String expression) {
		try {
			List<?> result = XPath.selectNodes(document, expression);
			return result;
		}
		catch (JDOMException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return null;
	}


}
