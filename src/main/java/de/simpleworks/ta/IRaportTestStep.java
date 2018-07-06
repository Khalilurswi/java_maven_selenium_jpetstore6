package de.simpleworks.ta;

public interface IRaportTestStep {
	String getId();

	void done(boolean _success);

	boolean isSuccessful();

	void setComment(String _comment);

	String getComment();

	void setScreenShot();
}
