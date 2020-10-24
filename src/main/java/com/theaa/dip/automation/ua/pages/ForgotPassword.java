package com.theaa.dip.automation.ua.pages;

public class ForgotPassword {
	
	public static String emailaddress_inp_name="mail";
	public static String sendBtn_id="submit_email";
	public static String backtoLoginLnk_xpath="//a[text()='Back to login']";
	public static String emailsent_xpath="//p[@id='email_sent']";
	public static String invalidEmail_xpath="//p[@id='invalid_account']";
	public static String emailTextBox_xpath="//input[@id='addOverlay']";
	public static String goBtn_xpath="//button[@id='go-to-public']";
	public static String goInbox_xpath="//button[@id='go_inbox']";
	public static String emailsub_xpath="(//div[@id='inboxpane']//table//tbody//tr//td)[4]//a";
	public static String emailsub2_xpath="(//div[@id='inboxpane']//table//tbody//tr[2]//td)[4]//a";
	public static String confirmEmail_xpath="//a[contains(text(),'Confirm')]";
	public static String setNewPassword_xpath="//img[@alt='Set new password']/parent::a";
	public static String invalidsetNewPassword_xpath="//p[@id='invalid_token']";
	
	public static String confirmEmail_name="confirmPasswordField";
	public static String password_name="passwordField";
	public static String sendPassword_xpath="//button[@id='submit_password']";
	public static String resetSuccess_xpath="//p[@id='reset_success_advise']";
	
	public static String PasswordErrMSg_xpath="//input[@name='passwordField']/parent::div/following-sibling::div//ul";
	public static String confirmPassErrMsg_xpath="//small[@id='password_mismatch']";
	
	public static String email_txt_id="idToken1";
	public static String password_txt_id="idToken2";
	public static String loginBtn_id="loginButton_0";
	public static String editBtn_id="btnEdit";
	
	public static String yourpolicies_xpath="//label[contains(text(),'Your policies')]";
	
	//Change Password
	public static String oldPassword_id="old-password";
	public static String newPassword_id="new-password";
	public static String confirmNewPassword_id="confirm-new-password";
	
	public static String setBtn_xpath="//div[@class='strength-meter']/following-sibling::div//input";
	public static String oldPasswordErrMsg_xpath="//div[@class='form-field cf old-password-group error']";
	public static String newPasswordErrMsg_xpath="//div[@class='form-field cf new-password-group error']";
	public static String confirmPasswordErrMsg_xpath="//div[@class='form-field cf confirm-new-password-group error']";
	public static String wrongOldPasswordErrMsg_xpath="(//div[@class='strength-meter']/following-sibling::div)[2]//div[@class='cf error-message service-error-message']";
	public static String wrongnewPasswordErrMsg_xpath="//label[contains(text(),'New password')]/../following-sibling::div//div[contains(text(),'Your password should contain at least one capital letter')]";
	public static String wrongNewPasswordErrMsg1_xpath="//label[contains(text(),'New password')]/../following-sibling::div//div[contains(text(),'Your old and new passwords are the same')]";
	public static String confirmPasswordnotMatchErrMsg_xpath="//label[contains(text(),'Confirm new password')]/../following-sibling::div//div[@data-validation='confirmStrict']";
	public static String passwordStrength_xpath="//div[@class='score-meter']";
	
	public static String old_email_id="old-email-address";
	public static String new_email_id="new-email-address";
	public static String unamesetBtn_xpath="(//input[@value='Set'])[1]";
	public static String oldEmailErrMsg_xpath="//div[@class='form-field cf old-email-address-group error']";
	public static String newEmailErrMsg_xpath="//div[@class='form-field cf new-email-address-group error']";
	public static String emailAlreadyExists_xpath="//div[contains(text(),'The email address already exists')]";
	public static String usernameSuccess_xpath="//p[@id='change_success']";
	
	
	
	
	
	
	
	
	
	
	

}
