package Tck.Base;

import org.testng.AssertJUnit;

import java.io.File;
import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners(Tck.Listeners.CustomListeners.class)
public class BaseClass {

	public static Properties Config;
	public static Properties OR;
	public static FileInputStream fis;
	public static WebDriver driver;

	public static ExtentReports extent;
	public static ExtentTest test;

	/* ===============================
	        EXTENT REPORT SETUP
	   =============================== */

	@BeforeSuite(alwaysRun = true)
	public void setUpExtentReports() {

		String timeStamp =
				new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

		String reportFolder =
				System.getProperty("user.dir")+ "/test-output/Extend-Reports/";

		String reportName =
				reportFolder + "extentReports_" + timeStamp + ".html";

		new File(reportFolder).mkdirs();

		ExtentSparkReporter htmlReporter =
				new ExtentSparkReporter(reportName);

		htmlReporter.config().setDocumentTitle("Automation Report");
		htmlReporter.config().setReportName("Functional Testing");
		htmlReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("Hostname", "Localhost");
		extent.setSystemInfo("OS", "Windows 10");
		extent.setSystemInfo("Tester Name", "Lalit Fatehpuriya");
		extent.setSystemInfo("Browser", "Chrome");

		System.out.println("Extent Report generated at: " + reportName);
	}

	/* ===============================
	        DRIVER SETUP
	   =============================== */

	@BeforeClass(alwaysRun = true)
	public void setUp() {

		Config = new Properties();
		OR = new Properties();

		try {

			fis = new FileInputStream(
					System.getProperty("user.dir")+ "/src/test/resources/properties/Config.properties");
			Config.load(fis);

			fis = new FileInputStream(
					System.getProperty("user.dir")+ "/src/test/resources/properties/OR.properties");
			OR.load(fis);

		} catch (Exception e) {
			e.printStackTrace();
		}

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.get(Config.getProperty("testSiteUrl"));
		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(
				Integer.parseInt(Config.getProperty("inplicit.wait")),TimeUnit.SECONDS);
	}

	/* ===============================
	        SMART WAITS ⭐
	   =============================== */

	protected WebElement waitForVisibility(By locator) {

	    WebDriverWait wait = new WebDriverWait(driver, 10);

	    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected WebElement waitForClick(By locator) {

	    WebDriverWait wait = new WebDriverWait(driver, 10);

	    return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected WebElement waitForPresence(By locator) {

	    WebDriverWait wait = new WebDriverWait(driver, 10);

	    return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/* ===============================
	        COMMON UTILITIES
	   =============================== */

	public boolean isElementPresent(By by) {

		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	protected void verifyEquals(String actual, String expected) {
		AssertJUnit.assertEquals(actual, expected);
	}

	/* ===============================
	        TEARDOWN
	   =============================== */

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}

	@AfterSuite(alwaysRun = true)
	public void tearDownExtentReports() {

		if (extent != null) {
			extent.flush();
			System.out.println("Extent Report Flushed ✅");
		}
	}
}