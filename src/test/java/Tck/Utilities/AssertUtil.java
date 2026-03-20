package Tck.Utilities;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class AssertUtil {
	

	 private static SoftAssert softAssert = new SoftAssert();

	    public static void softEquals(String actual, String expected, String message) {
	        softAssert.assertEquals(actual, expected, message);
	    }

	    public static void assertAll() {
	        softAssert.assertAll();
	    }

	    public static void hardEquals(String actual, String expected, String message) {
	        Assert.assertEquals(actual, expected, message);
	    }

}
