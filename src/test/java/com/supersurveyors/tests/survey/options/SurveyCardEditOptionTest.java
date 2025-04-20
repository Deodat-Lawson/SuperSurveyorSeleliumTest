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

public class SurveyCardEditOptionTest {

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
                    "Survey Card Edit Option Test",
                    "No survey cards found to test."
                );
                return;
            }

            String title = initialCards.get(0)
                .findElement(By.cssSelector("a[href*='/survey-view/']"))
                .getText();

            testEditOption(driver, wait, title);

        } catch (Exception e) {
            printTestResult(false,
                "Survey Card Edit Option Test",
                "Fatal error: " + e.getMessage()
            );
            e.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    private static void testEditOption(WebDriver driver, WebDriverWait wait, String title) {
        WebElement card = getFreshCardReference(driver, wait, title);
        WebElement moreOptionsButton = findMoreOptionsButton(card);
        if (moreOptionsButton == null) {
             printTestResult(false, "Edit survey option", "Could not find More Options button for card: " + title);
             return;
        }
        moreOptionsButton.click();
        printTestHeader("TEST: Survey Card -> Edit Option");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='menuitem' and .//span[text()='Edit']]")
            )).click();
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("#/edit"),
                ExpectedConditions.urlContains("#/create") // Some versions might redirect to create
            ));
            boolean ok = driver.getCurrentUrl().contains("#/edit") ||
                         driver.getCurrentUrl().contains("#/create");
            printTestResult(ok,
                "Edit survey option",
                ok ? "Navigated successfully to edit page: " + driver.getCurrentUrl()
                   : "Failed to navigate to edit/create page. Current URL: " + driver.getCurrentUrl()
            );
            if (ok) {
                // Navigate back for subsequent tests if needed, or just end here.
                driver.navigate().back();
                wait.until(ExpectedConditions.urlContains("#/view"));
                printTestResult(true, "Edit survey option", "Navigated back to View page successfully.");
            }

        } catch (Exception e) {
            printTestResult(false, "Edit survey option", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
} 