package Tck.TestCases;

import org.testng.annotations.Test;
import org.openqa.selenium.By;

public class LoginAccount extends Tck.Base.BaseClass {

	@Test(groups = { "positive", "smoke", "regression" })
	public void loginWithValid() {

		driver.findElement(By.xpath(OR.getProperty("EnterEmail"))).sendKeys("yash.c@dreamcast.co");

		driver.findElement(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

		driver.findElement(By.xpath(OR.getProperty("EnterPassword"))).sendKeys("System@123");

		driver.findElement(By.xpath(OR.getProperty("SubmitLogin"))).click();
	}

	@Test(groups = { "negative" })
	public void loginWithBlankEmail() {

		driver.findElement(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

		

		String actualText =
				waitForVisibility(By.xpath(OR.getProperty("BlankEmailValidation"))).getText();

		verifyEquals(actualText,"Email can't be blank.");
		
	}

	@Test(groups = { "negative" })
	public void loginWithInvalidEmail() {

		driver.findElement(By.xpath(OR.getProperty("EnterEmail"))).sendKeys("yash@yopmail.com");

		driver.findElement(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

		String actualText =waitForVisibility(By.xpath(OR.getProperty("InvalidEmailValidation1"))).getText();

		verifyEquals(actualText,"Please sign up with a different email address.");
	}

	@Test(groups = { "negative" })
	public void loginWithInvalidPassword() {

		driver.findElement(By.xpath(OR.getProperty("EnterEmail"))).clear();

		driver.findElement(By.xpath(OR.getProperty("EnterEmail"))).sendKeys("yash.c@dreamcast.co");

		driver.findElement(By.xpath(OR.getProperty("ContinueWithEmailBtn"))).click();

		driver.findElement(By.xpath(OR.getProperty("EnterPassword"))).sendKeys("System@123454");

		driver.findElement(By.xpath(OR.getProperty("SubmitLogin"))).click();

		String actualText =
				waitForVisibility(By.xpath(OR.getProperty("InvalidPasswordVlaidation"))).getText();

		verifyEquals(actualText,"Incorrect password.");
	}
}