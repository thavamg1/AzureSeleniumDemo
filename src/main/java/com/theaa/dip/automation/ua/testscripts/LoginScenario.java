package com.theaa.dip.automation.ua.testscripts;

import org.testng.annotations.Test;

import com.theaa.dip.automation.ua.craft.DriverScript;
import com.theaa.dip.automation.ua.craft.TestConfigurations;
import com.theaa.dip.automation.ua.framework.selenium.SeleniumTestParameters;

public class LoginScenario extends TestConfigurations {

	@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class)
	public void testForInValidLogin(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for login with invalid user credentials");

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class)
	public void testForLoginWithNewlyRegisteredUser(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for Newly Registered user");
		
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class)
	public void testForValidLogin(SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("Test for login with valid user credentials");
		
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

}
