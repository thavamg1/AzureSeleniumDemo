package com.theaa.dip.automation.ua.businesscomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.theaa.dip.automation.ua.craft.ReusableLibrary;
import com.theaa.dip.automation.ua.craft.ScriptHelper;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.pages.AppHomePage;

import io.appium.java_client.MobileElement;
import io.appium.java_client.touch.WaitOptions;


public class AAMobileApp extends ReusableLibrary {
	AppHomePage hp=new AppHomePage();
	public AAMobileApp(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	
	public void logInToApp()
	{
		try {
			PauseScript(3);
			String userName = dataTable.getData("General_Data", "Username");
			String password = dataTable.getData("General_Data", "Password"); 
			
			if(driver.findElementsById(hp.login).size()>0) {
				report.updateTestLog("Login", "The AA App Opened Successfully", Status.PASS);
			    WebElement loginbtn=driver.findElementById(hp.login);
				loginbtn.click();
				report.updateTestLog("Login", "Login Option Cliked Successfully", Status.PASS);
				PauseScript(3);
				WebElement emailid=driver.findElementById(hp.email_txt_id);
				emailid.click();
				emailid.clear();
				if(driver.findElementsById(hp.firstusername_txt_id).size()>0)
					{
					driver.findElementById(hp.firstusername_txt_id).click();
					}
				//driver.hideKeyboard();
				emailid.clear();
				emailid.sendKeys(userName);
				report.updateTestLog("Email", userName+"- Entered Successfully", Status.PASS);
				WebElement pwd=driver.findElement(By.id(hp.password_txt_id));
		        pwd.click();
			    driver.hideKeyboard();
			    pwd.sendKeys(password);
			    report.updateTestLog("Password", password+"- Entered Successfully", Status.PASS);
			    WebElement login_btn=driver.findElementById(hp.login_btn_id);
			    login_btn.click();
			    PauseScript(3);
			    if(driver.findElementsById("hp.traficloginErrMsg_txt_id").size()>0)
			    {
			    	report.updateTestLog("Login", "Login to App Failed/Invalid username & Password", Status.FAIL);
				
			    }
			    else
			    {
			    	report.updateTestLog("Login", "User Logged in Succesfully", Status.PASS);
			    }

			}
			else
			{
				report.updateTestLog("Login", "User Already Loggedin", Status.PASS);
			}
		} 
		catch(Exception e)
		{
			report.updateTestLog("Login Page", "Page/Object Not Found", Status.FAIL);
		}
	}
	
	public void createAccount()
	{
		String membership_no = dataTable.getData("General_Data", "MembershipNumber");
		String postcode = dataTable.getData("General_Data", "Postcode"); 
		WebElement loginbtn=driver.findElementById(hp.createAnAccountBtn_id);
		if(loginbtn.isEnabled())
		{
			loginbtn.click();
			WebElement membershipNumber=driver.findElementById(hp.membership_number_id);
			membershipNumber.sendKeys(membership_no);
			WebElement Postcode=driver.findElementById(hp.postcode_txt_id);
			Postcode.sendKeys(postcode);
			
			
		}
	}
	/*
	 * Function to click Account Icon
	 * 
	 */
	public void clickAccount()
	{
		try {
			PauseScript(3);
			if(driver.findElementsByXPath(hp.accountIcon_xpath).size()>0)
			{
				driver.findElementByXPath(hp.accountIcon_xpath).click();
				report.updateTestLog("Home Page", "Account Option Clicked Successfully", Status.PASS);
				
				
			}
		}
		catch(Exception e)
		{
			report.updateTestLog("Home Page", "Page/Object Not Found", Status.FAIL);
		}
	}

}
