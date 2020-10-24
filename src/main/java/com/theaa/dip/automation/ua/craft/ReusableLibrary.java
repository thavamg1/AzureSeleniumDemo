package com.theaa.dip.automation.ua.craft;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.theaa.dip.automation.ua.framework.APIReusuableLibrary;
import com.theaa.dip.automation.ua.framework.CraftDataTable;
import com.theaa.dip.automation.ua.framework.FrameworkParameters;
import com.theaa.dip.automation.ua.framework.Settings;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.framework.selenium.CraftDriver;
import com.theaa.dip.automation.ua.framework.selenium.SeleniumReport;
import com.theaa.dip.automation.ua.framework.selenium.WebDriverUtil;

/**
 * Abstract base class for reusable libraries created by the user
 * 
 * @author Cognizant
 */
public abstract class ReusableLibrary extends MyReusableLibrary{

	int responseStatus;
	int responseCode;
	private HttpURLConnection httpURLConnect;

	protected Map<String, Object> perfectoCommand = new HashMap<>();
	Dimension winSize;
	/**
	 * The {@link CraftDataTable} object (passed from the test script)
	 */
	protected CraftDataTable dataTable;
	/**
	 * The {@link SeleniumReport} object (passed from the test script)
	 */
	protected SeleniumReport report;
	/**
	 * The {@link CraftDriver} object
	 */
	protected CraftDriver driver;
	

	protected WebDriverUtil driverUtil;

	/**
	 * The {@link ScriptHelper} object (required for calling one reusable
	 * library from another)
	 */
	protected ScriptHelper scriptHelper;

	/**
	 * The {@link Properties} object with settings loaded from the framework
	 * properties file
	 */
	protected Properties properties;
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters;
	
	/**
	 * The {@link APIReusuableLibrary} object
	 */
	protected APIReusuableLibrary apiDriver;
	
	protected ExtentTest extentTest;
	
	protected Map<String,String> reusableHandle;

	
	public String XPATH="xpath";
	public String NAME="name";
	public String ID="id";
	public String LINKTEXT="linkText";
	public String PARTIALLINKTEXT="partialLinkText";
	public String CSS="cssSelector";
	public String CLASSNAME="className";
	
	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the
	 * objects wrapped by it
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object
	 */
	public ReusableLibrary(ScriptHelper scriptHelper) {
		this.scriptHelper = scriptHelper;
		this.dataTable = scriptHelper.getDataTable();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getcraftDriver();
		this.driverUtil = scriptHelper.getDriverUtil();
		this.apiDriver = scriptHelper.getApiDriver();
		this.extentTest = scriptHelper.getExtentTest();
		this.reusableHandle = scriptHelper.getReusablehandle();

		properties = Settings.getInstance();
		frameworkParameters = FrameworkParameters.getInstance();
	}

	/**
	 * All reusuable Appium Functions with Perfecto
	 */

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Name of the App as displayed in Mobile
	 */
	protected void openApp(final String context, final String appName) {
		if (context.equals("NATIVE_APP")) {
			final Map<String, Object> perfectoCommand = new HashMap<>();
			perfectoCommand.put("name", appName);
			driver.getAppiumDriver().executeScript("mobile:application:open", perfectoCommand);
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Identifier of the App.
	 */
	protected void openAppWithIdentifier(final String context, final String identifer) {
		if (context.equals("NATIVE_APP")) {
			perfectoCommand.put("identifier", identifer);
			driver.getAppiumDriver().executeScript("mobile:application:open", perfectoCommand);
			perfectoCommand.clear();
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param type
	 *            - Type of report like pdf
	 */
	protected byte[] downloadReport(final String type) throws IOException {
		final String command = "mobile:report:download";
		final Map<String, String> params = new HashMap<>();
		params.put("type", type);
		final String report = (String) (driver.getRemoteWebDriver()).executeScript(command, params);
		final byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
		return reportBytes;
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 */
	protected byte[] downloadWTReport() {
		final String reportUrl = (String) driver.getAppiumDriver().getCapabilities()
				.getCapability("windTunnelReportUrl");
		String returnString = "<html><head><META http-equiv=\"refresh\" content=\"0;URL=";
		returnString = returnString + reportUrl + "\"></head><body /></html>";

		return returnString.getBytes();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Name of the App.
	 */
	protected void closeApp(final String context, final String appName) {
		if (context.equals("NATIVE_APP")) {
			perfectoCommand.put("name", appName);
			try {
				driver.getAppiumDriver().executeScript("mobile:application:close", perfectoCommand);
			} catch (final WebDriverException e) {
			}
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Identifier of the App.
	 */
	protected void closeAppWithIdentifier(final String context, final String bundleId) {
		if (context.equals("NATIVE_APP")) {
			perfectoCommand.put("identifier", bundleId);
			try {
				driver.getAppiumDriver().executeScript("mobile:application:close", perfectoCommand);
			} catch (final WebDriverException e) {
			}
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param textToFind
	 *            - text that has to be searched
	 * @param timeout
	 */
	protected Boolean textCheckpoint(final String textToFind, final Integer timeout) {
		perfectoCommand.put("content", textToFind);
		perfectoCommand.put("timeout", timeout);
		final Object result = driver.getAppiumDriver().executeScript("mobile:checkpoint:text", perfectoCommand);
		final Boolean resultBool = Boolean.valueOf(result.toString());
		perfectoCommand.clear();
		return resultBool;
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param textToFind
	 *            - text that has to be searched
	 * @param timeout
	 */
	protected void textClick(final String textToFind, final Integer timeout) {
		perfectoCommand.put("content", textToFind);
		perfectoCommand.put("timeout", timeout);
		driver.getAppiumDriver().executeScript("mobile:text:select", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param label
	 *            - text that has to be searched
	 * @param threshold
	 */
	protected void visualScrollToClick(final String label, final Integer threshold) {
		perfectoCommand.put("label", label);
		perfectoCommand.put("threshold", threshold);
		perfectoCommand.put("scrolling", "scroll");
		driver.getAppiumDriver().executeScript("mobile:button-text:click", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param label
	 *            - text that has to be searched
	 * @param timeout
	 * @param threshold
	 */
	protected void visualClick(final String label, final Integer timeout, final Integer threshold) {
		perfectoCommand.put("label", label);
		perfectoCommand.put("threshold", threshold);
		perfectoCommand.put("timeout", timeout);
		driver.getAppiumDriver().executeScript("mobile:button-text:click", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param label
	 *            - text that has to be searched
	 * @param timeout
	 * @param threshold
	 * @param labelDirection
	 * @param labelOffset
	 */
	protected void visualClick(final String label, final Integer timeout, final Integer threshold,
			final String labelDirection, final String labelOffset) {
		perfectoCommand.put("label", label);
		perfectoCommand.put("threshold", threshold);
		perfectoCommand.put("timeout", timeout);
		perfectoCommand.put("label.direction", labelDirection);
		perfectoCommand.put("label.offset", labelOffset);
		driver.getAppiumDriver().executeScript("mobile:button-text:click", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param imagePath
	 */
	protected void imageClick(String imagePath) {
		perfectoCommand.put("content", imagePath);
		perfectoCommand.put("timeout", "5");
		perfectoCommand.put("screen.top", "0%");
		perfectoCommand.put("screen.height", "100%");
		perfectoCommand.put("screen.left", "0%");
		perfectoCommand.put("screen.width", "100%");
		driver.executeScript("mobile:image:select", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param imagePath
	 */
	protected Boolean imageCheckpoint(String imagePath) {
		perfectoCommand.put("content", imagePath);
		perfectoCommand.put("threshold", "90");
		perfectoCommand.put("screen.top", "0%");
		perfectoCommand.put("screen.height", "100%");
		perfectoCommand.put("screen.left", "0%");
		perfectoCommand.put("screen.width", "100%");
		Object result = driver.executeScript("mobile:image:find", perfectoCommand);
		final Boolean resultBool = Boolean.valueOf(result.toString());
		perfectoCommand.clear();
		return resultBool;
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param repositoryFile
	 * @param handsetFile
	 */
	protected void putFileOnDevice(final String repositoryFile, final String handsetFile) {
		perfectoCommand.put("repositoryFile", repositoryFile);
		perfectoCommand.put("handsetFile", handsetFile);
		driver.getAppiumDriver().executeScript("mobile:media:put", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param handsetFile
	 * @param repositoryFile
	 */
	protected void getFileOnDevice(final String handsetFile, final String repositoryFile) {
		perfectoCommand.put("repositoryFile", repositoryFile);
		perfectoCommand.put("handsetFile", handsetFile);
		driver.getAppiumDriver().executeScript("mobile:media:get", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param handsetFile
	 */
	protected void deleteFromDevice(final String handsetFile) {
		perfectoCommand.put("handsetFile", handsetFile);
		driver.getAppiumDriver().executeScript("mobile:media:delete", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param repositoryFile
	 */
	protected void deleteFromRepository(final String repositoryFile) {
		perfectoCommand.put("repositoryFile", repositoryFile);
		driver.getAppiumDriver().executeScript("mobile:media:delete", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param keyPress
	 */
	protected void deviceKeyPress(final String keyPress) {

		perfectoCommand.put("keySequence", keyPress);
		driver.getAppiumDriver().executeScript("mobile:presskey", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	protected void swipe(final String x1, final String y1, final String x2, final String y2) {
		final List<String> swipeCoordinates = new ArrayList<>();
		swipeCoordinates.add(x1 + ',' + y1);
		swipeCoordinates.add(x2 + ',' + y2);
		perfectoCommand.put("location", swipeCoordinates);
		driver.getAppiumDriver().executeScript("mobile:touch:drag", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param textToFind
	 */
	protected void swipeTillText(String textToFind) {
		perfectoCommand.put("content", textToFind);
		perfectoCommand.put("scrolling", "scroll");
		perfectoCommand.put("maxscroll", "10");
		perfectoCommand.put("next", "SWIPE_UP");
		driver.executeScript("mobile:text:select", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable to Pause the Script, Generic Application
	 * 
	 * @param How_Long_To_Pause
	 */
	public void PauseScript(int How_Long_To_Pause) {
		// convert to seconds
		How_Long_To_Pause = How_Long_To_Pause * 1000;

		try {
			Thread.sleep(How_Long_To_Pause);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * All reusuable Selenium Functions with Perfecto
	 */

	/**
	 * Function to switch the Context
	 * 
	 * @param driver
	 * @RemoteWebDriver
	 * @param context
	 */
	protected static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param driver
	 * @param list
	 */
	@SuppressWarnings("rawtypes")
	protected void scrollChecker(IOSDriver driver, String[] list) {
		for (int i = 0; i < list.length; i++) {

			MobileElement me = (MobileElement) driver.findElement(By.xpath("//UIAPickerWheel[" + (i + 1) + "]"));
			int mget = getMonthInt(me.getText().split(",")[0]);

			if (i == 0) {
				if (mget > getMonthInt(list[i])) {
					scrollAndSearch(driver, list[i], me, true);
				} else {
					scrollAndSearch(driver, list[i], me, false);
				}
			} else {
				if (Integer.parseInt(me.getText().split(",")[0]) > Integer.parseInt(list[i])) {
					scrollAndSearch(driver, list[i], me, true);
				} else {
					scrollAndSearch(driver, list[i], me, false);
				}
			}
		}
	}

	// Used to get the integer for a month based on the string of the month
	private int getMonthInt(String month) {
		int monthInt = 0;
		switch (month) {
		case "Jan":
			monthInt = 1;
			break;
		case "January":
			monthInt = 1;
			break;
		case "February":
			monthInt = 2;
			break;
		case "Feb":
			monthInt = 2;
			break;
		case "March":
			monthInt = 3;
			break;
		case "Mar":
			monthInt = 3;
			break;
		case "April":
			monthInt = 4;
			break;
		case "Apr":
			monthInt = 4;
			break;
		case "May":
			monthInt = 5;
			break;
		case "June":
			monthInt = 6;
			break;
		case "Jun":
			monthInt = 6;
			break;
		case "July":
			monthInt = 7;
			break;
		case "Jul":
			monthInt = 7;
			break;
		case "August":
			monthInt = 8;
			break;
		case "Aug":
			monthInt = 8;
			break;
		case "September":
			monthInt = 9;
			break;
		case "Sep":
			monthInt = 9;
			break;
		case "October":
			monthInt = 10;
			break;
		case "Oct":
			monthInt = 10;
			break;
		case "November":
			monthInt = 11;
			break;
		case "Nov":
			monthInt = 11;
			break;
		case "December":
			monthInt = 12;
			break;
		case "Dec":
			monthInt = 12;
			break;
		}
		return monthInt;
	}

	// Code here shouldn't be modified
	@SuppressWarnings("rawtypes")
	private void scrollAndSearch(IOSDriver driver, String value, MobileElement me, Boolean direction) {
		String x = getLocationX(me);
		String y = getLocationY(me);
		while (!driver.findElementByXPath(getXpathFromElement(me)).getText().contains(value)) {
			swipe(driver, x, y, direction);
		}
	}

	// Performs the swipe and search operation
	// Code here shouldn't be modified
	@SuppressWarnings("rawtypes")
	private void swipe(IOSDriver driver, String start, String end, Boolean up) {
		String direction;
		if (up) {
			direction = start + "," + (Integer.parseInt(end) + 70);
		} else {
			direction = start + "," + (Integer.parseInt(end) - 70);
		}

		Map<String, Object> params1 = new HashMap<>();
		params1.put("location", start + "," + end);
		params1.put("operation", "down");
		driver.executeScript("mobile:touch:tap", params1);

		Map<String, Object> params2 = new HashMap<>();
		List<String> coordinates2 = new ArrayList<>();

		coordinates2.add(direction);
		params2.put("location", coordinates2);
		params2.put("auxiliary", "notap");
		params2.put("duration", "3");
		driver.executeScript("mobile:touch:drag", params2);

		Map<String, Object> params3 = new HashMap<>();
		params3.put("location", direction);
		params3.put("operation", "up");
		driver.executeScript("mobile:touch:tap", params3);
	}

	// Gets the objects X location in pixels
	private String getLocationX(MobileElement me) {
		int x = me.getLocation().x;
		int width = (Integer.parseInt(me.getAttribute("width")) / 2) + x;
		return width + "";
	}

	// Gets the objects X location in pixels
	private String getLocationY(MobileElement me) {
		int y = me.getLocation().y;
		int height = (Integer.parseInt(me.getAttribute("height")) / 2) + y;
		return height + "";
	}

	// Parses webelement to retrieve the xpath used for identification
	private String getXpathFromElement(MobileElement me) {
		return (me.toString().split("-> xpath: ")[1]).substring(0, (me.toString().split("-> xpath: ")[1]).length() - 1);
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param letter
	 */
	protected void drawLetter(final String letter) {
		final List<String> coordinates = new ArrayList<>();

		switch (letter) {
		case "A":

			break;
		case "B":

			break;
		case "C":

			break;
		case "D":

			break;
		case "E":
			coordinates.add("42%,40%");
			coordinates.add("42%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("42%,40%");
			coordinates.add("52%,40%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("42%,48%");
			coordinates.add("52%,48%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("42%,56%");
			coordinates.add("52%,56%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			break;
		case "F":

			break;
		case "G":

			break;
		case "H":

			break;
		case "I":

			break;
		case "J":

			break;
		case "K":

			break;
		case "L":

			break;
		case "M":

			break;
		case "N":

			break;
		case "O":

			break;
		case "P":
			coordinates.add("30%,40%");
			coordinates.add("30%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("30%,40%");
			coordinates.add("40%,40%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("38%,40%");
			coordinates.add("38%,52%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("38%,48%");
			coordinates.add("28%,48%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			break;
		case "Q":

			break;
		case "R":
			coordinates.add("54%,40%");
			coordinates.add("54%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("54%,40%");
			coordinates.add("64%,40%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("62%,40%");
			coordinates.add("62%,52%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("62%,48%");
			coordinates.add("52%,48%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("54%,48%");
			coordinates.add("64%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			break;
		case "S":

			break;
		case "T":

			break;
		case "U":

			break;
		case "V":

			break;
		case "W":

			break;
		case "X":

			break;
		case "Y":

			break;
		case "Z":

			break;
		}
	}

	/**
	 * Function to check the Specific broken Link
	 * 
	 * @param Url
	 */
	protected void brokenLinkValidator(String Url) {
		urlLinkStatus(validationOfLinks(Url));
	}

	private String[] validationOfLinks(String urlToValidate) {
		String[] responseArray = new String[3];
		try {
			URL url = new URL(urlToValidate);
			httpURLConnect = (HttpURLConnection) url.openConnection();
			httpURLConnect.setConnectTimeout(3000);
			httpURLConnect.connect();
			responseStatus = httpURLConnect.getResponseCode();
			responseCode = responseStatus / 100;
		} catch (Exception e) {
		}
		responseArray[0] = urlToValidate;
		responseArray[1] = String.valueOf(responseCode);
		responseArray[2] = String.valueOf(responseStatus);
		return responseArray;
	}

	private void urlLinkStatus(String[] responseArray) {
		try {
			String linkValue = responseArray[0];
			String responseValue = responseArray[1];
			responseCode = Integer.valueOf(responseValue);
			String responseStatus = responseArray[2];
			switch (responseCode) {
			case 2:
				report.updateTestLog(linkValue, "Response code : " + responseStatus + " - OK", Status.PASS);
				break;
			case 3:
				report.updateTestLog(linkValue, "Unknown Responce Code", Status.FAIL);
				break;
			case 4:
				report.updateTestLog(linkValue, "Response code : " + responseStatus + " - Client error", Status.FAIL);
				break;

			case 5:

				report.updateTestLog(linkValue, "Response code : " + responseStatus + " - Internal Server Error",
						Status.FAIL);
				break;
			default:
				report.updateTestLog(linkValue, "Unknown Responce Code", Status.FAIL);

				break;
			}

		} catch (Exception e) {

		} finally {
			httpURLConnect.disconnect();

		}
	}

	/**
	 * Function to check the All Broken Links available in the Page
	 * 
	 */
	protected void validateAllLinksInPage() {

		String url;
		int responseCode;

		List<WebElement> links = driver.findElements(By.tagName("a"));

		Iterator<WebElement> it = links.iterator();

		while (it.hasNext()) {

			url = it.next().getAttribute("href");

			if (url == null || url.isEmpty()) {
				continue;
			}

			try {
				httpURLConnect = (HttpURLConnection) (new URL(url).openConnection());

				httpURLConnect.setRequestMethod("HEAD");

				httpURLConnect.connect();

				responseCode = httpURLConnect.getResponseCode();

				if (responseCode >= 400) {
					report.updateTestLog(url, "Response code : " + responseStatus + " - BROKEN", Status.WARNING);
				} else {
					report.updateTestLog(url, "Response code : " + responseStatus + " - OK", Status.DONE);
				}

			} catch (MalformedURLException e) {
				report.updateTestLog("ValidateURL", "Error while validating URL" + e.getMessage(), Status.WARNING);

			} catch (IOException e) {
				report.updateTestLog("ValidateURL", "Error while validating URL" + e.getMessage(), Status.WARNING);
			}
		}

	}

	/**
	 * Function to check the All Broken Image Links available in the Page
	 * 
	 */
	protected void validateAllImageLinksInPage() {

		String url;
		int responseCode;

		List<WebElement> links = driver.findElements(By.tagName("img"));

		Iterator<WebElement> it = links.iterator();

		while (it.hasNext()) {

			url = it.next().getAttribute("href");

			if (url == null || url.isEmpty()) {
				continue;
			}

			try {
				httpURLConnect = (HttpURLConnection) (new URL(url).openConnection());

				httpURLConnect.setRequestMethod("HEAD");

				httpURLConnect.connect();

				responseCode = httpURLConnect.getResponseCode();

				if (responseCode >= 400) {
					report.updateTestLog(url, "Response code : " + responseStatus + " - BROKEN", Status.WARNING);
				} else {
					report.updateTestLog(url, "Response code : " + responseStatus + " - OK", Status.DONE);
				}

			} catch (MalformedURLException e) {
				report.updateTestLog("ValidateURL", "Error while validating URL" + e.getMessage(), Status.WARNING);

			} catch (IOException e) {
				report.updateTestLog("ValidateURL", "Error while validating URL" + e.getMessage(), Status.WARNING);
			}
		}

	}

	public static boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		} // catch
	}


	public String getInput(String ColName){

		String data=null;
		try {
			data=dataTable.getData("General_Data",ColName);
			return data;
		} 
		catch (Exception Ex) {	
			System.out.println("INPUT DATA ERROR : "+Ex.getMessage());
			report.updateTestLog("EXCEL DATA", "ERROR"+Ex.getMessage(), Status.FAIL);
			return null;
		} 
	}
	
	
	public String getMTAInput(String ColName){

		String data=null;
		try {
			data=dataTable.getData("MTA",ColName);
			return data;
		} 
		catch (Exception Ex) {	
			System.out.println("INPUT DATA ERROR : "+Ex.getMessage());
			report.updateTestLog("EXCEL DATA", "ERROR"+Ex.getMessage(), Status.FAIL);
			return null;
		} 
	}

	public void putOutput(String ColName, String data){
		try {
			dataTable.putData("Output_Data",ColName,data);
		} 
		catch (Exception Ex) {	
			System.out.println("OUTPUT DATA ERROR : "+Ex.getMessage());
			report.updateTestLog("EXCEL DATA", "ERROR"+Ex.getMessage(), Status.FAIL);
		} 
	}

	public void putGeneralData(String ColName, String data){
        try {
              dataTable.putData("General_Data",ColName,data);
        } 
        catch (Exception Ex) {  
              System.out.println("OUTPUT DATA ERROR : "+Ex.getMessage());
              report.updateTestLog("EXCEL DATA", "ERROR"+Ex.getMessage(), Status.FAIL);
        } 
  }

	

	public String generateString(Random rng, String characters, int length)
	{
		char[] text = new char[length];
		for (int i = 0; i < length; i++)
		{
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}


	
	
	public static void SelectValuefromDropDown(CraftDriver driver, String identifyBy, String locator, String valuetoSelect)
	{
		//WebElement select =(driver.findElement(By.id("unitOfMeasure")));
		//select.sendKeys(value);
		if (isElementPresent(driver, identifyBy, locator)) {
			//	System.out.println("clicking radio button" +locator);
			WebElement select= getWebElement(driver, identifyBy, locator);
			select.sendKeys(valuetoSelect);
		}
	} 



	// #############################################################################
	// Function Name : clickButton
	// Description : Function to click a button
	// Input Parameters : driver, identifier, locator
	// Return Value : None
	// Author : Cognizant
	// Date Created : 05/16/2012
	// #############################################################################
	public static void clickObject(CraftDriver driver, String identifyBy,
			String locator) {
		
		checkPresence(5, driver, identifyBy, locator);
		if (isElementPresent(driver, identifyBy, locator)) {
			getWebElement(driver, identifyBy, locator).click();
		}

//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void safeJavaScriptClick(WebElement element) throws Exception {
		try {
			
			if (element.isEnabled() || element.isDisplayed()) {
				System.out.println("Clicking on element with using java script click");

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else {
				System.out.println("Unable to click on element");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document "+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM "+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to click on element "+ e.getStackTrace());
		}
	}

	public static void waitUntilClickable(CraftDriver driver, String identifyBy, String locator)
	{
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement element = null;
		if (identifyBy.equalsIgnoreCase("id")) {
			element = wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
		} 
		else if (identifyBy.equalsIgnoreCase("xpath")) {
			element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		} 
		else if (identifyBy.equalsIgnoreCase("name")) {
			element = wait.until(ExpectedConditions.elementToBeClickable(By.name(locator)));
		} 
	}

	public void waitForPageLoad(int period)
	{
		WebDriverWait wait = new WebDriverWait(driver, period);

		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver wdriver) {
				return ((JavascriptExecutor) ((CraftDriver) driver).getWebDriver()).executeScript(
						"return document.readyState"
						).equals("complete");
			}
		});
	}


	// #############################################################################
	// Function Name : closeJscriptPopup
	// Description : Function to close the Javascript Popup
	// Input Parameters : driver and alert
	// Return Value : None
	// Author : Cognizant
	// Date Created : 05/16/2012
	// #############################################################################
	public static void ignorePopup(CraftDriver driver, Alert alert) {
		try {
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			alert = driver.switchTo().alert();
			String str = alert.getText();
			System.out.println("Alert-" + str);
			alert.accept();
		} catch (Exception e) {
			System.out.println("Alert Not appearing");
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		} // catch
	} // isAlertPresent()

	// #############################################################################
	// Function Name : navigatetoWebpage
	// Description : Function to Navigate to the WebPage
	// Input Parameters : driver and url
	// Return Value : None
	// Author : Cognizant
	// Date Created : 05/16/2012
	// #############################################################################
	public static void navigatetoWebpage(CraftDriver driver, String url) {
		driver.get(url);
	}
	



	// #############################################################################
	// Function Name : isElementPresent
	// Description : Function to validate the existence of an element
	// Input Parameters : wait time, driver, identifier, locator
	// Return Value : tpuAmoha
	// Author : Cognizant
	// Date Created : 05/02/2016
	// #############################################################################
	
	public boolean checkPresenceX(int waitTime, CraftDriver driver, String identifyBy,
            String locator) {
      int timeout = waitTime;
      boolean isPresent = false;

      List<WebElement> elements=getWebElements(driver, identifyBy, locator);
      try {
            int x = 0;
            do {
                    WebDriverWait wait1 = new WebDriverWait(driver, 120); 
                  wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
                  
                  if (elements.size()>0) {
                //  System.out.println("Element Available");
                	  Thread t = Thread.currentThread();
                	  if(t.isAlive())
                    //if (!a.elementPresent())
                    {
                        System.out.println("Inside if---> t.isAlive("+t.isAlive());
                          
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
	

	/**
	 * Convenience function to verify header by Class 'page-title'
	 * 
	 * @FunctionName verifyHeaderByClassPageTitle
	 * @InputParameters None
	 * @Author Cognizant
	 * @DateCreated Jun 25, 2012
	 * @param pgText
	 * @return True of False
	 */
	public boolean verifyHeaderByClassPageTitle(String pgText) {
		long start = System.currentTimeMillis();
		boolean isPresent = false;
		try {
			//if (isElementPresent(driver, "xpath", "//h1[@class='page-title']")) {
			if (driver.findElement(By.tagName("h1")).isDisplayed()) {
				String strText = driver.findElement(
						//	By.xpath("//h1[@class='page-title']")).getText();
						By.tagName("h1")).getText();
				System.out.println(strText);
				if (strText.matches(pgText))
					isPresent = true;
			}
		} catch (Exception e) {
		}
		System.out.println("Time taken in this verify header" + pgText
				+ " call is " + (System.currentTimeMillis() - start));
		return (isPresent);
	}

	// #############################################################################
	// Function Name : click link from table
	// Description : Function to click a link from a table
	// Input Parameters : driver and url
	// Return Value : None
	// Author : Nanditha
	// Date Created : 8-July-13
	// #############################################################################		
	public static void clickLinkFromTable(CraftDriver driver, String identifyBy,
			String locator, int Row) {
		int timeout = 3;
		String text = "";		
		locator=locator.replaceAll("<>","["+Row+"]");		
		try {


			getWebElement(driver, identifyBy, locator).click() ;


		} catch (Exception e) {

		}
	}


	/********************************************************
	 *FUNCTION    :switchToWindow
	 *AUTHOR      :Arun M
	 *DATE        :02-July-13
	 *DESCRIPTION :Function to Switch to New Window.
	 *PAGE        :Navigating to new window. 
	 ********************************************************/
	public void switchToWindow()throws NoSuchWindowException
	{
		try
		{
			Set<String> handles = driver.getWindowHandles();
			String current = driver.getWindowHandle();
			handles.remove(current);
			//Thread.sleep(1000);
			String newTab = handles.iterator().next();
			driver.switchTo().window(newTab);

		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Switvh to Window", "Switch to window not appear"+e.toString(), Status.FAIL);
		}
	}

	/********************************************************
	 *FUNCTION    :switchToFrame
	 *AUTHOR      :Arun M
	 *DATE        :02-July-13
	 *DESCRIPTION :Function to Switch to New Window.
	 *PAGE        :Navigating to new window. 
	 ********************************************************/
	public void switchToFrame(String frameName)throws NoSuchWindowException
	{
		try
		{
			waitOnPage(driver);
			driver.switchTo().frame(frameName);
			waitOnPage(driver);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Switvh to Frame", "Switch to Frame not appear"+e.toString(), Status.FAIL);
		}
	}


	/********************************************************
	 *FUNCTION    :switchToDefault
	 *AUTHOR      :Arun M
	 *DATE        :02-July-13
	 *DESCRIPTION :Function to Switch to New Window.
	 *PAGE        :Navigating to new window. 
	 ********************************************************/
	public void switchToFrame()
	{
		try
		{
			waitOnPage(driver);
			driver.switchTo().defaultContent();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Switvh to Frame", "Switch to Frame not appear"+e.toString(), Status.FAIL);
		}

	}
	

	/********************************************************
	 *FUNCTION    :scrollDownTillEndpage
	 *AUTHOR      :Gajapathy D.R.
	 *DATE        :16-Jul-14
	 *DESCRIPTION :Function to scroll down till end of the page
	 *PAGE        :Re-usable component. 
	 ********************************************************/
	public void scrollDownTillEndpage()
	{
		try
		{
			Actions actions = new Actions(driver);
			actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
			report.updateTestLog("Scroll down", "Scroll down till page end", Status.DONE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Scroll down", "Scroll down is not happened"+e.toString(), Status.FAIL);
		}
	}
	/********************************************************
	 *FUNCTION    :scrollUpTillToppage
	 *AUTHOR      :Arun M
	 *DATE        :16-Jul-14
	 *DESCRIPTION :Function to scroll up till top of the page
	 *PAGE        :Re-usable component. 
	 ********************************************************/
	public void scrollUpTillToppage()
	{
		try
		{
			Actions actions = new Actions(driver);
			actions.keyUp(Keys.CONTROL).sendKeys(Keys.UP).perform();
			report.updateTestLog("Scroll Up", "Scroll Up till top of the page", Status.DONE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Scroll Up", "Scroll Up is not happened"+e.toString(), Status.FAIL);
		}
	}
	/********************************************************
	 *FUNCTION    :navigatePreviousPage
	 *AUTHOR      :Gajapathy D.R.
	 *DATE        :18-Jul-14
	 *DESCRIPTION :Function to scroll up till top of the page
	 *PAGE        :Re-usable component. 
	 ********************************************************/
	public void navigatePreviousPage()
	{
		try
		{
			driver.navigate().back();
			report.updateTestLog("Navigate Previous Page", "Navigated to previos page", Status.DONE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Navigate Previous Page", "Not Navigated to previos page"+e.toString(), Status.FAIL);
		}
	}
	

	//#############################################################################
	// Function Name : getAttribute
	// Description : Function to get any property value of the element
	// Author : Arun M
	// Date Created : 13-nov-2014
	// #############################################################################
	public static String  getAttribute(CraftDriver driver, String identifyBy,
			String locator, String property) {
		String strText = null;
		WebElement element=getWebElement(driver, identifyBy, locator);
		if (isElementPresent(driver, identifyBy, locator)) {
			strText=element.getAttribute(property);
		}

		return strText;
	}

	public static void SelectValuefromDropDownByIndex(CraftDriver driver, String identifyBy, String locator, String valuetoSelect)
	{
		WebElement element =getWebElement(driver, identifyBy, locator);
		//select.sendKeys(value);
		if (isElementPresent(driver, "id", locator)) {
			//	System.out.println("clicking radio button" +locator);
			Select select1 = new Select(element);
			select1.selectByIndex(Integer.parseInt(valuetoSelect));
		}
	}

	public static void SelectValuefromDropDownByVisibleText(CraftDriver driver, String identifyBy, String locator, String valuetoSelect)
	{
			checkPresence(5, driver, identifyBy, locator);
			WebElement element =getWebElement(driver, identifyBy, locator);
			Select select1 = new Select(element);
			select1.selectByVisibleText(valuetoSelect);
		
	}
	

	// #############################################################################
	// Function Name : getListCount
	// Description : Function to get List Count
	// Input Parameters : Driver Object, Identifyby, Locator 
	// Return Value : List Count
	// Author : Cognizant
	// Date Created : 08/24/2012
	// #############################################################################
	public static int  getListCount(CraftDriver driver, String identifyBy,
			String locator) {
		int intListCount = 0;
		if (identifyBy.equalsIgnoreCase("xpath")) {
			if (isElementPresent(driver, "xpath", locator)) {
				List<WebElement> element=driver.findElements(By.xpath(locator));
				intListCount = element.size();
			}
		} else if (identifyBy.equalsIgnoreCase("id")) {
			if (isElementPresent(driver, "id", locator)) {
				List<WebElement> element=driver.findElements(By.id(locator));
				intListCount = element.size();
			}
		} else if (identifyBy.equalsIgnoreCase("name")) {
			if (isElementPresent(driver, "name", locator)) {
				List<WebElement> element=driver.findElements(By.name(locator));
				intListCount = element.size();
			}
		} else if (identifyBy.equalsIgnoreCase("cssSelector")) {
			if (isElementPresent(driver, "cssSelector", locator)) {
				List<WebElement> element=driver.findElements(By.cssSelector(locator));
				intListCount = element.size();
			}
		} else if (identifyBy.equalsIgnoreCase("className")) {
			if (isElementPresent(driver, "className", locator)) {
				List<WebElement> element=driver.findElements(By.className(locator));
				intListCount = element.size();
			}
		} else if (identifyBy.equalsIgnoreCase("linkText")) {
			if (isElementPresent(driver, "linkText", locator)) {
				List<WebElement> element=driver.findElements(By.linkText(locator));
				intListCount = element.size();
			}
		} else if (identifyBy.equalsIgnoreCase("partialLinkText")) {
			if (isElementPresent(driver, "partialLinkText", locator)) {
				List<WebElement> element=driver.findElements(By.partialLinkText(locator));
				intListCount = element.size();
			}
		}
		return intListCount;
	}


	/********************************************************
	 *FUNCTION    :explicitWait
	 *AUTHOR      :Arun M
	 *DATE        :30/05/14
	 *DESCRIPTION :Function to explicitWait
	 *PAGE        :Wait for expected element. 
	 ********************************************************/
	public void explicitWait(CraftDriver driver, String identifyBy, String locator,String locatorname)
	{
		try
		{
			if(identifyBy.equalsIgnoreCase("id"))
			{
				WebElement elementCheck=(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
				if(elementCheck.isDisplayed())
				{
					report.updateTestLog("Expected Element", "Expected element is present in page"+locatorname, Status.PASS);
				}
				else
					report.updateTestLog("Expected Element", "Expected element is not present in page", Status.FAIL);
			}
			if(identifyBy.equalsIgnoreCase("xpath"))
			{
				WebElement elementCheck=(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
				if(elementCheck.isDisplayed())
				{
					report.updateTestLog("Expected Element", "Expected element is present in page"+locatorname, Status.PASS);
				}
				else
					report.updateTestLog("Expected Element", "Expected element is not present in page", Status.FAIL);
			}
			if(identifyBy.equalsIgnoreCase("linkText"))
			{
				WebElement elementCheck=(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(locator)));
				if(elementCheck.isDisplayed())
				{
					report.updateTestLog("Expected Element", "Expected element is present in page"+locatorname, Status.PASS);
				}
				else
					report.updateTestLog("Expected Element", "Expected element is not present in page", Status.FAIL);
			}
			if(identifyBy.equalsIgnoreCase("name"))
			{
				WebElement elementCheck=(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.name(locator)));
				if(elementCheck.isDisplayed())
				{
					report.updateTestLog("Expected Element", "Expected element is present in page"+locatorname, Status.PASS);
				}
				else
					report.updateTestLog("Expected Element", "Expected element is not present in page", Status.FAIL);
			}
			if(identifyBy.equalsIgnoreCase("partialLinkText"))
			{
				WebElement elementCheck=(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(locator)));
				if(elementCheck.isDisplayed())
				{
					report.updateTestLog("Expected Element", "Expected element is present in page"+locatorname, Status.PASS);
				}
				else
					report.updateTestLog("Expected Element", "Expected element is not present in page", Status.FAIL);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			report.updateTestLog("Expected Element", "Expected element is not present in page"+e.toString(), Status.FAIL);

		}
	}
	public String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		return dateFormat.format(date);
	}
	
	public String returnMonth(String userMonth)
	{
		String monthInWords=null;
	switch (userMonth) {
		case "01":
		monthInWords = "January";
		break;
		case "02":
			monthInWords = "February";
		break;
		case "03":
			monthInWords = "March";
		break;
		case "04":
			monthInWords = "April";
		break;
		case "05":
			monthInWords = "May";
		break;
		case "06":
			monthInWords = "June";
		break;
		case "07":
			monthInWords = "July";
		break;
		
		case "08":
			monthInWords = "August";
		break;
		case "09":
			monthInWords = "September";
		break;
		case "10":
			monthInWords = "October";
		break;
		case "11":
			monthInWords = "November";
		break;
		case "12":
			monthInWords = "December";
		break;
		default:
			monthInWords = "Invalid month";
		break;
		}
		return monthInWords;
	}


	public void typeByCharacter(CraftDriver driver, String identifyBy,String locator,String value){
        try {
            WebElement ele = getWebElement(driver, identifyBy, locator);
                  for(int i=0;i<value.length();i++){
                  
              ele.sendKeys(value.charAt(i)+"");
            }
           // EnteraLocation.sendKeys(Keys.DOWN);
           // EnteraLocation.sendKeys(Keys.TAB); 
        } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
        }
  }	

	public static boolean typeinEditbox(CraftDriver driver, String identifyBy,
			String locator, String valuetoType) {
		
		boolean isPresent = false;	
		checkPresence(5, driver, identifyBy, locator);
		if (isElementPresent(driver, identifyBy, locator)) {
			isPresent=true;
			WebElement element=getWebElement(driver, identifyBy, locator);
			element.clear();
			JavascriptExecutor executor = (JavascriptExecutor)((CraftDriver) driver).getWebDriver();
			executor.executeScript("arguments[0].click();", element);
			//((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			//element.click();
			element.sendKeys(valuetoType);
		}		
		
		return isPresent;
		}
	
	public static boolean javaScriptClickObject(CraftDriver driver, String identifyBy,String locator) {
		boolean isPresent=false;
		checkPresence(5, driver, identifyBy, locator);
		WebElement element=getWebElement(driver, identifyBy, locator);
		if (isElementPresent(driver, identifyBy, locator) ||element.isEnabled()) {
			isPresent = true;
			JavascriptExecutor executor = (JavascriptExecutor)((CraftDriver) driver).getWebDriver();
			executor.executeScript("arguments[0].focus();", element);
			executor.executeScript("arguments[0].click();", element);
		}
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return isPresent;
	}
	
	
	
	public static boolean javaScriptClickElement(CraftDriver driver, WebElement WElement) {
		boolean isPresent=false;
		JavascriptExecutor executor = (JavascriptExecutor)(driver).getWebDriver();
		executor.executeScript("arguments[0].click();", WElement);
			isPresent=true;
			return isPresent;
	}
	
	
	
}