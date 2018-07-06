package de.simpleworks.ta.configuration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.Convert;
import de.simpleworks.ta.WebDriverType;

public class ConfigurationFactory {
	private static final Logger logger = LogManager.getLogger(ConfigurationFactory.class);

	private static final String OPTION_WEBDRIVER = "webdriver";
	private static final String OPTION_TESTSUITE = "testsuite";
	private static final String OPTION_ENVIROMENT = "enviroment";
	private static final String OPTION_PROJECT = "project";

	private static Options createOptions() {
		final Options result = new Options();

		result.addOption(Option.builder().longOpt(ConfigurationFactory.OPTION_WEBDRIVER)
				.desc(String.format("type of webdriver, possible values: [%s | %s]", WebDriverType.CHROME,
						WebDriverType.NEOLOAD_CHROME))
				.hasArg().argName("SIZE").build());
		result.addOption(Option.builder().longOpt(ConfigurationFactory.OPTION_TESTSUITE).desc("testsuite").hasArg()
				.argName("CUSTOMER").build());
		result.addOption(Option.builder().longOpt(ConfigurationFactory.OPTION_ENVIROMENT).desc("test enviroment")
				.hasArg().argName("TEST_ENVIROMENT").build());
		result.addOption(Option.builder().longOpt(ConfigurationFactory.OPTION_PROJECT).desc("path to NeoLoad project")
				.hasArg().argName("FILE").build());

		return result;
	}

	private static CommandLine getCommandLine(final String[] _args) throws SystemException {
		if (_args == null) {
			throw new SystemException("_args can't be null.");
		}

		final CommandLineParser parser = new DefaultParser();
		try {
			return parser.parse(ConfigurationFactory.createOptions(), _args);
		} catch (final ParseException ex) {
			final String message = "can't parse arguments.";
			ConfigurationFactory.logger.error(message, ex);
			throw new SystemException(message);
		}
	}

	private static String getOption(final CommandLine _line, final String _option, final boolean _mandatory)
			throws SystemException {
		final String result = _line.hasOption(_option) ? _line.getOptionValue(_option) : null;
		if (_mandatory && Convert.isEmpty(result)) {
			throw new SystemException(String.format("missing command line argument: \"%s\".", _option));
		}

		return result;
	}

	public static final IConfiguration create(final String[] _args) throws SystemException {
		final CommandLine line = ConfigurationFactory.getCommandLine(_args);
		final String customer = ConfigurationFactory.getOption(line, ConfigurationFactory.OPTION_TESTSUITE, true);
		final String enviroment = ConfigurationFactory.getOption(line, ConfigurationFactory.OPTION_ENVIROMENT, true);
		final String resource = String.format("%s_%s.properties", customer, enviroment);
		final IConfiguration result = new ConfigurationFile(resource,
				ConfigurationFactory.getOption(line, ConfigurationFactory.OPTION_WEBDRIVER, true),
				ConfigurationFactory.getOption(line, ConfigurationFactory.OPTION_PROJECT, false));

		return result;
	}
}
