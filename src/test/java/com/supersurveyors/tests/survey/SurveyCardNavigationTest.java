package com.supersurveyors.tests.survey;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.supersurveyors.tests.TestUtils.*;

public class SurveyCardNavigationTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        try {
            // 1. Setup WebDriver
            System.setProperty(
              "webdriver.chrome.driver",
              "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver"
            );
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();

            // 2. Log in
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login");
            WebElement email = wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.id("email"))
            );
            email.clear();
            email.sendKeys("timothylinziqimc@gmail.com");

            WebElement pass = wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.id("password"))
            );
            pass.clear();
            pass.sendKeys("test12345678!");

            WebElement signIn = wait.until(
              ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' and contains(text(),'Sign In')]")
              )
            );
            signIn.click();
            wait.until(ExpectedConditions.urlContains("#/home"));

            // 3. Go to “View” page
            WebElement viewNav = wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'View')]"))
            );
            viewNav.click();
            wait.until(ExpectedConditions.urlContains("#/view"));
            System.out.println("→ Navigated to View page");

            // 4. Run the survey‐card navigation test
            printTestHeader("TEST: SURVEY CARD NAVIGATION");
            boolean ok = testSurveyCardNavigation(driver, wait);

            // 5. Report overall result
            if (ok) {
                printTestResult(true,
                    "Survey Card Navigation Test",
                    "Completed successfully"
                );
            } else {
                printTestResult(false,
                    "Survey Card Navigation Test",
                    "One or more steps failed"
                );
            }

        } catch (Exception e) {
            printTestResult(false,
                "Survey Card Navigation Test",
                "Error: " + e.getMessage()
            );
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    /**
     * Returns true if all steps pass, false otherwise.
     */
    private static boolean testSurveyCardNavigation(WebDriver driver, WebDriverWait wait) {
        try {
            // 1) Wait for at least one card to appear
            wait.until(ExpectedConditions
              .visibilityOfElementLocated(By.cssSelector("div.MuiCard-root")));
            List<WebElement> cards = driver.findElements(
              By.cssSelector("div.MuiCard-root")
            );
            if (cards.isEmpty()) {
                printTestResult(false,
                    "Survey card navigation",
                    "No survey cards found"
                );
                return false;
            }

            // 2) Find the title link in the first card
            WebElement link = cards.get(0)
              .findElement(By.cssSelector("a[href*='/survey-view/']"));
            String title = link.getText();
            System.out.println("→ Found survey titled: " + title);

            // 3) Click and verify
            link.click();
            wait.until(ExpectedConditions.urlContains("#/survey-view/"));
            boolean navOk = driver.getCurrentUrl().contains("#/survey-view/");
            printTestResult(navOk,
                "Survey card navigation",
                navOk
                  ? "Successfully navigated to " + driver.getCurrentUrl()
                  : "Did not reach survey-view URL"
            );
            if (!navOk) return false;

            // 4) Go back and verify
            driver.navigate().back();
            wait.until(ExpectedConditions.urlContains("#/view"));
            System.out.println("→ Back on View page");
            return true;

        } catch (Exception e) {
            printTestResult(false,
                "Survey card navigation",
                "Exception: " + e.getMessage()
            );
            return false;
        }
    }
}
