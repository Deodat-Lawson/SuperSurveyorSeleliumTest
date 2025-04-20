package com.supersurveyors.tests.survey;

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

public class CreateSurveyTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        boolean overallStatus = true;

        try {
            printTestHeader("SETUP: Initializing WebDriver");
            System.setProperty("webdriver.chrome.driver",
                    "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            printTestResult(true, "WebDriver Setup", "Initialization successful.");

            login(driver, wait, "timothylinziqimc@gmail.com", "test12345678!");

            navigateToCreatePage(driver, wait);
            fillInitialSurveyDetails(driver, wait);
            addSingleSelectQuestion(driver, wait);
            addMultipleSelectQuestion(driver, wait);
            manageSurveyTags(driver, wait);
            submitSurvey(driver, wait);

            printTestResult(true, "Create Survey Test", "All steps completed successfully.");

        } catch (Exception e) {
            printTestResult(false, "Create Survey Test", "Test failed with error: " + e.getMessage());
            e.printStackTrace();
            overallStatus = false;
        } finally {
            printTestHeader("CLEANUP: Closing WebDriver");
            if (driver != null) {
                driver.quit();
                printTestResult(true, "WebDriver Cleanup", "Closed successfully.");
            } else {
                printTestResult(false, "WebDriver Cleanup", "Driver was null, nothing to close.");
            }
            printTestResult(overallStatus, "Create Survey Test - FINAL STATUS", "");
        }
    }

    private static void navigateToCreatePage(WebDriver driver, WebDriverWait wait) {
        printTestHeader("STEP: Navigate to Create Page");
        try {
            WebElement createNavLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='#/create' and contains(text(),'Create')]")
            ));
            createNavLink.click();
            wait.until(ExpectedConditions.urlContains("#/create"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(),'Create a Survey')]")
            ));
            printTestResult(true, "Navigation", "Successfully navigated to Create page.");
        } catch (Exception e) {
            printTestResult(false, "Navigation", "Failed to navigate to Create page: " + e.getMessage());
            throw e;
        }
    }

    private static void fillInitialSurveyDetails(WebDriver driver, WebDriverWait wait) {
        printTestHeader("STEP: Fill Initial Survey Details");
        try {
            WebElement surveyTitleInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("surveyTitle"))
            );
            surveyTitleInput.clear();
            surveyTitleInput.sendKeys("Selenium Automated Test Survey");
            printElementCheck(true, "Survey Title", "Title entered.");

            WebElement questionInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[normalize-space(text())='Question']/following-sibling::*//input")
                )
            );
            questionInput.clear();
            questionInput.sendKeys("What is your favorite testing framework?");
            printElementCheck(true, "Question 1", "Question text entered.");

            WebElement responseTypeDiv = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[@role='combobox'])[1]")
                )
            );
            printElementCheck(
                responseTypeDiv.getText().contains("Free Response"),
                "Question 1 Type",
                "Default is Free Response"
            );
        } catch (Exception e) {
            printTestResult(false, "Fill Initial Details", "Failed: " + e.getMessage());
            throw e;
        }
    }

    private static void addSingleSelectQuestion(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Add Single Select Question (Q2)");
        try {
            WebElement addBtn = findAddQuestionButton(driver, wait, 1);
            scrollAndClick(driver, addBtn, "Add Question (1st)");

            WebElement qInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[normalize-space(text())='Question']/following-sibling::*//input")
                )
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", qInput);
            Thread.sleep(300);
            qInput.clear();
            qInput.sendKeys("Which IDE do you prefer?");
            printElementCheck(true, "Question 2 Text", "Entered Q2 text.");

            // DEBUG: locate triggers by CSS class
            List<WebElement> triggers = driver.findElements(
                By.cssSelector("div.MuiSelect-select")
            );
            System.out.println("DEBUG: Found " + triggers.size() + " dropdown triggers (MuiSelect-select).");
            for (int i = 0; i < triggers.size(); i++) {
                System.out.println("DEBUG: Trigger[" + i + "] text='" + triggers.get(i).getText() + "'");
            }

            // Use the first trigger for Q2
            if (triggers.isEmpty()) {
                throw new RuntimeException("No dropdown triggers found for Select");
            }
            WebElement responseType = triggers.get(0);
            scrollAndClick(driver, responseType, "Q2 Response Type");

            WebElement singleOpt = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("li[role='option'][data-value='radio']")
                )
            );
            singleOpt.click();
            wait.until(ExpectedConditions.textToBePresentInElement(responseType, "Single Select"));
            printElementCheck(true, "Question 2 Type", "Set to Single Select");

            addOptionsToQuestion(driver, wait, 2, new String[]{"VS Code", "IntelliJ IDEA", "Eclipse"});
        } catch (Exception e) {
            printTestResult(false, "Add Single Select Q", "Failed: " + e.getMessage());
            throw e;
        }
    }

    private static void addMultipleSelectQuestion(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Add Multiple Select Question (Q3)");
        try {
            WebElement addBtn2 = findAddQuestionButton(driver, wait, 2);
            scrollAndClick(driver, addBtn2, "Add Question (2nd)");

            WebElement qInput2 = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[normalize-space(text())='Question']/following-sibling::*//input")
                )
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", qInput2);
            Thread.sleep(300);
            qInput2.clear();
            qInput2.sendKeys("Which languages do you know?");
            printElementCheck(true, "Question 3 Text", "Entered Q3 text.");

            List<WebElement> triggers = driver.findElements(By.cssSelector("div.MuiSelect-select"));
            System.out.println("DEBUG: Found " + triggers.size() + " dropdown triggers.");
            for (int i = 0; i < triggers.size(); i++) {
                System.out.println("DEBUG: Trigger[" + i + "] text='" + triggers.get(i).getText() + "'");
            }

            if (triggers.size() < 2) {
                throw new RuntimeException("Not enough dropdown triggers; expected at least 2");
            }
            WebElement responseType3 = triggers.get(1);
            scrollAndClick(driver, responseType3, "Q3 Response Type");

            WebElement multiOpt = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("li[role='option'][data-value='checkbox']")
                )
            );
            multiOpt.click();
            wait.until(ExpectedConditions.textToBePresentInElement(responseType3, "Multiple Select"));
            printElementCheck(true, "Question 3 Type", "Set to Multiple Select");

            addOptionsToQuestion(driver, wait, 3, new String[]{"Java", "Python", "JavaScript"});
        } catch (Exception e) {
            printTestResult(false, "Add Multiple Select Q", "Failed: " + e.getMessage());
            throw e;
        }
    }

    private static void addOptionsToQuestion(WebDriver driver, WebDriverWait wait, int i, String[] opts) {
        printTestHeader("--> Adding Options to Question " + i);
        String optXPath = String.format("(//input[@placeholder='Enter option'])[%d]", i);
        String btnXPath = optXPath + "/following-sibling::button[contains(text(),'Add') or @aria-label='Add']";
        for (String o : opts) {
            try {
                WebElement inp = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(optXPath))
                );
                inp.clear(); inp.sendKeys(o);
                WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath(btnXPath))
                );
                scrollAndClick(driver, btn, "Add option '" + o + "'");
                printElementCheck(true, "Add Option Q" + i, "Added '" + o + "'");
            } catch (Exception e) {
                printElementCheck(false, "Add Option Q" + i, "Failed to add '" + o + "': " + e.getMessage());
            }
        }
    }

    private static WebElement findAddQuestionButton(WebDriver driver, WebDriverWait wait, int idx) {
        String xp = String.format("(//button[contains(text(),'Add Question')])[%d]", idx);
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xp)));
    }

    private static void manageSurveyTags(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Manage Survey Tags");
        throw new UnsupportedOperationException("Tag management omitted for brevity");
    }

    private static void submitSurvey(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Submit Survey");
        throw new UnsupportedOperationException("Submit omitted for brevity");
    }

    private static void scrollAndClick(WebDriver driver, WebElement el, String name) throws InterruptedException {
        try {
            // Scroll element to the bottom of the view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", el);
            Thread.sleep(500); // Increased pause after scroll
            el.click();
            printElementCheck(true, "Click Element", "'" + name + "' clicked.");
        } catch (Exception e) {
            printElementCheck(false, "Click Element", "Failed to click '" + name + "': " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
