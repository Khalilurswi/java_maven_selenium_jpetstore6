package de.simpleworks.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Marcin Brzoza
 *
 */

public class Convert {
	public static final String EMPTY_STRING = "";
	public static final String FORMAT = "dd.MM.yyyy HH:mm:ss SSS";
	private static final SimpleDateFormat formatter = new SimpleDateFormat(Convert.FORMAT);

	public static final boolean isEmpty(final String _text) {
		return (_text == null) || (Convert.EMPTY_STRING.equals(_text.trim()));
	}

	public static final Date getDate(final String _text) {
		try {
			return Convert.formatter.parse(_text);
		} catch (@SuppressWarnings("unused") final ParseException ex) {
			return null;
		}
	}

	public static final String format(final Date _date) {
		return Convert.formatter.format(_date);
	}

	public static final String getString(final String _text) {
		return _text == null ? "" : _text;
	}

	public static final int getInt(final String _text, final int _default) {
		try {
			return Integer.parseInt(_text);
		} catch (@SuppressWarnings("unused") final Exception ex) {
			return _default;
		}
	}

	public static final long getLong(final String _text, final long _default) {
		try {
			return Long.parseLong(_text);
		} catch (@SuppressWarnings("unused") final Exception ex) {
			return _default;
		}
	}

	public static final boolean getBoolean(final String _text, final boolean _default) {
		boolean result = _default;

		if (!Convert.isEmpty(_text)) {
			try {
				result = Boolean.parseBoolean(_text);
			} catch (@SuppressWarnings("unused") final Exception ex) {
				return _default;
			}
		}

		return result;
	}
}
