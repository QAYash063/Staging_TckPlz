package Tck.TestCases;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import Tck.Base.BaseClass;
import Tck.Listeners.CustomListeners;
import Tck.Utilities.SeleniumDriver;

public class LoginAccount extends BaseClass {

    /**
     * Positive login: valid credentials
     */
    @Test(groups = { "positive", "smoke", "regression" })
    public void loginWithValid() {
        doLogin();  // manually call login

        CustomListeners.test.get().pass("Logged in successfully with valid credentials");
    }

    /**
     * Negative login: blank email
     */
    @Test(groups = { "negative" })
    public void loginWithBlankEmail() {
        // Open login page
        SeleniumDriver.getDriver().get(Config.getProperty("testSiteUrl"));

        // Click continue without entering email
        waitForClick(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

        String actualText = waitForVisibility(By.xpath(OR.getProperty("BlankEmailValidation"))).getText();
        verifyEquals(actualText, "Email can't be blank.");

        
        CustomListeners.test.get().info("Blank email validation checked");
    }

    /**
     * Negative login: invalid email
     */
    @Test(groups = { "negative" })
    public void loginWithInvalidEmail() {
        SeleniumDriver.getDriver().get(Config.getProperty("testSiteUrl"));

        waitForPresence(By.xpath(OR.getProperty("EnterEmail"))).sendKeys("yash@yopmail.com");
        waitForClick(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

        String actualText = waitForVisibility(By.xpath(OR.getProperty("InvalidEmailValidation1"))).getText();
        verifyEquals(actualText, "Please sign up with a different email address.");

        CustomListeners.test.get().info("Invalid email validation checked");
    }

    /**
     * Negative login: invalid password
     */
    @Test(groups = { "negative" })
    public void loginWithInvalidPassword() {
        SeleniumDriver.getDriver().get(Config.getProperty("testSiteUrl"));

        waitForPresence(By.xpath(OR.getProperty("EnterEmail"))).clear();
        waitForPresence(By.xpath(OR.getProperty("EnterEmail"))).sendKeys("yash.c+21@dreamcast.co");
        waitForClick(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

        waitForPresence(By.xpath(OR.getProperty("EnterPassword"))).sendKeys("System@123454");
        waitForClick(By.xpath(OR.getProperty("SubmitLogin"))).click();

        String actualText = waitForVisibility(By.xpath(OR.getProperty("InvalidPasswordVlaidation"))).getText();
        verifyEquals(actualText, "Incorrect password.");

        CustomListeners.test.get().info("Invalid password validation checked");
    }
}