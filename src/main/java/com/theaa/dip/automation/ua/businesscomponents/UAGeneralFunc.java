package com.theaa.dip.automation.ua.businesscomponents;


import org.openqa.selenium.By;

import com.theaa.dip.automation.ua.craft.ReusableLibrary;
import com.theaa.dip.automation.ua.craft.ScriptHelper;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.pages.CreateAccountPage;

public class UAGeneralFunc extends ReusableLibrary{

	public UAGeneralFunc(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	public static String url;
	public static String version;
	public static String Env;
	
	public  void getVersionNumber() {
		System.out.println("TC: "+getInput("Mapping ID"));
		
        if(Env.equalsIgnoreCase("QA")) {
    		url=properties.getProperty("QA");
    		}
    		else if(Env.equalsIgnoreCase("CI")){
    		url=properties.getProperty("CI");
    		}
    		else if(Env.equalsIgnoreCase("UAT")){
        		url=properties.getProperty("UAT");
        	}
		
		driver.get(url);
		//version = getText(driver, XPATH, RoutePlannerPage.version_txt_xpath);
		//System.out.println(version);
		report.updateTestLog("Build Version", "", Status.PASS); 

	}
	
	public void openDigitalApplication()
	{
		try {
			
			String enironmentselect=properties.getProperty("ENV");
			if(!enironmentselect.isEmpty())
			{
		        if(enironmentselect.equalsIgnoreCase("PL6")) {
		    		url=properties.getProperty("PL6");
		    		}
		    		else if(enironmentselect.equalsIgnoreCase("PL3")){
		    		url=properties.getProperty("PL3");
		    		}
		    		else if(enironmentselect.equalsIgnoreCase("PL5")){
		        		url=properties.getProperty("PL5");
		    		}
		        System.out.println("URl--"+url);
		        String sub_url=getInput("DigitallaunchURL");
		        url=url+sub_url;
		        AACommonData.url=url;
				navigatetoWebpage(driver, url);
				waitForPageLoad(90);
				report.updateTestLog("Lauch App", "Browser Launched Successfully", Status.PASS);
		        if(driver.findElements(By.xpath(CreateAccountPage.cookie_info)).size()>0)	
		        {
		        	driver.findElement(By.xpath(CreateAccountPage.cookie_info)).click();
		        	report.updateTestLog("Cookie Preference", "Accepted cookies", Status.PASS);
		        }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void openDockerURL()
	{
		try {
			String url="https://www-pre.theaa.digital/home-insurance/sales/you-and-your-home";
			navigatetoWebpage(driver, url);
			waitForPageLoad(90);
			Thread.sleep(5000);
		}
		catch(Exception e) {}
	}

}
