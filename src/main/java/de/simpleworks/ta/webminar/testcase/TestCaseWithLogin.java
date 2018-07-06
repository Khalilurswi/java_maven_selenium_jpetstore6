package de.simpleworks.ta.webminar.testcase;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.ta.configuration.IConfiguration;
import de.simpleworks.ta.configuration.User;

public abstract class TestCaseWithLogin extends TestCase {
	private final User user;

	public TestCaseWithLogin(final IConfiguration _configuration, final String _id) throws SystemException {
		super(_configuration, _id);

		user = configuration.getUser(getId());
		if (user == null) {
			throw new SystemException("user can't be null.");
		}
	}

	private void login() throws SystemException {
		testStepStart("020 login page");
		clickByXpath("//*[@id=\"MenuContent\"]/a[2]");
		waitForClickable("//*[@id=\"Catalog\"]/form/input");
		testStepDone(true);

		testStepStart("030 login");
		setText("//input[@name='username']", user.getLogin());
		setText("//input[@name='password']", user.getPassword());
		clickByXpath("//*[@id=\"Catalog\"]/form/input");
		testStepDone(true);
	}

	private void logout() throws SystemException {
		testStepStart("999 logout");

		clickByXpath("//*[@id=\"MenuContent\"]/a[2]");

		testStepDone(true);
	}

	@Override
	protected final void init() throws SystemException {
		super.init();

		login();
	}

	@Override
	protected final void finish() throws SystemException {
		logout();
	}
}
