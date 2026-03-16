package Tck.Utilities;


import java.util.concurrent.TimeUnit;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import Tck.Base.BaseClass;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumDriver {
	
	 // Singleton instance
    private static SeleniumDriver seleniumDriver;

    // ThreadLocal for parallel-safe WebDriver and WebDriverWait
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitDriver = new ThreadLocal<>();

 // Timeout constants
    static int implicitWait = Integer.parseInt(BaseClass.Config.getProperty("inplicit.wait"));
    static int explicitWait = Integer.parseInt(BaseClass.Config.getProperty("explicit.Wait"));
    static int pageLOADWait = Integer.parseInt(BaseClass.Config.getProperty("pageLOAD.Wait"));
    public static final int IMPLICIT_WAIT = implicitWait;   // faster implicit wait
    public static final int EXPLICIT_WAIT = explicitWait;  // explicit wait
    public static final int PAGE_LOAD_TIMEOUT = pageLOADWait;  

    // Private constructor
    private SeleniumDriver() {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver());
        driver.get().manage().window().maximize();

        // Faster implicit wait
        driver.get().manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        driver.get().manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);

        // Explicit wait per thread
        waitDriver.set(new WebDriverWait(driver.get(), EXPLICIT_WAIT));
    }

    // Initialize singleton if not already
    public static synchronized void setUpDriver() {
        if (seleniumDriver == null) {
            seleniumDriver = new SeleniumDriver();
        }
    }

    // Get WebDriver instance for current thread
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            setUpDriver();
        }
        return driver.get();
    }

    // Get WebDriverWait instance for current thread
    public static WebDriverWait getWaitDriver() {
        if (waitDriver.get() == null) {
            setUpDriver();
        }
        return waitDriver.get();
    }

    // Open a URL
    public static void openPage(String url) {
        getDriver().get(url);
    }

    // Tear down driver
    public static void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            waitDriver.remove();
        }
        seleniumDriver = null;
    }

}
