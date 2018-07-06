package de.simpleworks.ta;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.neotys.selenium.proxies.NLWebDriver;
import com.neotys.selenium.proxies.NLWebDriverFactory;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.common.utils.UtilsFormat;
import de.simpleworks.ta.configuration.IConfiguration;

public class WebDriverFactory {
	private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

	private static void setupWebDriver(final IConfiguration _configuration, final WebDriver _driver) throws SystemException {
		if (_configuration.isWebDriverMaximized()) {
			_driver.manage().window().maximize();
		} else {
			Window window = _driver.manage().window();
			window.setPosition(new Point(_configuration.getWebDriverX(), _configuration.getWebDriverY()));
			window.setSize(new Dimension(_configuration.getWebDriverWidth(), _configuration.getWebDriverHeight()));
		}
	}

	private static WebDriver createChrome(final IConfiguration _configuration, final boolean _forNeoLoad) throws SystemException {
		if (WebDriverFactory.logger.isInfoEnabled()) {
			WebDriverFactory.logger.info("create Chrome driver..");
		}

		final ChromeOptions options = new ChromeOptions();
		final DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
		capabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, options);

		final DesiredCapabilities capabilities = _forNeoLoad ? NLWebDriverFactory.addProxyCapabilitiesIfNecessary(capabilitiesChrome) : capabilitiesChrome;
		final ChromeDriver result = new ChromeDriver(capabilities);

		setupWebDriver(_configuration, result);

		return result;
	}

	private static WebDriver createNeoloadWithChrome(final IConfiguration _configuration, final ITestCase _testCase) throws SystemException {
		if (_configuration == null) {
			throw new SystemException("_configuration can't be null.");
		}

		if (_testCase == null) {
			throw new SystemException("_testCase can't be null or empty.");
		}

		final String projectPath = _configuration.getNeoLoadProject();
		if (Convert.isEmpty(projectPath)) {
			throw new SystemException("projectPath can't be null or empty.");
		}

		final WebDriver webDriver = WebDriverFactory.createChrome(_configuration, true);
		setupWebDriver(_configuration, webDriver);

		final TestCaseMapping testCaseMapping = new TestCaseMapping(_configuration.getTestCaseMapping());
		final String scriptName = testCaseMapping.get(UtilsFormat.getClassName(_testCase));
		if (WebDriverFactory.logger.isInfoEnabled()) {
			WebDriverFactory.logger.info("setup NeoLoad for script: \"" + scriptName + "\" and project: \"" + projectPath + "\"..");
		}

		return NLWebDriverFactory.newNLWebDriver(webDriver, scriptName, projectPath);
	}

	public static final WebDriver create(final IConfiguration _configuration, final ITestCase _testCase) throws SystemException {
		WebDriver result = null;

		final WebDriverType type = _configuration.getWebDriverType();
		switch (type) {
		case CHROME:
			result = WebDriverFactory.createChrome(_configuration, false);
			break;
		case NEOLOAD_CHROME:
			result = WebDriverFactory.createNeoloadWithChrome(_configuration, _testCase);
			break;
		default:
			throw new SystemException("type_ \"" + type + "\" not implemented.");
		}

		return result;
	}

	public static final void close(final WebDriver _driver) {
		if (WebDriverFactory.logger.isInfoEnabled()) {
			WebDriverFactory.logger.info("close driver..");
		}

		if (_driver instanceof NLWebDriver) {
			((NLWebDriver) _driver).quit();
		} else {
			_driver.close();
		}
	}
}
