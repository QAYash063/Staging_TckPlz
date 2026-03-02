package Tck.Listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

import Tck.Base.BaseClass;

public class CustomListeners extends BaseClass implements ITestListener {

    // Thread-safe ExtentTest for parallel execution
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test in ExtentReports for each test method
        ExtentTest extentTest = extent.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        // Log the failure in ExtentReports
        test.get().fail(result.getThrowable());

        try {
            // Take screenshot as Base64 (embedded only, not saved)
            String base64Screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BASE64);

            test.get().addScreenCaptureFromBase64String(base64Screenshot, "Failed Screenshot");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
/*
    @Override
    public void onTestFailure(ITestResult result) {
        // Log the exception in report
        test.get().fail(result.getThrowable());

        // Take screenshot
        TakesScreenshot scrShot = (TakesScreenshot) driver;
        Date date = new Date();

        String fileName = date.toString().replace(":", "_").replace(" ", "_") + ".png";
        String screenshotPath = System.getProperty("user.dir") 
                + "/src/test/resources/screenShot/" + fileName;

        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(screenshotPath);

        try {
            // Create folder if it doesn't exist
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(srcFile, destFile);

            // Attach screenshot to report
            test.get().addScreenCaptureFromPath(screenshotPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Optional
    }

    @Override
    public void onStart(ITestContext context) {
        // Optional
    }

    @Override
    public void onFinish(ITestContext context) {
        // Do not flush here because BaseClass flushes in @AfterSuite
    }
}