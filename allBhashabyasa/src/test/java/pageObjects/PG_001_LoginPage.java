package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import utils.ExtentReportManager;
import utils.TestContext;

public class PG_001_LoginPage {

    // Constructor
    public PG_001_LoginPage(WebDriver driver) {
        TestContext.setDriver(driver); // Use TestContext to set the driver
        PageFactory.initElements(driver, this); // Initialize page elements
    }

    @FindBy(name = "email")
    public WebElement userName;

    @FindBy(name = "password")
    public WebElement password;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginButton;

    @FindBy(xpath = "//p[text()='Chatbot ']")
    public WebElement ChatBot;

    @FindBy(xpath = "((//div)[5]/div)[2]")
    public WebElement toastMessage;

    @FindBy(xpath = "(//div[contains(@class,'standardWarning')])[1]//div[2]")
    public WebElement dateWarning;

    @FindBy(xpath = "(//span[text()='Email']//following::span)[1]")
    public WebElement emailFieldwarnings;

    @FindBy(xpath = "(//span[text()='Password']//following::span)[1]")
    public WebElement passwordFieldwarnings;

    public PG_001_LoginPage Enter_the_username(String username) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName().replace("_", " ");
        try {
            userName.sendKeys(username);
            ExtentReportManager.reportStep(methodName + " " + username, "pass");
            TestContext.getLogger().info(methodName + " " + username);
        } catch (Exception e) {
            ExtentReportManager.reportStep(methodName + " " + username, "fail");
            TestContext.getLogger().error(methodName + " " + username);
            e.printStackTrace();
        }
        return this;
    }

    public PG_001_LoginPage Enter_the_password(String passwordValue) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName().replace("_", " ");
        try {
            password.sendKeys(passwordValue);
            ExtentReportManager.reportStep(methodName + " " + passwordValue, "pass");
            TestContext.getLogger().info(methodName + " " + passwordValue);
        } catch (Exception e) {
            ExtentReportManager.reportStep(methodName + " " + passwordValue, "fail");
            TestContext.getLogger().error(methodName + " " + passwordValue);
            e.printStackTrace();
        }
        return this;
    }

    public PG_001_LoginPage Click_on_the_loginButton() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName().replace("_", " ");
        try {
            loginButton.click();
            ExtentReportManager.reportStep(methodName, "pass");
            TestContext.getLogger().info(methodName);
        } catch (Exception e) {
            ExtentReportManager.reportStep(methodName, "fail");
            e.printStackTrace();
            TestContext.getLogger().error(methodName);
        }
        return this;
    }

    //public PG_002_DashbaordPage VerifyToastMessage(String message)
    public PG_001_LoginPage VerifyToastMessage(String message) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName().replace("_", " ");
        try {
            WebElement toast = TestContext.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("((//div)[5]/div)[2]")));
            String actualMessage = toastMessage.getText();

            if (toast.getText() != null) {
                String expectedMessage = message;
                if (toast.getText().equals("Login successful")) {
                    Assert.assertEquals(actualMessage, expectedMessage);

                    TestContext.getDriver().findElement(By.xpath("//p[text()='Desktop / Tab ']")).click();

                    WebElement homepage = TestContext.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Dashboard']")));
                    String dashboard = homepage.getText();
                    Assert.assertEquals(dashboard, "Dashboard");
                } else if (toast.getText().equals(message)) {
                    Assert.assertEquals(actualMessage, expectedMessage);
                }
                Assert.assertEquals(actualMessage, expectedMessage);
                ExtentReportManager.reportStep(methodName + " " + actualMessage, "pass");
                TestContext.getLogger().info(methodName + " " + actualMessage);
            }
        } catch (Exception e) {
      
            WebElement emailWarnings = TestContext.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[text()='Email']//following::span)[1]")));
            WebElement passwordWarnings = TestContext.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[text()='Password']//following::span)[1]")));

            String actualEmailWarnings = emailWarnings.getText().trim().replaceAll("^[^a-zA-Z0-9]+", "");
            String actualPasswordWarnings = passwordWarnings.getText().trim().replaceAll("^[^a-zA-Z0-9]+", "");

            String expectedMessageWarnings = message;

            if (userName.getAttribute("value").isEmpty() && password.getAttribute("value").isEmpty()) {
                String[] values = message.split("&");
                Assert.assertEquals(actualEmailWarnings, values[0]);
                Assert.assertEquals(actualPasswordWarnings, values[1]);
                ExtentReportManager.reportStep(methodName + " " + actualEmailWarnings + " " + actualPasswordWarnings, "pass");
                TestContext.getLogger().info(methodName + " " + expectedMessageWarnings);
            } else if (actualEmailWarnings.equals(expectedMessageWarnings)) {
                Assert.assertEquals(actualEmailWarnings, expectedMessageWarnings);
                ExtentReportManager.reportStep(methodName + " " + expectedMessageWarnings, "pass");
                TestContext.getLogger().info(methodName + " " + expectedMessageWarnings);
            } else if (actualPasswordWarnings.equals(expectedMessageWarnings)) {
                Assert.assertEquals(actualPasswordWarnings, expectedMessageWarnings);
                ExtentReportManager.reportStep(methodName + " " + expectedMessageWarnings, "pass");
                TestContext.getLogger().info(methodName + " " + expectedMessageWarnings);
            } else if (!userName.getAttribute("value").isEmpty() && !password.getAttribute("value").isEmpty()) {
                String[] values = message.split("&");
                Assert.assertEquals(actualEmailWarnings, values[0]);
                Assert.assertEquals(actualPasswordWarnings, values[1]);
                ExtentReportManager.reportStep(methodName + " " + actualEmailWarnings + " " + actualPasswordWarnings, "pass");
                TestContext.getLogger().info(methodName + " " + expectedMessageWarnings);
            } else {
                TestContext.getLogger().error(methodName + expectedMessageWarnings);
            }
        }
        return this;
       // return new PG_003_Dashboard(TestContext.getDriver());==> If you are going for next page use return new PG_002_Dashboard();
    }

   
}
