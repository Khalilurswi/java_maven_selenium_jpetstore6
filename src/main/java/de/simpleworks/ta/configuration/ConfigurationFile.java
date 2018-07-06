package de.simpleworks.ta.configuration;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.UtilsFormat;
import de.simpleworks.common.utils.UtilsIO;

/**
 * @author Marcin Brzoza
 */

public class ConfigurationFile extends Configuration {
	public ConfigurationFile(final String _resource, final String _webDriverType, final String _neoLoadProject)
			throws SystemException {
		super(UtilsIO.loadProperties(_resource), _webDriverType, _neoLoadProject);
	}

	@Override
	public String toString() {
		return "[" + UtilsFormat.getClassName(this) + ": " + super.toString() + "]";
	}
}
