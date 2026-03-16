package Tck.TestCases;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Tck.Base.BaseClass;

public class EditProfile extends BaseClass {

	/*
	 * =============================== Helper Method: Open Profile Option by Index
	 * ===============================
	 */
	public void openProfileOption(int optionIndex) {
		waitForClick(By.xpath(OR.getProperty("ProfileIcon"))).click();

		List<WebElement> profileMenuItems = driver.findElements(By.xpath(OR.getProperty("ProfileIconDropDown")));
		profileMenuItems.get(optionIndex).click();
	}

	/*
	 * =============================== Positive Test: Update Profile
	 * ===============================
	 */
	@Test(groups = { "positive" })
	public void profileUpdate() {
		openProfileOption(0); // Open first option

		waitForClick(By.xpath(OR.getProperty("SaveProfileBtn"))).click();
		waitForVisibility(By.xpath(OR.getProperty("VarifyProfileUpdate")));
		System.out.println("Profile updated successfully.");
		isElementPresent(By.xpath(OR.getProperty("VarifyProfileUpdate")));

		waitForClick(By.xpath(OR.getProperty("EmailUpdateBtn"))).click();
		waitForVisibility(By.xpath(OR.getProperty("VarifyProfileUpdate")));
	}

	/*
	 * =============================== Negative Test: Empty Profile Fields
	 * ===============================
	 */
	@Test(groups = { "negative" })
	public void emptyProfileUpdate() throws InterruptedException {
		doLogin();

		openProfileOption(0);
		WebElement firstNameField = waitForPresence(By.xpath(OR.getProperty("FirstNameInputField")));
		jsClear(firstNameField);
		firstNameField.sendKeys(" ");
		//firstNameField.clear();
		firstNameField.sendKeys(Keys.BACK_SPACE);
		waitForClick(By.xpath(OR.getProperty("SaveProfileBtn"))).click();
		//isElementPresent(By.xpath(OR.getProperty("EmpitySettingFieldSettingsValidation")));
		
		WebElement emailField = waitForPresence(By.xpath(OR.getProperty("EmailInputField")));
		jsClear(emailField);
		emailField.sendKeys(" ");
		emailField.sendKeys(Keys.BACK_SPACE);
		waitForClick(By.xpath(OR.getProperty("EmailUpdateBtn"))).click();
		isElementPresent(By.xpath(OR.getProperty("EmpitySettingFieldSettingsValidation")));

	}

	/*
	 * =============================== Positive Test: Organization/Account Page
	 * ===============================
	 */
	@Test(groups = { "positive1" })
	public void positiveTestForOption1() {
		openProfileOption(1); // Account

		waitForPresence(By.xpath(OR.getProperty("AccountNameField"))).clear();
		waitForPresence(By.xpath(OR.getProperty("AccountNameField"))).sendKeys("Yash Updated");

		waitForClick(By.xpath(OR.getProperty("SaveAccountBtn"))).click();

		String msg = waitForVisibility(By.xpath(OR.getProperty("AccountSuccessMsg"))).getText();
		verifyEquals(msg, "Account updated successfully.");
	}

	/*
	 * =============================== Negative Test: Invalid Account Email
	 * ===============================
	 */
	@Test(groups = { "negative1" })
	public void negativeTestForOption1() {
		openProfileOption(1);

		waitForPresence(By.xpath(OR.getProperty("AccountEmailField"))).clear();
		waitForPresence(By.xpath(OR.getProperty("AccountEmailField"))).sendKeys("invalidemail");

		waitForClick(By.xpath(OR.getProperty("SaveAccountBtn"))).click();

		String errorMsg = waitForVisibility(By.xpath(OR.getProperty("AccountErrorMsg"))).getText();
		verifyEquals(errorMsg, "Please enter a valid email address.");
	}
}