package com.supersurveyors.tests.profile;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import static com.supersurveyors.tests.TestUtils.*;

public class ProfileEditTagsTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        String[] tagNames = {"arts", "gaming", "technology", "cooking", "eduLife", 
                             "environment", "healthLife", "sport", "travel"};

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
            System.out.println("→ Navigated to Profile Page");
            closeOpenDialogs(driver); // Close any potential leftover dialogs

            // --- Test: Tag Selection --- 
            printTestHeader("TEST: TAG SELECTION (TOGGLE ON)");
            selectTags(driver, wait, tagNames);
            verifyTagsOnProfile(driver, wait, tagNames, true);

            // --- Test: Tag Deselection --- 
            printTestHeader("TEST: TAG DESELECTION (TOGGLE OFF)");
            selectTags(driver, wait, tagNames);
            verifyTagsOnProfile(driver, wait, tagNames, false);

            // --- Test: Tag Edit Cancel --- 
            printTestHeader("TEST: EDIT PROFILE TAGS (CANCEL TEST)");
            testTagCancel(driver, wait, tagNames);
            
            printTestResult(true, "Profile Tag Edit Tests", "Completed successfully");

        } catch (Exception e) {
            printTestResult(false, "Profile Tag Edit Tests", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // --- Test Helper Methods ---

    private static void selectTags(WebDriver driver, WebDriverWait wait, String[] tagNames) throws InterruptedException {
        // Click edit tags button
        WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit tags']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editTagsButton);
        System.out.println("→ Clicked the 'edit tags' button using JavaScript");
        Thread.sleep(1000); // Wait for modal
        
        // Wait for dialog presence
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
        System.out.println("→ Tags edit dialog is open for selection");

        // Select all tags
        System.out.println("\n→ Selecting all available interest tags...");
        for (String tagName : tagNames) {
            try {
                WebElement tag = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class, 'MuiDialog-paper')]//*[contains(text(), '" + tagName + "')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tag);
                Thread.sleep(300);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tag);
                printElementCheck(true, "Selected tag", tagName);
                Thread.sleep(500);
            } catch (Exception e) {
                printElementCheck(false, "Selecting tag", tagName + " - Error: " + e.getMessage());
            }
        }

        // Save changes
        WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Save Changes')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveChangesButton);
        System.out.println("→ Saving selected tags...");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
        Thread.sleep(1000); // Wait for UI update
    }

    private static void verifyTagsOnProfile(WebDriver driver, WebDriverWait wait, String[] expectedTags, boolean shouldBePresent) {
        System.out.println("\n→ Verifying tags on profile (Expected present: " + shouldBePresent + ")");
        boolean overallResult = true;
        
        if (shouldBePresent) {
             for (String tagName : expectedTags) {
                try {
                    WebElement tagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(@class, 'css-hze7mg')]//*[contains(text(), '" + tagName + "')]")));
                    printElementCheck(true, "Found tag on profile", tagName);
                } catch (Exception e) {
                    printElementCheck(false, "Found tag on profile", tagName);
                    overallResult = false;
                }
            }
            printTestResult(overallResult, "Tag Selection Verification", 
                    overallResult ? "All expected tags were found on profile" : "Some expected tags were MISSING");
        } else {
            // Verify "No tags selected" message or absence of tags
            try {
                WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'css-hze7mg')]//*[contains(text(), 'No tags selected')]")));
                printTestResult(true, "Tag Deselection Verification", "'No tags selected' message is displayed");
            } catch (Exception e) {
                // If "No tags selected" isn't found, check if any tags are still visible
                boolean anyTagsStillVisible = false;
                System.out.println("\n→ 'No tags selected' not found. Checking for remaining tags:");
                for (String tagName : expectedTags) {
                    try {
                        driver.findElement(By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), '" + tagName + "')]"));
                        printElementCheck(false, "Tag removal check", tagName + " is still visible!");
                        anyTagsStillVisible = true;
                        overallResult = false;
                    } catch (Exception ex) {
                        printElementCheck(true, "Tag removal check", tagName + " was successfully removed");
                    }
                }
                printTestResult(overallResult, "Tag Deselection Verification", 
                        anyTagsStillVisible ? "FAIL: Some tags were not removed" : "PASS: All tags seem removed, but 'No tags selected' msg missing");
            }
        }
    }

    private static void testTagCancel(WebDriver driver, WebDriverWait wait, String[] allTagNames) throws InterruptedException {
        closeOpenDialogs(driver);
        System.out.println("→ Checking initial state before cancel test...");
        // Initial state: Should be no tags
        boolean noTagsInitially = false;
        try {
            WebElement noTagsMessage = driver.findElement(
                  By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]"));
            noTagsInitially = noTagsMessage.isDisplayed();
            printElementCheck(true, "Initial state", "No tags are selected");
        } catch (Exception e) {
            printElementCheck(false, "Initial state", "Expected no tags but found some tags");
        }
      
        // Click edit tags button
        WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit tags']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editTagsButton);
        System.out.println("→ Clicked the 'edit tags' button for cancel test");
        Thread.sleep(1000); // Wait for modal

        // Select a few tags 
        String[] tagsToSelectAndCancel = {allTagNames[0], allTagNames[1]}; // Select first two
        System.out.println("\n→ Selecting tags that will NOT be saved...");
        for (String tagName : tagsToSelectAndCancel) {
            try {
                 WebElement tag = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class, 'MuiDialog-paper')]//*[contains(text(), '" + tagName + "')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tag);
                printElementCheck(true, "Selected tag (for cancel)", tagName);
                Thread.sleep(300);
            } catch (Exception e) {
                 printElementCheck(false, "Selecting tag (for cancel)", tagName + " - Error: " + e.getMessage());
            }
        }
        
        // Click Cancel button 
        try {
            ((JavascriptExecutor) driver).executeScript(
              "const buttons = Array.from(document.querySelectorAll('button')); " +
              "const cancelButton = buttons.find(b => b.textContent.includes('Cancel')); " +
              "if(cancelButton) { cancelButton.click(); }"
            );
            System.out.println("→ Clicked 'Cancel' button using JavaScript");
        } catch (Exception e) {
            System.out.println("→ Failed to click Cancel button with JavaScript: " + e.getMessage());
            WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Cancel')]")));
            cancelButton.click();
            System.out.println("→ Clicked 'Cancel' button with regular click");
        }
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[contains(text(), 'Cancel')]")));
        Thread.sleep(1000);
        
        // Verify state hasn't changed
        boolean noTagsAfterCancel = false;
        try {
            WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")));
            noTagsAfterCancel = noTagsMessage.isDisplayed();
            printElementCheck(true, "State after cancel", "No tags are selected");
        } catch (Exception e) {
            printElementCheck(false, "State after cancel", "Expected no tags but found some tags");
        }
        
        boolean cancelSuccessful = (noTagsInitially == noTagsAfterCancel);
        printTestResult(cancelSuccessful, "Edit Profile Tags (Cancel Test)", 
                      cancelSuccessful ? 
                      "Changes were successfully discarded when clicking Cancel" :
                      "Changes were incorrectly saved despite clicking Cancel");
    }
} 