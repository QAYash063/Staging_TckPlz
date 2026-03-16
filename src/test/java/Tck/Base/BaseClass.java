package Tck.Base;

import org.testng.Assert;
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
	// Thread-safe WebDriver instance
    protected WebDriver driver;

    // Properties
    public static Properties Config;
    public static Properties OR;

    // ExtentReports (initialized but no logging inside BaseClass methods)
    public static ExtentReports extent;

    /* ===============================
    DRIVER AND EXTENT REPORT SETUP
    =============================== */
 @BeforeSuite(alwaysRun = true)
 public void setUpSuite() {
     // Initialize Extent Reports
     extent = ExtentManager.getInstance();

     // Load properties
     Config = new Properties();
     OR = new Properties();

     try (FileInputStream configFis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/Config.properties");
          FileInputStream orFis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/OR.properties")) {

         Config.load(configFis);
         OR.load(orFis);

     } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Failed to load properties");
     }

     // Setup driver once for the suite
     SeleniumDriver.setUpDriver();
 }

 /* ===============================
    SMART WAITS
    =============================== */
 protected WebElement waitForVisibility(By locator) {
     return SeleniumDriver.getWaitDriver().until(ExpectedConditions.visibilityOfElementLocated(locator));
 }

 protected WebElement waitForClick(By locator) {
     return SeleniumDriver.getWaitDriver().until(ExpectedConditions.elementToBeClickable(locator));
 }

 protected WebElement waitForPresence(By locator) {
     return SeleniumDriver.getWaitDriver().until(ExpectedConditions.presenceOfElementLocated(locator));
 }

 /* ===============================
    COMMON UTILITIES
    =============================== */
 public void jsClick(WebElement element) {
     ((JavascriptExecutor) SeleniumDriver.getDriver())
             .executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", element);
 }

 public void jsClear(WebElement input) {
     ((JavascriptExecutor) SeleniumDriver.getDriver())
             .executeScript("arguments[0].value='';", input);
 }

 public boolean isElementPresent(By by) {
     try {
         SeleniumDriver.getDriver().findElement(by);
         return true;
     } catch (NoSuchElementException e) {
         return false;
     }
 }

 protected void verifyEquals(String actual, String expected) {
     Assert.assertEquals(actual, expected);
 }

 /* ===============================
    REUSABLE LOGIN METHOD
    Call manually in tests when needed
    =============================== */
 public void doLogin() {
     WebDriver driver = SeleniumDriver.getDriver();
     String email = Config.getProperty("Email");
     String password = Config.getProperty("Password");

     driver.get(Config.getProperty("testSiteUrl"));
     waitForPresence(By.xpath(OR.getProperty("EnterEmail"))).sendKeys(email);
     waitForClick(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();
     waitForPresence(By.xpath(OR.getProperty("EnterPassword"))).sendKeys(password);
     waitForClick(By.xpath(OR.getProperty("SubmitLogin"))).click();
     waitForClick(By.xpath(OR.getProperty("SkipPhone"))).click();
 }

    /* =============================== 
       TEARDOWN SUITE
       =============================== */

	@AfterSuite(alwaysRun = true)
	public void tearDownSuite() {
        SeleniumDriver.tearDown();

        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report Flushed ✅");
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

				Tck.Utilities.EmailUtils.sendEmailWithReport(latestFile.getAbsolutePath());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		System.out.println("Suite Execution Completed");
	}
}
