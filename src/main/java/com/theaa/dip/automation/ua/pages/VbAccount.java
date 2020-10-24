package com.theaa.dip.automation.ua.pages;

public class VbAccount {

	//vrn entry page:
	
	public static String vrn_heading="//input[@id='hiddenPolicyNumber']/preceding-sibling::h2";
	public static String vrn_entry="txtVehicleNumber";
	public static String vrn_cont="//div[@class='form-field narrow cf']//input[@type='button' and @value='Continue'][1]";
	//div[@class='form-field narrow cf']/..//input[@class='full-submit primary button ra']
	//vrn lookup confirmation
	public static String vrnlookup_cont="//input[@id='hiddenVehicleNumber']/..//h2";
	public static String vrn_yes="vehicleDetailsYes";
	public static String vrn_no="vehicleDetailsNo";
	
	//policy holder page
	public static String polholder_heading="(//form[@id='vrn-check'])[4]/..//h2";
	public static String not_polholder="//input[@id='haveMembershipNo']";
	public static String polholder_cont="//div[@id='dvHaveMembership']//input[@value='Continue']";
	
	//not policy holder screen
	public static String cont_check="//input[@id='btnVbAssociation']/../..//h2[contains(text(),'Not a policyholder')]";
	public static String createAcc="//input[@id='btnVbAssociation']";
}
