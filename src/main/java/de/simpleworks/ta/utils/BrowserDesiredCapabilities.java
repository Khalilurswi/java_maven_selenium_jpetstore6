package de.simpleworks.ta.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserDesiredCapabilities {
	private static final Logger logger = LogManager.getLogger(BrowserDesiredCapabilities.class);

	public static DesiredCapabilities createForFireFox() {
		if (BrowserDesiredCapabilities.logger.isDebugEnabled()) {
			BrowserDesiredCapabilities.logger.debug("create DesiredCapabilities..");
		}

		final FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.cache.disk.enable", false);
		profile.setPreference("browser.cache.memory.enable", false);
		profile.setPreference("browser.cache.offline.enable", false);
		profile.setPreference("network.http.use-cache", false);
		profile.setPreference("network.http.max-persistent-connections-per-server", 12);

		final DesiredCapabilities result = DesiredCapabilities.firefox();
		result.setCapability("applicationCacheEnabled", true);
		result.setCapability("webStorageEnabled", true);
		result.setCapability("locationContextEnabled", true);

		result.setCapability(FirefoxDriver.PROFILE, profile);
		result.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

		return result;
	}
}
