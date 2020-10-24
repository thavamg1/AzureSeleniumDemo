package com.theaa.dip.automation.ua.pages;

public class EditdetailsPage {
	
	public static String heading="(//h2[text()='We couldn’t find your details'])[2]";
	public static String Err_content="(//p[contains(text(),'information below and edit anything')])[2]";
	public static String edit_button="//button[@id='btnEdit']";
	
	
	//fields on EDIt details
	public static String fn_editscreen="//input[@id='firstName1']";
	public static String ln_editscreen="//input[@id='lastName1']";
	public static String day_edit="//input[@id='number-day1']";
	public static String mnth_edit="//input[@id='number-month1']";
	public static String year_edit="//input[@id='number-year1']";
	public static String phnnum_edit="//input[@id='Telephone1']";
	public static String postcode_edit="//input[@id='HomePostcode1']";
	public static String policynum_edit="//input[@id='membership1']";
	public static String submit_edit="//input[@value='Submit']";
	
	//bank fields edut screen
	public static String edit_sortcode="(//label[text()='Sort code']//following::input[@id='sortcode']/.)[2]";
	public static String edit_accnum="//label[text()='Account number']//following::input[@id='accountnumber'][2]";

}
