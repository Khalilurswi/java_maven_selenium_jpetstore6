package de.simpleworks.ta;

import de.simpleworks.common.exception.SystemException;

/**
 * @author Marcin Brzoza
 */

public interface ITestCase extends AutoCloseable {
	String getId();

	TestCaseStatus getStatus();

	void execute() throws SystemException;

	@Override
	void close() throws SystemException;
}
