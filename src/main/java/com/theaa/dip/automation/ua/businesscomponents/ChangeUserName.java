package com.theaa.dip.automation.ua.businesscomponents;

import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.theaa.dip.automation.ua.craft.ReusableLibrary;
import com.theaa.dip.automation.ua.craft.ScriptHelper;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.pages.DigitalHomePage;
import com.theaa.dip.automation.ua.pages.ForgotPassword;

public class ChangeUserName extends ReusableLibrary{

	public ChangeUserName(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	WebDriverWait wait = new WebDriverWait(driver, 10);
	Random rand = new Random();
	String email=getInput("EmailId");
	String password=getInput("Password");
	
	public void verifyUNErrorReponse()
	{
		try {
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.old_email_id)));
			clickObject(driver, XPATH, ForgotPassword.unamesetBtn_xpath);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.oldEmailErrMsg_xpath)));
			WebElement oldEmailErr = driver.findElement(By.xpath(ForgotPassword.oldEmailErrMsg_xpath));
			WebElement newEmailErr = driver.findElement(By.xpath(ForgotPassword.newEmailErrMsg_xpath));
			
			if (oldEmailErr.isDisplayed() && newEmailErr.isDisplayed()) {
				report.updateTestLog("Update UserName", "Error Response is displayed", Status.PASS);
			} else {
				report.updateTestLog("Update UserName", "Error Response is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			report.updateTestLog("Change UserName", "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void changeUserWithInvalidData()
	{
		try {
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.old_email_id)));
		     WebElement oldEmail=driver.findElement(By.id(ForgotPassword.old_email_id));
			 WebElement newEmail=driver.findElement(By.id(ForgotPassword.new_email_id));
			 String newUserName = "cun" + generateString(rand, "abcd", 3) + generateString(rand, "wxyz", 4)+"@mailinator.com";
			
			 AACommonData.UserName=newUserName;
			 // Entering with Wrong Old UserName
			 oldEmail.sendKeys("changeusername");
			 newEmail.sendKeys(newUserName);
			 report.updateTestLog("New Email Address",  newUserName+"- is Entered Successfully", Status.PASS);
			
			 clickObject(driver, XPATH, ForgotPassword.unamesetBtn_xpath);
			 driver.findElement(By.xpath(ForgotPassword.oldEmailErrMsg_xpath));
			 report.updateTestLog("Old Email Address",  " Old Email Id invalid Error Message is displayed", Status.PASS);
			 
			
			 
			 // Entering with Wrong New UserName
			 try {
			 oldEmail.clear();
			 oldEmail.sendKeys(email);
			 newEmail.clear();
			 newEmail.sendKeys("testinvalidemail");
			 clickObject(driver, XPATH, ForgotPassword.unamesetBtn_xpath);
			 driver.findElement(By.xpath(ForgotPassword.newEmailErrMsg_xpath));
			  report.updateTestLog("New Email Address",  "New Email Address is not valid Error Message is displayed", Status.PASS);
			 }
			 catch(Exception e)
			 {
				 report.updateTestLog("New Email Address",  "New Email Address is not valid Error Message is not displayed", Status.FAIL);
			 }
			 
			 // Change username with already existing data
			 
		 try {
				 oldEmail.clear();
				 oldEmail.sendKeys(email);
				 newEmail.clear();
				 newEmail.sendKeys("pl614232validemail@mailinator.com");
				 oldEmail.click();
				 clickObject(driver, XPATH, ForgotPassword.unamesetBtn_xpath);
				 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailAlreadyExists_xpath)));
				 driver.findElement(By.xpath(ForgotPassword.emailAlreadyExists_xpath));
				 report.updateTestLog("New Email Address",  "New Email Address is already Exisit Error Message is displayed", Status.PASS);
			 }
			 catch(Exception e)
			 {
				 report.updateTestLog("New Email Address",  "New Email Address is already Exisit Error Message is not displayed", Status.FAIL);
			 }
		}
		catch(Exception e)
		{
			report.updateTestLog("Change Username", "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void changeUserWithValidData()
	{
		try {
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.old_email_id)));
		     WebElement oldEmail=driver.findElement(By.id(ForgotPassword.old_email_id));
			 WebElement newEmail=driver.findElement(By.id(ForgotPassword.new_email_id));
			 String newUserName = "cun" + generateString(rand, "abcd", 3) + generateString(rand, "wxyz", 4)+"@mailinator.com";
			 AACommonData.UserName=newUserName;
			 oldEmail.sendKeys(email);
			 report.updateTestLog("Old Username", email+" - Entered Successfully", Status.PASS);
			 newEmail.sendKeys(newUserName);
			 dataTable.putData("General_Data", "EmailId", newUserName);
			 report.updateTestLog("New Username", newUserName+" - Entered Successfully", Status.PASS);
			 clickObject(driver, XPATH, ForgotPassword.unamesetBtn_xpath);
			 report.updateTestLog("Change UserName", " UserName Changed Successfully", Status.PASS);
		}
		catch(Exception e)
		{
			report.updateTestLog("Change Username", "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void confirmMailinatorForUNChange()
	{
		try {
			
		    driver.get("https://www.mailinator.com/");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailTextBox_xpath)));
			report.updateTestLog("Mailinator",  "Mailinator Launched Successfully", Status.PASS);
			
			driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(email);
			report.updateTestLog("Mailinator",  email+"entered Successfully", Status.PASS);
			driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
			try {
				Boolean flag=true;
				int count=1;
				while(flag)
				{
				try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailsub_xpath)));
			String resetPasswordTxt=driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).getText();
			
			if(resetPasswordTxt.contains("Have we got the right email address for your account?"))
			{
				driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).click();
			}
			else
			{
				report.updateTestLog("Mailinator",  " Have we got the right email address for your account? is not displayed", Status.FAIL);
			}
			
			break;
				}
				catch(Exception e)
				{
					count=count+1;
					driver.findElement(By.xpath(ForgotPassword.goInbox_xpath)).click();
					driver.navigate().refresh();
					driver.navigate().back();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).clear();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(email);
					driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
					if(count==40)
					{
						break;
					}
					
				}
				}
			
			report.updateTestLog("Mailinator",  "User Name changed on your account email is triggered", Status.PASS);
			}
			catch(Exception e)
			{
			report.updateTestLog("Mailinator",  "User Name changed on your account email is not triggered", Status.FAIL);
			}
		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	
	public void confirmMailinatorAfterUNChange()
	{
		try {
			
		    driver.get("https://www.mailinator.com/");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailTextBox_xpath)));
			report.updateTestLog("Mailinator",  "Mailinator Launched Successfully", Status.PASS);
			
			driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(AACommonData.UserName);
			report.updateTestLog("Mailinator",AACommonData.UserName+"entered Successfully", Status.PASS);
			driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
			try {
				Boolean flag=true;
				int count=1;
				while(flag)
				{
				try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailsub_xpath)));
			String resetPasswordTxt=driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).getText();
			
			if(resetPasswordTxt.contains("Is this the right email address for your account?"))
			{
				driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).click();
			}
			else
			{
				report.updateTestLog("Mailinator",  "Is this the right email address for your account? is not displayed", Status.FAIL);
			}
			
			break;
				}
				catch(Exception e)
				{
					count=count+1;
					driver.findElement(By.xpath(ForgotPassword.goInbox_xpath)).click();
					driver.navigate().refresh();
					driver.navigate().back();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).clear();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(AACommonData.UserName);
					driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
					if(count==40)
					{
						break;
					}
					
				}
				}
				driver.switchTo().frame("msg_body");
				
				JavascriptExecutor jse = (JavascriptExecutor)driver.getWebDriver();
				jse.executeScript("window.scrollBy(0,500)");
				Thread.sleep(2000);
				WebElement element=driver.findElement(By.xpath(ForgotPassword.confirmEmail_xpath));
				
				((JavascriptExecutor) driver.getWebDriver()).executeScript(
		                "arguments[0].scrollIntoView();", element);
				Thread.sleep(3000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.confirmEmail_xpath)));
				clickObject(driver, XPATH, ForgotPassword.confirmEmail_xpath);
				Thread.sleep(5000);
			
			}
			catch(Exception e)
			{
			report.updateTestLog("Mailinator",  "User Name changed on your account email is not triggered", Status.FAIL);
			}
		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void loginAfterChangeUserName()
	{
		try {
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(1));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.usernameSuccess_xpath)));
			driver.findElement(By.xpath(ForgotPassword.usernameSuccess_xpath));
			
			report.updateTestLog("Mailinator",  "User Name changed on your account email is triggered", Status.PASS);
			clickObject(driver, XPATH, ForgotPassword.backtoLoginLnk_xpath);
			report.updateTestLog("Mailinator",  "Back to Login button clicked", Status.PASS);
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.email_txt_id)));
			 WebElement logbtn=driver.findElement(By.id(ForgotPassword.loginBtn_id));
			 WebElement emailinp=driver.findElement(By.id(ForgotPassword.email_txt_id));
			 emailinp.sendKeys(email);
			 WebElement Passwordinp=driver.findElement(By.id(ForgotPassword.password_txt_id));
			 Passwordinp.sendKeys(password);
			 clickObject(driver, ID, ForgotPassword.loginBtn_id);
			 if(logbtn.isDisplayed())
			 {
				 report.updateTestLog("Login",  "Your email and password combination Error Message is displayed", Status.PASS); 
			 }else
			 {
				 report.updateTestLog("Login",  "Your email and password combination Error Message is not displayed", Status.FAIL); 
			 }
			 emailinp.clear();
			 emailinp.sendKeys(AACommonData.UserName);
			 Passwordinp.sendKeys(password);
			 clickObject(driver, ID, ForgotPassword.loginBtn_id);
			 Thread.sleep(5000);
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.editBtn_id)));
			 report.updateTestLog("Login",  "User can able to login after password reset", Status.PASS);
		}
		catch(Exception e)
		{
			 report.updateTestLog("Login",  "Page/Object if not found", Status.FAIL);
		}
	}
	
	public void confirmMailinatorAfterUNChanges()
	{
		try {
			
		    driver.get("https://www.mailinator.com/");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailTextBox_xpath)));
			report.updateTestLog("Mailinator",  "Mailinator Launched Successfully", Status.PASS);
			
			driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(AACommonData.UserName);
			report.updateTestLog("Mailinator",  email+"entered Successfully", Status.PASS);
			driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailsub_xpath)));
			
			Boolean flag=true;
			int count=1;
			while(flag)
			{
				String resetPasswordTxt=driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).getText();
				if(resetPasswordTxt.contains("We've changed your username for your account"))
				{
					report.updateTestLog("Mailinator",  "User Name changed on your account email is triggered", Status.PASS);
					driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).click();
					flag=false;
				}
				else
				{
					count=count+1;
					driver.findElement(By.xpath(ForgotPassword.goInbox_xpath)).click();
					driver.navigate().refresh();
					driver.navigate().back();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).clear();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(AACommonData.UserName);
					driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
					if(count==40)
					{
						report.updateTestLog("Mailinator",  "Is this the right email address for your account? is not displayed", Status.FAIL);
						break;
					}
					
				}
			}
		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void confirmExpiredLinkAfterUNChange()
	{
		try {
			
		    driver.get("https://www.mailinator.com/");
		    ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(0));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailTextBox_xpath)));
			report.updateTestLog("Mailinator",  "Mailinator Launched Successfully", Status.PASS);
			
			driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(AACommonData.UserName);
			report.updateTestLog("Mailinator",AACommonData.UserName+"entered Successfully", Status.PASS);
			driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
			
			
			Boolean flag=true;
			int count=1;
			while(flag)
			{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailsub2_xpath)));
				String resetPasswordTxt=driver.findElement(By.xpath(ForgotPassword.emailsub2_xpath)).getText();
				if(resetPasswordTxt.contains("Is this the right email address for your account?"))
				{
					report.updateTestLog("Mailinator",  "User Name changed on your account email is triggered", Status.PASS);
					driver.findElement(By.xpath(ForgotPassword.emailsub2_xpath)).click();
					driver.switchTo().frame("msg_body");
					
					JavascriptExecutor jse = (JavascriptExecutor)driver.getWebDriver();
					jse.executeScript("window.scrollBy(0,500)");
					Thread.sleep(2000);
					WebElement element=driver.findElement(By.xpath(ForgotPassword.confirmEmail_xpath));
					
					((JavascriptExecutor) driver.getWebDriver()).executeScript(
			                "arguments[0].scrollIntoView();", element);
					Thread.sleep(3000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.confirmEmail_xpath)));
					clickObject(driver, XPATH, ForgotPassword.confirmEmail_xpath);
					Thread.sleep(5000);
					flag=false;
				}
				else
				{
					count=count+1;
					driver.findElement(By.xpath(ForgotPassword.goInbox_xpath)).click();
					driver.navigate().refresh();
					driver.navigate().back();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).clear();
					driver.findElement(By.xpath(ForgotPassword.emailTextBox_xpath)).sendKeys(AACommonData.UserName);
					driver.findElement(By.xpath(ForgotPassword.goBtn_xpath)).click();
					if(count==40)
					{
						report.updateTestLog("Mailinator",  "Is this the right email address for your account? is not displayed", Status.FAIL);
						break;
					}
					
				}
			}

		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void checkUserNameLinkExpired()
	{
		try {
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(2));
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.invalidsetNewPassword_xpath)));
		    WebElement linkexpiredmsg=driver.findElement(By.xpath(ForgotPassword.invalidsetNewPassword_xpath));
		    if(linkexpiredmsg.isDisplayed())
		    {
		    	report.updateTestLog("Change UserName",  "Unable to change your username Message is Displayed", Status.PASS);
		    }
		    else
		    {
		    	report.updateTestLog("Change UserName",  "Unable to change your username Message is not Displayed", Status.FAIL);
		    }
		    clickObject(driver, XPATH, ForgotPassword.backtoLoginLnk_xpath);
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.forgotPasswordLnk_xpath)));
		    report.updateTestLog("Back TO Login",  "Back to Login Button is Displayed and navigated to login page", Status.PASS);
		    
		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
}
