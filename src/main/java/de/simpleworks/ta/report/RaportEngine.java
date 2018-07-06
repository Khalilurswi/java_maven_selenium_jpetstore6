package de.simpleworks.ta.report;

import java.util.ArrayList;
import java.util.List;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.ta.IRaportEngine;
import de.simpleworks.ta.IRaportTestCase;

public class RaportEngine implements IRaportEngine {
	private final List<IRaportTestCase> testCases;
	private String testRun;

	public RaportEngine() {
		testCases = new ArrayList<>();
		testRun = null;
	}

	@Override
	public void setTestRun(String _value) throws SystemException {
		if (Convert.isEmpty(_value)) {
			throw new SystemException("_value can't be null or empty string.");
		}

		testRun = _value;
	}

	@Override
	public String getTestRun() {
		return testRun;
	}

	@Override
	public IRaportTestCase createTestCase(String _id) throws SystemException {
		IRaportTestCase result = new RaportTestCase(_id);

		testCases.add(result);

		return result;
	}

	@Override
	public void save() {
		// implement it

	}

}
