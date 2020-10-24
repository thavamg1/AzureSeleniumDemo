package com.theaa.dip.automation.ua.testscripts;

import org.testng.annotations.Test;

import com.theaa.dip.automation.ua.craft.DriverScript;
import com.theaa.dip.automation.ua.craft.TestConfigurations;
import com.theaa.dip.automation.ua.framework.selenium.SeleniumTestParameters;

public class FlightReservationScenario extends TestConfigurations {

	@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class)
	public void testForBookTicketsWithValidCreditCard(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for book flight tickets and verify booking");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

}
