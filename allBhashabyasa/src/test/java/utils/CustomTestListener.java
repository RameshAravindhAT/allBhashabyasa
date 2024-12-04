package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;

import projectSpecifications.BaseClass;

public class CustomTestListener extends BaseClass implements ITestListener {

    public static ThreadLocal<String> currentMethodName = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        currentMethodName.set(result.getMethod().getMethodName());
    }

    public static String getCurrentTestMethodName() {
        return currentMethodName.get();
    }

    public void onTestFinish(ITestResult result) {
        currentMethodName.remove();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logMethodExecution(result.getMethod().getMethodName(), "succeeded");
        ExtentReportManager.getTest().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName().replace("_", " ");
        String errorMessage = result.getThrowable().getMessage();
        String detailedMessage = "Test failed: " + methodName + "\nError: " + errorMessage;
        String screenshotPath = takeScreenshot(TestContext.getDriver());

        // Report failure with screenshot
        ExtentReportManager.getTest().fail(detailedMessage,
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        logMethodExecution(result.getMethod().getMethodName(), "failed");
    }

    public void logMethodExecution(String methodName, String status) {
        // Log the method execution status
        System.out.println("Method: " + methodName + " " + status);
    }

    public String takeScreenshot(WebDriver driver) {
        String uniqueScreenshotFileName = "screenshot_" + System.currentTimeMillis() + ".png";
        String uniqueFilePath = Paths.get("screenshots", uniqueScreenshotFileName).toAbsolutePath().toString();

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File(uniqueFilePath));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
        return uniqueFilePath;
    }

}
