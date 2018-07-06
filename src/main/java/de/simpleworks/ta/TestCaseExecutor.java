package de.simpleworks.ta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.common.utils.UtilsFormat;
import de.simpleworks.ta.configuration.ConfigurationFactory;
import de.simpleworks.ta.configuration.IConfiguration;

public class TestCaseExecutor implements AutoCloseable {
	private static final Logger logger = LogManager.getLogger(TestCaseExecutor.class);

	private final IConfiguration configuration;

	public TestCaseExecutor(final String[] _args) throws SystemException {
		if (TestCaseExecutor.logger.isInfoEnabled()) {
			TestCaseExecutor.logger.info("create configuration..");
		}
		configuration = ConfigurationFactory.create(_args);
	}

	@SuppressWarnings("unchecked")
	private TestCaseBase createTestCase(@SuppressWarnings("rawtypes") final Class _clazz) throws SystemException {
		if (_clazz == null) {
			throw new SystemException("_class can't be null.");
		}

		if (!TestCaseBase.class.isAssignableFrom(_clazz)) {
			throw new SystemException(
					String.format("the class: \"%s\" isn't a valid test case (a test case must extend class \"%s\").",
							_clazz.getCanonicalName(), TestCaseBase.class.getCanonicalName()));
		}

		TestCaseBase result = null;

		if (TestCaseExecutor.logger.isInfoEnabled()) {
			TestCaseExecutor.logger
					.info(String.format("create new instance of class \"%s\": start..", _clazz.getName()));
		}

		Constructor<TestCaseBase> constructor;
		try {
			constructor = _clazz.getConstructor(IConfiguration.class);
		} catch (final Exception ex) {
			final String message = String.format("can't create constructor for class: \"%s\".", _clazz.getName());
			TestCaseExecutor.logger.error(message, ex);
			throw new SystemException(message);
		}

		try {
			result = constructor.newInstance(configuration);
		} catch (final InvocationTargetException ex) {
			final String message = String.format("can't create an object for class: \"%s\".", _clazz.getName());
			TestCaseExecutor.logger.error(message, ex);
			if (ex.getTargetException() != null) {
				TestCaseExecutor.logger.error("target exception:", ex.getTargetException());
			}
			throw new SystemException(message);
		} catch (final Exception ex) {
			final String message = String.format("can't create an object for class: \"%s\".", _clazz.getName());
			TestCaseExecutor.logger.error(message, ex);
			throw new SystemException(message);
		}

		return result;
	}

	public void execute(@SuppressWarnings("rawtypes") final Class _clazz) throws SystemException {
		if (TestCaseExecutor.logger.isInfoEnabled()) {
			TestCaseExecutor.logger
					.info(String.format("execute test case \"%s\": start..", UtilsFormat.getClassName(_clazz)));
		}

		try (TestCaseBase testCase = createTestCase(_clazz);) {
			testCase.execute();
		} finally {
			configuration.getRaportEngine().save();
		}

		if (TestCaseExecutor.logger.isInfoEnabled()) {
			TestCaseExecutor.logger
					.info(String.format("execute test case \"%s\": DONE.", UtilsFormat.getClassName(_clazz)));
		}
	}

	@Override
	public void close() throws SystemException {
		configuration.getRaportEngine().save();
	}

	public static final void execute(final String[] _args, @SuppressWarnings("rawtypes") final Class _clazz)
			throws SystemException {
		if (TestCaseExecutor.logger.isInfoEnabled()) {
			TestCaseExecutor.logger.info("start..");
		}

		try (TestCaseExecutor executor = new TestCaseExecutor(_args);) {
			executor.execute(_clazz);
		}

		if (TestCaseExecutor.logger.isInfoEnabled()) {
			TestCaseExecutor.logger.info("DONE.");
		}
	}
}
