package de.simpleworks.common.utils;

import java.util.Date;

/**
 * @author Marcin Brzoza
 * @since 19.11.2014
 */

public final class UtilsFormat {
	public static final String getClassName(final Object _object) {
		return _object.getClass().getSimpleName();
	}

	private static String format(final String _variable, final Object _value, final String _suffix,
			final boolean _isString) {
		final String value;
		if (_value == null) {
			value = "null";
		} else {
			value = _isString ? "\"" + _value + "\"" : _value.toString();
		}

		return _variable + ": " + value + (_suffix == null ? "" : _suffix);
	}

	public static final String format(final String _variable, final Object _value, final String _suffix) {
		return UtilsFormat.format(_variable, _value, _suffix, false);
	}

	public static final String format(final String _variable, final Date _value) {
		return UtilsFormat.format(_variable, _value, null);
	}

	public static final String format(final String _variable, final boolean _value, final String _suffix) {
		return UtilsFormat.format(_variable, Boolean.toString(_value), _suffix, false);
	}

	public static final String format(final String _variable, final String _value, final String _suffix) {
		return UtilsFormat.format(_variable, _value, _suffix, true);
	}

	public static final String format(final String _variable, final Object _value) {
		return UtilsFormat.format(_variable, _value, null, false);
	}

	public static final String format(final String _variable, final String _value) {
		return UtilsFormat.format(_variable, _value, null, true);
	}

	public static final String format(final String _variable, final int _value, final String _suffix) {
		return UtilsFormat.format(_variable, Integer.valueOf(_value), _suffix, false);
	}

	public static final String format(final String _variable, final int _value) {
		return UtilsFormat.format(_variable, Integer.valueOf(_value), null, false);
	}

	public static final String format(final String _variable, final boolean _value) {
		return UtilsFormat.format(_variable, Boolean.valueOf(_value), null, false);
	}
}
