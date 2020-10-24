package com.theaa.dip.automation.ua.pages;

public class CreateAccountPage {
//registration fields
	public static String mrTitle_rad_xpath="//label[@for='Mr']";
	public static String mrsTitle_rad_xpath="//label[@for='Mrs']";
	public static String missTitle_rad_xpath="//label[@for='Miss']";
	public static String msTitle_rad_xpath="//label[@for='Ms']";
	public static String firstname_text_id ="firstName";
	public static String lastname_text_id ="lastName";
	public static String day_dob_id ="number-day";
	public static String month_dob_id ="number-month";
	public static String year_dob_id ="number-year";
	public static String phone_number_id ="Telephone";
	public static String email_text_id = "Email";
	public static String postcode_text_id ="HomePostcode";
	
	//policy number
	public static String bank_select="//label[@for='membershipOptions2']";
	public static String ins_eb_select="//label[@for='membershipOptions3']";
	public static String ins_eb_selectt="//input[@id='membershipOptions3']";
	public static String road_select ="//label[text()='I have an AA membership card']";	
	public static String road_mem="membership";
	public static String sortcode="//input[@id='sortcode']";
	public static String accnumber="//input[@id='accountnumber']";
	public static String ins_ebc_mem="insurancepolicy";	
	
	//policy number error messages
	public static String roadmem_blank="//div[contains(text(),'Enter your membership number.')]";
	public static String roadmem_minln="//div[contains(text(),'Check your membership number.')][1]";
	public static String roadmem_checksumerr="//div[contains(text(),'Check your membership number.')][2]";
	public static String sortcode_blank="//div[text()='Enter your sort code.']";
	public static String sortcode_invalid="//div[text()='Check your sort code.'][2]";
	public static String accnum_blank="//div[text()='Enter your account number.']";
	public static String accnum_invalid="//div[text()='Check your account number.']";
	public static String inseb_blank="//div[text()='Enter your policy number.']";
	public static String inseb_invalid="//div[text()='Check your policy number.']";
	
	//password section
	public static String paswword_pwd_id ="password1";
	public static String confoimrpwd_pwd_id ="password2";
	public static String recaptcha_checkbox_xpath="//div[@class='recaptcha-checkbox-checkmark']";
	public static String recaptcha_tick_xpath="//span[@aria-checked='true']";
	public static String Next_button_xpath= "//input[@type='button' and @value='Next']";
	//cookies prefernece
	public static String cookie_info="//button[@id='truste-consent-button']";
	//check inbox
	public static String checkinbox_heading_xpath= "//h2[text()='Check your inbox']";
	public static String emailcheck_compare_xpath="//a[@class='userEmail']";
	public static String checkbox_text_xpath="(//div[@class='check-inbox']//.//p[text()])[1]";
	//mailinator
	public static String enteremail_input_xpath="//input[@id='addOverlay']";
	public static String submit_click_xpath="//button[@id='go-to-public']";
	public static String Activationemail_email_xpath="(//a[@class='ng-binding'])[1]";
	public static String activationlink_click_xpath="//a[contains(text(),'Confirm')]";
	//link click
	public static String act_link="//a[contains(text(),'prpauth.theaa.com')]";
	
	//activation expired link
	public static String Exp_text="//p[text()='Your link has expired']";

	public static String exp_cont="//p[contains(text(),'activation link has expired.')]";
	
	
	//create account screen Error valiations:
	public static String title_err= "//div[text()='Choose a title.']";
	public static String fn_blank="//div[text()='Enter your first name.']";
	public static String fn_minln="//div[text()='Check your first name.']";
	public static String ln_blank="//div[text()='Enter your last name.']";
	public static String ln_minln="//div[text()='Check your last name.']";
	public static String day_blank="//div[text()='Enter the date']";
	public static String month_blank="//div[text()='Enter the month']";
	public static String year_blank= "//div[text()='Enter the year']";
	public static String day_invalid="//div[text()='Check your date of birth.'][1]";
	public static String month_invalid="//div[text()='Check your date of birth.'][2]";
	public static String year_invalid="//div[text()='Check your date of birth.'][3]";
	public static String phnnum_blank="//div[text()='Enter your phone number.']";
	public static String phnnum_invalid="//div[text()='Check your phone number.']";
	public static String email_blank="//div[text()='Enter your email address.']";
	public static String email_invalid="//div[text()='Check your email address.'][2]";
	public static String postcode_blank="//div[text()='Enter your home postcode.']";
	public static String postcode_invalid="//div[text()='Check your home postcode.'][1]";
	
	
	//password error msgs
	public static String pwd_blank="//div[text()='Enter a password.']";
	public static String pwd_minln="//div[@data-validation='isPassCriteriaAllWithUnique']";
	public static String confirmpwd_blank="//div[text()='Confirm your password.']";
	public static String confirmpwd_minln="//div[@data-validation='isPassCriteriaAllWithUniqueConfirm']";
	public static String recaptcha_err="//div[text()='Tick this box to continue.']";
	
	//email already registered pages
	public static String ear_text="(//div[@class='check-inbox'])[3]";
	public static String login_link="//a[text()='Login']";
	public static String rest_link="//a[text()='request a password reset']";
	
	
}
