package de.simpleworks.ta;

import de.simpleworks.common.exception.SystemException;

public interface IRaportTestCase {
	String getId();

	IRaportTestStep createTestStep(final String _id) throws SystemException;

	void done(boolean _success);

	boolean isSuccessful();
}
