package com.supersurveyors.tests.view;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import static com.supersurveyors.tests.TestUtils.*;

public class SurveyListViewTest {

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

            // --- Test: Navigation to View Page --- 
            printTestHeader("TEST: VIEW PAGE NAVIGATION");
            WebElement viewNavButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'View')]")));
            viewNavButton.click();
            System.out.println("→ Clicked on 'View' navigation button");
            wait.until(ExpectedConditions.urlContains("#/view"));
            printTestResult(driver.getCurrentUrl().contains("#/view"), 
                           "Navigation to View page",
                           "Successfully navigated to View page: " + driver.getCurrentUrl());

            // --- Test: View Page UI Elements --- 
            printTestHeader("TEST: VIEW PAGE UI ELEMENTS");
            verifyViewPageUI(driver, wait);

            // --- Test: Survey Card Verification --- 
            printTestHeader("TEST: SURVEY CARD VERIFICATION");
            verifySurveyCards(driver, wait);

            printTestResult(true, "Survey List View Tests", "Completed successfully");

        } catch (Exception e) {
            printTestResult(false, "Survey List View Tests", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    
    // --- Test Helper Methods ---

    private static void verifyViewPageUI(WebDriver driver, WebDriverWait wait) {
        // Verify page header
        WebElement pageHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Your Surveys')]")));
        printElementCheck(pageHeader.isDisplayed(), "Page header", pageHeader.getText());
        
        // Verify SuperSurveyors logo and header
        WebElement headerLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[contains(text(), 'SuperSurveyors')]")));
        printElementCheck(headerLogo.isDisplayed(), "Header logo text", headerLogo.getText());
        
        // Verify navigation buttons
        String[] navButtons = {"View", "Trending", "Create", "Answer"};
        for (String buttonText : navButtons) {
            WebElement navButton = driver.findElement(By.xpath("//a[contains(text(),'" + buttonText + "')]"));
            printElementCheck(navButton.isDisplayed(), "Navigation button", buttonText);
        }
    }

    private static void verifySurveyCards(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        Thread.sleep(2000); // Wait for cards to potentially load
        List<WebElement> surveyCards = new java.util.ArrayList<>();
        // Use multiple selectors to find cards
        try {
            surveyCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//div[contains(@class,'MuiCard-root')]")));
        } catch (Exception e) { 
            try { surveyCards = driver.findElements(By.xpath("//div[contains(@class,'MuiGrid-item')]//div[contains(@class,'MuiPaper-root')]")); } catch (Exception e2) {}
        }
        if (surveyCards.isEmpty()) {
             try { surveyCards = driver.findElements(By.xpath("//a[contains(@class,'MuiTypography') and @href]/ancestor::div[contains(@class,'MuiPaper-root')]")); } catch (Exception e3) {} 
        }

        printTestResult(!surveyCards.isEmpty(), 
                        "Survey cards display",
                        surveyCards.isEmpty() ? "No survey cards found (user may not have any surveys)" : 
                                              surveyCards.size() + " survey cards found");

        if (!surveyCards.isEmpty()) {
            // Check structure of the first card
            WebElement firstCard = surveyCards.get(0);
            WebElement surveyTitle = firstCard.findElement(By.xpath(".//a[contains(@class, 'MuiTypography-root')]"));
            printElementCheck(surveyTitle.isDisplayed(), "Survey title", surveyTitle.getText());
            WebElement moreOptionsButton = firstCard.findElement(By.xpath(".//button[contains(@class, 'MuiIconButton-root')]"));
            printElementCheck(moreOptionsButton.isDisplayed(), "More options button", "Present");
            try {
                WebElement tagsSection = firstCard.findElement(By.xpath(".//p[contains(text(), 'No tags') or contains(@class, 'MuiChip-root')]"));
                printElementCheck(tagsSection.isDisplayed(), "Tags section", tagsSection.getText());
            } catch (Exception e) { printElementCheck(false, "Tags section", "Not found or has chips"); }
            try {
                WebElement questionCount = firstCard.findElement(By.xpath(".//p[contains(text(), 'question')]"));
                printElementCheck(questionCount.isDisplayed(), "Question count", questionCount.getText());
            } catch (Exception e) { printElementCheck(false, "Question count", "Not found"); }
        }
    }
} 