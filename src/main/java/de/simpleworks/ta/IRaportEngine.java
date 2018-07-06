package de.simpleworks.ta;

import de.simpleworks.common.exception.SystemException;

public interface IRaportEngine {
	void setTestRun(String _value) throws SystemException;

	String getTestRun();

	IRaportTestCase createTestCase(String _id) throws SystemException;

	void save();
}
