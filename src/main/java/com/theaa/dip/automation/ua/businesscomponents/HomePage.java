package com.theaa.dip.automation.ua.businesscomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.theaa.dip.automation.ua.craft.ReusableLibrary;
import com.theaa.dip.automation.ua.craft.ScriptHelper;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.pages.DigitalHomePage;
import com.theaa.dip.automation.ua.pages.ForgotPassword;

public class HomePage extends ReusableLibrary {

	public HomePage(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
   WebDriverWait wait = new WebDriverWait(driver, 10);
	
	public void clickLoginBtn()
	{
		try {
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.yourAccount_lnk_xpath)));
			  clickObject(driver, XPATH, DigitalHomePage.yourAccount_lnk_xpath);
			  report.updateTestLog("Home page", "Your Account Link is clicked", Status.PASS);
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.Login_CTA_xpath)));
			  clickObject(driver, XPATH, DigitalHomePage.Login_CTA_xpath);
			  report.updateTestLog("Home page", "Login Button Clicked", Status.PASS);
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.forgotPasswordLnk_xpath)));
			  String currentURL=driver.getCurrentUrl();
			  
			  if(currentURL.contains("https://auth.theaa.com/"))
			  {
				  report.updateTestLog("Home page", "Redirecting to Live URL", Status.FAIL);
			  }else
			  {
				  report.updateTestLog("Home page", "Navigated Successfully to Login Page", Status.PASS);
			  }
			
		}catch(Exception e)
		{
			report.updateTestLog("Home page", "Page/Object is not found in Home page", Status.FAIL);
		}
	}
	
	public void userLogin()
	{
		try {
			String email=getInput("EmailId");
			String password=getInput("Password");
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.email_txt_id)));
			 WebElement emailinp=driver.findElement(By.id(ForgotPassword.email_txt_id));
			 emailinp.sendKeys(email);
			 report.updateTestLog("Email", email+" - Entered Successfully", Status.PASS);
			 WebElement Passwordinp=driver.findElement(By.id(ForgotPassword.password_txt_id));
			 Passwordinp.sendKeys(password);
			 clickObject(driver, ID, ForgotPassword.loginBtn_id);
			 report.updateTestLog("Login", "User Successfully Loggedin", Status.PASS);
		}
		catch(Exception e)
		{
			report.updateTestLog("Login page", "Page/Object is not found in Home page", Status.FAIL);
		}
	}
	
	public void clickChangeYourDetails() {
		try {
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.yourAccount_lnk_xpath)));
			  clickObject(driver, XPATH, DigitalHomePage.yourAccount_lnk_xpath);
			  report.updateTestLog("Home page", "Your Account Link is clicked", Status.PASS);
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.changeYourDetails_xpath)));
			  clickObject(driver, XPATH, DigitalHomePage.changeYourDetails_xpath);
			  report.updateTestLog("Your Account", "Change Your Details Clicked", Status.PASS);
		}
		catch(Exception e)
		{
			 report.updateTestLog("Your Account", "Page/Object is not found", Status.FAIL);
		}
		
	}
	
	public void signout()
	{
		try {
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.yourAccount_lnk_xpath)));
			  clickObject(driver, XPATH, DigitalHomePage.yourAccount_lnk_xpath);
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.signout_xpath)));
			  clickObject(driver, XPATH, DigitalHomePage.signout_xpath);
			  report.updateTestLog("Your Account", "Change Your Details Clicked", Status.PASS);
		}catch(Exception e)
		{
			 report.updateTestLog("Home Page", "Page/Object is not found", Status.FAIL);
		}
	}
	
	
	
	
	
}
