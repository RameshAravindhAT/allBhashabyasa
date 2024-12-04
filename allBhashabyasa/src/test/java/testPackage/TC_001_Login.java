package testPackage;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import projectSpecifications.BaseClass;
import utils.ExtentReportManager;
import utils.TestContext;

@Listeners(utils.CustomTestListener.class)  // Custom listeners for reporting or test actions
public class TC_001_Login extends BaseClass {

    @BeforeClass
    public void testDetails() {
        // Set the sheet name for the test
        TestContext.setSheetName("Login");
    }

    @Test(dataProvider = "sendData") 
    public void validateLogin(String testNameDetails, String authorName, String category, 
                              String username, String password, String message) throws InterruptedException {

        // Initialize the test in Extent Reports using TestContext
        ExtentReportManager.setTest(extent.createTest(testNameDetails)); // Create the test instance in Extent Reports
        ExtentReportManager.getTest().assignAuthor(authorName); // Assign the author for the test
        ExtentReportManager.getTest().assignCategory(category);  // Assign the category for the test

        // Perform the login action using the login page object
        TestContext.getLoginPage()
            .Enter_the_username(username)
            .Enter_the_password(password)
            .Click_on_the_loginButton()
            .VerifyToastMessage(message); // Verify the toast message after login
    }
}
