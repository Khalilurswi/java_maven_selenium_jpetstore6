package de.simpleworks.ta.webminar.testcase;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.ta.configuration.IConfiguration;

/**
 * @author marcin,brzoza@simpleworks.de
 */

public class TC010_Buyer extends TestCaseWithLogin {
	private static final String ID = "tc010";

		public TC010_Buyer(final IConfiguration _configuration) throws SystemException {
		super(_configuration, TC010_Buyer.ID);
	}

	@Override
	protected void executeCase() throws SystemException {
		testStepStart("100 open category");
		clickByXpath("//*[@id=\"QuickLinks\"]/a[2]"); 
		testStepDone(true);

		testStepStart("110 open product");
		clickByXpath("//*[@id=\"Catalog\"]/table/tbody/tr[2]/td[1]/a"); 
		testStepDone(true);
		                                             
		testStepStart("120 open item");
		clickByXpath("//*[@id=\"Catalog\"]/table/tbody/tr[2]/td[1]/a");
		testStepDone(true);

		testStepStart("130 add to cart");
		clickByXpath("//*[@id=\"Catalog\"]/table/tbody/tr[7]/td/a");
		testStepDone(true);

		testStepStart("140 check out start");
		clickByXpath("//*[@id=\"Cart\"]/a");
		testStepDone(true);

		testStepStart("150 check out continue");
		clickByXpath("//*[@id=\"Catalog\"]/form/input");
		testStepDone(true);

		testStepStart("160 submit");
		clickByXpath("//*[@id=\"Catalog\"]/a");
		testStepDone(true);

		
	}
}
