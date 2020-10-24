package com.theaa.dip.automation.ua.craft;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.framework.selenium.CraftDriver;

public class MyReusableLibrary {
	
	// #############################################################################
		// Function Name : typeinEditbox
		// Description : Function to type in text box
		// Input Parameters : driver, identifier, locator and value to be typed
		// Return Value : None
		// Author : Cognizant
		// Date Created : 05/16/2012
		// #############################################################################
//		public static boolean typeinEditbox(WebDriver driver, String identifyBy,
//				String locator, String valuetoType) {
//			
//			boolean isPresent = false;	
//			checkPresence(5, driver, identifyBy, locator);
//			if (isElementPresent(driver, identifyBy, locator)) {
//				isPresent=true;
//				WebElement element=getWebElement(driver, identifyBy, locator);
//				element.clear();
////				JavascriptExecutor executor = (JavascriptExecutor)driver;
////				executor.executeScript("arguments[0].click();", element);
//				element.click();
//				element.sendKeys(valuetoType);
//			}		
//			return isPresent;
//			}
		
		public static boolean checkPresence(int waitTime, CraftDriver driver, String identifyBy,
				String locator) {
			int timeout = waitTime;
			boolean isPresent = false;
			List<WebElement> elements=getWebElements(driver, identifyBy, locator);
			try {
				int x = 0;
				do {
					
					if (elements.size()!=0 && isEnabled(driver, identifyBy, locator) && isElementPresent(driver, identifyBy, locator)) {
						driver.getCurrentUrl();
					
		               	  Thread t = Thread.currentThread();
		               	 
	              	  if(t.isAlive()) {
						isPresent = true;
	              	  }
					} else {

						x = x + 1;
						isPresent = false;
					}
				} while (x < timeout && isPresent == false);

			} catch (Exception e) {

			}
			return isPresent;

		}

		//#############################################################################
		//Function Name : isEnabled
		//Description : Function to check is element is enabled
		//Input Parameters : driver, identifier, locator
		//Return Value : boolean
		//Author : Arun M
		//Date Created : 31-Jul-2014
		//#############################################################################
		public static boolean isEnabled(CraftDriver  driver, String identifyBy,
				String locator) {
			int timeout = 10;
			boolean isPresent = false;
			WebElement element=getWebElement(driver, identifyBy, locator);
			try {
				int x = 0;
				do {
					if (element.isEnabled()) {
						isPresent = true;
					} else {
						waitOnPage(driver);
						x = x + 1;
						isPresent = false;
					}
				} while (x < timeout && isPresent == false);

			} catch (Exception e) {

			}
			return isPresent;

		}
		// #############################################################################
		// Function Name : isElementPresent
		// Description : Function to validate the existence of an element
		// Input Parameters : driver, identifier, locator
		// Return Value : None
		// Author : Cognizant
		// Date Created : 05/16/2012
		// #############################################################################
		public static boolean isElementPresent(CraftDriver driver, String identifyBy,
				String locator) {
			int timeout =30;
			boolean isPresent = false;
			WebElement element=getWebElement(driver, identifyBy, locator);
			try {
				int x = 0;
				do {
					if (element.isDisplayed()) {
						isPresent = true;
					} else {
						x = x + 1;
						isPresent = false;
					}
				} while (x < timeout && isPresent == false);

			} catch (Exception e) {

			}
			return isPresent;

		}
		// #############################################################################
				// Function Name : Is present - updated
				// Description : Function to click the Link
				// Input Parameters : driver, identifier, locator
				// Return Value : None
				// Author : Arun M
				// Date Created : 05/06/2017
				// #############################################################################
			public static boolean isPresent( CraftDriver driver, String identifyBy,
					String locator) {
				boolean isPresent = false;
				List<WebElement> elements=getWebElements(driver, identifyBy, locator);
						if (elements.size()>0) {
							isPresent = true;
						} else {
							isPresent = false;
						}
						return isPresent;
			}

			
			// #############################################################################
			// Function Name : clickLink - updated
			// Description : Function to click the Link
			// Input Parameters : driver, identifier, locator
			// Return Value : None
			// Author :  Arun M
			// Date Created : 15/06/2017
			// #############################################################################
//			public static boolean javaScriptClickObject(WebDriver driver, String identifyBy,String locator) {
//				boolean isPresent=false;
//				checkPresence(5, driver, identifyBy, locator);
//				WebElement element=getWebElement(driver, identifyBy, locator);
//				if (isElementPresent(driver, identifyBy, locator) ||element.isEnabled()) {
//					isPresent = true;
////					JavascriptExecutor executor1 = (JavascriptExecutor)driver;
////					executor1.executeScript("arguments[0].focus();", element);
//					
//					JavascriptExecutor executor = (JavascriptExecutor)driver;
//					executor.executeScript("arguments[0].click();", element);
//				}
////				try {
////					Thread.sleep(2000);
////				} catch (InterruptedException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//				return isPresent;
//			}
			
			// #############################################################################
				// Function Name : clickLink - updated
				// Description : Function to click the Link
				// Input Parameters : driver, identifier, locator
				// Return Value : None
				// Author :  Arun M
				// Date Created : 15/06/2017
				// #############################################################################
				public static boolean javaScriptTypeText(CraftDriver driver, String identifyBy,String locator) {
					boolean isPresent=false;
					checkPresence(5, driver, identifyBy, locator);
					WebElement element=getWebElement(driver, identifyBy, locator);
					if (isElementPresent(driver, identifyBy, locator) ||element.isEnabled()) {
						isPresent = true;
						JavascriptExecutor executor1 = (JavascriptExecutor)((CraftDriver) driver).getWebDriver();
						executor1.executeScript("arguments[0].focus();", element);
						
						JavascriptExecutor executor = (JavascriptExecutor)((CraftDriver) driver).getWebDriver();
						executor.executeScript("document.getElementById('Email').value='SoftwareTestingMaterial.com';");
					}
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					return isPresent;
				}
			// #############################################################################
					// Function Name : clickLink - updated
					// Description : Function to click the Link
					// Input Parameters : driver, identifier, locator
					// Return Value : None
					// Author : Arun M
					// Date Created : 05/06/2015
					// #############################################################################
					public static boolean javaScriptFocus(CraftDriver driver, String identifyBy,String locator) {
						boolean isPresent=false;
						checkPresence(5, driver, identifyBy, locator);
						WebElement element=getWebElement(driver, identifyBy, locator);
						if (isElementPresent(driver, identifyBy, locator) ||element.isEnabled()) {
							isPresent = true;
							JavascriptExecutor executor = (JavascriptExecutor)((CraftDriver) driver).getWebDriver();
							executor.executeScript("arguments[0].focus();", element);
						}
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						return isPresent;
					}
			
			// #############################################################################
				// Function Name : clickLink - updated
				// Description : Function to click the Link
				// Input Parameters : driver, identifier, locator
				// Return Value : None
				// Author :  Arun M
				// Date Created : 05/06/2015
				// #############################################################################
				

		public static void doubleClickObject(CraftDriver driver, String identifyBy,String locator){
		try{
			//Actions action = new Actions(driver);
			Actions action = new Actions(((CraftDriver) driver).getWebDriver());
			checkPresence(5, driver, identifyBy, locator);
			WebElement element=getWebElement(driver, identifyBy, locator);
			action.doubleClick(element).perform();
		}catch(Exception e){
			
		}
		}
			
		
		
			// #############################################################################
			// Function Name : selectCheckbox
			// Description : Function to Select a check box
			// Input Parameters : driver, identifier, locator and check flag to be
			// Switched on/off
			// Return Value : None
			// Author : Cognizant
			// Date Created : 05/16/2012
			// #############################################################################
			public static void selectCheckbox(CraftDriver driver, String identifyBy,
					String locator, String checkFlag) {
				WebElement element=getWebElement(driver, identifyBy, locator);
				if (isElementPresent(driver, identifyBy, locator)) {
					if ((checkFlag).equalsIgnoreCase("ON")) {
						if (!(element.isSelected())) {
							element.click();
						}
					}
				}
			}

			/**
			 * Component to verify header. Uses h1 tag in page.
			 * 
			 * @param pgText
			 * @return Returns true if header is found
			 */
			public  boolean verifyHeader(CraftDriver driver, String identifyBy,String locator,String pgText) {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				long start = System.currentTimeMillis();
				boolean isPresent = false;
				try {
					if (isElementPresent(driver,identifyBy,locator)) {
						String strText = getText(driver, identifyBy, locator);
						System.out.println(strText);
						if (strText.contains(pgText))
							isPresent = true;
					}
				} catch (Exception e) {
				}
				System.out.println("Time taken in this verify header call is "
						+ (System.currentTimeMillis() - start));
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				return (isPresent);
			}

			/********************************************************
			 *FUNCTION    :getWebElement
			 *AUTHOR      :Arun M
			 *DATE        :07-July-17
			 *DESCRIPTION :Function to get web element from HTML tags
			 ********************************************************/ 

			public static WebElement getWebElement(CraftDriver driver, String identifyBy,
					String locator ) {
				WebElement element=null;
				if (identifyBy.equalsIgnoreCase("xpath")) {
					try {
						element=driver.findElement(By.xpath(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}
				} else if (identifyBy.equalsIgnoreCase("id")) {
					try {
						element=driver.findElement(By.id(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}
				} else if (identifyBy.equalsIgnoreCase("name")) {
					try {
						element=driver.findElement(By.name(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}
				} else if (identifyBy.equalsIgnoreCase("linkText")) {
					try {
						element=driver.findElement(By.linkText(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}		
				} else if (identifyBy.equalsIgnoreCase("partialLinkText")) {
					try {
						element=driver.findElement(By.partialLinkText(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}
				} else if (identifyBy.equalsIgnoreCase("cssSelector")) {
					try {
						element=driver.findElement(By.cssSelector(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}	
				} else if (identifyBy.equalsIgnoreCase("className")) {
					try {
						element=driver.findElement(By.className(locator));
					} catch (Exception e) {
						System.out.println("Error in Webelement identifier :"+e.getMessage());
					}				
				}
				return element;
			}

			/********************************************************
			 *FUNCTION    :getWebElement
			 *AUTHOR      :Arun M
			 *DATE        :07-July-17
			 *DESCRIPTION :Function to get web element from HTML tags
			 ********************************************************/ 

//			public static List<WebElement> getWebElements(WebDriver driver, String identifyBy,
//					String locator ) {
//				List<WebElement> elements=null;
//				if (identifyBy.equalsIgnoreCase("xpath")) {
//					try {
//						elements=driver.findElements(By.xpath(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}
//				} else if (identifyBy.equalsIgnoreCase("id")) {
//					try {
//						elements=driver.findElements(By.id(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}
//				} else if (identifyBy.equalsIgnoreCase("name")) {
//					try {
//						elements=driver.findElements(By.name(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}
//				} else if (identifyBy.equalsIgnoreCase("linkText")) {
//					try {
//						elements=driver.findElements(By.linkText(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}		
//				} else if (identifyBy.equalsIgnoreCase("partialLinkText")) {
//					try {
//						elements=driver.findElements(By.partialLinkText(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}
//				} else if (identifyBy.equalsIgnoreCase("cssSelector")) {
//					try {
//						elements=driver.findElements(By.cssSelector(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}	
//				} else if (identifyBy.equalsIgnoreCase("className")) {
//					try {
//						elements=driver.findElements(By.className(locator));
//					} catch (Exception e) {
//						System.out.println("Error in Webelement identifier :"+e.getMessage());
//					}				
//				}
//				return elements;
//			}

			public static List<WebElement> getWebElements(CraftDriver driver , String identifyBy,
		            String locator ) {
		      List<WebElement> elements=null;
		      if (identifyBy.equalsIgnoreCase("xpath")) {
		            try {
		                  elements=driver.findElements(By.xpath(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }
		      } else if (identifyBy.equalsIgnoreCase("id")) {
		            try {
		                  elements=driver.findElements(By.id(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }
		      } else if (identifyBy.equalsIgnoreCase("name")) {
		            try {
		                  elements=driver.findElements(By.name(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }
		      } else if (identifyBy.equalsIgnoreCase("linkText")) {
		            try {
		                  elements=driver.findElements(By.linkText(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }           
		      } else if (identifyBy.equalsIgnoreCase("partialLinkText")) {
		            try {
		                  elements=driver.findElements(By.partialLinkText(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }
		      } else if (identifyBy.equalsIgnoreCase("cssSelector")) {
		            try {
		                  elements=driver.findElements(By.cssSelector(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }     
		      } else if (identifyBy.equalsIgnoreCase("className")) {
		            try {
		                  elements=driver.findElements(By.className(locator));
		            } catch (Exception e) {
		                  System.out.println("Error in Webelement identifier :"+e.getMessage());
		            }                       
		      }
		      return elements;
		}

			/********************************************************
			 *FUNCTION    :waitUntilElement
			 *AUTHOR      :Arun M
			 *DATE        :15-July-14
			 *DESCRIPTION :Function to validate the existence and non-Existence of an element
			 ********************************************************/ 

			public boolean waitUntilElement(CraftDriver driver, String identifyBy,
					String locator, boolean checkPresence, int timeInSec) {
				int timeout = timeInSec;
				boolean isPresent = false;
				WebElement element=getWebElement(driver, identifyBy, locator);
				if(checkPresence){
					try {
						int x = 0;
						do {
							if (element.isDisplayed()) {
								isPresent = true;
							} else {
								waitOnPage(driver,1);
								x = x + 1;
								isPresent = false;
							}
						} while (x < timeout && isPresent == false);

					} catch (Exception e) {

					}	 
				}else
				{
					try {
						int x = 0;
						do {
							if (element.isDisplayed()) {
								Thread.sleep(1000);
								x = x + 1;
								isPresent = false;
							} else {
								isPresent = true;
							}
						} while (x < timeout && isPresent == false);

					} catch (Exception e) {

					}
				}
				return isPresent;

			}
			
			// #############################################################################
			// Function Name : getText
			// Description : Function to text from the WebPage
			// Input Parameters : driver and url
			// Return Value : None
			// Author : Cognizant
			// Date Created : 05/16/2012
			// #############################################################################
			public static String  getText(CraftDriver driver, String identifyBy,
					String locator) {
				String strText = null;
				WebElement element=getWebElement(driver, identifyBy, locator);
				if (isElementPresent(driver, identifyBy, locator)) {
					strText=element.getText();
				}
				return strText;
			}

			// #############################################################################
			// Function Name : clickButton
			// Description : Function to click a button
			// Input Parameters : driver, identifier, locator
			// Return Value : None
			// Author : Cognizant
			// Date Created : 05/16/2012
			// #############################################################################
			public static void clearText(CraftDriver driver, String identifyBy,
					String locator) {
				WebElement element=getWebElement(driver , identifyBy, locator);
				if (isElementPresent(driver, identifyBy, locator)) {
					element.clear();
				}
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			
			public static void waitOnPage(CraftDriver  driver,int TimeInSec) {
				try {
					driver.switchTo().alert();
					int time=TimeInSec*1000;
					Thread.sleep(time);
				} 
				catch (Exception Ex) {
				}
			}


			public static void waitOnPage(CraftDriver  driver){
				try {
					int time=10*1000;
					Thread.sleep(time);
					
				} 
				catch (Exception Ex) {
				}
			}
			
			
			public static void SelectValueByIndex(CraftDriver driver, String identifyBy, String locator, String valuetoSelect)
            {
                try
                {                        
                    WebElement element =getWebElement(driver, identifyBy, locator);
                    checkPresence(5, driver, identifyBy, locator);
                    
                    Select select1 = new Select(element);
                    select1.selectByIndex(Integer.parseInt(valuetoSelect));
                }catch (Exception Ex) {
                }
            }
			
			public static String replaceLocator1Var(String locator,String variable1)
            {
                  try 
                  {     if(locator.contains("var1"))
                        {     
                              locator=locator.replace("var1", "Var1");
                        }
                        locator=locator.replace("Var1", variable1);           
                                                
                  }
                  catch (Exception Ex)
                  {
                  }return locator;
            }


			public static String replaceLocator2Vars(String locator,String variable1,String variable2)
		      {
		            try 
		            {     if(locator.contains("var1") || locator.contains("var2"))
		                  {     
		                        locator=locator.replace("var1", "Var1");
		                        locator=locator.replace("var2", "Var2");
		                  }
		                  locator=locator.replace("Var1", variable1);     
		                  locator=locator.replace("Var2", variable2);
		                  
		            }
		            catch (Exception Ex) {
		            }return locator;
		      }
			
			public void scrollDownTillElement(CraftDriver driver, String locator)
			{
				try
				{	
					Actions action = new Actions(driver);
					WebElement element = driver.findElement(By.xpath(locator));
					action.sendKeys(Keys.END).perform();
					action.moveToElement(element).perform();
					action.moveToElement(element).click().perform();
				}
				catch(Exception e)
				{
					
				}
			}
			
			public static void SelectValueFromDropdownByValueAndText(CraftDriver driver, String identifyBy, String locator, String valuetoSelect)
            {
                try
                {   String value = null;                  
                    WebElement element =getWebElement(driver, identifyBy, locator);
                    checkPresence(5, driver, identifyBy, locator);
                    Select select1 = new Select(element);
                    List<WebElement> list = select1.getOptions();
                    for(int i=0; i<list.size(); i++)        
                        {
                            if(list.get(i).getText().trim().equals(valuetoSelect.trim())) {
                            	value = list.get(i).getAttribute("value");
                            	break;
                            }   
                        }
                    select1.selectByValue(value);
                }catch (Exception Ex) {
                }
            }
			
			
			public void javaScriptSelectValueFromDropdown(CraftDriver driver, String identifyBy, String locator, String valuetoSelect) {
				
                try
                {   int index=0;                  
                    WebElement element =getWebElement(driver, identifyBy, locator);
                    checkPresence(5, driver, identifyBy, locator);
                    Select select1 = new Select(element);
                    List<WebElement> list = select1.getOptions();
                    for(int i=1; i<=list.size(); i++)        
                        {
                            if(list.get(i).getText().trim().equals(valuetoSelect.trim())) {
                            	index = i;
                            	break;
                            }   
                        }
                    JavascriptExecutor executor = (JavascriptExecutor)((CraftDriver) driver).getWebDriver();

                    executor.executeScript("const element = arguments[0]; element.selectedIndex = \"" + index + "\"; var event = new Event('change', { bubbles: true }); element.dispatchEvent(event);",element);

                }catch (Exception Ex) {
                }	
			}
			
}
