package com.supersurveyors.tests.profile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static com.supersurveyors.tests.TestUtils.*;


public class ProfileUploadMediaTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        try {
            // Setup WebDriver
            // System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();

            // Login first
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login");
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys("timothylinziqimc@gmail.com");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys("test12345678!");
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
            signInButton.click();
            wait.until(ExpectedConditions.urlContains("#/home"));

            // Navigate to Profile page
            String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
            driver.get(profileUrl);
            System.out.println("→ Navigated to Profile Page for Upload Media test");
            closeOpenDialogs(driver); // Close any potential leftover dialogs
            
            // --- Test: Upload Media --- 
            printTestHeader("TEST: UPLOAD MEDIA FUNCTIONALITY");
            uploadMedia(driver, wait);
            
            printTestResult(true, "Upload Media Test", "Completed successfully (Note: Actual upload success depends on file existence and server response)");

        } catch (Exception e) {
            printTestResult(false, "Upload Media Test", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    
    private static void uploadMedia(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        // Click edit profile button
        WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit profile']")));
        Thread.sleep(500);
        editProfileButton.click();
        System.out.println("→ Clicked the 'edit profile' button");
        Thread.sleep(500); // Wait for modal

        // Click Upload Media button
        WebElement uploadMediaButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Upload Media')]")));
        uploadMediaButton.click();
        System.out.println("→ Clicked 'Upload Media' button");
        System.out.println("Cloudinary API widget Open. No need to test thirdparty API");
    }
} 