package com.theaa.dip.automation.ua.craft;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.theaa.dip.automation.ua.framework.APIReusuableLibrary;
import com.theaa.dip.automation.ua.framework.CraftDataTable;
import com.theaa.dip.automation.ua.framework.ExcelDataAccess;
import com.theaa.dip.automation.ua.framework.FrameworkException;
import com.theaa.dip.automation.ua.framework.FrameworkParameters;
import com.theaa.dip.automation.ua.framework.IterationOptions;
import com.theaa.dip.automation.ua.framework.ObjectMachinate;
import com.theaa.dip.automation.ua.framework.OnError;
import com.theaa.dip.automation.ua.framework.ReportSettings;
import com.theaa.dip.automation.ua.framework.ReportTheme;
import com.theaa.dip.automation.ua.framework.ReportThemeFactory;
import com.theaa.dip.automation.ua.framework.Settings;
import com.theaa.dip.automation.ua.framework.Status;
import com.theaa.dip.automation.ua.framework.TestCaseBean;
import com.theaa.dip.automation.ua.framework.TimeStamp;
import com.theaa.dip.automation.ua.framework.Util;
import com.theaa.dip.automation.ua.framework.WhitelistingPath;
import com.theaa.dip.automation.ua.framework.ReportThemeFactory.Theme;
import com.theaa.dip.automation.ua.framework.selenium.AppiumDriverFactory;
import com.theaa.dip.automation.ua.framework.selenium.Browser;
import com.theaa.dip.automation.ua.framework.selenium.BrowserStackDriverFactory;
import com.theaa.dip.automation.ua.framework.selenium.CraftDriver;
import com.theaa.dip.automation.ua.framework.selenium.ExecutionMode;
import com.theaa.dip.automation.ua.framework.selenium.FastestDriverFactory;
import com.theaa.dip.automation.ua.framework.selenium.MobileExecutionPlatform;
import com.theaa.dip.automation.ua.framework.selenium.PerfectoDriverFactory;
import com.theaa.dip.automation.ua.framework.selenium.RemoteWebDriverUtils;
import com.theaa.dip.automation.ua.framework.selenium.SauceLabsDriverFactory;
import com.theaa.dip.automation.ua.framework.selenium.SeleniumReport;
import com.theaa.dip.automation.ua.framework.selenium.SeleniumTestParameters;
import com.theaa.dip.automation.ua.framework.selenium.ToolName;
import com.theaa.dip.automation.ua.framework.selenium.WebDriverFactory;
import com.theaa.dip.automation.ua.framework.selenium.WebDriverUtil;

import io.appium.java_client.AppiumDriver;

/**
 * Driver script class which encapsulates the core logic of the framework
 * 
 * @author Cognizant
 */
public class DriverScript {
	private List<String> businessFlowData;
	private int currentIteration, currentSubIteration;
	private Date startTime, endTime;
	private String executionTime;

	private CraftDataTable dataTable;
	private ReportSettings reportSettings;
	private SeleniumReport report;
	private CRAFTLiteTestCase testCase;

	private CraftDriver driver;

	private WebDriverUtil driverUtil;
	private ScriptHelper scriptHelper;

	private Properties properties;
	private Properties mobileProperties;
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	private Boolean linkScreenshotsToTestLog = true;

	private final SeleniumTestParameters testParameters;
	private String reportPath;

	private APIReusuableLibrary apiDriver = new APIReusuableLibrary();
	public ExtentReports extentReport;
	public ExtentTest extentTest;
	public Map<String, String> reusableHandle = new HashMap<>();

	private ObjectMachinate objectHandling;
	private WebDriver pureWebDriver;

	/**
	 * DriverScript constructor
	 * 
	 * @param testParameters
	 *            A {@link SeleniumTestParameters} object
	 */
	public DriverScript(SeleniumTestParameters testParameters) {
		this.testParameters = testParameters;
		this.extentReport = testParameters.getExtentReport();
		this.extentTest = testParameters.getExtentTest();
	}

	/**
	 * Function to configure the linking of screenshots to the corresponding
	 * test log
	 * 
	 * @param linkScreenshotsToTestLog
	 *            Boolean variable indicating whether screenshots should be
	 *            linked to the corresponding test log
	 */
	public void setLinkScreenshotsToTestLog(Boolean linkScreenshotsToTestLog) {
		this.linkScreenshotsToTestLog = linkScreenshotsToTestLog;
	}

	/**
	 * Function to get the name of the test report
	 * 
	 * @return The test report name
	 */
	public String getReportName() {
		return reportSettings.getReportName();
	}

	/**
	 * Function to get the status of the test case executed
	 * 
	 * @return The test status
	 */
	public String getTestStatus() {
		return report.getTestStatus();
	}

	/**
	 * Function to get the decription of any failure that may occur during the
	 * script execution
	 * 
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription() {
		return report.getFailureDescription();
	}

	/**
	 * Function to get the execution time for the test case
	 * 
	 * @return The test execution time
	 */
	public String getExecutionTime() {
		return executionTime;
	}

	/**
	 * Function to get the testcasebean object for the test case
	 * 
	 * @return The testcasebean object
	 */
	public TestCaseBean getTestCaseBean() {
		return report.getTestCaseBean();
	}

	/**
	 * Function to execute the given test case
	 */
	public void driveTestExecution() {
		startUp();
		initializeTestIterations();
		initializeWebDriver();
		initializeTestReport();
		initializeDatatable();
		executeCraftOrCraftLite();
		quitWebDriver();
		wrapUp();
	}

	private void executeCraftOrCraftLite() {

		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			executeCraft();
		} else {
			initializeTestCase();
			try {
				testCase.setUp();
				executeCRAFTLiteTestIterations();
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			} finally {
				testCase.tearDown(); // tearDown will ALWAYS be called
			}
		}
	}

	private void executeCraft() {
		initializeTestScript();
		executeCRAFTTestIterations();
	}

	private void startUp() {
		startTime = Util.getCurrentTime();

		properties = Settings.getInstance();
		mobileProperties = Settings.getMobilePropertiesInstance();

		setDefaultTestParameters();
		setExtentTestcase();
	}

	private void setExtentTestcase() {
		extentTest = extentReport.createTest(
				testParameters.getCurrentTestcase() + "-" + testParameters.getCurrentTestInstance(),
				testParameters.getCurrentTestDescription());

	}

	private void setDefaultTestParameters() {
		if (testParameters.getIterationMode() == null) {
			testParameters.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
		}

		if (testParameters.getExecutionMode() == null) {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties.getProperty("DefaultExecutionMode")));
		}

		if (testParameters.getMobileExecutionPlatform() == null) {
			testParameters.setMobileExecutionPlatform(
					MobileExecutionPlatform.valueOf(mobileProperties.getProperty("DefaultMobileExecutionPlatform")));
		}

		if (testParameters.getMobileToolName() == null) {
			testParameters.setMobileToolName(ToolName.valueOf(mobileProperties.getProperty("DefaultMobileToolName")));
		}

		if (testParameters.getDeviceName() == null) {
			testParameters.setDeviceName(mobileProperties.getProperty("DefaultDevice"));
		}

		if (testParameters.getBrowser() == null) {
			testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
		}

		if (testParameters.getPlatform() == null) {
			testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));
		}

		if (testParameters.getSeeTestPort() == null) {
			testParameters.setSeeTestPort(mobileProperties.getProperty("SeeTestDefaultPort"));
		}

		testParameters.setInstallApplication(
				Boolean.parseBoolean(mobileProperties.getProperty("InstallApplicationInDevice")));

	}

	private void initializeTestIterations() {
		switch (testParameters.getIterationMode()) {
		case RUN_ALL_ITERATIONS:
			int nIterations = getNumberOfIterations();
			testParameters.setEndIteration(nIterations);

			currentIteration = 1;
			break;

		case RUN_ONE_ITERATION_ONLY:
			currentIteration = 1;
			break;

		case RUN_RANGE_OF_ITERATIONS:
			if (testParameters.getStartIteration() > testParameters.getEndIteration()) {
				throw new FrameworkException("Error", "StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = testParameters.getStartIteration();
			break;

		default:
			throw new FrameworkException("Unhandled Iteration Mode!");
		}
	}

	private int getNumberOfIterations() {
		String encryptedDatatablePath = WhitelistingPath.cleanStringForFilePath(
				frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src" + Util.getFileSeparator()
						+ "main" + Util.getFileSeparator() + "resources" + Util.getFileSeparator() + "Datatables");
		String datatablePath = encryptedDatatablePath;
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, testParameters.getCurrentScenario());
		testDataAccess.setDatasheetName(properties.getProperty("DefaultDataSheet"));

		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			int startRowNum = testDataAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
			int nTestcaseRows = testDataAccess.getRowCount(testParameters.getCurrentTestcase(), 0, startRowNum);
			int nSubIterations = testDataAccess.getRowCount("1", 1, startRowNum); // Assumption:
																					// Every
																					// test
																					// case
																					// will
																					// have
																					// at
																					// least
																					// one
																					// iteration
			return nTestcaseRows / nSubIterations;
		} else {
			return testDataAccess.getRowCount(testParameters.getCurrentTestcase(), 0);
		}

	}

	@SuppressWarnings("rawtypes")
	private void initializeWebDriver() {

		switch (testParameters.getExecutionMode()) {
		case API:
			break;

		case LOCAL:
			WebDriver webDriver = WebDriverFactory.getWebDriver(testParameters.getBrowser());
			driver = new CraftDriver(webDriver);
			pureWebDriver = webDriver;
			driver.setTestParameters(testParameters);
			maximizeWindow();
			break;

		case GRID:
		
			WebDriver remoteGridDriver = WebDriverFactory.getRemoteWebDriver(testParameters.getBrowser(),
					testParameters.getBrowserVersion(), testParameters.getPlatform(),
					properties.getProperty("RemoteUrl"));
			driver = new CraftDriver(remoteGridDriver);
			pureWebDriver = remoteGridDriver;
			driver.setTestParameters(testParameters);
			maximizeWindow();
			break;

		case MOBILE:
			WebDriver appiumDriver = AppiumDriverFactory.getAppiumDriver(testParameters.getMobileExecutionPlatform(),
					testParameters.getDeviceName(), testParameters.getMobileOSVersion(),
					testParameters.shouldInstallApplication(), mobileProperties.getProperty("AppiumURL"));
			driver = new CraftDriver(appiumDriver);
			pureWebDriver = appiumDriver;
			driver.setTestParameters(testParameters);

			break;

		case PERFECTO:
			if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				WebDriver appiumPerfectoDriver = PerfectoDriverFactory.getPerfectoAppiumDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						mobileProperties.getProperty("PerfectoHost"));
				driver = new CraftDriver(appiumPerfectoDriver);
				pureWebDriver = appiumPerfectoDriver;
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				WebDriver remotePerfectoDriver = PerfectoDriverFactory
						.getPerfectoRemoteWebDriverForDesktop(testParameters);
				driver = new CraftDriver(remotePerfectoDriver);
				pureWebDriver = remotePerfectoDriver;
				driver.setTestParameters(testParameters);
			}

			break;

		case SAUCELABS:
			if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				AppiumDriver appiumSauceDriver = SauceLabsDriverFactory.getSauceAppiumDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						mobileProperties.getProperty("SauceHost"), testParameters);
				driver = new CraftDriver(appiumSauceDriver);
				pureWebDriver = appiumSauceDriver;
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				WebDriver remoteSauceDriver = SauceLabsDriverFactory.getSauceRemoteWebDriver(
						mobileProperties.getProperty("SauceHost"), testParameters.getBrowser(),
						testParameters.getBrowserVersion(), testParameters.getPlatform(), testParameters);
				driver = new CraftDriver(remoteSauceDriver);
				pureWebDriver = remoteSauceDriver;
				driver.setTestParameters(testParameters);
			}

			break;

		case TESTOBJECT:

			WebDriver testObjectAppiumDriver = SauceLabsDriverFactory.getTestObjectAppiumDriver(
					testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
					mobileProperties.getProperty("TestObjectHost"), testParameters);
			driver = new CraftDriver(testObjectAppiumDriver);
			pureWebDriver = testObjectAppiumDriver;
			driver.setTestParameters(testParameters);

			break;

		case FASTEST:
			if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				WebDriver fastestRemoteDriver = FastestDriverFactory.getRemoteWebDriver(testParameters.getBrowser(),
						testParameters.getBrowserVersion(), testParameters.getPlatform(),
						mobileProperties.getProperty("FastestHost"), testParameters.getCurrentTestcase());
				driver = new CraftDriver(fastestRemoteDriver);
				pureWebDriver = fastestRemoteDriver;
				driver.setTestParameters(testParameters);
			} else if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				WebDriver mintAppiumtDriver = FastestDriverFactory.getMintAppiumDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						mobileProperties.getProperty("MintHost"), testParameters.getMobileOSVersion());
				driver = new CraftDriver(mintAppiumtDriver);
				pureWebDriver = mintAppiumtDriver;
				driver.setTestParameters(testParameters);
			}

			break;

		case BROWSERSTACK:
			if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				WebDriver browserstackRemoteDrivermobile = BrowserStackDriverFactory
						.getBrowserStackRemoteWebDriverMobile(testParameters.getMobileExecutionPlatform(),
								testParameters.getDeviceName(), mobileProperties.getProperty("BrowserStackHost"),
								testParameters);
				driver = new CraftDriver(browserstackRemoteDrivermobile);
				pureWebDriver = browserstackRemoteDrivermobile;
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(ToolName.DEFAULT)) {
				WebDriver browserstackRemoteDriver = BrowserStackDriverFactory.getBrowserStackRemoteWebDriver(
						mobileProperties.getProperty("BrowserStackHost"), testParameters.getBrowser(),
						testParameters.getBrowserVersion(), testParameters.getPlatform(), testParameters);

				driver = new CraftDriver(browserstackRemoteDriver);
				pureWebDriver = browserstackRemoteDriver;
				driver.setTestParameters(testParameters);
			}

			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}
	}

	private void maximizeWindow() {
		driver.manage().window().maximize();
	}

	private void initializeTestReport() {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory
				.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));

		report = new SeleniumReport(reportSettings, reportTheme, testParameters);

		report.initialize();
		setReportParameters();
		report.initializeTestLog();
		createTestLogHeader();
	}

	private void setReportParameters() {
		report.setExtentTest(extentTest);
		report.setDriver(driver);
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(reportPath, testParameters.getCurrentScenario() + "_"
				+ testParameters.getCurrentTestcase() + "_" + testParameters.getCurrentTestInstance());

		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setLogLevel(Integer.parseInt(properties.getProperty("LogLevel")));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties.getProperty("HtmlReport")));
		reportSettings.setGenerateSeeTestReports(
				Boolean.parseBoolean(mobileProperties.getProperty("SeeTestReportGeneration")));
		reportSettings.setGeneratePerfectoReports(
				Boolean.parseBoolean(mobileProperties.getProperty("PerfectoReportGeneration")));
		reportSettings
				.setTakeScreenshotFailedStep(Boolean.parseBoolean(properties.getProperty("TakeScreenshotFailedStep")));
		reportSettings
				.setTakeScreenshotPassedStep(Boolean.parseBoolean(properties.getProperty("TakeScreenshotPassedStep")));
		if (isAPITest()) {
			reportSettings.setTakeScreenshotFailedStep(false);
			reportSettings.setTakeScreenshotPassedStep(false);
		}
		reportSettings.setConsolidateScreenshotsInWordDoc(
				Boolean.parseBoolean(properties.getProperty("ConsolidateScreenshotsInWordDoc")));
		reportSettings.setisMobileExecution(isMobileAutomation());
		reportSettings.setAPIAutomation(isAPITest());

		reportSettings.setLinkScreenshotsToTestLog(this.linkScreenshotsToTestLog);
	}

	private void createTestLogHeader() {
		report.addTestLogHeading(reportSettings.getProjectName() + " - " + reportSettings.getReportName()
				+ " Automation Execution Results");
		report.addTestLogSubHeading("Date & Time",
				": " + Util.getFormattedTime(startTime, properties.getProperty("DateFormatString")), "Iteration Mode",
				": " + testParameters.getIterationMode());
		report.addTestLogSubHeading("Start Iteration", ": " + testParameters.getStartIteration(), "End Iteration",
				": " + testParameters.getEndIteration());

		switch (testParameters.getExecutionMode()) {
		case API:
			report.addTestLogSubHeading("Execution Mode", ": " + "API", "Execution on", ": " + "Local Machine");
			break;
		case LOCAL:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Execution on", ": " + "Local Machine");
			break;

		case GRID:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Executed on", ": " + "Grid @ " + properties.getProperty("RemoteUrl"));
			break;

		case MOBILE:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());
			break;

		case PERFECTO:

			if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Browser/Platform",
						": " + testParameters.getBrowserAndPlatform());
				report.addTestLogSubHeading("Executed on",
						": " + "Perfecto MobileCloud @ " + mobileProperties.getProperty("PerfectoHost"), "", "");
			} else if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
						": " + testParameters.getDeviceName());
				report.addTestLogSubHeading("Executed on",
						": " + "Perfecto MobileCloud @ " + mobileProperties.getProperty("PerfectoHost"),
						"Perfecto User", ": " + mobileProperties.getProperty("PerfectoUser"));
			}
			break;

		case TESTOBJECT:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());
			report.addTestLogSubHeading("Executed on",
					": " + "TestObject @ " + mobileProperties.getProperty("SauceHost"), "", "");
			break;

		case SAUCELABS:
			if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Browser/Platform",
						": " + testParameters.getBrowserAndPlatform());
				report.addTestLogSubHeading("Executed on",
						": " + "Sauce Labs @ " + mobileProperties.getProperty("SauceHost"), "", "");
			} else if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
						": " + testParameters.getDeviceName());
				report.addTestLogSubHeading("Executed on",
						": " + "Sauce Labs @ " + mobileProperties.getProperty("SauceHost"), "", "");
			}
			break;

		case FASTEST:

			if (testParameters.getMobileToolName().equals(ToolName.REMOTE_WEBDRIVER)) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Browser/Platform",
						": " + testParameters.getBrowserAndPlatform());
				report.addTestLogSubHeading("Executed on",
						": " + "Mint Cloud @ " + mobileProperties.getProperty("MintHost"), "Mint User",
						": " + mobileProperties.getProperty("MintUsername"));

			} else if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device OS version",
						": " + testParameters.getMobileOSVersion());
				report.addTestLogSubHeading("Executed on",
						": " + "Mint Cloud @ " + mobileProperties.getProperty("MintHost"), "Mint User",
						": " + mobileProperties.getProperty("MintUsername"));
			}

			break;

		case BROWSERSTACK:
			if (testParameters.getMobileToolName().toString().equalsIgnoreCase("REMOTE_WEBDRIVER")) {
				report.addTestLogSubHeading("ExecutionPlatform", ": " + testParameters.getExecutionMode(),
						"Executed on", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
						": " + testParameters.getDeviceName());
				report.addTestLogSubHeading("Executed on",
						": " + "BrowserStack @ " + mobileProperties.getProperty("BrowserStackHost"), "Browser Stack",
						": " + mobileProperties.getProperty("BrowserStackHost"));
			} else {
				report.addTestLogSubHeading("ExecutionPlatform", ": " + testParameters.getExecutionMode(),
						"Executed on", ": " + testParameters.getPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Browser/Platform",
						": " + testParameters.getBrowserAndPlatform());
				report.addTestLogSubHeading("Executed on",
						": " + "BrowserStack @ " + mobileProperties.getProperty("BrowserStackHost"), "Sauce User",
						": " + mobileProperties.getProperty("BrowserStackHost"));
			}
			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}

		report.addTestLogTableHeadings();
	}

	private synchronized void initializeDatatable() {
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src"
				+ Util.getFileSeparator() + "main" + Util.getFileSeparator() + "resources" + Util.getFileSeparator()
				+ "Datatables";
		String encryptedDatatablePath = WhitelistingPath.cleanStringForFilePath(
				datatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");

		String runTimeDatatablePath;
		Boolean includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));
		if (includeTestDataInReport) {
			runTimeDatatablePath = reportPath + Util.getFileSeparator() + "Datatables";
			String encryptedRunTimeDatatablePath = WhitelistingPath.cleanStringForFilePath(
					runTimeDatatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");

			File runTimeDatatable = new File(encryptedRunTimeDatatablePath);
			if (!runTimeDatatable.exists()) {
				File datatable = new File(encryptedDatatablePath);

				try {
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the datatable failed...");
				}
			}

			String encryptedRunTimeCommonDatatable = WhitelistingPath
					.cleanStringForFilePath(runTimeDatatablePath + Util.getFileSeparator() + "Common Testdata.xls");
			File runTimeCommonDatatable = new File(encryptedRunTimeCommonDatatable);
			if (!runTimeCommonDatatable.exists()) {
				String encryptedCommonDatatable = WhitelistingPath
						.cleanStringForFilePath(datatablePath + Util.getFileSeparator() + "Common Testdata.xls");
				File commonDatatable = new File(encryptedCommonDatatable);

				try {
					FileUtils.copyFile(commonDatatable, runTimeCommonDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the common datatable failed...");
				}
			}
		} else {
			runTimeDatatablePath = datatablePath;
		}

		dataTable = new CraftDataTable(runTimeDatatablePath, testParameters.getCurrentScenario());
		dataTable.setDataReferenceIdentifier(properties.getProperty("DataReferenceIdentifier"));

		// CRAFTLite Change
		if (properties.getProperty("Approach").equalsIgnoreCase("ModularDriven")) {
			// Initialize the datatable row in case test data is required during
			// the setUp()
			dataTable.setCurrentRow(testParameters.getCurrentTestcase(), currentIteration, 0);
		}

	}

	private void initializeTestScript() {
		driverUtil = new WebDriverUtil(driver);
		apiDriver.setRport(report);
		scriptHelper = new ScriptHelper(dataTable, report, driver, driverUtil, apiDriver, extentTest, reusableHandle);
		if (!isAPITest()) {
			driver.setRport(report);
			objectHandling = new ObjectMachinate(report,pureWebDriver,frameworkParameters,testParameters);
			driver.setObjectHandling(objectHandling);
		}
		
		initializeBusinessFlow();
	}

	private void initializeBusinessFlow() {
		String encryptedBusinessFlowAccess = WhitelistingPath.cleanStringForFilePath(
				frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src" + Util.getFileSeparator()
						+ "main" + Util.getFileSeparator() + "resources" + Util.getFileSeparator() + "Datatables");
		ExcelDataAccess businessFlowAccess = new ExcelDataAccess(encryptedBusinessFlowAccess,
				testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("Business_Flow");

		int rowNum = businessFlowAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
		if (rowNum == -1) {
			throw new FrameworkException("The test case \"" + testParameters.getCurrentTestcase()
					+ "\" is not found in the Business Flow sheet!");
		}

		String dataValue;
		businessFlowData = new ArrayList<String>();
		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if ("".equals(dataValue)) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}

		if (businessFlowData.isEmpty()) {
			throw new FrameworkException(
					"No business flow found against the test case \"" + testParameters.getCurrentTestcase() + "\"");
		}
	}

	private void executeCRAFTTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: " + Integer.toString(currentIteration));
			report.setIteration(currentIteration);
			// Evaluate each test iteration for any errors
			try {
				executeTestcase(businessFlowData);
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (InvocationTargetException ix) {
				exceptionHandler((Exception) ix.getCause(), "Error");
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
		}
	}

	private void executeTestcase(List<String> businessFlowData)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, IOException {
		Map<String, Integer> keywordDirectory = new HashMap<String, Integer>();

		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum).split(",");
			String currentKeyword = currentFlowData[0];

			int nKeywordIterations;
			if (currentFlowData.length > 1) {
				nKeywordIterations = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIterations = 1;
			}

			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIterations; currentKeywordIteration++) {
				if (keywordDirectory.containsKey(currentKeyword)) {
					keywordDirectory.put(currentKeyword, keywordDirectory.get(currentKeyword) + 1);
				} else {
					keywordDirectory.put(currentKeyword, 1);
				}
				currentSubIteration = keywordDirectory.get(currentKeyword);

				dataTable.setCurrentRow(testParameters.getCurrentTestcase(), currentIteration, currentSubIteration);

				if (currentSubIteration > 1) {
					report.addTestLogSubSection(currentKeyword + " (Sub-Iteration: " + currentSubIteration + ")");
					report.setSubIteration(currentSubIteration);
				} else {
					report.addTestLogSubSection(currentKeyword);
				}

				invokeBusinessComponent(currentKeyword);
			}
		}
	}

	private void invokeBusinessComponentOld(String currentKeyword)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		/*String encryptedBusinessComponentsPath = WhitelistingPath
				.cleanStringForFilePath(frameworkParameters.getRelativePath() + Util.getFileSeparator() + "bin"
						+ Util.getFileSeparator() + "businesscomponents");
		String encryptedComponentGroupsPath = WhitelistingPath
				.cleanStringForFilePath(frameworkParameters.getRelativePath() + Util.getFileSeparator() + "bin"
						+ Util.getFileSeparator() + "componentgroups");*/

		 String encryptedBusinessComponentsPath = WhitelistingPath
					.cleanStringForFilePath(frameworkParameters.getRelativePath() + Util.getFileSeparator() 
							+ "build" + Util.getFileSeparator() + "classes" + Util.getFileSeparator() + "java" + Util.getFileSeparator() 
							+ "main" + Util.getFileSeparator() + "com" + Util.getFileSeparator()+ "theaa" + Util.getFileSeparator()+ "dip" + Util.getFileSeparator() +"automation" + Util.getFileSeparator() +"mobileapp" + Util.getFileSeparator()+ "businesscomponents");
		 String encryptedComponentGroupsPath = WhitelistingPath
					.cleanStringForFilePath(frameworkParameters.getRelativePath() + Util.getFileSeparator() 
					+ "build" + Util.getFileSeparator() + "classes" + Util.getFileSeparator() + "java" + Util.getFileSeparator() 
					+ "main" + Util.getFileSeparator() + "com" + Util.getFileSeparator()+ "theaa" + Util.getFileSeparator()+ "dip" + Util.getFileSeparator() +"automation" + Util.getFileSeparator() +"mobileapp" + Util.getFileSeparator()+ "componentgroups");

		
		File[] packageDirectories = { new File(encryptedBusinessComponentsPath),
				new File(encryptedComponentGroupsPath) };

		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = "com.theaa.dip.automation.mobileapp."+packageDirectory.getName();
			
			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();
				
				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length() - CLASS_FILE_EXTENSION.length());

					String encryptedReusableComponents = WhitelistingPath
							.cleanStringForFilePath(packageName + "." + className);
					Class<?> reusableComponents = Class.forName(encryptedReusableComponents);
					Method executeComponent;

					try {
						// Convert the first letter of the method to lowercase
						// (in line with java naming conventions)
						currentKeyword = currentKeyword.substring(0, 1).toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the
						// next class
						continue;
					}

					isMethodFound = true;

					report.setCurrentClassName(className);
					report.setBusinessComponent(currentKeyword);

					Constructor<?> ctor = reusableComponents.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);

					executeComponent.invoke(businessComponent, (Object[]) null);

					break;
				}
			}
		}

		if (!isMethodFound) {
			throw new FrameworkException("Keyword " + currentKeyword + " not found within any class "
					+ "inside the businesscomponents package");
		}
	}
	
	private void invokeBusinessComponent(String currentKeyword)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		Boolean isMethodFound = false;

			String packageName = "com.theaa.dip.automation.ua.businesscomponents.";
			List<String> classFiles = new ArrayList();
			classFiles.add("PegaHomePage");
			classFiles.add("CreateAccount");
			classFiles.add("UAGeneralFunc");
			classFiles.add("ForgotPasswordPage");
			classFiles.add("HomePage");
			classFiles.add("ChangeUserName");
			
			for(int i=0;i<classFiles.size();i++) {

					Class<?> reusableComponents = Class.forName(packageName+classFiles.get(i));
					Method executeComponent;

					try {
						// Convert the first letter of the method to lowercase
						// (in line with java naming conventions)
						currentKeyword = currentKeyword.substring(0, 1).toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the
						// next class
						continue;
					}

					isMethodFound = true;

					report.setCurrentClassName(classFiles.get(i));
					report.setBusinessComponent(currentKeyword);

					Constructor<?> ctor = reusableComponents.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);

					executeComponent.invoke(businessComponent, (Object[]) null);

					break;
			}

		if (!isMethodFound) {
			throw new FrameworkException("Keyword " + currentKeyword + " not found within any class "
					+ "inside the businesscomponents package");
		}
	}
	


	private void exceptionHandler(Exception ex, String exceptionName) {
		// Error reporting
		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null) {
			exceptionDescription = ex.toString();
		}

		if (ex.getCause() != null) {
			report.updateTestLog(exceptionName, exceptionDescription + " <b>Caused by: </b>" + ex.getCause(),
					Status.FAIL);
		} else {
			report.updateTestLog(exceptionName, exceptionDescription, Status.FAIL);
		}

		// Print stack trace for detailed debug information
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		String stackTrace = stringWriter.toString();
		report.updateTestLog("Exception stack trace", stackTrace, Status.DEBUG);

		// Error response
		if (frameworkParameters.getStopExecution()) {
			report.updateTestLog("CRAFT Info", "Test execution terminated by user! All subsequent tests aborted...",
					Status.DONE);
			currentIteration = testParameters.getEndIteration();
		} else {
			OnError onError = OnError.valueOf(properties.getProperty("OnError"));
			switch (onError) {
			// Stop option is not relevant when run from QC
			case NEXT_ITERATION:
				report.updateTestLog("CRAFT Info",
						"Test case iteration terminated by user! Proceeding to next iteration (if applicable)...",
						Status.DONE);
				break;

			case NEXT_TESTCASE:
				report.updateTestLog("CRAFT Info",
						"Test case terminated by user! Proceeding to next test case (if applicable)...", Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;

			case STOP:
				frameworkParameters.setStopExecution(true);
				report.updateTestLog("CRAFT Info", "Test execution terminated by user! All subsequent tests aborted...",
						Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;

			default:
				throw new FrameworkException("Unhandled OnError option!");
			}
		}
	}

	private void quitWebDriver() {
		switch (testParameters.getExecutionMode()) {
		case API:
			break;
		case LOCAL:
		case GRID:
		case MOBILE:
		case SAUCELABS:
		case TESTOBJECT:
		case PERFECTO:
		case BROWSERSTACK:
		case FASTEST:
			driver.quit();
			break;
		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}

	}

	private void wrapUp() {
		endTime = Util.getCurrentTime();
		closeTestReport();
		downloadAddtionalReport();
	}

	private void closeTestReport() {
		executionTime = Util.getTimeDifference(startTime, endTime);
		report.addTestLogFooter(executionTime);

		if (reportSettings.shouldConsolidateScreenshotsInWordDoc()) {
			report.consolidateScreenshotsInWordDoc();
		}

	}

	private void downloadAddtionalReport() {
		if (testParameters.getExecutionMode().equals(ExecutionMode.PERFECTO)
				&& reportSettings.shouldGeneratePerfectoReports()
				&& testParameters.getMobileToolName().equals(ToolName.DEFAULT)) {
			try {
				driver.close();
				String encryptedPdfReportPath = WhitelistingPath.cleanStringForFilePath(
						reportPath + Util.getFileSeparator() + "Perfecto Results" + Util.getFileSeparator()
								+ testParameters.getCurrentScenario() + "_" + testParameters.getCurrentTestcase() + "_"
								+ testParameters.getCurrentTestInstance() + ".pdf");
				RemoteWebDriverUtils.downloadReport((RemoteWebDriver) driver.getWebDriver(), "pdf",
						encryptedPdfReportPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isMobileAutomation() {
		boolean isMobileAutomation = false;
		if (testParameters.getMobileToolName().equals(ToolName.APPIUM)) {
			isMobileAutomation = true;
		}
		return isMobileAutomation;
	}

	private boolean isAPITest() {
		boolean isAPI = false;
		if (testParameters.getExecutionMode().equals(ExecutionMode.API)) {
			isAPI = true;
		}
		return isAPI;
	}

	private void executeCRAFTLiteTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: " + Integer.toString(currentIteration));

			// Evaluate each test iteration for any errors
			try {
				testCase.executeTest();
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
			dataTable.setCurrentRow(testParameters.getCurrentTestcase(), currentIteration, 0);
		}
	}

	private void initializeTestCase() {
		driverUtil = new WebDriverUtil(driver);
		scriptHelper = new ScriptHelper(dataTable, report, driver, driverUtil, apiDriver, extentTest, reusableHandle);
		driver.setRport(report);
		testCase = getTestCaseInstance();
		testCase.initialize(scriptHelper);
	}

	private CRAFTLiteTestCase getTestCaseInstance() {
		Class<?> testScriptClass;
		try {
			testScriptClass = Class.forName(
					"testscripts." + testParameters.getCurrentScenario() + "." + testParameters.getCurrentTestcase());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified test case is not found!");
		}

		try {
			return (CRAFTLiteTestCase) testScriptClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkException("Error while instantiating the specified test script");
		}
	}
}