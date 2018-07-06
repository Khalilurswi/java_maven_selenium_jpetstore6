package de.simpleworks.ta.report;

import java.util.ArrayList;
import java.util.List;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.common.utils.UtilsFormat;
import de.simpleworks.ta.IRaportTestCase;
import de.simpleworks.ta.IRaportTestStep;

public class RaportTestCase implements IRaportTestCase {
	private final String id;
	private final List<IRaportTestStep> testSteps;

	private boolean successful;

	public RaportTestCase(String _id) throws SystemException {
		if (Convert.isEmpty(_id)) {
			throw new SystemException("_id can't be null or empty string.");
		}

		id = _id;
		testSteps = new ArrayList<>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public IRaportTestStep createTestStep(String _name) throws SystemException {
		IRaportTestStep result = new RaportTestStep(_name);

		testSteps.add(result);

		return result;
	}

	@Override
	public void done(boolean _success) {
		successful = _success;
	}

	@Override
	public boolean isSuccessful() {
		return successful;
	}

	@Override
	public String toString() {
		return "[" + UtilsFormat.getClassName(this) + ": " + UtilsFormat.format("id", id, ", ")
				+ UtilsFormat.format("successful", successful) + "]";
	}
}
