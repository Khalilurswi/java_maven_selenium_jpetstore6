package de.simpleworks.ta.configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.ta.IRaportEngine;
import de.simpleworks.ta.WebDriverType;
import de.simpleworks.ta.report.RaportEngine;

/**
 * @author Marcin Brzoza
 */

public class Configuration implements IConfiguration {
	private static final Logger logger = LogManager.getLogger(Configuration.class);

	private final Properties properties;
	private final IRaportEngine raportEngine;

	protected Configuration(final Properties _properties, final String _webDriverType, final String _neoLoadProject)
			throws SystemException {
		if (_properties == null) {
			throw new SystemException("_properties can't be null.");
		}
		if (Convert.isEmpty(_webDriverType)) {
			throw new SystemException("_webDriverType can't be null or empty string.");
		}

		properties = _properties;
		final WebDriverType type = Configuration.getWebDriverType(_webDriverType);
		if (type == WebDriverType.NEOLOAD_CHROME) {
			if (Convert.isEmpty(_neoLoadProject)) {
				throw new SystemException(
						String.format("type of WebDriver is \"%s\", but the path to NeoLoad project is not setted.",
								WebDriverType.NEOLOAD_CHROME));
			}

			setString(ConfigurationKeys.NEOLOAD_PROJECT, _neoLoadProject);
		}

		setString(ConfigurationKeys.WEBDRIVER_TYPE, _webDriverType);

		raportEngine = new RaportEngine();
	}

	private static WebDriverType getWebDriverType(final String _name) throws SystemException {
		WebDriverType result = null;

		try {
			result = WebDriverType.valueOf(_name);
		} catch (final Exception ex) {
			final String message = "webDriver: \"" + _name + "\" isn't valid.";
			Configuration.logger.error(message, ex);
			throw new SystemException(message);
		}

		if (result == null) {
			throw new SystemException("webDriver: \"" + _name + "\" isn't valid.");
		}

		return result;
	}

	private void setString(final String _key, final String _value) throws SystemException {
		if (Convert.isEmpty(_key)) {
			throw new SystemException("_key can't be null or empty string.");
		}

		properties.remove(_key);
		if (!Convert.isEmpty(_value)) {
			properties.put(_key, _value);
		}
	}

	private String getString(final String _key, final boolean _mandatory, final String _default)
			throws SystemException {
		final String result = properties.getProperty(_key, null);
		if (_mandatory && (result == null)) {
			throw new SystemException("key \"" + _key + "\" without value.");
		}

		return result == null ? _default : result;
	}

	private String getString(final String _key) throws SystemException {
		return getString(_key, true, null);
	}

	private int getInt(final String _key) throws SystemException {
		return Convert.getInt(getString(_key), -1);
	}

	private boolean getBoolean(final String _key) throws SystemException {
		return Convert.getBoolean(getString(_key), false);
	}

	private URL getURL(final String _key) throws SystemException {
		final String url = getString(_key);
		URL result;
		try {
			result = new URL(url);
		} catch (final MalformedURLException ex) {
			final String message = "key \"" + _key + "\" has invalid value (expected is an URL, but got: \"" + url
					+ "\").";
			Configuration.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	@Override
	public final IRaportEngine getRaportEngine() throws SystemException {
		return raportEngine;
	}

	@Override
	public final WebDriverType getWebDriverType() throws SystemException {
		return Configuration.getWebDriverType(getString(ConfigurationKeys.WEBDRIVER_TYPE, true, null));
	}

	@Override
	public final int getTimeToWait() throws SystemException {
		return getInt(ConfigurationKeys.TIME_TO_WAIT);
	}

	@Override
	public final int getTimeToWaitForElement() throws SystemException {
		return getInt(ConfigurationKeys.TIME_TO_WAIT_FOR_ELEMENT);
	}

	@Override
	public final int getTeststepPacing() throws SystemException {
		return getInt(ConfigurationKeys.TESTSTEP_PACING);
	}

	@Override
	public final String getNeoLoadProject() throws SystemException {
		return getString(ConfigurationKeys.NEOLOAD_PROJECT);
	}

	@Override
	public final String getTestCaseMapping() throws SystemException {
		return getString(ConfigurationKeys.TESTCASE_MAPPING);
	}

	@Override
	public final boolean isWebDriverToCloseOnError() throws SystemException {
		return getBoolean(ConfigurationKeys.WEBDRIVER_CLOSE_ON_ERROR);
	}

	@Override
	public boolean isWebDriverMaximized() throws SystemException {
		return getBoolean(ConfigurationKeys.WEBDRIVER_MAXIMIZED);
	}

	@Override
	public int getWebDriverX() throws SystemException {
		return getInt(ConfigurationKeys.WEBDRIVER_X);
	}

	@Override
	public int getWebDriverY() throws SystemException {
		return getInt(ConfigurationKeys.WEBDRIVER_Y);
	}

	@Override
	public int getWebDriverWidth() throws SystemException {
		return getInt(ConfigurationKeys.WEBDRIVER_WIDTH);
	}

	@Override
	public int getWebDriverHeight() throws SystemException {
		return getInt(ConfigurationKeys.WEBDRIVER_HEIGHT);
	}

	@Override
	public final URL getBaseUrl() throws SystemException {
		return getURL(ConfigurationKeys.URL_APP);
	}

	private static void checkTestCaseId(final String _testCaseId) throws SystemException {
		if (Convert.isEmpty(_testCaseId)) {
			throw new SystemException("_testCaseId can't be null or empty string.");
		}
	}

	@Override
	public final User getUser(final String _testCaseId) throws SystemException {
		Configuration.checkTestCaseId(_testCaseId);

		final String user = getString(String.format("%s.%s", _testCaseId, ConfigurationKeys.USER), true, null);
		final String password = getString(String.format("%s.%s", _testCaseId, ConfigurationKeys.PASSWORD), true, null);

		return new User(user, password);
	}

	@Override
	public final String getCategory(final String _testCaseId) throws SystemException {
		Configuration.checkTestCaseId(_testCaseId);

		return getString(String.format("%s.%s", _testCaseId, ConfigurationKeys.CATEGORY));
	}

	@Override
	public final String getProductId(final String _testCaseId) throws SystemException {
		Configuration.checkTestCaseId(_testCaseId);

		return getString(String.format("%s.%s", _testCaseId, ConfigurationKeys.PRODUCT_ID));
	}

	@Override
	public final String getProductName(final String _testCaseId) throws SystemException {
		Configuration.checkTestCaseId(_testCaseId);

		return getString(String.format("%s.%s", _testCaseId, ConfigurationKeys.PRODUCT_NAME));
	}

	@Override
	public final String getItemId(final String _testCaseId) throws SystemException {
		Configuration.checkTestCaseId(_testCaseId);

		return getString(String.format("%s.%s", _testCaseId, ConfigurationKeys.ITEM_ID));
	}
}
