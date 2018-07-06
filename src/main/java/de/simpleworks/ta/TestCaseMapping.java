package de.simpleworks.ta;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.common.utils.UtilsIO;
import de.simpleworks.common.utils.UtilsXML;

public class TestCaseMapping {
	private static final Logger logger = LogManager.getLogger(TestCaseMapping.class);
	private static final String XML_MAPPING = "mapping";
	private static final String XML_TESTCASE = "testcase";
	private static final String XML_CLASSNAME = "classname";
	private static final String XML_NAME = "name";

	public final Map<String, String> map;

	public TestCaseMapping(final String _ressource) throws SystemException {
		if (Convert.isEmpty(_ressource)) {
			throw new SystemException("_ressource can't be null or empty string.");
		}

		map = new HashMap<>();

		read(_ressource);
	}

	private void read(final String _ressource) throws SystemException {
		Document document;
		if (TestCaseMapping.logger.isInfoEnabled()) {
			TestCaseMapping.logger.info(String.format("reading mapping from \"%s\": start..", _ressource));
		}

		try (InputStream input = UtilsIO.createInputStream(_ressource)) {
			document = UtilsXML.read(input);
		} catch (final IOException ex) {
			final String message = "can't read or close InputStream for \"" + _ressource + "\".";
			TestCaseMapping.logger.error(message, ex);
			throw new SystemException(message);
		}

		final Element root = UtilsXML.getRoot(document, TestCaseMapping.XML_MAPPING);
		for (final Element node : root.getChildren(TestCaseMapping.XML_TESTCASE)) {
			final String className = UtilsXML.getAttribute(node, TestCaseMapping.XML_CLASSNAME);
			final String name = UtilsXML.getAttribute(node, TestCaseMapping.XML_NAME);

			map.put(className, name);
		}

		if (TestCaseMapping.logger.isInfoEnabled()) {
			TestCaseMapping.logger.info(String.format("reading mapping from \"%s\": DONE.", _ressource));
		}
	}

	public String get(final String _className) throws SystemException {
		final String result = map.get(_className);

		if (Convert.isEmpty(result)) {
			throw new SystemException(String.format("can't find test case for class name \"%s\".", _className));
		}

		return result;
	}
}
