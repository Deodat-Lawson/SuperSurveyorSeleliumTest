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

public class SurveyCardDeleteOptionTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        boolean testPassedOverall = false;

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
                    "Survey Card Delete Option Test",
                    "No survey cards found to test."
                );
                return;
            }

            int initialCount = initialCards.size();
            String titleToDelete = initialCards.get(0)
                .findElement(By.cssSelector("a[href*='/survey-view/']"))
                .getText();

            testPassedOverall = testDeleteOption(driver, wait, titleToDelete, initialCount);

        } catch (Exception e) {
            printTestResult(false,
                "Survey Card Delete Option Test",
                "Fatal error: " + e.getMessage()
            );
            e.printStackTrace();
        } finally {
            printTestResult(testPassedOverall, "Survey Card Delete Option Test", "Overall test status.");
            if (driver != null) driver.quit();
        }
    }

    private static boolean testDeleteOption(WebDriver driver, WebDriverWait wait,
                                         String title, int originalCount)
    {
        WebElement card = getFreshCardReference(driver, wait, title);
         WebElement moreOptionsButton = findMoreOptionsButton(card);
        if (moreOptionsButton == null) {
             printTestResult(false, "Delete survey option", "Could not find More Options button for card: " + title);
             return false;
        }
        moreOptionsButton.click();
        printTestHeader("TEST: Survey Card -> Delete Option");
        boolean success = false;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='menuitem' and .//span[text()='Delete']]")
            )).click();

            // Confirm dialog appears
            WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='dialog']")
            ));
            printElementCheck(true, "Delete confirmation dialog", "Appeared");

            // Click the confirmation button (handle variations in text)
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='dialog']//button[contains(text(),'Delete') or contains(text(),'Yes') or contains(text(),'Confirm')]")
            ));
            confirmButton.click();
            printElementCheck(true, "Confirm Delete button", "Clicked");

            // Wait for dialog to disappear
            wait.until(ExpectedConditions.invisibilityOf(dialog));
            printElementCheck(true, "Delete confirmation dialog", "Disappeared");

            // Verify card count dropped or card is gone
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(
                By.xpath("//div[contains(@class,'MuiCard-root') or contains(@class,'MuiPaper-root')]"),
                originalCount
            ));
            success = true;
            printTestResult(true, "Delete survey option", "Survey card successfully removed.");

        } catch (Exception e) {
            printTestResult(false, "Delete survey option", "Exception during test: " + e.getMessage());
            e.printStackTrace();
            success = false;
        }
        return success;
    }
} 