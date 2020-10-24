package com.theaa.dip.automation.ua.businesscomponents;

import java.awt.Toolkit;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.theaa.dip.automation.ua.craft.ReusableLibrary;
import com.theaa.dip.automation.ua.craft.ScriptHelper;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.pages.PegaPage;





public class PegaHomePage extends ReusableLibrary {

	public PegaHomePage(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	WebDriverWait wait = new WebDriverWait(driver, 10);
	
	public void openApp() {
		try {
			
			// LOGIC
			waitForPageLoad(90);
			String strURL = getInput("URL");
			System.out.println(strURL);
			navigatetoWebpage(driver, strURL);
			waitForPageLoad(90);

			waitOnPage(driver);
			if (driver.findElements(By.id("overridelink")).size() > 0) {
				driver.navigate().to("javascript:document.getElementById('overridelink').click()");
				waitOnPage(driver, 4);
			}
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenResolution = new Dimension((int) toolkit.getScreenSize().getWidth(),
					(int) toolkit.getScreenSize().getHeight());

			driver.manage().window().setSize(screenResolution);
			if (driver.findElements(By.id("txtUserID")).size() > 0) {
				typeinEditbox(driver, ID, "txtUserID", getInput("Username").trim());
				typeinEditbox(driver, ID, "txtPassword", getInput("Pswd").trim());
				clickObject(driver, ID, "sub");
				waitForPageLoad(90);
				waitOnPage(driver, 4);
			}

			// REPORTING
			report.updateTestLog("Open Application", "App URL launched in Browser as expected" + strURL, Status.DONE);
			System.out.println("title :" + driver.getTitle());
			if (driver.getTitle().equalsIgnoreCase("CPMInteractionPortal")) {
				report.updateTestLog("HomePage", "Home page displayed as expected", Status.PASS);
			} else {
				report.updateTestLog("HomePage", "Home page NOT displayed as expected", Status.FAIL);
			}
		} catch (Exception e) {
			report.updateTestLog("HomePage", "Home page NOT displayed as expected" + e.getMessage(),
					Status.FAIL);
		}

	}
	
	public void openPhoneCallInbound() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PegaPage.createNewInteraction_btn_xpath)));
            clickObject(driver, XPATH, PegaPage.createNewInteraction_btn_xpath);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PegaPage.phoneCallInboundoverlay_btn_css)));
			clickObject(driver, CSS, PegaPage.phoneCallInboundoverlay_btn_css);
			clickObject(driver, XPATH, PegaPage.phoneCallInbound_btn_xpath);
			//waitForPageLoad(10);
			report.updateTestLog("Home page", "Phone Call Inbound clicked", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("HomePage", "Home page is NOT displayed as expected" + e.getMessage(), Status.FAIL);
		}
	}

	 public void searchExistingPolicy() {
	        try {
	              
	                    // AACoreData.policyNumber = dh.getParamChanges(getInput("TC_ID"));
	                    AACoreData.policyNumber = getInput("Existing Policy Num");
	                    // Definition
	                    switchToFrame("PegaGadget1Ifr");
	                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PegaPage.policyNum_txt_id)));
	                    clickObject(driver, ID, PegaPage.policyNum_txt_id);
	                    typeinEditbox(driver, ID, PegaPage.policyNum_txt_id, AACoreData.policyNumber);
	                    clickObject(driver, XPATH, PegaPage.searchTypeOfCall_lbl_xpath);
	                    javaScriptClickObject(driver, XPATH, PegaPage.search_btn_xpath);

	                    checkPresence(5, driver, XPATH, PegaPage.selectmyPolicy_btn_xpath);
	                    for (int z = 1; z <= 20; z++) {
	                          boolean available = checkPresence(1, driver, XPATH, PegaPage.selectmyPolicy_btn_xpath);
	                          if (available) {
	                                break;
	                          } else {
	                                javaScriptClickObject(driver, XPATH, PegaPage.search_btn_xpath);
	                                waitOnPage(driver);
	                          }
	                    }
                        getcallerRoleDetails();
                        waitForPageLoad(60);
                        List<WebElement> policyTable = driver.findElements(By.xpath(
                                "//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr"));
                          int tableSize = policyTable.size();
                          System.out.println("Table size is:" + tableSize);

                          int j = 1;
                          for (int i = 2; i <= (tableSize - 1) * 2;) {
                                javaScriptClickObject(driver, XPATH,
                                      "//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
                                                        + i + "]/td[1]//span[contains(@title,'press enter to expand row')]");
                                waitOnPage(driver, 5);
                                String role = driver.findElement(By.xpath(
                                            "(//table[@class='gridTable repeatReadOnly']//table[@class='gridTable ']//tr[2]//td[3])["
                                                        + j + "]"))
                                            .getText();
                                System.out.println("role = " + role);
                                if (role.contains("Policy Holder")) {
                                      javaScriptClickObject(driver, XPATH,
                                                  "(//div[@class='pzbtn-rgt']//div[contains(text(), 'Select')])[" + j + "]");
                                      report.updateTestLog("HomePage", "Selected Policy Holder", Status.PASS);
                                      break;
                                }
                                i = i + 2;
                                ++j;
                          }

                          // IDV Screen
                          javaScriptClickObject(driver, ID, PegaPage.FirstNameVerified_txt_id);
                          javaScriptClickObject(driver, ID, PegaPage.DateOfBirthVerified_txt_id);
                          javaScriptClickObject(driver, XPATH, PegaPage.Submit_btn_xpath);
                          waitOnPage(driver, 5);
                          waitForPageLoad(60);

	              
	        } catch (Exception e) {
	              report.updateTestLog("HomePage", "Home page is NOT displayed as expected" + e.getMessage(), Status.FAIL);
	        }
	  }
	
		public void getcallerRoleDetails()
		{
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr")));
				List<WebElement> policyTable = driver.findElements(By.xpath(
						"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr"));
				int tableSize = policyTable.size();
				
				int j = 1;
	            for (int i = 2; i <= (tableSize - 1) * 2;) {
	            	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr[2]/td[1]//span[contains(@title,'press enter to expand row')]")));
	              
	            	javaScriptClickObject(driver, XPATH,
	                      "//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
	                                        + i + "]/td[1]//span[contains(@title,'press enter to expand row')]");
	            	
	            	waitOnPage(driver, 5);
	            	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='gridTable repeatReadOnly']//table[@class='gridTable ']//tr[2]")));
	            	String role = driver.findElement(By
	                            .xpath("(//table[@class='gridTable repeatReadOnly']//table[@class='gridTable ']//tr[2]//td[3])["
	                                        + j + "]"))
	                            .getText();
	                
	                javaScriptClickObject(driver, XPATH,
	                      "//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr/td[1]//span[contains(@title,'press enter to collapse row')]");
	                
	                waitOnPage(driver, 5);
				if (role.contains("Policy Holder")) {
					AACoreData.phTitle = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[3]"))
							.getText();
					AACoreData.phFirstName = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[4]"))
							.getText();

					AACoreData.phLastName = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[5]"))
							.getText();
					AACoreData.phDOB = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[6]"))
							.getText();
					AACoreData.phPostCode = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[7]"))
							.getText();
					AACoreData.phPhoneNumber = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[9]"))
							.getText();
					AACoreData.phEmailId = driver.findElement(By.xpath(
							"//input[@id='D_ContactListPpxResults1colWidthGBR']/parent::td//table[@id='bodyTbl_right']/tbody/tr["
									+ i + "]/td[10]"))
							.getText();
					report.updateTestLog("HomePage", "The Policy having Policy Holder Contact : Storing the Values",
							Status.PASS);

				}

	                
	                i = i + 2;
	                ++j;
	          }


			} catch (Exception e) {
				report.updateTestLog("HomePage", "Home page is NOT displayed as expected" + e.getMessage(), Status.FAIL);
			}
		}
		
		public void selectCSIPage() {
			try {
				
				String policyNo = getInput("Existing Policy Num").trim();

				
					driver.switchTo().defaultContent();
					switchToFrame("PegaGadget1Ifr");
					waitOnPage(driver, 5);
					boolean available = checkPresence(10, driver, XPATH, PegaPage.skip_selection_btn_xpath);
					if (available) {
						int tablesize = driver.findElements(By.xpath(PegaPage.policyTable_lnk_xpath)).size();
						System.out.println("size:" + tablesize);
						for (int i = 1; i <= tablesize; i++) {
							String actualPolicyNum = driver
									.findElement(By.xpath(PegaPage.policyTable_lnk_xpath + "[" + i + "]")).getText();
							System.out.println("Policy Number:" + actualPolicyNum);
							if (actualPolicyNum.contains(policyNo)) {
								String policyStatus = driver
										.findElement(By.xpath(PegaPage.policyStatus_val_xpath + "[" + i + "]")).getText();
								waitOnPage(driver, 5);
								String tier = driver.findElement(By.xpath(PegaPage.Tier_val_xpath + "[" + i + "]"))
										.getText();
								System.out.println("Policy Status:" + policyStatus);
								System.out.println("Tier:" + tier);
								checkPresence(45, driver, XPATH, PegaPage.select_btn_xpath);
                                waitOnPage(driver, 5);
								javaScriptClickObject(driver, XPATH, PegaPage.select_btn_xpath);
								report.updateTestLog("CSP Page", "Clicked Select Button",Status.PASS);
								waitOnPage(driver, 5);
								break;

							}
						}
					}
				
			} catch (Exception e) {
				report.updateTestLog("CustomerPre360", "CSA Page is NOT displayed as expected" + e.getMessage(),
						Status.FAIL);
			}

		}
	

}
