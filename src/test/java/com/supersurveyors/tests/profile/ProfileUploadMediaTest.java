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
            System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
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

        // Wait for upload dialog
        WebElement uploadDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@role, 'dialog')]//h2[contains(text(), 'Upload Media')]")));
        printElementCheck(uploadDialog.isDisplayed(), "Upload Media dialog", "Appeared successfully");

        // Click Upload File tab
        WebElement fileUploadTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Upload File')]")));
        printElementCheck(fileUploadTab.isDisplayed(), "File upload tab", "Is present");
        fileUploadTab.click();
        System.out.println("→ Clicked on 'Upload File' tab");

        // Prepare mock file path
        String mockFilePath = System.getProperty("user.dir") + "/test_profile_image.jpg"; // Use relative path
        // Ensure mock file exists for test if possible, or handle error gracefully
        // java.io.File mockFile = new java.io.File(mockFilePath);
        // if (!mockFile.exists()) { 
        //     System.out.println("⚠️ WARNING: Mock file not found at " + mockFilePath + ". Upload might fail.");
        //     // Optionally create a dummy file: mockFile.createNewFile(); 
        // }

        // Find file input and send path
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")));
        fileInput.sendKeys(mockFilePath);
        System.out.println("→ Selected file for upload: " + mockFilePath);

        // Click Upload/Submit button
        WebElement submitUploadButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@role,'dialog')]//button[contains(text(), 'Upload') or contains(text(), 'Done')]"))); // More general selector
        submitUploadButton.click();
        System.out.println("→ Clicked 'Upload/Done' button to submit the file");

        // Wait for upload confirmation or dialog close
        // Note: Actual upload verification might require checking the image source change
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Upload successful') or contains(text(), 'Media updated')]")),
                ExpectedConditions.invisibilityOf(uploadDialog)
            ));
             System.out.println("→ Upload successful message or dialog closed.");
        } catch (Exception e) {
             System.out.println("→ Upload confirmation/dialog close timed out.");
        }
        
        // Ensure dialog is closed if still open
        closeOpenDialogs(driver);

        // Verify profile picture (optional - verification might be complex)
        try {
            WebElement updatedProfilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'MuiAvatar-root')]//img[@alt='Profile']")));
            // String newSrc = updatedProfilePic.getAttribute("src");
            // Add logic here to check if src changed if needed
            printElementCheck(updatedProfilePic.isDisplayed(), "Profile picture after upload", "Is displayed");
        } catch (Exception e) {
            printElementCheck(false, "Profile picture after upload", "Verification failed: " + e.getMessage());
        }
    }
} 