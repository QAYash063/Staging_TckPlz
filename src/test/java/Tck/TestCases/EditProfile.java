package Tck.TestCases;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import Tck.Base.BaseClass;
import Tck.Utilities.AssertUtil;
import Tck.Utilities.SeleniumDriver;

public class EditProfile extends BaseClass {

	public void openProfileOption(int optionIndex) {
		waitForClick(By.xpath(OR.getProperty("ProfileIcon"))).click();

		List<WebElement> profileMenuItems = SeleniumDriver.getDriver().findElements(By.xpath(OR.getProperty("ProfileIconDropDown")));
		profileMenuItems.get(optionIndex).click();
		
		log.info("Opened profile option at index: " + optionIndex);
	}

	
	@Test(groups = { "positive" })
	public void profileUpdate() {
		openProfileOption(0); // Open first option

		waitForClick(By.xpath(OR.getProperty("SaveProfileBtn"))).click();
		waitForVisibility(By.xpath(OR.getProperty("VarifyProfileUpdate")));
		isElementPresent(By.xpath(OR.getProperty("VarifyProfileUpdate")));

		waitForClick(By.xpath(OR.getProperty("EmailUpdateBtn"))).click();
		waitForVisibility(By.xpath(OR.getProperty("VarifyProfileUpdate")));
		log.info("Profile update verified successfully");
	}


	@Test(groups = { "negative" })
	public void emptyProfileUpdate() throws InterruptedException {
		doLogin();
		
		openProfileOption(0);
		//Thread.sleep(3000); // Wait for login to complete and profile icon to be interactable
		WebElement firstNameField = waitForClick(By.xpath(OR.getProperty("FirstNameInputField")));
		jsClear(firstNameField);
		firstNameField.sendKeys(" ");
		firstNameField.sendKeys(Keys.BACK_SPACE);
		waitForClick(By.xpath(OR.getProperty("SaveProfileBtn"))).click();
		AssertUtil.booleanHardTrue(isElementPresent(By.xpath(OR.getProperty("EmpitySettingFieldSettingsValidation"))), "Empty first name validation message not displayed");
		log.info("Empty first name validation checked");
		
		WebElement emailField = waitForPresence(By.xpath(OR.getProperty("EmailInputField")));
		jsClear(emailField);
		emailField.sendKeys(" ");
		emailField.sendKeys(Keys.BACK_SPACE);
		emailField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		emailField.sendKeys(Keys.DELETE);
		waitForClick(By.xpath(OR.getProperty("EmailUpdateBtn"))).click();
		AssertUtil.booleanHardTrue(isElementPresent(By.xpath(OR.getProperty("EmpitySettingFieldSettingsValidation"))), "Empty email validation message not displayed");
		log.info("Empty email validation checked");

	}

	
	@Test(groups = { "positive1" })
	public void positiveTestForOption1() {
		openProfileOption(1); // Account

		waitForPresence(By.xpath(OR.getProperty("AccountNameField"))).clear();
		waitForPresence(By.xpath(OR.getProperty("AccountNameField"))).sendKeys("Yash Updated");

		waitForClick(By.xpath(OR.getProperty("SaveAccountBtn"))).click();

	//	String msg = waitForVisibility(By.xpath(OR.getProperty("AccountSuccessMsg"))).getText();
	//	verifyEquals(msg, "Account updated successfully.");
	}


	@Test(groups = { "negative1" })
	public void negativeTestForOption1() {
		openProfileOption(1);

		waitForPresence(By.xpath(OR.getProperty("AccountEmailField"))).clear();
		waitForPresence(By.xpath(OR.getProperty("AccountEmailField"))).sendKeys("invalidemail");

		waitForClick(By.xpath(OR.getProperty("SaveAccountBtn"))).click();

	//	String errorMsg = waitForVisibility(By.xpath(OR.getProperty("AccountErrorMsg"))).getText();
	//	verifyEquals(errorMsg, "Please enter a valid email address.");
	}
}