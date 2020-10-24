package com.theaa.dip.automation.ua.framework.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.theaa.dip.automation.ua.framework.FrameworkException;
import com.theaa.dip.automation.ua.framework.FrameworkParameters;
import com.theaa.dip.automation.ua.framework.ReportSettings;
import com.theaa.dip.automation.ua.framework.ReportTheme;
import com.theaa.dip.automation.ua.framework.ReportThemeFactory;
import com.theaa.dip.automation.ua.framework.Settings;
import com.theaa.dip.automation.ua.framework.TimeStamp;
import com.theaa.dip.automation.ua.framework.Util;
import com.theaa.dip.automation.ua.framework.WhitelistingPath;
import com.theaa.dip.automation.ua.framework.ReportThemeFactory.Theme;



/**
 * Singleton class that manages the result summary creation during a batch
 * execution
 * 
 * @author Cognizant
 */
public class ResultSummaryManager {
	private SeleniumReport summaryReport;

	private ReportSettings reportSettings;
	private String reportPath;

	private Date overallStartTime, overallEndTime;

	private Properties properties;
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	private static final ResultSummaryManager RESULT_SUMMARY_MANAGER = new ResultSummaryManager();
	private SeleniumTestParameters testParameters;

	private ResultSummaryManager() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the singleton instance of the
	 * {@link ResultSummaryManager} object
	 * 
	 * @return Instance of the {@link ResultSummaryManager} object
	 */
	public static ResultSummaryManager getInstance() {
		return RESULT_SUMMARY_MANAGER;
	}

	public String getReportPath() {
		return this.reportPath;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Function to set the absolute path of the framework (to be used as a
	 * relative path)
	 */
	public void setRelativePath() {
		String encryptedPath = WhitelistingPath.cleanStringForFilePath(System.getProperty("user.dir"));
		String relativePath = new File(encryptedPath).getAbsolutePath();
		if (relativePath.contains("supportlibraries")) {
			relativePath = new File(encryptedPath).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	/**
	 * Function to initialize the test batch execution
	 * 
	 * @param runConfiguration
	 *            The run configuration to be executed
	 */
	public void initializeTestBatch(String runConfiguration) {
		overallStartTime = Util.getCurrentTime();

		properties = Settings.getInstance();

		frameworkParameters.setRunConfiguration(runConfiguration);

		frameworkParameters
				.setStartCapturingObjects(Boolean.parseBoolean(properties.getProperty("StartCapturingObjects")));

		frameworkParameters.setHealObject(Boolean.parseBoolean(properties.getProperty("HealObjects")));

		frameworkParameters.setForceHeal(Boolean.parseBoolean(properties.getProperty("ForceHeal")));
	}

	/**
	 * Function to initialize the summary report
	 * 
	 * @param nThreads
	 *            The number of parallel threads configured for the test batch
	 *            execution
	 */
	public void initializeSummaryReport(int nThreads) {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory
				.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));

		summaryReport = new SeleniumReport(reportSettings, reportTheme, testParameters);

		summaryReport.initialize();
		summaryReport.initializeResultSummary();

		createResultSummaryHeader(nThreads);
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(reportPath, "");

		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties.getProperty("HtmlReport")));
		reportSettings.setLinkTestLogsToSummary(true);
	}

	private void createResultSummaryHeader(int nThreads) {
		summaryReport
				.addResultSummaryHeading(reportSettings.getProjectName() + " - Automation Execution Results Summary");
		summaryReport.addResultSummarySubHeading("Date & Time",
				": " + Util.getFormattedTime(overallStartTime, properties.getProperty("DateFormatString")), "OnError",
				": " + properties.getProperty("OnError"));
		summaryReport.addResultSummarySubHeading("Run Configuration", ": " + frameworkParameters.getRunConfiguration(),
				"No. of threads", ": " + nThreads);

		summaryReport.addResultSummaryTableHeadings();
	}

	/**
	 * Function to set up the error log file within the test report
	 */
	public void setupErrorLog() {
		String errorLogFile = reportPath + Util.getFileSeparator() + "ErrorLog.txt";
		String encryptedPath = WhitelistingPath.cleanStringForFilePath(errorLogFile);
		try {
			System.setErr(new PrintStream(new FileOutputStream(encryptedPath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link SeleniumTestParameters} object containing the
	 *            details of the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public void updateResultSummary(SeleniumTestParameters testParameters, String testReportName, String executionTime,
			String testStatus) {
		summaryReport.updateResultSummary(testParameters, testReportName, executionTime, testStatus);
	}

	/**
	 * Function to do the required wrap-up activities after completing the test
	 * batch execution
	 * 
	 * @param testExecutedInUnitTestFramework
	 *            Boolean variable indicating whether the test is executed in
	 *            JUnit/TestNG
	 */
	public void wrapUp(Boolean testExecutedInUnitTestFramework) {
		overallEndTime = Util.getCurrentTime();
		String totalExecutionTime = Util.getTimeDifference(overallStartTime, overallEndTime);
		summaryReport.addResultSummaryFooter(totalExecutionTime);

		/*Commenting below to avoid capturing testNG css and Reports*/
		/*String encrpytedResultSrc = WhitelistingPath.cleanStringForFilePath(frameworkParameters.getRelativePath()
				+ Util.getFileSeparator() + properties.getProperty("TestNgReportPath") + Util.getFileSeparator()
				+ frameworkParameters.getRunConfiguration());

		String encryptedCss = WhitelistingPath
				.cleanStringForFilePath(frameworkParameters.getRelativePath() + Util.getFileSeparator()
						+ properties.getProperty("TestNgReportPath") + Util.getFileSeparator() + "testng.css");

		if (testExecutedInUnitTestFramework && System.getProperty("ReportPath") == null) {
			File testNgResultSrc = new File(encrpytedResultSrc);
			File testNgResultCssFile = new File(encryptedCss);
			File testNgResultDest = summaryReport.createResultsSubFolder("TestNG Results");

			try {
				FileUtils.copyDirectoryToDirectory(testNgResultSrc, testNgResultDest);
				FileUtils.copyFileToDirectory(testNgResultCssFile, testNgResultDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

	/**
	 * Function to launch the summary report at the end of the test batch
	 * execution
	 */
	public void launchResultSummary() {
		if (reportSettings.shouldGenerateHtmlReports()) {
			try {
				/**
				 * Use this Area for Sending any Mails through framework
				 */
				String encryptedPath = WhitelistingPath
						.cleanStringForFilePath(reportPath + Util.getFileSeparator() + "ErrorLog.txt");
				if (Boolean.parseBoolean(properties.getProperty("LaunchCRAFTCentral"))) {
					URI url = null;
					try {
						url = new URI(properties.getProperty("CRAFTCentralURL"));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					java.awt.Desktop.getDesktop().browse(url);
				} else {
					if (checkExceptionInErrorLogTxt()) {
						File f = new File(encryptedPath);
						java.awt.Desktop.getDesktop().edit(f);
					} else {
						String encryptedHtml = WhitelistingPath.cleanStringForFilePath(reportPath
								+ Util.getFileSeparator() + "HTML Results" + Util.getFileSeparator() + "Summary.Html");
						File htmlFile = new File(encryptedHtml);
						java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("share Path"+properties.getProperty("ShareIt"));
        String data=properties.getProperty("ShareIt");
        if(data.equalsIgnoreCase("Yes"))
        {
              String source = reportPath;
              System.out.println("Report Path:"+source);
              File srcDir = new File(source);
              String[] tStamp=source.split("Run_");
              String destination=properties.getProperty("SharePath")+"\\Digital_Automation_"+tStamp[1];
              File destDir = new File(destination);
              try {
                    FileUtils.copyDirectory(srcDir, destDir);
              } catch (IOException e) {
                    e.printStackTrace();
              }
        }


	}

	@SuppressWarnings("resource")
	private boolean checkExceptionInErrorLogTxt() throws IOException {
		boolean isException = false;
		String encryptedPath = WhitelistingPath
				.cleanStringForFilePath(reportPath + Util.getFileSeparator() + "ErrorLog.txt");
		File file = new File(encryptedPath);

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("Exception")) {
					isException = true;
					break;
				} else {
					isException = false;
				}
			}
		} catch (FileNotFoundException e) {
		}
		return isException;

	}
	
	
	
	

}