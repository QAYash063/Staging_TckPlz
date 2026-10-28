package Tck.Utilities;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DropDownUtills {
	
	public static void selectByText(By dropdownIcon, By optionsLocator, String value) {

		try {
		    Thread.sleep(3000); // simulate wait or pause
		} catch (InterruptedException e) {
		    // Throw a runtime exception with a custom message
		    throw new RuntimeException("Dropdown utility class failed during wait/sleep: " + e.getMessage(), e);
		}

	    SeleniumDriver.getDriver().findElement(dropdownIcon).click();

	 
	    SeleniumDriver.getWaitDriver()
	            .until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));
	    
	   // BaseClass.waitForVisibility(optionsLocator);

	    List<WebElement> options =
	            SeleniumDriver.getDriver().findElements(optionsLocator);
	    
	    
	    for (WebElement option : options) {
	        if (option.getText().trim().equalsIgnoreCase(value)) {
	            option.click();
	            break;
	        }
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	

}
