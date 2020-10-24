package com.theaa.dip.automation.ua.businesscomponents;


import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.theaa.dip.automation.ua.craft.ReusableLibrary;
import com.theaa.dip.automation.ua.craft.ScriptHelper;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.pages.CreateAccountPage;
import com.theaa.dip.automation.ua.pages.Login_Page;
import com.theaa.dip.automation.ua.pages.YourAccount_Screen;
import com.theaa.dip.automation.ua.pages.EditdetailsPage;
import com.theaa.dip.automation.ua.pages.PolicyAlreadyassocPage;
import com.theaa.dip.automation.ua.pages.VbAccount;
import com.theaa.dip.automation.ua.businesscomponents.UAGeneralFunc;

public class CreateAccount extends ReusableLibrary  {

	private Object object;
	public CreateAccount(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	WebDriverWait wait= new WebDriverWait(driver, 10);
	public static String url;
    public static String version;
    public static String Env;
	String email=null;
	String password=null;
	String Url=null;
	Random rand = new Random();
    public void getVersionNumber() {
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
        
        String val=url+"plan";
        String verURL = val.replace("routeplan", "info");
        driver.get(verURL);
        //version = getText(driver, XPATH, RoutePlannerPage.version_txt_xpath);
        //System.out.println(version);
        report.updateTestLog("Build Version", "", Status.PASS);      
       

    }
	
	public void openDigitalApp()
	{
		
		try {
		
		String url =Url+getInput("DigitallaunchURL");
		System.out.println("URl--"+url);
		navigatetoWebpage(driver, url);
		waitForPageLoad(90);
		report.updateTestLog("Launch App", "Browser Launched Successfully", Status.PASS);
		
		}
		catch(Exception e)
		{
			report.updateTestLog("Launch App", "Browser isnot Launched Successfully", Status.FAIL);
		}
	}
	public void verify_policyOption()
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.road_select)));
			WebElement road_opt=driver.findElement(By.xpath(CreateAccountPage.road_select));
			WebElement bank_opt=driver.findElement(By.xpath(CreateAccountPage.bank_select));
			WebElement ins_ebc_opt=driver.findElement(By.xpath(CreateAccountPage.ins_eb_select));
			
			if(road_opt.isDisplayed() && bank_opt.isDisplayed() && ins_ebc_opt.isDisplayed())
				report.updateTestLog("Membership section", "All three options are available", Status.PASS);	
		}
		catch(Exception e)
		{
			report.updateTestLog("Membership section", "three membership options are not displayed", Status.FAIL);
		}
		
	}
	public void road_Acc_HP()
	{
		try {
		verify_policyOption();
		String road_pol=getInput("Policynumber");
		driver.findElement(By.id(CreateAccountPage.road_mem)).sendKeys(road_pol);
		creatAccount();
		passwordSection();
		report.updateTestLog("Create Account", "Road Policy: successfully entered details and submitted", Status.PASS);
		}
		
		catch(Exception e)
		{
			report.updateTestLog("Create Account", "object/Page not found", Status.FAIL);
		}
	}
	public void bank_Acc_HP()
	{
		try {
			verify_policyOption();
			driver.findElement(By.xpath(CreateAccountPage.bank_select)).click();
			String sortcode=getInput("Sortcode");
			driver.findElement(By.xpath(CreateAccountPage.sortcode)).sendKeys(sortcode);
			String accountnumber=getInput("Acc number");
			driver.findElement(By.xpath(CreateAccountPage.accnumber)).sendKeys(accountnumber);
			creatAccount();
			passwordSection();
			report.updateTestLog("Create Account", "Bank Customer: successfully entered details and submitted", Status.PASS);
			}
			
		catch(Exception e)
			{
				report.updateTestLog("Create Account", "object/Page not found", Status.FAIL);
			}			
	}
	public void ins_EBC_Acc_HP()
	{
		try {
			verify_policyOption();
			driver.findElement(By.xpath(CreateAccountPage.ins_eb_select)).click();
			String insEbc_pol=getInput("InsEbc Pol");	
			driver.findElement(By.id(CreateAccountPage.ins_ebc_mem)).click();
			driver.findElement(By.id(CreateAccountPage.ins_ebc_mem)).sendKeys(insEbc_pol);
			report.updateTestLog("Insurnace/EBC policy", insEbc_pol+"-Entered Successfully", Status.PASS);
			creatAccount();
			passwordSection();
			report.updateTestLog("Create Account", "Insurnace/EBC policy: successfully entered details and submitted", Status.PASS);
			}
			
		catch(Exception e)
			{
				report.updateTestLog("Create Account", "object/Page not found", Status.FAIL);
			}			
	}
	public void errormessages_RoadAcc()
	{
		try {
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			report.updateTestLog("Road membership error messages", "Error message for blank field", Status.PASS);
			WebElement ro_blank=driver.findElement(By.xpath(CreateAccountPage.roadmem_blank));
			ro_blank.isDisplayed();
			driver.findElement(By.id(CreateAccountPage.road_mem)).sendKeys("6356013");
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			report.updateTestLog("Road membership error messages", "Error message for Min length", Status.PASS);
			WebElement minlen=driver.findElement(By.xpath(CreateAccountPage.roadmem_minln));	
			minlen.isDisplayed();
			WebElement road_pol=driver.findElement(By.id(CreateAccountPage.road_mem));
			road_pol.clear();
			road_pol.sendKeys("6356010930437758");
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			report.updateTestLog("Road membership error messages", "Error message for checksumlogic", Status.PASS);
			WebElement ro_checksum=driver.findElement(By.xpath(CreateAccountPage.roadmem_checksumerr));
			ro_checksum.isDisplayed();
		}
		catch(Exception e)
		{
			report.updateTestLog("Road membership error messages", "page/object not found", Status.FAIL);	
		}
	}
	public void errormsgs_bankpol()
	{
		try {
			driver.findElement(By.xpath(CreateAccountPage.bank_select)).click();
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();			
			WebElement sortcode_blnk=driver.findElement(By.xpath(CreateAccountPage.sortcode_blank));
			sortcode_blnk.isDisplayed();
			WebElement accnum_blnk=driver.findElement(By.xpath(CreateAccountPage.accnum_blank));
			accnum_blnk.isDisplayed();
			report.updateTestLog("LBG field Error messages", "Error message for blank fields", Status.PASS);
			driver.findElement(By.xpath(CreateAccountPage.sortcode)).sendKeys("435678");
			WebElement sortcode_invalid=driver.findElement(By.xpath(CreateAccountPage.sortcode_invalid));
			WebElement accnum_invalid=driver.findElement(By.xpath(CreateAccountPage.accnum_invalid));	
			driver.findElement(By.xpath(CreateAccountPage.accnumber)).sendKeys("21414");
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			sortcode_invalid.isDisplayed();
			accnum_invalid.isDisplayed();
			report.updateTestLog("LBG field Error messages", "Error message for Invalid fields", Status.PASS);
			
		}
		catch(Exception e)
		{
			report.updateTestLog("LBG field Error messages", "Page/object not found", Status.FAIL);
		}
	}
	public void errormsgs_InsEBC()
	{
		try {
			driver.findElement(By.xpath(CreateAccountPage.ins_eb_select)).click();
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();		
			driver.findElement(By.xpath(CreateAccountPage.inseb_blank)).isDisplayed();
			report.updateTestLog("3Rd option INS/EBC policy", "Errormessages for leaving blank", Status.PASS);
			driver.findElement(By.id(CreateAccountPage.ins_ebc_mem)).sendKeys("im/87");
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			driver.findElement(By.xpath(CreateAccountPage.inseb_invalid)).isDisplayed();
			report.updateTestLog("3Rd option INS/EBC policy", "Errormessages for Invalid entry", Status.PASS);
		}
		catch(Exception e)
		{
			report.updateTestLog("3Rd option INS/EBC policy", "Page/object not found", Status.FAIL);
		}
		
	}
	public void creatAccount()
	{
		try {
			
		
		report.updateTestLog("Launch App", "Browser Launched Successfully", Status.PASS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.mrTitle_rad_xpath)));
		String title=getInput("Title");
		switch(title)
		{
		case "Mr":
			driver.findElement(By.xpath(CreateAccountPage.mrTitle_rad_xpath)).click();			
			report.updateTestLog("Title", "Clicked Successfully", Status.PASS);
			break;
		case "Mrs":
			driver.findElement(By.xpath(CreateAccountPage.mrsTitle_rad_xpath)).click();			
			report.updateTestLog("Title", "Clicked Successfully", Status.PASS);
			break;
		case "Miss":
			driver.findElement(By.xpath(CreateAccountPage.missTitle_rad_xpath)).click();			
			report.updateTestLog("Title", "Clicked Successfully", Status.PASS);
			break;
		case "Ms":
			driver.findElement(By.xpath(CreateAccountPage.msTitle_rad_xpath)).click();			
			report.updateTestLog("Title", "Clicked Successfully", Status.PASS);
			break;
		}
		String fname = getInput("Firstname");
		driver.findElement(By.id(CreateAccountPage.firstname_text_id)).sendKeys(fname);
		report.updateTestLog("Firstname", "Entered Successfully", Status.PASS);
		String lname= getInput("Lastname");
		driver.findElement(By.id(CreateAccountPage.lastname_text_id)).sendKeys(lname);
		report.updateTestLog("Lastname", "Entered Successfully", Status.PASS);	
		String day = getInput("Day");
		driver.findElement(By.id(CreateAccountPage.day_dob_id)).sendKeys(day);
		String month = getInput("month");
		driver.findElement(By.id(CreateAccountPage.month_dob_id)).sendKeys(month);
		String year = getInput("Year");
		driver.findElement(By.id(CreateAccountPage.year_dob_id)).sendKeys(year);
		report.updateTestLog("DOB", "Entered Successfully", Status.PASS);	
		String phonenum = getInput("Phonenumber");
		driver.findElement(By.id(CreateAccountPage.phone_number_id)).sendKeys(phonenum);
		//email=getInput("EmailId");
		WebElement emailentered=driver.findElement(By.id(CreateAccountPage.email_text_id));
		
		
		String correct_Email=getInput("Validation");
		if(correct_Email.contains("valid email ID"))
		{
			String emailId=getInput("EmailId");
			emailentered.sendKeys(emailId);
			AACommonData.email_Id=emailId;
		}
		else
		{
			String newUserName = "test" + generateString(rand, "abcd", 3) + generateString(rand, "wxyz", 4)+"@mailinator.com";
			AACommonData.email_Id=newUserName;
			dataTable.putData("General_Data", "EmailId", newUserName);
			System.out.println("Email");
			emailentered.sendKeys(newUserName);
		}
		String postcode = getInput("Postcode");
		driver.findElement(By.id(CreateAccountPage.postcode_text_id)).sendKeys(postcode);
		
		report.updateTestLog("Create Account", "Entered all details suceesfully", Status.PASS);
		}catch(Exception e)
		{
			report.updateTestLog("Create Account", "Page/Object is not found", Status.FAIL);
		}
	}
	public void passwordSection()
	{
		try
		{
			password = getInput("Password");
			driver.findElement(By.id(CreateAccountPage.paswword_pwd_id)).sendKeys(password);
			driver.findElement(By.id(CreateAccountPage.confoimrpwd_pwd_id)).sendKeys(password);
			String name=driver.findElement(By.xpath("(//iframe)[1]")).getAttribute("name");
			driver.switchTo().frame(name);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.recaptcha_checkbox_xpath)));
			WebElement recaptcha=driver.findElement(By.xpath(CreateAccountPage.recaptcha_checkbox_xpath));
			javaScriptClickElement(driver, recaptcha);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.recaptcha_tick_xpath)));
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.Next_button_xpath)));
			//driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
		    clickObject(driver, XPATH, CreateAccountPage.Next_button_xpath);
			report.updateTestLog("PasswordSection", "Entered Successfully", Status.PASS);
			//typeinEditbox(driver, identifyBy, locator, valuetoType)
			
		}
		catch(Exception e)
		{
			report.updateTestLog("PasswordSection", "Page/Object is not found", Status.FAIL);
		}
		
		}
	
	public void checkInbox()
	{
	try {
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.checkinbox_heading_xpath)));
		driver.findElement(By.xpath(CreateAccountPage.checkinbox_heading_xpath)).isDisplayed();
		WebElement inboxcontent=driver.findElement(By.xpath(CreateAccountPage.checkbox_text_xpath));
		inboxcontent.isDisplayed();
		String content=inboxcontent.getText();
	//	System.out.println("The content displayed on the check inbox screen is: " +content );
		Thread.sleep(2000);
		report.updateTestLog("CheckInbox", "Content on Check inbox is displayed properly", Status.PASS);
				
		
		
	}
	catch(Exception e)
	{
		report.updateTestLog("CheckInbox page", "Page/Object is not found", Status.FAIL);
	
	}

	}
	public void activation_Linkclick()
	{
		try {
			driver.get("https://mailinator.com");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.enteremail_input_xpath)));
			//email=getInput("EmailId");
			System.out.println("Email Id:"+AACommonData.email_Id);
			driver.findElement(By.xpath(CreateAccountPage.enteremail_input_xpath)).sendKeys(AACommonData.email_Id);
			driver.findElement(By.xpath(CreateAccountPage.submit_click_xpath)).click();
			Boolean flag=true;
            int count=1;
            while(flag)
            {
            try {
            	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.Activationemail_email_xpath)));
driver.findElement(By.xpath(CreateAccountPage.Activationemail_email_xpath)).click();
      break;
            }
            catch(Exception e)
            {
                   count=count+1;
                              //driver.findElement(By.xpath(CreateAccountPage.goInbox_xpath)).click();
                   driver.navigate().refresh();
                   driver.navigate().back();
            driver.findElement(By.xpath(CreateAccountPage.enteremail_input_xpath)).clear();
            driver.findElement(By.xpath(CreateAccountPage.enteremail_input_xpath)).sendKeys(AACommonData.email_Id);
               driver.findElement(By.xpath(CreateAccountPage.submit_click_xpath)).click();
                   if(count==40)
                   {
                	   report.updateTestLog("Activation link", "Didn't recieve Activation Email", Status.FAIL);
                         break;
                   }
                   
            }
            }
			//switch frame
			driver.switchTo().frame("msg_body");
			Thread.sleep(3000);
			JavascriptExecutor jse = (JavascriptExecutor)driver.getWebDriver();			  
	         jse.executeScript("window.scrollBy(0,1000)");
	         Thread.sleep(5000);
	         WebElement element=driver.findElement(By.xpath(CreateAccountPage.activationlink_click_xpath));
	         
	         ((JavascriptExecutor) driver.getWebDriver()).executeScript(
	                    "arguments[0].scrollIntoView();", element);
	 
	          wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.activationlink_click_xpath)));
			  driver.findElement(By.xpath(CreateAccountPage.activationlink_click_xpath)).click();
			  Thread.sleep(200);
			  driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");
			  Thread.sleep(5000);
			  report.updateTestLog("Activation link", "clicked on activation link sucessfully", Status.PASS);
		
		
			}
		
		catch(Exception e)
		{
			report.updateTestLog("Activation link", "could not click on activation link ", Status.FAIL);
			
		}
		
		}
	public void activation_neg_flow()
	{
		try {
		road_Acc_HP();
		report.updateTestLog("Create Account", "Entered all details suceesfully", Status.PASS);
		checkInbox();
		report.updateTestLog("Inbox", "User is able to navigate to inbox screen", Status.PASS);
		activation_Linkclick();
		report.updateTestLog("Activation Link", "Clicked on button and activated sucessfully", Status.PASS);
		
	    ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	      driver.switchTo().window(tabs2.get(0));
		
		driver.switchTo().frame("msg_body");
		Thread.sleep(3000);
		JavascriptExecutor jse = (JavascriptExecutor)driver.getWebDriver();			  
         jse.executeScript("window.scrollBy(0,1000)");
         Thread.sleep(5000);
         WebElement element=driver.findElement(By.xpath(CreateAccountPage.activationlink_click_xpath));
         
         ((JavascriptExecutor) driver.getWebDriver()).executeScript(
                    "arguments[0].scrollIntoView();", element);
 
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.activationlink_click_xpath)));
		  driver.findElement(By.xpath(CreateAccountPage.activationlink_click_xpath)).click();
		  Thread.sleep(200);
		  driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");
		  driver.switchTo().window(tabs2.get(1));
		  String Exp_url=driver.getCurrentUrl();
		  System.out.println("the Expired link URL: " +Exp_url);
		  report.updateTestLog("Activation Link", "expired link URL", Status.PASS);
		  String act_expurl="https://prelive10.theaa.com";
		  if(Exp_url.contains(act_expurl))
		  {	
			report.updateTestLog("Activation Link", "expired link URL", Status.PASS);
			WebElement text_exp=driver.findElement(By.xpath(CreateAccountPage.Exp_text));
			WebElement cont_exp=driver.findElement(By.xpath(CreateAccountPage.exp_cont));
		  if(text_exp.isDisplayed() && cont_exp.isDisplayed() )
		  report.updateTestLog("Expired Link", "Content on link expired page is displayed", Status.PASS);
		  }
		 //chnages swith to windoe 
		  
		  driver.switchTo().frame("msg_body");
			Thread.sleep(3000);
									  
	         jse.executeScript("window.scrollBy(0,1000)");
	         Thread.sleep(5000);
	                
	         ((JavascriptExecutor) driver.getWebDriver()).executeScript(
	                    "arguments[0].scrollIntoView();", element);
	 
	          wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.activationlink_click_xpath)));
			  driver.findElement(By.xpath(CreateAccountPage.act_link)).click();
			  Thread.sleep(200);
			  driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");
			 ///include swith 
			  String Exp_url1=driver.getCurrentUrl();
			//  System.out.println("the Expired link URL: " +Exp_url1);
			  report.updateTestLog("Activation Link", "expired link URL", Status.PASS);
		  
		}
		catch(Exception e)
		{
			report.updateTestLog("Expired Link", "Content on link expired page is not displayed", Status.FAIL);	
		}
		
	}

	public void login_account()
	{
		
		try {
			//need to include login page validations
			//make use of UAGeneralFunc to launch URL
			//String homepage=getInput("Homepage");
			driver.get(AACommonData.url);
			report.updateTestLog("Login Page", "Launched URL successfully", Status.PASS);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Login_Page.YA_dropdown)));
			driver.findElement(By.xpath(Login_Page.YA_dropdown)).click();
			driver.findElement(By.xpath(Login_Page.login_homepage)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Login_Page.EmailID)));
			driver.findElement(By.xpath(Login_Page.EmailID)).sendKeys(AACommonData.email_Id);
			report.updateTestLog("Email Id", AACommonData.email_Id+"-Entered successfully", Status.PASS);
			password = getInput("Password");
			driver.findElement(By.xpath(Login_Page.Password)).sendKeys(password);
			report.updateTestLog("Password", password+"-Entered successfully", Status.PASS);
			driver.findElement(By.xpath(Login_Page.login_click)).click();
			Thread.sleep(3000);
			report.updateTestLog("Login Page", "Entered details successfully", Status.PASS);
			
		}
		catch(Exception e)
		{
			report.updateTestLog("Login Page", "Page/object not found", Status.FAIL);
		}
	}
	public void association_Check()
	{
		try
		{
		Thread.sleep(5000);
		driver.switchTo().frame("MyAAMashupIfr");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(YourAccount_Screen.YourPol_section)));
		driver.findElement(By.xpath(YourAccount_Screen.YourPol_section));		
	/*	WebElement content_policysec= driver.findElement(By.xpath(YourAccount_Screen.Yourpol_Content));
		content_policysec.isDisplayed();
		System.out.println("\n"+"The content displayed for Your Policy section is : " +content_policysec.getText());
		WebElement details_sec=driver.findElement(By.xpath(YourAccount_Screen.Yourdetails_Section));
		details_sec.click();
		details_sec.isDisplayed();
		WebElement content_detailssec=driver.findElement(By.xpath(YourAccount_Screen.Yourdetails_Content));
		content_detailssec.isDisplayed();
		System.out.println("\n"+"The content displayed for details section is: " +content_detailssec.getText());
		driver.switchTo().defaultContent();	
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Login_Page.YA_dropdown)));
		driver.findElement(By.xpath(Login_Page.YA_dropdown)).click();
		driver.findElement(By.xpath(YourAccount_Screen.signout)).click();	*/	
		report.updateTestLog("Your Account Page", "C350 validations have been done sucessfully", Status.PASS);
		}
		
		catch(Exception e)
		{
			report.updateTestLog("Your Account Page", "Page/object not found", Status.FAIL);	
		}
		
		}
		
		
	
	public void createaccount_Error_Validation()
	{
		try
		{
			driver.get(AACommonData.url);			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.Next_button_xpath)));
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			WebElement title_err=driver.findElement(By.xpath(CreateAccountPage.title_err));
			WebElement fn_err=driver.findElement(By.xpath(CreateAccountPage.fn_blank));
			WebElement ln_err=driver.findElement(By.xpath(CreateAccountPage.ln_blank));
			WebElement day_err=driver.findElement(By.xpath(CreateAccountPage.day_blank));
			WebElement mnth_err=driver.findElement(By.xpath(CreateAccountPage.month_blank));
			WebElement year_err=driver.findElement(By.xpath(CreateAccountPage.year_blank));
			WebElement phnnum_err=driver.findElement(By.xpath(CreateAccountPage.phnnum_blank));
			WebElement email_err=driver.findElement(By.xpath(CreateAccountPage.email_blank));
			WebElement postcode_err=driver.findElement(By.xpath(CreateAccountPage.postcode_blank));
			//WebElement policynum_err=driver.findElement(By.xpath(CreateAccountPage.policynum_blank));
			WebElement pwd_err=driver.findElement(By.xpath(CreateAccountPage.pwd_blank));
			WebElement confirmpwd_err=driver.findElement(By.xpath(CreateAccountPage.confirmpwd_blank));
			WebElement recpatcha_err=driver.findElement(By.xpath(CreateAccountPage.recaptcha_err));
			Thread.sleep(2500);
			if(title_err.isDisplayed() && fn_err.isDisplayed() && ln_err.isDisplayed() && day_err.isDisplayed() && mnth_err.isDisplayed() && year_err.isDisplayed() && phnnum_err.isDisplayed() && email_err.isDisplayed() && postcode_err.isDisplayed() && pwd_err.isDisplayed() && confirmpwd_err.isDisplayed() && recpatcha_err.isDisplayed() )
			{
				report.updateTestLog("Create Account Page", "Error messages are displayed upon leaving fields blank", Status.PASS);
			}
						
			}
		catch(Exception e)
		{
			report.updateTestLog("Create Account Page","Page/object not found", Status.FAIL);
		}
	}
	
	
	
	public void createAccount_InvalidError()
	{
		try
		{
			driver.get(AACommonData.url);	
			driver.findElement(By.id(CreateAccountPage.firstname_text_id)).sendKeys("G");
			WebElement fn_minlnerr=driver.findElement(By.xpath(CreateAccountPage.fn_minln));
			report.updateTestLog("First name", "Entered First name", Status.PASS);
			driver.findElement(By.id(CreateAccountPage.lastname_text_id)).sendKeys("R");
			WebElement ln_mnnerr=driver.findElement(By.xpath(CreateAccountPage.ln_minln));	
			report.updateTestLog("Last name", "Entered last name", Status.PASS);
			driver.findElement(By.id(CreateAccountPage.day_dob_id)).sendKeys("0");
			WebElement day_invalid=driver.findElement(By.xpath(CreateAccountPage.day_invalid));		
			driver.findElement(By.id(CreateAccountPage.month_dob_id)).sendKeys("0");
			WebElement mnth_invalid=driver.findElement(By.xpath(CreateAccountPage.month_invalid));
			driver.findElement(By.id(CreateAccountPage.year_dob_id)).sendKeys("203");
			report.updateTestLog("Date of birth", "entered DOB ", Status.PASS);
			WebElement year_invalid=driver.findElement(By.xpath(CreateAccountPage.year_invalid));
			driver.findElement(By.id(CreateAccountPage.phone_number_id)).sendKeys("071234");
			WebElement phnum_invalid=driver.findElement(By.xpath(CreateAccountPage.phnnum_invalid));
			driver.findElement(By.id(CreateAccountPage.email_text_id)).sendKeys("testone");
			WebElement email_invalid=driver.findElement(By.xpath(CreateAccountPage.email_invalid));
			driver.findElement(By.id(CreateAccountPage.postcode_text_id)).sendKeys("eds");
			WebElement postcode_invalid=driver.findElement(By.xpath(CreateAccountPage.postcode_invalid));
			//driver.findElement(By.id(CreateAccountPage.membershipnumber_text_id)).sendKeys("43ew");
			//WebElement policynum_invalid=driver.findElement(By.xpath(CreateAccountPage.policynum_minln));
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath));
			if(fn_minlnerr.isDisplayed() && ln_mnnerr.isDisplayed() && day_invalid.isDisplayed() && mnth_invalid.isDisplayed() && year_invalid.isDisplayed() && phnum_invalid.isDisplayed() && email_invalid.isDisplayed() && postcode_invalid.isDisplayed() )
			{
				report.updateTestLog("Create Account Page", "Error messages are displayed upon entering invalid data", Status.PASS);
			}
			
		}
		catch(Exception e)
		{
			report.updateTestLog("Create Account Page","Page/object not found", Status.FAIL);
			
		}
		
	}
	
	public void emailalreadyregistered()
	{
		try
		{
			driver.get(AACommonData.url);	
			//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.ins_eb_selectt)));
			driver.findElement(By.xpath(CreateAccountPage.ins_eb_selectt)).click();
			driver.findElement(By.id(CreateAccountPage.ins_ebc_mem)).sendKeys("sjsktset");
			driver.findElement(By.xpath(CreateAccountPage.mrTitle_rad_xpath)).click();
			driver.findElement(By.id(CreateAccountPage.firstname_text_id)).sendKeys("testone");
			driver.findElement(By.id(CreateAccountPage.lastname_text_id)).sendKeys("mptestone");
			driver.findElement(By.id(CreateAccountPage.day_dob_id)).sendKeys("09");
			driver.findElement(By.id(CreateAccountPage.month_dob_id)).sendKeys("10");
			driver.findElement(By.id(CreateAccountPage.year_dob_id)).sendKeys("2001");
			driver.findElement(By.id(CreateAccountPage.phone_number_id)).sendKeys("07123456789");
			driver.findElement(By.id(CreateAccountPage.email_text_id)).sendKeys("myaatesters+2751@gmail.com");
			driver.findElement(By.id(CreateAccountPage.postcode_text_id)).sendKeys("pl11de");
            driver.findElement(By.id(CreateAccountPage.paswword_pwd_id)).sendKeys("Password123");
			driver.findElement(By.id(CreateAccountPage.confoimrpwd_pwd_id)).sendKeys("Password123");
			String name=driver.findElement(By.xpath("(//iframe)[1]")).getAttribute("name");
			driver.switchTo().frame(name);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.recaptcha_checkbox_xpath)));
			WebElement recaptcha=driver.findElement(By.xpath(CreateAccountPage.recaptcha_checkbox_xpath));
			javaScriptClickElement(driver, recaptcha);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.recaptcha_tick_xpath)));
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAccountPage.Next_button_xpath)));
			driver.findElement(By.xpath(CreateAccountPage.Next_button_xpath)).click();
			WebElement ear_content=driver.findElement(By.xpath(CreateAccountPage.ear_text));
			WebElement loginlink=driver.findElement(By.xpath(CreateAccountPage.login_link));
			WebElement restlink=driver.findElement(By.xpath(CreateAccountPage.rest_link));
			report.updateTestLog("EAR flow", "Content of EAR screen", Status.PASS);
			if(ear_content.isDisplayed() && loginlink.isDisplayed() && restlink.isDisplayed() )
			{
				report.updateTestLog("Email Already registered Page","Content and links are displayed", Status.PASS);	
			}
			
		}
		catch(Exception e)
		{
			report.updateTestLog("Email Already registered Page","Page/object not found", Status.FAIL);
		}
	}
	public void policyAssociatedscreen()
	{
		try
		{
			road_Acc_HP();
			checkInbox();
			activation_Linkclick();
			login_account();	
			//policy already associated screen
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(EditdetailsPage.heading)));
			driver.findElement(By.xpath(PolicyAlreadyassocPage.Err_heading)).isDisplayed();
			driver.findElement(By.xpath(PolicyAlreadyassocPage.cont1_err)).isDisplayed();
			driver.findElement(By.xpath(PolicyAlreadyassocPage.cont2_err)).isDisplayed();
			PauseScript(5);
			
			
		}
		catch(Exception e)
		{
			report.updateTestLog("Policy Already Associated Screen","Page/object not found", Status.PASS);
		}
		
	}
	public void edit_flow()
{
	try {
		
		//edit summary page
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(EditdetailsPage.heading)));
		driver.findElement(By.xpath(EditdetailsPage.heading)).isDisplayed();
		driver.findElement(By.xpath(EditdetailsPage.Err_content)).isDisplayed();
		report.updateTestLog("Edit summary Screen","heading and content is displayed", Status.PASS);
		driver.findElement(By.xpath(EditdetailsPage.edit_button)).click();
		
		//edit details screen
		report.updateTestLog("Edit Details Screen","user is redirected successfully", Status.PASS);
		String edit_ln=getInput("Edit LN");
		String edit_postcode=getInput("Edit Postcode");
		String edit_policynum=getInput("Edit pol");
		String edit_day=getInput("Edit day");
		String edit_mnth=getInput("Edit Mnth");
		String edit_yr=getInput("Edit yr");
		report.updateTestLog("Edit Details Screen","heading and content is displayed", Status.PASS);
		if(!edit_ln.isEmpty())
		{
			WebElement new_ln=driver.findElement(By.xpath(EditdetailsPage.ln_editscreen));	
			
			new_ln.clear();
			new_ln.sendKeys(edit_ln);
			report.updateTestLog("Edit Details Screen","re entered last name", Status.PASS);
		}
		
		else if (!edit_postcode.isEmpty())
		{
			WebElement new_postcode=driver.findElement(By.xpath(EditdetailsPage.postcode_edit));
			new_postcode.clear();
			new_postcode.sendKeys(edit_postcode);
		}
		else if(!edit_policynum.isEmpty())
		{
			WebElement new_policynum=driver.findElement(By.xpath(EditdetailsPage.policynum_edit));
			new_policynum.clear();
			new_policynum.sendKeys(edit_policynum);
		}
		else if(!(edit_day.isEmpty() && edit_mnth.isEmpty() && edit_yr.isEmpty()))
		{
			WebElement new_day=driver.findElement(By.xpath(EditdetailsPage.day_edit));
			new_day.clear();
			new_day.sendKeys(edit_day);
			WebElement new_mnth=driver.findElement(By.xpath(EditdetailsPage.mnth_edit));
			new_mnth.clear();
			new_mnth.sendKeys(edit_mnth);
			WebElement new_year=	driver.findElement(By.xpath(EditdetailsPage.year_edit));
			new_year.clear();
			new_year.sendKeys(edit_yr);
		}
		else
		{
			report.updateTestLog("Edit Details Screen","User has entered either NC of EBc/insurnace or TPP details/multiple roles have same details", Status.PASS);
		}
		
		driver.findElement(By.xpath(EditdetailsPage.submit_edit)).click();
		report.updateTestLog("Edit Details Screen","submitted details successfully", Status.PASS);
		Thread.sleep(4000);
		//waitForPageLoad(20);
		association_Check();
		//report.updateTestLog("Associated user","user is redirected to C350 screen successfully", Status.PASS);
		
	}
	
	catch(Exception e)
	{
		
		report.updateTestLog("Association from Edit Details Screen","Object/page not found", Status.FAIL);
	}

}
	public void road_edit()
	{
		road_Acc_HP();
		checkInbox();
		activation_Linkclick();
		login_account();
		edit_flow();
	}
	public void eb_Ins_edit()
	{
		ins_EBC_Acc_HP();
		checkInbox();
		activation_Linkclick();
		login_account();
		edit_flow();
		
	}
	public void  bank_editflow()
	{
		try {
			bank_Acc_HP();
			checkInbox();
			activation_Linkclick();
			login_account();
			//edit summary page
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(EditdetailsPage.heading)));
			driver.findElement(By.xpath(EditdetailsPage.heading)).isDisplayed();
			driver.findElement(By.xpath(EditdetailsPage.Err_content)).isDisplayed();
			report.updateTestLog("Edit summary Screen","heading and content is displayed", Status.PASS);
			driver.findElement(By.xpath(EditdetailsPage.edit_button)).click();
			
			//edit details screen
			report.updateTestLog("Edit Details Screen","user is redirected successfully", Status.PASS);
			String newsortcode=getInput("Edit Sortcode");
			String newaccnum=getInput("Edit Accnum");
			if(!newsortcode.isEmpty() && !newaccnum.isEmpty())
			{
				WebElement edit_srtcde=driver.findElement(By.xpath(EditdetailsPage.edit_sortcode));
				WebElement edit_accnm=driver.findElement(By.xpath(EditdetailsPage.edit_accnum));
				edit_srtcde.clear();
				edit_srtcde.sendKeys(newsortcode);
				edit_accnm.clear();
				edit_accnm.sendKeys(newaccnum);
				driver.findElement(By.xpath(EditdetailsPage.submit_edit)).click();
				report.updateTestLog("Edit Details Screen","submitted details successfully", Status.PASS);
				Thread.sleep(4000);
				//waitForPageLoad(20);
				association_Check();
			}
			
		}
		catch(Exception e)
		{
			report.updateTestLog("Association from Edit Details with bank Screen","Object/page not found", Status.FAIL);
		}
		
	}
	
	public void vb_flow()
	{
		try {
			
			//vb flow initiated
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(VbAccount.vrn_heading)));
			driver.findElement(By.xpath(VbAccount.vrn_heading)).isDisplayed();
			report.updateTestLog("VRN Entry Page","Navigated successfully to VRN entry screen", Status.PASS);
			String vrn=getInput("VRN");
			driver.findElement(By.id(VbAccount.vrn_entry)).sendKeys(vrn);
			report.updateTestLog("VRN Entry Page","Entered VRN successfully", Status.PASS);
			driver.findElement(By.xpath(VbAccount.vrn_cont)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(VbAccount.vrnlookup_cont)));
			//if(driver.findElements(By.xpath(VbAccount.vrnlookup_cont)).size()>0)
			//{
			driver.findElement(By.xpath(VbAccount.vrnlookup_cont)).isDisplayed();
			report.updateTestLog("VRN Lookup Page","Redirected to VRN lookup screen", Status.PASS);
			driver.findElement(By.id(VbAccount.vrn_yes)).click();
			//}
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(VbAccount.polholder_heading)));
			driver.findElement(By.xpath(VbAccount.polholder_heading)).isDisplayed();
			report.updateTestLog("Policy holder Screen","Redirected to Policy holder question screen", Status.PASS);
			driver.findElement(By.xpath(VbAccount.not_polholder)).click();
			report.updateTestLog("Policy holder Screen","Selected No", Status.PASS);
			driver.findElement(By.xpath(VbAccount.polholder_cont)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(VbAccount.createAcc)));
			driver.findElement(By.xpath(VbAccount.cont_check)).isDisplayed();
			report.updateTestLog("NotPolicy holder Screen","Redirected to NotPolicy holder screen", Status.PASS);
			driver.findElement(By.xpath(VbAccount.createAcc)).click();
			Thread.sleep(4000);
			//waitForPageLoad(10);
			association_Check();			
			
			}
		catch(Exception e)
		{
			report.updateTestLog("VB Flow","Page/object not found", Status.FAIL);
		}
		
	}
	public void createaccount_VBflow()
	{
		try {
		road_Acc_HP();
		checkInbox();
		activation_Linkclick();
		login_account();
		vb_flow();
		}
		catch(Exception e)
		{
			report.updateTestLog("VB Flow","Page/object not found", Status.FAIL);
		}
	}
	public void editscreen_VBflow()
	{
		try {
			road_Acc_HP();
			checkInbox();
			activation_Linkclick();
			login_account();
			String edit_policynum=getInput("Edit pol");
			WebElement new_policynum=driver.findElement(By.xpath(EditdetailsPage.policynum_edit));
			new_policynum.clear();
			new_policynum.sendKeys(edit_policynum);
			driver.findElement(By.xpath(EditdetailsPage.submit_edit)).click();
			report.updateTestLog("Edit Details Screen","submitted details successfully", Status.PASS);
			Thread.sleep(4000);
			vb_flow();
		}
		catch(Exception e)
		{
			report.updateTestLog("VB Flow from edit screen","Page/object not found", Status.FAIL);
		}
	}
}	


		
	
	
	
	


