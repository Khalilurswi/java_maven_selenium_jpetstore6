package de.simpleworks.ta.webminar.testcase.run;

import de.simpleworks.common.exception.SystemException;
import de.simpleworks.ta.TestCaseExecutor;
import de.simpleworks.ta.webminar.testcase.TC010_Buyer;

public class RunTC010_Buyer {
	public static void main(final String[] args) throws SystemException {
		TestCaseExecutor.execute(args, TC010_Buyer.class);
	}
}
