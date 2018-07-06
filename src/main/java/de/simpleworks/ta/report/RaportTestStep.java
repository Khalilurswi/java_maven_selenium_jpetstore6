package de.simpleworks.ta.report;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.common.utils.UtilsFormat;
import de.simpleworks.ta.IRaportTestStep;

public class RaportTestStep implements IRaportTestStep {
	private final String id;
	private boolean successful;
	private String comment;

	public RaportTestStep(String _id) throws SystemException {
		if (Convert.isEmpty(_id)) {
			throw new SystemException("_id can't be null or empty string.");
		}

		id = _id;
		successful = false;
		comment = null;
	}

	@Override
	public String getId() {
		return id;
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
	public void setComment(String _value) {
		comment = _value;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public void setScreenShot() {
		// implement it
	}

	@Override
	public String toString() {
		return "[" + UtilsFormat.getClassName(this) + ": " + UtilsFormat.format("id", id, ", ")
				+ UtilsFormat.format("successful", successful, ", ") + UtilsFormat.format("comment", comment) + "]";
	}
}
