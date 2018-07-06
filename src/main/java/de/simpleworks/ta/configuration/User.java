package de.simpleworks.ta.configuration;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.common.utils.UtilsFormat;

public class User {
	private final String login;
	private final String password;

	public User(final String _login, final String _password) throws SystemException {
		if (Convert.isEmpty(_login)) {
			throw new SystemException("_login can't be null.");
		}

		if (Convert.isEmpty(_password)) {
			throw new SystemException("_password can't be null.");
		}
		login = _login;
		password = _password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "[" + UtilsFormat.getClassName(this) + ": " + UtilsFormat.format("login", login, ", ")
				+ UtilsFormat.format("password", password) + "]";
	}
}
