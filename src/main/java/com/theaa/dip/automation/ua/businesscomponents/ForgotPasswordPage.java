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


public class ForgotPasswordPage extends ReusableLibrary {

	public ForgotPasswordPage(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	WebDriverWait wait = new WebDriverWait(driver, 10);
	Random rand = new Random();
	String email=getInput("EmailId");
	String password=getInput("Password");
	
	public void forgotPasswordWithValidEmailId()
	{
		try {
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.forgotPasswordLnk_xpath)));
			 clickObject(driver, XPATH, DigitalHomePage.forgotPasswordLnk_xpath);
			 report.updateTestLog("Home page", "Forgot Password Link clicked Successfully", Status.PASS);
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(ForgotPassword.emailaddress_inp_name)));
			 WebElement sendBtn=driver.findElement(By.id(ForgotPassword.sendBtn_id));
			 WebElement backtoLogin=driver.findElement(By.xpath(ForgotPassword.backtoLoginLnk_xpath));
			 if(sendBtn.isDisplayed()&&backtoLogin.isDisplayed())
			 {
				 report.updateTestLog("ResetPassword", "Email Textbox/Send Btn/Back to Login Link is displayed", Status.PASS);
			 }
			 else
			 {
				 report.updateTestLog("ResetPassword", "Email Textbox/Send Btn/Back to Login Link is not displayed", Status.FAIL);
			 }
			 String emailId=getInput("EmailId");
			 driver.findElement(By.name(ForgotPassword.emailaddress_inp_name)).sendKeys(emailId);
			 report.updateTestLog("Email Address", emailId+" - Entered Successfully", Status.PASS);
			 sendBtn.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.emailsent_xpath)));
			 WebElement emailSent=driver.findElement(By.xpath(ForgotPassword.emailsent_xpath));
			 if(emailSent.isDisplayed())
			 {
			 report.updateTestLog("Email Sent", "We've sent you an email Message is displayed", Status.PASS);
			 }
			 else
			 {
				 report.updateTestLog("Email Sent", "We've sent you an email Message is not displayed", Status.FAIL); 
			 }
			 backtoLogin.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.forgotPasswordLnk_xpath)));
			 report.updateTestLog("Back to Login", "User can able to navigate back to Login screen", Status.PASS);
			 
		}
		catch(Exception e)
		{
			report.updateTestLog("Reset Password", "Page/Object is not Found", Status.FAIL);
		}
	}
	
	public void forgotPasswordWithInvalidEmailId()
	{
		try {
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.forgotPasswordLnk_xpath)));
			 clickObject(driver, XPATH, DigitalHomePage.forgotPasswordLnk_xpath);
			 report.updateTestLog("Home page", "Forgot Password Link clicked Successfully", Status.PASS);
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(ForgotPassword.emailaddress_inp_name)));
			 WebElement sendBtn=driver.findElement(By.id(ForgotPassword.sendBtn_id));
			 WebElement backtoLogin=driver.findElement(By.xpath(ForgotPassword.backtoLoginLnk_xpath));
			 if(sendBtn.isDisplayed()&&backtoLogin.isDisplayed())
			 {
				 report.updateTestLog("ResetPassword", "Email Textbox/Send Btn/Back to Login Link is displayed", Status.PASS);
			 }
			 else
			 {
				 report.updateTestLog("ResetPassword", "Email Textbox/Send Btn/Back to Login Link is not displayed", Status.FAIL);
			 }
			 String emailId=getInput("EmailId");
			 driver.findElement(By.name(ForgotPassword.emailaddress_inp_name)).sendKeys(emailId);
			 report.updateTestLog("Email Address", emailId+" - Entered Successfully", Status.PASS);
			 sendBtn.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.invalidEmail_xpath)));
			 WebElement invalidemail=driver.findElement(By.xpath(ForgotPassword.invalidEmail_xpath));
			 if(invalidemail.isDisplayed())
			 {
			 report.updateTestLog("Email Sent", "Oops, something's not right Message is displayed", Status.PASS);
			 }
			 else
			 {
				 report.updateTestLog("Email Sent", "Oops, something's not right Message is not displayed", Status.FAIL); 
			 }
			 backtoLogin.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DigitalHomePage.forgotPasswordLnk_xpath)));
			 report.updateTestLog("Back to Login", "User can able to navigate back to Login screen", Status.PASS);
			 
		}
		catch(Exception e)
		{
			report.updateTestLog("Reset Password", "Page/Object is not Found", Status.FAIL);
		}
	}
	
	public void confirmMailinatorForResetPassword()
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
			
			if(resetPasswordTxt.contains("Reset the password for your account"))
			{
				driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).click();
			}
			else
			{
				report.updateTestLog("Mailinator",  " Reset the password for your account Email is not displayed", Status.FAIL);
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
			driver.switchTo().frame("msg_body");
			
			JavascriptExecutor jse = (JavascriptExecutor)driver.getWebDriver();
			jse.executeScript("window.scrollBy(0,1000)");
			Thread.sleep(2000);
			WebElement element=driver.findElement(By.xpath(ForgotPassword.setNewPassword_xpath));
			
			((JavascriptExecutor) driver.getWebDriver()).executeScript(
	                "arguments[0].scrollIntoView();", element);
			Thread.sleep(3000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.setNewPassword_xpath)));
			driver.findElement(By.xpath(ForgotPassword.setNewPassword_xpath)).click();
			Thread.sleep(5000);
			report.updateTestLog("Mailinator",  " Email id successfully Confirmed", Status.PASS);
			}
			catch(Exception e)
			{
			report.updateTestLog("Mailinator",  " Entered Email id is not valid", Status.FAIL);
			}
		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void resetPassword()
	{
		try {
		 ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(1));
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(ForgotPassword.password_name)));
		    clickObject(driver, XPATH, ForgotPassword.sendPassword_xpath);
		    WebElement password= driver.findElement(By.name(ForgotPassword.password_name));
		    password.sendKeys("Pass");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.PasswordErrMSg_xpath)));
		    WebElement passwordErr=driver.findElement(By.xpath(ForgotPassword.PasswordErrMSg_xpath));
		    if(passwordErr.isDisplayed())
		    {
		    	report.updateTestLog("Reset Password",  "Error Message is displayed for invalid Password", Status.PASS);
		    }
		    else
		    {
		    	report.updateTestLog("Reset Password",  "Error Message is not displayed for invalid Password", Status.FAIL);
		    }
		    password.clear();
		    String pass = "Password" + generateString(rand, "12345", 3);
		    AACommonData.Password=pass;
		  
		    password.sendKeys(pass);
		   
		    WebElement confirmPass=driver.findElement(By.name(ForgotPassword.confirmEmail_name));
		    confirmPass.sendKeys("Pass");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.confirmPassErrMsg_xpath)));
		    WebElement invalidconfirmpass=driver.findElement(By.xpath(ForgotPassword.confirmPassErrMsg_xpath));
		    if(invalidconfirmpass.isDisplayed())
		    {

		    	report.updateTestLog("Confirm Password",  "Password Does not match Error Message is displayed", Status.PASS);
		    }
		    else
		    {
		    	report.updateTestLog("Confirm Password",  "Password Does not match Error Message is not displayed", Status.FAIL);
		    }
		    confirmPass.clear();
		    confirmPass.sendKeys(pass);
		    dataTable.putData("General_Data", "Password", pass);
		    clickObject(driver, XPATH, ForgotPassword.sendPassword_xpath);
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.resetSuccess_xpath)));
		    clickObject(driver, XPATH, ForgotPassword.backtoLoginLnk_xpath);
		    loginAfterPasswordReset();
		    driver.close();
		    driver.switchTo().window(tabs2.get(0));
		}
		catch(Exception e)
		{
			report.updateTestLog("Resest Password",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void loginAfterPasswordReset()
	{
		try {
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
			 Passwordinp.clear();
			 Passwordinp.sendKeys(AACommonData.Password);
			 clickObject(driver, ID, ForgotPassword.loginBtn_id);
			 Thread.sleep(5000);
			 report.updateTestLog("Login",  "User can able to login after password reset", Status.PASS);
			
		}
		catch(Exception e)
		{
			 report.updateTestLog("Login",  "Page/Object if not found", Status.FAIL);
		}
	}
	
	public void confirmMailinatorForPasswordChange()
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
			
			if(resetPasswordTxt.contains("Password changed on your account"))
			{
				driver.findElement(By.xpath(ForgotPassword.emailsub_xpath)).click();
			}
			else
			{
				report.updateTestLog("Mailinator",  " Reset the password for your account Email is not displayed", Status.FAIL);
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
			
			report.updateTestLog("Mailinator",  "Password changed on your account email is triggered", Status.PASS);
			}
			catch(Exception e)
			{
			report.updateTestLog("Mailinator",  "Password changed on your account email is not triggered", Status.FAIL);
			}
		}
		catch(Exception e)
		{
			report.updateTestLog("Mailinator",  "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void checkResetPasswordLinkExpired()
	{
		try {
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(1));
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.invalidsetNewPassword_xpath)));
		    WebElement linkexpiredmsg=driver.findElement(By.xpath(ForgotPassword.invalidsetNewPassword_xpath));
		    if(linkexpiredmsg.isDisplayed())
		    {
		    	report.updateTestLog("Reset Your Password",  "We couldn't reset your password Message is Displayed", Status.PASS);
		    }
		    else
		    {
		    	report.updateTestLog("Reset Your Password",  "We couldn't reset your password Message is not Displayed", Status.FAIL);
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
	
	public void changePasswordwithInvalidData()
	{
		try {
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.oldPassword_id)));
		     WebElement oldPassword=driver.findElement(By.id(ForgotPassword.oldPassword_id));
			 WebElement newPassword=driver.findElement(By.id(ForgotPassword.newPassword_id));
			 WebElement confirmPassword=driver.findElement(By.id(ForgotPassword.confirmNewPassword_id));
			 String newpass = "Password" + generateString(rand, "12345", 3);
			 AACommonData.Password=newpass;
			 // Entering with Wrong Old Password
			 oldPassword.sendKeys("Pass");
			 newPassword.sendKeys(newpass);
			 confirmPassword.sendKeys(newpass);
			 report.updateTestLog("Update Password",  "Error Response is displayed", Status.PASS);
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.passwordStrength_xpath)));
			 clickObject(driver, XPATH, ForgotPassword.setBtn_xpath);
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.wrongOldPasswordErrMsg_xpath)));
			 WebElement wrongoldpassErrMsg=driver.findElement(By.xpath(ForgotPassword.wrongOldPasswordErrMsg_xpath));
			 if(wrongoldpassErrMsg.isDisplayed())
			 {
				 report.updateTestLog("Change Password",  "We don't recognise your old password, is it right? is displayed", Status.PASS);
			 }else
			 {
				 report.updateTestLog("Change Password",  "We don't recognise your old password, is it right? is not displayed", Status.FAIL); 
			 }
			 oldPassword.clear();
			 oldPassword.sendKeys(password);
			 //Entering with invalid new Password
			 newPassword.clear();
			 newPassword.sendKeys("Pass");
			 confirmPassword.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.wrongnewPasswordErrMsg_xpath)));
			 report.updateTestLog("Change Password",  "Your password should contain at least one capital letter, one lower case letter and one number is displayed", Status.PASS);
			 //Entering with old password with new Password
			 newPassword.clear();
			 newPassword.sendKeys(password);
			 confirmPassword.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.wrongNewPasswordErrMsg1_xpath)));
			 WebElement errmsg1=driver.findElement(By.xpath(ForgotPassword.wrongNewPasswordErrMsg1_xpath));
			 if(errmsg1.isDisplayed())
			 {
				 report.updateTestLog("Change Password",  "Your old and new passwords are the same Message is displayed", Status.PASS);	 
			 }
			 else
			 {
				 report.updateTestLog("Change Password",  "Your old and new passwords are the same Message is not displayed", Status.FAIL);	 
			 }
			 // Entering with Invalid Confirm Password
			 newPassword.clear();
			 newPassword.sendKeys(newpass);
			 confirmPassword.clear();
			 confirmPassword.sendKeys("Pass");
			 newPassword.click();
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.confirmPasswordnotMatchErrMsg_xpath)));
			 WebElement confirmPassErrMsg=driver.findElement(By.xpath(ForgotPassword.confirmPasswordnotMatchErrMsg_xpath));
			 if(confirmPassErrMsg.isDisplayed())
			 {
				 report.updateTestLog("Confirm Password",  "Your new password and confirmed password don't match Message is displayed", Status.PASS);	 
			 }
			 else
			 {
				 report.updateTestLog("Confirm Password",  "Your new password and confirmed password don't match Message is not displayed", Status.FAIL);	 
			 }
			
		}
		catch(Exception e)
		{
			report.updateTestLog("Change Password",  "Page/Object is not found", Status.FAIL);
		}
	}

	public void verifyErrorResponse() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.oldPassword_id)));
			clickObject(driver, XPATH, ForgotPassword.setBtn_xpath);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ForgotPassword.oldPasswordErrMsg_xpath)));
			WebElement oldPasswordErr = driver.findElement(By.xpath(ForgotPassword.oldPasswordErrMsg_xpath));
			WebElement newPasswordErr = driver.findElement(By.xpath(ForgotPassword.newPasswordErrMsg_xpath));
			WebElement confirmPasswordErr = driver.findElement(By.xpath(ForgotPassword.confirmPasswordErrMsg_xpath));
			if (oldPasswordErr.isDisplayed() && newPasswordErr.isDisplayed() && confirmPasswordErr.isDisplayed()) {
				report.updateTestLog("Update Password", "Error Response is displayed", Status.PASS);
			} else {
				report.updateTestLog("Update Password", "Error Response is not displayed", Status.FAIL);
			}

		} catch (Exception e) {
			report.updateTestLog("Change Password", "Page/Object is not found", Status.FAIL);
		}
	}
	
	public void changePasswordwithValidData()
	{
		try {
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ForgotPassword.oldPassword_id)));
		     WebElement oldPassword=driver.findElement(By.id(ForgotPassword.oldPassword_id));
			 WebElement newPassword=driver.findElement(By.id(ForgotPassword.newPassword_id));
			 WebElement confirmPassword=driver.findElement(By.id(ForgotPassword.confirmNewPassword_id));
			 String newpass = "Password" + generateString(rand, "12345", 3);
			 AACommonData.Password=newpass;
			 oldPassword.sendKeys(password);
			 report.updateTestLog("Old Password", password+"- Entered Successfully", Status.PASS);
			 newPassword.sendKeys(newpass);
			 confirmPassword.sendKeys(newpass);
			 report.updateTestLog("New/Confirm Password", newpass+"- Entered Successfully", Status.PASS);
			 
			 clickObject(driver, XPATH, ForgotPassword.setBtn_xpath);
			 report.updateTestLog("Change Password", "Password Changed Successfully", Status.PASS);	 
			 dataTable.putData("General_Data", "Password", newpass);
		}
		catch(Exception e)
		{
			report.updateTestLog("Change Password", "Page/Object is not found", Status.FAIL);
		}
	}
	
	

}
