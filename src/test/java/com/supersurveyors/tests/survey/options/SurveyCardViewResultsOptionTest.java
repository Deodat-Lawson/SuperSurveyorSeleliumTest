package com.supersurveyors.tests.survey.options;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.supersurveyors.tests.TestUtils.*;

public class SurveyCardViewResultsOptionTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;

        try {
            System.setProperty(
                "webdriver.chrome.driver",
                "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver"
            );
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            login(driver, wait, "timothylinziqimc@gmail.com", "test12345678!");
            navigateToViewPage(driver, wait);

            List<WebElement> initialCards = getVisibleCards(driver, wait);
            if (initialCards.isEmpty()) {
                printTestResult(false,
                    "Survey Card View Results Test",
                    "No survey cards found to test."
                );
                return;
            }

            String title = initialCards.get(0)
                .findElement(By.cssSelector("a[href*='/survey-view/']"))
                .getText();

            testViewResultsOption(driver, wait, title);

        } catch (Exception e) {
            printTestResult(false,
                "Survey Card View Results Test",
                "Fatal error: " + e.getMessage()
            );
            e.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    private static void testViewResultsOption(WebDriver driver, WebDriverWait wait, String title) {
        WebElement card = getFreshCardReference(driver, wait, title);
         WebElement moreOptionsButton = findMoreOptionsButton(card);
        if (moreOptionsButton == null) {
             printTestResult(false, "View Results option", "Could not find More Options button for card: " + title);
             return;
        }
        moreOptionsButton.click();
        printTestHeader("TEST: Survey Card -> View Results Option");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='menuitem' and .//span[contains(text(),'Results')]]")
            )).click();
            wait.until(ExpectedConditions.urlContains("#/survey-results"));
            printTestResult(true,
                "View Results option",
                "Navigated successfully to results page: " + driver.getCurrentUrl()
            );
            // Navigate back
            driver.navigate().back();
            wait.until(ExpectedConditions.urlContains("#/view"));
            printTestResult(true, "View Results option", "Navigated back to View page successfully.");

        } catch (Exception e) {
            printTestResult(false, "View Results option", "Exception during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 