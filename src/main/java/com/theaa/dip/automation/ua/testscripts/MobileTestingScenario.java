package com.theaa.dip.automation.ua.testscripts;

import org.testng.annotations.Test;

import com.theaa.dip.automation.ua.craft.DriverScript;
import com.theaa.dip.automation.ua.craft.TestConfigurations;
import com.theaa.dip.automation.ua.framework.selenium.SeleniumTestParameters;

public class MobileTestingScenario extends TestConfigurations {

	@Test(dataProvider = "MobileDevice", dataProviderClass = TestConfigurations.class)
	public void eriBankSendPayment(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for Login to EriBank App and MakePayment");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@Test(dataProvider = "MobileDevice")
	public void testMunkSignIn(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for Login for Test Munk");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@Test(dataProvider = "MobileDevice")
	public void tiesSelection(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Selecting Ties");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

}
