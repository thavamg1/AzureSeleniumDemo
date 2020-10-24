package com.theaa.dip.automation.ua.pages;

public class PegaPage {
	public static String createNewInteraction_btn_xpath="//*[@id='RULE_KEY']/div[1]/div/div/div[1]/div/div/div/div/div[2]/div/div/span/a/img";
	public static String createNewInteraction_btn_xpath2="//a[contains(text(), ' New')]";
	public static String phoneCallInboundoverlay_btn_css="span.menu-item-title";
	public static String phoneCallInbound_btn_xpath="(//button[@type='button'])[6]";
	public static String policyNum_txt_id="IDReferenceNo";
	public static String searchTypeOfCall_lbl_xpath="//label[contains(text(),'Type Of Call:')]";
	public static String search_btn_xpath="//div[@class='field-item dataValueWrite']//div[contains(text(), 'Search')]";
	public static String selectmyPolicy_btn_xpath="(//div[@class='pzbtn-rgt']//div[contains(text(), 'Select')])";
	public static String FirstNameVerified_txt_id="pyWorkPagepyWorkPartyCONTIsFirstNameVerified"; // IsFirstNameVerified
	public static String DateOfBirthVerified_txt_id="pyWorkPagepyWorkPartyCONTIsDateOfBirthVerified"; // IsDateOfBirthVerified
	public static String Submit_btn_xpath="//div[contains(text(), 'Submit')]/parent::div/parent::div/parent::div";
	public static String skip_selection_btn_xpath="//div[text()='Skip selection']/parent::div/parent::div/parent::div";
	public static String policyTable_lnk_xpath="(//span[starts-with(@id,'headerlabel')])";
	public static String policyStatus_val_xpath="(//label[contains(text(),'Policy status:')]/following-sibling::div)";
	public static String Tier_val_xpath="(//div[text()='Tier:']/parent::div/parent::div/following-sibling::div//b)";
	public static String select_btn_xpath="//span/a[contains(text(),'Select')]";
}
