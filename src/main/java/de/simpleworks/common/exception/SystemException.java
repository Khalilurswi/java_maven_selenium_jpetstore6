package de.simpleworks.common.exception;

/**
 * @author Marcin Brzoza
 */

public class SystemException extends Exception {
	private static final long serialVersionUID = -5650100048267181768L;

	public SystemException(final String _message) {
		super(_message);
	}
}
