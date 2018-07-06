package de.simpleworks.ta;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.neotys.selenium.proxies.TransactionModifier;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.ta.configuration.IConfiguration;

abstract public class TestCaseBase implements ITestCase {
	private static final Logger logger = LogManager.getLogger(TestCaseBase.class);

	private static final long MILIS_IN_SECOND = 1000;

	protected final IConfiguration configuration;
	private final String id;
	private final IRaportTestCase raportTestCase;

	private final WebDriver driver;

	private final long timeToWait;
	private final WebDriverWait wait;

	private TestCaseStatus status;

	private final long testStepPacing;
	private IRaportTestStep raportTestStep;
	private long timeStart;

	public TestCaseBase(final IConfiguration _configuration, final String _id) throws SystemException {
		if (_configuration == null) {
			throw new SystemException("_configuration can't be null.");
		}

		if (Convert.isEmpty(_id)) {
			throw new SystemException("_id can't be null or empty string.");
		}

		configuration = _configuration;
		id = _id;
		raportTestCase = configuration.getRaportEngine().createTestCase(id);

		setStatus(TestCaseStatus.NEW);
		driver = WebDriverFactory.create(configuration, this);

		timeToWait = TestCaseBase.MILIS_IN_SECOND * configuration.getTimeToWait();
		wait = new WebDriverWait(driver, configuration.getTimeToWaitForElement());

		testStepPacing = TestCaseBase.MILIS_IN_SECOND * configuration.getTeststepPacing();
		raportTestStep = null;
		timeStart = 0;
	}

	@Override
	public final String getId() {
		return id;
	}

	@Override
	public final TestCaseStatus getStatus() {
		return status;
	}

	private void setStatus(TestCaseStatus _value) {
		status = _value;

		switch (status) {
		case OK:
			raportTestCase.done(true);
			break;
		case FAIL:
			raportTestCase.done(false);
			break;
		default:
			// nothing to do.
		}

	}

	abstract protected void executeCase() throws SystemException;

	protected final void deleteAllCookies() {
		driver.manage().deleteAllCookies();
	}

	protected final void openBaseUrl() throws SystemException {
		final String url = configuration.getBaseUrl().toString();
		try {
			driver.get(url);
		} catch (Exception ex) {
			final String message = String.format("[%s] can't open url: \"%s\".", id, url);
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}
	}

	protected final void testStepStart(final String _name) throws SystemException {
		if (raportTestStep != null) {
			throw new SystemException(
					String.format("[%s] test step %s allready in progress.", id, raportTestStep.getId()));
		}

		if (Convert.isEmpty(_name)) {
			throw new SystemException("_name can't be null or empty string.");
		}

		raportTestStep = raportTestCase.createTestStep(_name);

		if (TestCaseBase.logger.isInfoEnabled()) {
			TestCaseBase.logger.info(String.format("[%s] test step \"%s\": start..", id, _name));
		}

		if (driver instanceof TransactionModifier) {
			((TransactionModifier) driver).startTransaction(_name);
		}

		timeStart = System.currentTimeMillis();
	}

	protected final void testStepDone(boolean _success) throws SystemException {
		if (raportTestStep == null) {
			throw new SystemException(String.format("[%s] nothing to stop.", id));
		}

		if (!_success) {
			raportTestStep.done(false);
			setStatus(TestCaseStatus.FAIL);
			throw new SystemException(String.format("[%s] test step \"%s\" FAIL.", id, raportTestStep.getId()));
		}

		raportTestStep.done(true);

		final long duration = System.currentTimeMillis() - timeStart;
		final long timeToSleep = testStepPacing - duration;
		if (0 < timeToSleep) {
			TestCaseBase.logger.info(String.format("[%s] test step \"%s\": waiting..", id, raportTestStep.getId()));
			sleep(timeToSleep);
		} else {
			TestCaseBase.logger
					.warn(String.format("[%s] test step \"%s\": took too long time.", id, raportTestStep.getId()));
		}

		if (driver instanceof TransactionModifier) {
			((TransactionModifier) driver).stopTransaction();
		}

		if (TestCaseBase.logger.isInfoEnabled()) {
			TestCaseBase.logger.info(String.format("[%s] test step \"%s\": DONE.", id, raportTestStep.getId()));
		}

		raportTestStep = null;
	}

	protected final void sleep(final long _milis) {
		try {
			Thread.sleep(_milis);
		} catch (final InterruptedException ex) {
			TestCaseBase.logger.error(String.format("[%s] thread interrupted, we ignore it.", id), ex);
		}
	}

	protected final void sleep() {
		sleep(timeToWait);
	}

	protected final void waitForClickable(final String _xpathExpression) throws SystemException {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(_xpathExpression)));
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't get WebElement for xpath: \"%s\".", id, _xpathExpression);
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}
	}

	protected final Actions createActions() {
		return new Actions(driver);
	}

	protected final WebElement findElement(final By _by) throws SystemException {
		WebElement result = null;

		try {
			result = driver.findElement(_by);
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't get WebElement for By: \"%s\".", id, _by.toString());
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	protected final WebElement findElement(final WebElement _parent, final By _by) throws SystemException {
		WebElement result = null;

		try {
			result = _parent.findElement(_by);
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't get child of WebElement \"%s\" for By: \"%s\".", id,
					_parent, _by.toString());
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	protected final WebElement findElementById(final String _id) throws SystemException {
		return findElement(By.id(_id));
	}

	protected final WebElement findElementByXpath(final String _xpathExpression) throws SystemException {
		return findElement(By.xpath(_xpathExpression));
	}

	protected final WebElement findElementByXpath(final WebElement _element, final String _xpathExpression)
			throws SystemException {
		if (!_xpathExpression.startsWith(".")) {
			TestCaseBase.logger
					.warn(String.format("[%s] missing \".\" on begin of xpath \"%s\".", id, _xpathExpression));
		}

		return findElement(_element, By.xpath(_xpathExpression));
	}

	protected final WebElement findElementByLinkText(final String _text) throws SystemException {
		return findElement(By.linkText(_text));
	}

	protected final List<WebElement> findElementsByXpath(final String _xpathExpression) throws SystemException {
		List<WebElement> result = null;

		try {
			result = driver.findElements(By.xpath(_xpathExpression));
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't get WebElement for xpath: \"%s\".", id, _xpathExpression);
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	protected final WebElement clickById(final String _id) throws SystemException {
		final WebElement result = findElementById(_id);

		try {
			result.click();
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't click WebElement for id: \"%s\".", id, _id);
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	protected final WebElement clickByXpath(final WebElement _parent, final String _xpathExpression)
			throws SystemException {
		final WebElement result = findElementByXpath(_parent, _xpathExpression);

		try {
			result.click();
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't click WebElement \"%s\" for xpath: \"%s\".", id, _parent,
					_xpathExpression);
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	protected final WebElement clickByXpath(final String _xpathExpression) throws SystemException {
		final WebElement result = findElementByXpath(_xpathExpression);

		try {
			result.click();
		} catch (final Exception ex) {
			final String message = String.format("[%s] can't click WebElement for xpath: \"%s\".", id,
					_xpathExpression);
			TestCaseBase.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	protected static final void setText(final WebElement _element, final String _value) throws SystemException {
		if (_element == null) {
			throw new SystemException("_element can't be null.");
		}

		_element.clear();
		_element.sendKeys(_value);
	}

	protected final void setText(final String _xpathExpression, final String _value) throws SystemException {
		TestCaseBase.setText(findElementByXpath(_xpathExpression), _value);
	}

	protected abstract void init() throws SystemException;

	protected abstract void finish() throws SystemException;

	@Override
	public final void execute() throws SystemException {
		if (TestCaseBase.logger.isInfoEnabled()) {
			TestCaseBase.logger.info(String.format("[%s] start..", id));
		}

		setStatus(TestCaseStatus.RUN);

		try {
			init();

			executeCase();

			finish();

			setStatus(TestCaseStatus.OK);
		} catch (final Throwable th) {
			setStatus(TestCaseStatus.FAIL);
			logger.error(String.format("[%s] test case FAILED, based on exception:", id), th);
			throw new SystemException(String.format("[%s] test case FAILED.", id));
		}

		if (TestCaseBase.logger.isInfoEnabled()) {
			TestCaseBase.logger.info(String.format("[%s] DONE.", id));
		}
	}

	@Override
	public final void close() throws SystemException {
		if ((status == TestCaseStatus.FAIL) && !configuration.isWebDriverToCloseOnError()) {
			return;
		}

		WebDriverFactory.close(driver);
	}
}
