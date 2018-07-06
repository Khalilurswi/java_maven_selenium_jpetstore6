package de.simpleworks.ta.configuration;

import java.net.URL;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.ta.IRaportEngine;
import de.simpleworks.ta.WebDriverType;

public interface IConfiguration {
	IRaportEngine getRaportEngine() throws SystemException;

	WebDriverType getWebDriverType() throws SystemException;

	int getTimeToWait() throws SystemException;

	int getTimeToWaitForElement() throws SystemException;

	int getTeststepPacing() throws SystemException;

	boolean isWebDriverToCloseOnError() throws SystemException;

	boolean isWebDriverMaximized() throws SystemException;

	int getWebDriverX() throws SystemException;

	int getWebDriverY() throws SystemException;

	int getWebDriverWidth() throws SystemException;

	int getWebDriverHeight() throws SystemException;

	String getTestCaseMapping() throws SystemException;

	String getNeoLoadProject() throws SystemException;

	URL getBaseUrl() throws SystemException;

	User getUser(String _testCaseId) throws SystemException;

	String getCategory(String _testCaseId) throws SystemException;

	String getProductId(String _testCaseId) throws SystemException;

	String getProductName(String _testCaseId) throws SystemException;

	String getItemId(String _testCaseId) throws SystemException;
}
