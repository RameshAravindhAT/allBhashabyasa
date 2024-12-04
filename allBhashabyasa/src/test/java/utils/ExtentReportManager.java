package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.log4j.Logger;

import java.nio.file.Paths;

public class ExtentReportManager {

    private static final Logger logger = Logger.getLogger(ExtentReportManager.class);
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extentReports;
    private static String reportFilePath = Paths.get("ExtentReports", "extentReports.html").toAbsolutePath().toString();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Initialize the Extent Reports - should be called once before the suite
    public static ExtentReports startReports() {
        // Ensure the report directory exists
        try {
            String reportDirectory = Paths.get("ExtentReports").toAbsolutePath().toString();
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(reportDirectory)); // Create folder if not exists
        } catch (Exception e) {
            logger.error("Failed to create ExtentReports directory", e);
        }

        sparkReporter = new ExtentSparkReporter(reportFilePath);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        logger.info("Extent Reports started at " + reportFilePath);
        return extentReports;
    }

    // Report step based on the status (pass or fail)
    public static void reportStep(String message, String status) {
        // Ensure that ExtentTest is initialized properly for the current thread
        if (test.get() == null) {
            logger.error("ExtentTest is not initialized properly!");
            return;
        }

        // Log and report based on status
        switch (status.toLowerCase()) {
            case "pass":
                test.get().pass(message);
                break;
            case "fail":
                test.get().fail(message);
                break;
            case "info":
                test.get().info(message);
                break;
            default:
                logger.warn("UNKNOWN STATUS: " + status + " for message: " + message);
                break;
        }
    }

    // Set the ExtentTest instance for the current thread
    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    // Get the ExtentTest instance for the current thread
    public static ExtentTest getTest() {
        return test.get();
    }

    // Stop and flush the reports - should be called once after the suite
    public static void stopReports() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Extent Reports flushed successfully.");
        }
    }
}
