package Tck.TestCases;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import Tck.Base.BaseClass;
import Tck.Listeners.CustomListeners;
import Tck.Utilities.DateUtils;
import Tck.Utilities.DropDownUtills;
import Tck.Utilities.SeleniumDriver;

public class CreateEvent extends BaseClass {
	
	
	
	public void openEventOption(String name) throws InterruptedException  {
		
	    DropDownUtills.selectByText(By.xpath(OR.getProperty("EventIcon")), By.xpath(OR.getProperty("CreateEventDropDown")), name);
	
	}
	

	@Test(groups = { "positive" })
	public void createEventValid() throws InterruptedException {
		
		
		openEventOption("Create Event");
		Thread.sleep(5000);
		String eventName = "Test Event"+DateUtils.getCurrentTime();
		waitForPresence(By.xpath(OR.getProperty("EventNameField"))).sendKeys(eventName);
		System.out.println(waitForPresence(By.xpath(OR.getProperty("EventNameField"))).getText());
		waitForPresence(By.xpath(OR.getProperty("EventNameField"))).sendKeys(Keys.TAB,"Hello This is description of  "+ eventName);
		WebElement eventStartDate = waitForClick(By.xpath(OR.getProperty("EventStartDateField")));
		SeleniumDriver.selectDateUsingKeyboard(eventStartDate, 1);
		

		WebElement eventEndDate = waitForClick(By.xpath(OR.getProperty("EventEndDateField")));
		SeleniumDriver.selectDateUsingKeyboard(eventEndDate, 2);
		WebElement eventLocation = waitForClick(By.xpath(OR.getProperty("EventLocationInputField")));
		SeleniumDriver.sendValueThenClick(eventLocation, "jaipur");
		waitForPresence(By.xpath(OR.getProperty("SelectEventType"))).sendKeys(Keys.ENTER, Keys.TAB, Keys.ENTER);
		waitForPresence(By.xpath(OR.getProperty("MapLocationBox")));
		waitForPresence(By.xpath(OR.getProperty("UniqueCodePrefixInputField"))).sendKeys("TestC");
		waitForPresence(By.xpath(OR.getProperty("AddCategoryBtn"))).click();
		waitForPresence(By.xpath(OR.getProperty("CategoryNameInputField"))).sendKeys("TestCategory");
	    DropDownUtills.selectByText(By.xpath(OR.getProperty("CategoryMethodBtn")), By.xpath(OR.getProperty("CategoryMethodDropDown")), "Standard");
		waitForPresence(By.xpath(OR.getProperty("CreateCategoryBtn"))).click();
		waitForPresence(By.xpath(OR.getProperty("EventCreateFormBtn"))).click();
		Thread.sleep(3000);
		CustomListeners.test.get().info("Created event with name: " + eventName);
		
	}
	
	
	
	
	
	@Test(groups = { "negative" })
	public void createEvent() throws InterruptedException {
		
		openEventOption("Create Event");
	}
	
	

}
