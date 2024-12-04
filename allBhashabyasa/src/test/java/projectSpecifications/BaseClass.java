package projectSpecifications;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import pageObjects.PG_001_LoginPage;
import utils.ExcelReader;
import utils.ExtentReportManager;
import utils.TestContext;

public class BaseClass {

    // Logger configuration
    public static Logger logger = Logger.getLogger(BaseClass.class);
    public static String logfile = Paths.get("src", "test", "java", "utils", "Log4j.properties").toAbsolutePath().toString();

    // Test data location
    public static String excelfilename = Paths.get("TestData", "AllTeacherTestDatas.xlsx").toAbsolutePath().toString();
    public static String configFilePath = Paths.get("Properties", "Config.properties").toAbsolutePath().toString();
    public static Properties properties;
    public static FileInputStream file;

    // Extent Reports
    protected static ExtentReports extent;

    // Start Extent Reports at the beginning of the suite
    @BeforeSuite
    public void startReports() throws IOException {
        PropertyConfigurator.configure(logfile);
        logger.info("Log4j configured successfully.");

        // Load the config file
        properties = new Properties();
        file = new FileInputStream(configFilePath);
        properties.load(file);

        // Start ExtentReports
        extent = ExtentReportManager.startReports();
    }

    // Initialize WebDriver and other resources per thread before each test
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        WebDriver driver = initializeDriver(browser);
        driver.manage().window().maximize();
        driver.get(properties.getProperty("url"));

        // Set WebDriver and other resources in TestContext
        TestContext.setDriver(driver);
        TestContext.setWait(TestContext.getDriver());
        TestContext.setJsExecutor((JavascriptExecutor) driver);

        // Initialize the LoginPage object in TestContext
        TestContext.setLoginPage(new PG_001_LoginPage(driver));
    }

    // Initialize WebDriver based on the browser parameter
    private WebDriver initializeDriver(String browser) {
        WebDriver driver;
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Browser not supported");
        }
        return driver;
    }

    // Report step based on the status (pass or fail)
    public void reportStep(String message, String status) {
        if (ExtentReportManager.getTest() == null) {
            logger.error("ExtentTest is not initialized properly!");
            return;
        }

        // Report the step to ExtentReports
        ExtentReportManager.reportStep(message, status);
    }

    // Fetch test data from Excel for the given sheet
    @DataProvider(name = "sendData", parallel = true)
    public String[][] fetchData() throws IOException {
        if (TestContext.getSheetName() == null || TestContext.getSheetName().isEmpty()) {
            throw new IllegalStateException("Sheet name is not set.");
        }
        return ExcelReader.readexcelData(excelfilename, TestContext.getSheetName());
    }

    // Tear down after each test
    @AfterMethod
    public void tearDown() {
        WebDriver driver = TestContext.getDriver();
        if (driver != null) {
            driver.quit(); // Quit WebDriver for this thread
            TestContext.setDriver(null); // Remove the WebDriver instance from TestContext
        }

        reportStep("Closing the Browser", "pass");
        logger.info("Browser Closed");
        logger.info("=============================================================");
    }

    // Stop and flush Extent Reports after all tests
    @AfterSuite
    public void stopReports() {
        ExtentReportManager.stopReports();
    }
}
