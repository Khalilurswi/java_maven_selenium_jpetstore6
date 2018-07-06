package de.simpleworks.ta.webminar.testcase;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.ta.TestCaseBase;
import de.simpleworks.ta.configuration.IConfiguration;

public abstract class TestCase extends TestCaseBase {
	public TestCase(final IConfiguration _configuration, final String _id) throws SystemException {
		super(_configuration, _id);
	}

	private void startPage() throws SystemException {
		deleteAllCookies();

		testStepStart("010 start page");

		openBaseUrl();
        
		//waitForClickable("/html/body/table[1]/tbody/tr/td[2]/a[2]/img");
       
		testStepDone(true);
	}

	@Override
	protected void init() throws SystemException {
		startPage();
	}

	@Override
	protected void finish() throws SystemException {
		// nothing to do
	}
}
