package com.supersurveyors.tests.createSurvey;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static com.supersurveyors.tests.TestUtils.*;


public class CreateSurveyUploadMediaTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        boolean overallStatus = true;

        try {
            printTestHeader("SETUP: Initializing WebDriver");
            System.setProperty("webdriver.chrome.driver",
                    "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            printTestResult(true, "WebDriver Setup", "Initialization successful.");

            login(driver, wait, "timothylinziqimc@gmail.com", "test12345678!");

            navigateToCreatePage(driver, wait);
            uploadMedia(driver, wait);

            printTestResult(true, "Create Survey Test", "All steps completed successfully.");

        } catch (Exception e) {
            printTestResult(false, "Create Survey Test", "Test failed with error: " + e.getMessage());
            e.printStackTrace();
            overallStatus = false;
        } finally {
            printTestHeader("CLEANUP: Closing WebDriver");
            if (driver != null) {
                driver.quit();
                printTestResult(true, "WebDriver Cleanup", "Closed successfully.");
            } else {
                printTestResult(false, "WebDriver Cleanup", "Driver was null, nothing to close.");
            }
            printTestResult(overallStatus, "Create Survey Test - FINAL STATUS", "");
        }
    }


    private static void navigateToCreatePage(WebDriver driver, WebDriverWait wait) {
        printTestHeader("STEP: Navigate to Create Page");
        try {
            WebElement createNavLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='#/create' and contains(text(),'Create')]")
            ));
            createNavLink.click();
            wait.until(ExpectedConditions.urlContains("#/create"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(),'Create a Survey')]")
            ));
            printTestResult(true, "Navigation", "Successfully navigated to Create page.");
        } catch (Exception e) {
            printTestResult(false, "Navigation", "Failed to navigate to Create page: " + e.getMessage());
            throw e;
        }
    }


        
    private static void uploadMedia(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        // Click edit profile button

        Thread.sleep(500); // Wait for modal

        // Click Upload Media button
        WebElement uploadMediaButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Upload Media')]")));
        uploadMediaButton.click();
        System.out.println("→ Clicked 'Upload Media' button");
        System.out.println("Cloudinary API widget Open. No need to test thirdparty API");
    }
    
}

