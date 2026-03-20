package Tck.Utilities;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Tck.Base.BaseClass;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumDriver {

    // Singleton instance
    private static SeleniumDriver seleniumDriver;

    // ThreadLocal instances (parallel safe)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitDriver = new ThreadLocal<>();
    private static ThreadLocal<Actions> actions = new ThreadLocal<>();

    // Timeout constants
    static int implicitWait = Integer.parseInt(BaseClass.Config.getProperty("inplicit.wait"));
    static int explicitWait = Integer.parseInt(BaseClass.Config.getProperty("explicit.Wait"));
    static int pageLOADWait = Integer.parseInt(BaseClass.Config.getProperty("pageLOAD.Wait"));

    public static final int IMPLICIT_WAIT = implicitWait;
    public static final int EXPLICIT_WAIT = explicitWait;
    public static final int PAGE_LOAD_TIMEOUT = pageLOADWait;

    // Private constructor
    private SeleniumDriver() {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver());

        driver.get().manage().window().maximize();

        driver.get().manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        driver.get().manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);

        waitDriver.set(new WebDriverWait(driver.get(), EXPLICIT_WAIT));
        // ✅ Initialize Actions
        actions.set(new Actions(driver.get()));
    }

    // Initialize driver
    public static synchronized void setUpDriver() {
        if (seleniumDriver == null) {
            seleniumDriver = new SeleniumDriver();
        }
    }

    // Get WebDriver
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            setUpDriver();
        }
        return driver.get();
    }

    // Get WebDriverWait
    public static WebDriverWait getWaitDriver() {
        if (waitDriver.get() == null) {
            setUpDriver();
        }
        return waitDriver.get();
    }

    // ✅ Get Actions
    public static Actions getActions() {
        if (actions.get() == null) {
            actions.set(new Actions(getDriver()));
        }
        return actions.get();
    }

    // Open URL
    public static void openPage(String url) {
        getDriver().get(url);
    }

    // ===============================
    // 🔥 ACTION UTILITY METHODS
    // ===============================

    // Click using Actions
    public static void actionClick(WebElement element) {
        getActions().moveToElement(element).click().perform();
    }

    // Click + Send Key
    public static void actionClickAndSendKey(WebElement element, Keys key) {
        getActions()
                .moveToElement(element)
                .click()
                .sendKeys(key)
                .perform();
    }
    public static void sendValueThenClick(WebElement element, String value) {
        getActions()
            .moveToElement(element)     // focus the element
            .click()  
            .pause(Duration.ofMillis(200))// click to activate
            .sendKeys(value)            // type the string
            .pause(Duration.ofMillis(2000))
            .sendKeys(Keys.ARROW_DOWN,Keys.ARROW_DOWN)// small pause for UI
            .pause(Duration.ofMillis(200))
           // .sendKeys(Keys.ENTER)
            .click()                    // click again to confirm or trigger UI event
            .perform();
    }

    // Send key globally
    public static void actionSendKey(Keys key) {
        getActions().sendKeys(key).perform();
    }

    // Calendar: Move left (future days) and select
    public static void selectDateUsingKeyboard(WebElement element, int daysForword) {
        getActions().moveToElement(element).click().perform();

        // Wait for calendar UI
        getActions().pause(Duration.ofMillis(500)).perform();

        for (int i = 0; i < daysForword; i++) {
            getActions().sendKeys(Keys.ARROW_RIGHT).perform();
        }

        getActions().sendKeys(Keys.ENTER).perform();
    }

    // Hover
    public static void hover(WebElement element) {
        getActions().moveToElement(element).perform();
    }

    // ===============================
    // Tear Down
    // ===============================
    public static void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            waitDriver.remove();
            actions.remove(); // ✅ important
        }
        seleniumDriver = null;
    }
}