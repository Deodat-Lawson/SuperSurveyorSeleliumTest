package com.supersurveyors.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class TestUtils {

    // Helper methods for better output formatting (static)
    public static void printTestHeader(String header) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" " + header);
        System.out.println("=".repeat(80));
    }
  
    public static void printTestResult(boolean success, String testName, String message) {
        String resultIcon = success ? "✅ PASS" : "❌ FAIL";
        System.out.println("\n" + resultIcon + " | " + testName + ": " + message);
    }
  
    public static void printElementCheck(boolean success, String elementName, String value) {
        String checkIcon = success ? "✓" : "✗";
        System.out.println(checkIcon + " " + elementName + ": " + value);
    }
  
    // Helper method to check for and close any open dialogs (static)
    public static void closeOpenDialogs(WebDriver driver) {
        try {
            // Use a more general selector for close buttons
            List<WebElement> closeButtons = driver.findElements(
                By.xpath("//button[contains(@aria-label, 'close') or contains(text(), 'Close') or contains(text(), 'Cancel')] | //div[contains(@class, 'MuiBackdrop-root')]")
            );
            for (WebElement closeButton : closeButtons) {
                if (closeButton.isDisplayed() && closeButton.isEnabled()) {
                    try {
                        // Attempt to click, handle potential backdrop separately
                        if (closeButton.getTagName().equals("div")) { 
                            // It's a backdrop
                            System.out.println("→ Found backdrop, attempting to click it to close dialog");
                            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", closeButton);
                        } else {
                            System.out.println("→ Found close button, attempting to click it");
                            closeButton.click();
                        }
                        System.out.println("→ Closed previously open dialog/backdrop");
                        Thread.sleep(500); // Wait for dialog to close
                        // After closing one, break or re-check as the DOM might change
                        break; 
                    } catch (Exception clickEx) {
                        System.out.println("→ Error clicking close button/backdrop: " + clickEx.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            // It's okay if no dialogs are open
            System.out.println("→ No open dialogs detected or couldn't close dialog: " + e.getMessage());
        }
    }
  
    // Helper method to get a fresh reference to a survey card by title (static)
    public static WebElement getFreshCardReference(WebDriver driver, WebDriverWait wait, String cardTitle) {
        if (cardTitle == null || cardTitle.isEmpty()) {
            // If we don't have a title, just get the first card
            List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class,'MuiCard-root') or contains(@class,'MuiPaper-root')]"));
            if (!cards.isEmpty()) {
                return cards.get(0);
            } else {
                throw new RuntimeException("No survey cards found");
            }
        }
    
        // Wait for the card with the specific title to be present
        WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[normalize-space(text())='" + cardTitle + "']/ancestor::div[contains(@class,'MuiCard-root') or contains(@class,'MuiPaper-root')]")));
        return card;
    }
  
    // Helper method to find the more options button in a card (static)
    public static WebElement findMoreOptionsButton(WebElement card) {
        // Find button containing the MoreVertIcon SVG
        return card.findElement(By.cssSelector("button.MuiButtonBase-root"));
    }

    // Helper method to perform login
    public static void login(WebDriver driver, WebDriverWait wait, String email, String password) {
        printTestHeader("STEP: Logging In");
        driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(email);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' and contains(text(),'Sign In')]")
            )).click();
            wait.until(ExpectedConditions.urlContains("#/home"));
            printTestResult(true, "Login", "Successfully logged in as " + email);
        } catch (Exception e) {
            printTestResult(false, "Login", "Failed to log in: " + e.getMessage());
            throw e; // Re-throw exception to stop the test
        }
    }

    // Helper method to navigate to the View page
    public static void navigateToViewPage(WebDriver driver, WebDriverWait wait) {
        printTestHeader("STEP: Navigating to View Page");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'View')]")
            )).click();
            wait.until(ExpectedConditions.urlContains("#/view"));
            printTestResult(true, "Navigation", "Successfully navigated to View page.");
        } catch (Exception e) {
            printTestResult(false, "Navigation", "Failed to navigate to View page: " + e.getMessage());
            throw e; // Re-throw exception
        }
    }

    // Helper method to get currently visible survey cards
    public static List<WebElement> getVisibleCards(WebDriver driver, WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'MuiCard-root') or contains(@class,'MuiPaper-root')]")));
            return driver.findElements(By.xpath("//div[contains(@class,'MuiCard-root') or contains(@class,'MuiPaper-root')]"));
        } catch (Exception e) {
            printTestResult(false, "Get Cards", "Failed to find survey cards: " + e.getMessage());
            return List.of(); // Return empty list on failure
        }
    }
} 