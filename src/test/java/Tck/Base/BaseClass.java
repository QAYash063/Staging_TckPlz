package Tck.Base;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import Tck.Utilities.ExtentManager;
import Tck.Utilities.SeleniumDriver;

import com.aventstack.extentreports.ExtentReports;

@Listeners(Tck.Listeners.CustomListeners.class)
public class BaseClass {
	
	protected static final Logger log = LogManager.getLogger(BaseClass.class);
	// Thread-safe WebDriver instance
	protected WebDriver driver;

	// Properties
	public static Properties Config;
	public static Properties OR;

	// ExtentReports (initialized but no logging inside BaseClass methods)
	public static ExtentReports extent;

	/*
	 * =============================== DRIVER AND EXTENT REPORT SETUP
	 * ===============================
	 */
	@BeforeSuite(alwaysRun = true)
	public void setUpSuite() {

	    log.info("========= Suite Execution Started =========");

	    // Initialize Extent Reports
	    extent = ExtentManager.getInstance();
	    log.info("Extent Report initialized");

	    Config = new Properties();
	    OR = new Properties();

	    try (FileInputStream configFis = new FileInputStream(
	            System.getProperty("user.dir") + "/src/test/resources/properties/Config.properties");
	         FileInputStream orFis = new FileInputStream(
	            System.getProperty("user.dir") + "/src/test/resources/properties/OR.properties")) {

	        Config.load(configFis);
	        OR.load(orFis);

	        log.info("Properties files loaded successfully");

	    } catch (Exception e) {
	        log.error("Failed to load properties files", e);
	        throw new RuntimeException("Failed to load properties");
	    }

	    // Setup driver
	    SeleniumDriver.setUpDriver();
	    log.info("WebDriver initialized successfully");
	}

	/*
	 * =============================== SMART WAITS ===============================
	 */
	protected WebElement waitForVisibility(By locator) {
		log.debug("Waiting for visibility of element: " + locator);
		return SeleniumDriver.getWaitDriver().until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected WebElement waitForClick(By locator) {
		log.debug("Waiting for Click of element: " + locator);
		return SeleniumDriver.getWaitDriver().until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected WebElement waitForPresence(By locator) {
		log.debug("Waiting for Presence of element: " + locator);
		return SeleniumDriver.getWaitDriver().until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/*
	 * =============================== COMMON UTILITIES
	 * ===============================
	 */
	public void jsClick(WebElement element) {
		((JavascriptExecutor) SeleniumDriver.getDriver())
				.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", element);
	}

	public void jsClear(WebElement input) {
		((JavascriptExecutor) SeleniumDriver.getDriver()).executeScript("arguments[0].value='';", input);
	}

	public boolean isElementPresent(By by) {
		try {
			SeleniumDriver.getDriver().findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/*
	 * =============================== REUSABLE LOGIN METHOD Call manually in tests
	 * when needed ===============================
	 */
	public void doLogin() {

	    log.info("Starting login process");

	    WebDriver driver = SeleniumDriver.getDriver();
	    String email = Config.getProperty("Email");
	    String password = Config.getProperty("Password");

	    log.debug("Navigating to URL: " + Config.getProperty("testSiteUrl"));
	    driver.get(Config.getProperty("testSiteUrl"));

	    log.info("Entering email");
	    waitForPresence(By.xpath(OR.getProperty("EnterEmail"))).sendKeys(email);

	    log.info("Clicking Continue button");
	    waitForClick(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

	    log.info("Entering password");
	    waitForPresence(By.xpath(OR.getProperty("EnterPassword"))).sendKeys(password);

	    log.info("Submitting login");
	    waitForClick(By.xpath(OR.getProperty("SubmitLogin"))).click();

	    log.info("Handling skip phone");
	    waitForClick(By.xpath(OR.getProperty("SkipPhone"))).click();

	    log.info("Login process completed");
	}

	/*
	 * =============================== TEARDOWN SUITE
	 * ===============================
	 */

	@AfterSuite(alwaysRun = true)
	public void tearDownSuite() {

	    log.info("Closing WebDriver...");
	    SeleniumDriver.tearDown();

	    if (extent != null) {
	        extent.flush();
	        log.info("Extent Report flushed successfully");
	    }

      
/*
		try {

			String reportFolder = System.getProperty("user.dir") + "/test-output/Extend-Reports/";

			File dir = new File(reportFolder);

			File latestFile = null;

			for (File file : dir.listFiles()) {

				if (latestFile == null || file.lastModified() > latestFile.lastModified()) {

					latestFile = file;
				}
			}

			if (latestFile != null) {

				System.out.println("Sending Email with Report...");
				System.out.println("Test 1");

				Tck.Utilities.EmailUtils.sendEmailWithReport(latestFile.getAbsolutePath());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

	    log.info("========= Suite Execution Completed =========");
	}
}
