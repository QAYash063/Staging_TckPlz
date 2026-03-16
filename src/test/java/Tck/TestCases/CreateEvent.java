package Tck.TestCases;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Tck.Base.BaseClass;

public class CreateEvent extends BaseClass {
	
	public void openEventOption(int optionEventIndex) {
		waitForClick(By.xpath(OR.getProperty("ProfileIcon"))).click();
		waitForPresence(By.xpath(OR.getProperty("ProfileIconDropDown")));
		verifyEquals

		List<WebElement> profileMenuItems = driver.findElements(By.xpath(OR.getProperty("ProfileIconDropDown")));
		profileMenuItems.get(optionEventIndex).click();
	}
	
	

}
