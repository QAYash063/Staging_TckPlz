package Tck.Utilities;

import java.io.File;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class ExtentManager {

    public static ExtentReports extent;

    public static ExtentReports getInstance() {

        if (extent == null) {

        	String timeStamp =
    				new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

            String reportFolder =
            System.getProperty("user.dir") + "/test-output/Extend-Reports/";

            new File(reportFolder).mkdirs();

            String reportPath =
            reportFolder + "ExtentReport_" + timeStamp + ".html";

            ExtentSparkReporter spark =
            new ExtentSparkReporter(reportPath);

            spark.config().setDocumentTitle("Automation Report");
            spark.config().setReportName("Functional Testing");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Tester", "Yash");
            extent.setSystemInfo("Browser", "Chrome");

            System.out.println("Report Created: " + reportPath);
        }

        return extent;
    }
}