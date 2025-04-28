package com.supersurveyors.tests.profile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static com.supersurveyors.tests.TestUtils.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class ProfileEditNameTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        String originalName = "Timothy Lin"; // Store original name for reset and verification

        try {
            // Setup WebDriver
        //     System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
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
            System.out.println("→ Navigated to Profile Page");
            closeOpenDialogs(driver); // Close any potential leftover dialogs
            
            // --- Test: Edit Profile Name --- 
            printTestHeader("TEST: PROFILE NAME EDIT FUNCTIONALITY");
            editProfileName(driver, wait, originalName);
            
            // --- Test: Cancel Button --- 
            // Need to ensure name is back to original before testing cancel
            resetProfileName(driver, wait, originalName);
            printTestHeader("TEST: CANCEL BUTTON FUNCTIONALITY (PROFILE NAME)");
            testCancelNameChange(driver, wait, originalName);

            // --- Cleanup: Reset name if necessary ---
            printTestHeader("TEST: CLEANUP - RESET NAME");
            resetProfileName(driver, wait, originalName);
            
            printTestResult(true, "Profile Name Edit Tests", "Completed successfully");

        } catch (Exception e) {
            printTestResult(false, "Profile Name Edit Tests", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // --- Test Helper Methods ---

    private static void editProfileName(WebDriver driver, WebDriverWait wait, String originalName) throws InterruptedException {
        String newName = "New Test Name " + System.currentTimeMillis() % 1000;
        
        // Click edit profile button
        WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit profile']")));
        Thread.sleep(500); // Pause before click
        editProfileButton.click();
        System.out.println("→ Clicked the 'edit profile' button");
        Thread.sleep(500); // Wait for modal

        // Edit name
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")
        ));
        nameInput.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        nameInput.sendKeys(Keys.DELETE);

        nameInput.sendKeys(newName);
        System.out.println("→ Entered new display name: " + newName);

        // Save changes
        WebElement saveProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Save Changes')]")));
        printElementCheck(saveProfileButton.isDisplayed(), "Save Changes button", "Is displayed");
        saveProfileButton.click();
        System.out.println("→ Clicked 'Save Changes' button");

        // Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")));
        Thread.sleep(500); // Wait for UI update

        // Verify updated name
        WebElement updatedNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(@class, 'MuiTypography') and contains(text(), '" + newName + "')]")
        ));
        printTestResult(updatedNameElement.isDisplayed(), "Profile name update", "Successfully updated to: " + newName);
    }

    private static void testCancelNameChange(WebDriver driver, WebDriverWait wait, String originalName) throws InterruptedException {
        System.out.println("→ Verifying original name before cancel test: " + originalName);
        // Verify the name is currently the original name
        WebElement currentNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(@class, 'MuiTypography') and normalize-space(text())='" + originalName + "']")));
        printElementCheck(currentNameElement.isDisplayed(), "Pre-Cancel Check", "Name is currently: " + originalName);

        // Click edit profile button
        WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit profile']")));
        Thread.sleep(500);
        editProfileButton.click();
        System.out.println("→ Clicked the 'edit profile' button for Cancel test");
        Thread.sleep(500);

        // Enter temporary name
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")
        ));
        String tempName = "This Name Should Not Be Saved";
        nameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        nameInput.sendKeys(Keys.DELETE);
        nameInput.sendKeys(tempName);
        System.out.println("→ Entered temporary display name: " + tempName);

        // Click Cancel button
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Cancel')]")));
        cancelButton.click();
        System.out.println("→ Clicked 'Cancel' button");

        // Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")));
        Thread.sleep(500); // Wait for UI

        // Verify name remains unchanged
        WebElement nameAfterCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(@class, 'MuiTypography-root')]")));
        String nameAfterCancelText = nameAfterCancel.getText();
        printTestResult(nameAfterCancelText.equals(originalName), "Cancel button functionality", 
                      nameAfterCancelText.equals(originalName) ? 
                      "Name remained unchanged: " + nameAfterCancelText :
                      "Name changed unexpectedly! Original: " + originalName + ", After: " + nameAfterCancelText);
    }

    private static void resetProfileName(WebDriver driver, WebDriverWait wait, String resetName) throws InterruptedException {
        System.out.println("→ Attempting to reset name to: " + resetName);
        WebElement currentNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[contains(@class, 'MuiTypography-root')]")));
        if (currentNameElement.getText().equals(resetName)) {
            System.out.println("→ Name is already correct.");
            return; // No need to reset
        }

        // Click edit profile button
        WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit profile']")));
        Thread.sleep(500);
        editProfileButton.click();
        System.out.println("→ Clicked the 'edit profile' button to reset name");
        Thread.sleep(500);
        
        // Wait for the modal
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")
        ));
        nameInput.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        nameInput.sendKeys(Keys.DELETE);
        nameInput.sendKeys(resetName);
        System.out.println("→ Resetting name to: " + resetName);
        
        // Save changes
        WebElement saveProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Save Changes')]")));
        saveProfileButton.click();
        System.out.println("→ Clicked 'Save Changes' button to reset name");
        
        // Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")));
        Thread.sleep(500);
        
        // Verify reset
        currentNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[contains(@class, 'MuiTypography-root') and normalize-space(text())='" + resetName + "']")));
        printElementCheck(currentNameElement.isDisplayed(), "Post-Reset Check", "Name successfully reset to: " + resetName);
    }
} 