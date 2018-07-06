package de.simpleworks.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.simpleworks.common.exception.SystemException;

/**
 * @author Marcin Brzoza
 *
 */

public final class UtilsIO {
	private static final Logger logger = LogManager.getLogger(UtilsIO.class);
	private static final int BUFFER_SIZE = 128 * 1024;

	public static final void close(final Closeable _closeable) {
		try {
			if (_closeable != null) {
				_closeable.close();
			}
		} catch (final Exception ex) {
			UtilsIO.logger.error("can't close.", ex);
		}
	}

	public static final void dumpIntoFile(final String _title, final List<String> _list, final String _fileName) {
		if (UtilsIO.logger.isInfoEnabled() && (_title != null)) {
			UtilsIO.logger.info(_title + ": " + _list.size() + ".");
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(_fileName, false), UtilsIO.BUFFER_SIZE);
			for (final String line : _list) {
				writer.write(line);
				writer.newLine();
			}
		} catch (final IOException ex) {
			UtilsIO.logger.error("can't dump into file: \"" + _fileName + "\".", ex);
		} finally {
			UtilsIO.close(writer);
		}
	}

	public static final void dumpIntoFile(final String _title, final Set<String> _set, final String _fileName) {
		final List<String> list = new ArrayList<>();
		list.addAll(_set);
		Collections.sort(list);
		UtilsIO.dumpIntoFile(_title, list, _fileName);
	}

	public static final Properties loadProperties(final String _resource) throws SystemException {
		if (_resource == null) {
			throw new SystemException("_resource can't be null.");
		}

		final InputStream input = UtilsIO.class.getClassLoader().getResourceAsStream(_resource);
		if (input == null) {
			throw new SystemException("can't get InputStream for: \"" + _resource + "\".");
		}

		final Properties result = new Properties();
		try (InputStream stream = new BufferedInputStream(input, UtilsIO.BUFFER_SIZE);) {
			result.load(stream);
		} catch (final IOException ex) {
			final String message = "can't read or close InputStream for \"" + _resource + "\".";
			UtilsIO.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	public static InputStream createInputStream(final String _ressource) throws SystemException {
		final InputStream input = UtilsIO.class.getClassLoader().getResourceAsStream(_ressource);
		if (input == null) {
			throw new SystemException("can't get InputStream for : \"" + _ressource + "\".");
		}

		return new BufferedInputStream(input, UtilsIO.BUFFER_SIZE);
	}
}
