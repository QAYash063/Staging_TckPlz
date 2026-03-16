package Tck.Base;

import org.testng.AssertJUnit;
import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import Tck.Utilities.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

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
       DRIVER AND EXTENT REPORT SETUP
       =============================== */

    @BeforeSuite(alwaysRun = true)
    public void setUp() {

        extent = ExtentManager.getInstance(); // Initialize ExtentReports

        Config = new Properties();
        OR = new Properties();

        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/Config.properties");
            Config.load(fis);

            fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/OR.properties");
            OR.load(fis);

        } catch (Exception e) {
            e.printStackTrace();
        }

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        

        driver.get(Config.getProperty("testSiteUrl"));
        driver.manage().window().maximize();
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

    // JavaScript click utility
    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
    
    // JavaScript input utility
    public void jsClear(WebElement input) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", input);
    }

    // Check if element exists
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Assertion wrapper
    protected void verifyEquals(String actual, String expected) {
        AssertJUnit.assertEquals(actual, expected);
    }

    /* ===============================
    LOGIN METHOD (Reusable)
    =============================== */
    public void doLogin() {
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

		if (driver != null) {
			driver.quit();
		}

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
