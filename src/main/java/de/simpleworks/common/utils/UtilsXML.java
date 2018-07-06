package de.simpleworks.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.simpleworks.common.exception.SystemException;

/**
 * @author Marcin Brzoza
 *
 */

public final class UtilsXML {
	private static final Logger logger = LogManager.getLogger(UtilsXML.class);
	private static final String PATH_SEPARATOR = "/";
	private static final SAXBuilder builder = UtilsXML.createSaxBuilder();

	private static SAXBuilder createSaxBuilder() {
		final SAXBuilder result = new SAXBuilder();

		result.setFeature("http://xml.org/sax/features/validation", false);
		result.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		result.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		return result;
	}

	public static final Document read(final InputStream _input) throws SystemException {
		Document result = null;

		try {
			result = UtilsXML.builder.build(_input);
		} catch (final Exception ex) {
			final String message = "can't read xml from stream.";
			UtilsXML.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	public static final void write(final Document _document, final OutputStream _output) throws SystemException {
		final XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(_document, _output);
		} catch (final IOException ex) {
			final String message = "can't write xml to stream.";
			UtilsXML.logger.error(message, ex);
			throw new SystemException(message);
		}
	}

	public static String getPath(final Element _node) {
		final Element parent = _node.getParentElement();
		final String name = _node.getAttributeValue("name");
		return (parent == null ? UtilsXML.PATH_SEPARATOR : UtilsXML.getPath(parent)) + UtilsXML.PATH_SEPARATOR
				+ _node.getName() + (name != null ? String.format("[name: \"%s\"]", name) : "");
	}

	public static String getPath(final Element _node, final String _xmlTag) {
		return UtilsXML.getPath(_node) + UtilsXML.PATH_SEPARATOR + _xmlTag;
	}

	public static String getAttribute(final Element _node, final String _name, final boolean _mandatory)
			throws SystemException {
		final String result = _node.getAttributeValue(_name);

		if (_mandatory && Convert.isEmpty(result)) {
			final StringBuilder attributes = new StringBuilder();
			for (final Attribute attribute : _node.getAttributes()) {
				attributes.append(";").append(attribute.getName()).append("=").append(attribute.getValue());
			}

			throw new SystemException("attribute: \"" + _name + "\" of node: \"" + UtilsXML.getPath(_node) + "["
					+ ((0 < attributes.length()) ? attributes.toString().substring(1) : "") + "]\" is empty.");
		}

		return result;
	}

	public static String getAttribute(final Element _node, final String _name) throws SystemException {
		return UtilsXML.getAttribute(_node, _name, true);
	}

	public static Element getChild(final Element _root, final String _xmlTag) throws SystemException {
		final Element result = _root.getChild(_xmlTag);

		if (result == null) {
			throw new SystemException("can't get element for: \"" + UtilsXML.getPath(_root, _xmlTag) + "\".");
		}

		return result;
	}

	public static Element getRoot(final Document _document, final String _rootName) throws SystemException {
		if (_document == null) {
			throw new SystemException("_document can't be null.");
		}

		if (Convert.isEmpty(_rootName)) {
			throw new SystemException("_rootName can't be null or empty string.");
		}

		final Element result = _document.getRootElement();

		if (!_rootName.equals(result.getName())) {
			throw new SystemException(String.format("unexpected name of root element: \"%s\", expected is: \"%s\".",
					result.getName(), _rootName));
		}

		return result;
	}
}
