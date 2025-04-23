package com.supersurveyors.tests.survey.options;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.supersurveyors.tests.TestUtils.*;
import static org.openqa.selenium.Keys.*;

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

            // Test that we add a tag and the tag will be populated
            openEdit(driver,wait, title);
            addAndSaveTag(driver,wait,title);

            // Test that we remove a tag and the tag will disappear
            openEdit(driver,wait, title);
            removeAndSaveTag(driver,wait,title);




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

    private static void openEdit(WebDriver driver, WebDriverWait wait, String title){
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
        }
        catch (Exception e){
            printTestResult(false, "Failed to open Edit Survey Option", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }


    private static void addAndSaveTag(WebDriver driver, WebDriverWait wait, String title) {
        try {
            WebElement TagsEdit = driver.findElement(By.cssSelector("input[placeholder='Enter tags']"));
            TagsEdit.click();
            TagsEdit.sendKeys("testTag");
            TagsEdit.sendKeys(ENTER);


            Thread.sleep(500);

            // 3) Click “Save Changes”
            // Save changes
            WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Save Changes')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveChangesButton);
            System.out.println("→ Saving added tags...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
            Thread.sleep(1000); // Wait for UI update

            WebElement testTag = driver.findElement(By.xpath("//*[text()='testTag']"));

            System.out.println("Test Tag: " + testTag.getText());

            if (testTag.getText().equals("testTag")) {
                printTestResult(true, "Edit survey option", "Tag ‘testTag’ was successfully added.");
            } else {
                printTestResult(false, "Edit survey option", "Tag ‘testTag’ did not appear.");
            }

        } catch (Exception e) {
            printTestResult(false, "Edit survey option", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    private static void removeAndSaveTag(WebDriver driver, WebDriverWait wait, String title) {
        try {
            Thread.sleep(1000); // Wait for dialog to fully load

            // Find the tag input field
            WebElement tagsEdit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[placeholder='Enter tags']")));

            // First clear any existing tag
            WebElement existingTag = driver.findElement(By.xpath("//div[contains(@class, 'MuiChip-root')]"));
            WebElement deleteButton = existingTag.findElement(By.xpath(".//button"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);

            System.out.println("→ Removed existing tag");
            Thread.sleep(500); // Wait for UI update

            // Click "Save Changes"
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            System.out.println("→ Saving changes after tag removal...");

            // Wait for dialog to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
            Thread.sleep(1000); // Wait for UI update

            try {
                WebElement noTagsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[text()='No tags available.']")));
                System.out.println("Tag text: " + noTagsElement.getText());

                if (noTagsElement.getText().equals("No tags available.")) {
                    printTestResult(true, "Edit survey option", "Tag 'testTag' was successfully removed.");
                } else {
                    printTestResult(false, "Edit survey option", "Tag 'testTag' did not get removed.");
                }
            } catch (TimeoutException e) {
                // If we can't find "No tags available", check if any tags still exist
                try {
                    WebElement existingTagAfterSave = driver.findElement(By.xpath("//div[contains(@class, 'MuiChip-root')]"));
                    printTestResult(false, "Edit survey option", "Tag was not removed, still showing: " + existingTagAfterSave.getText());
                } catch (NoSuchElementException ex) {
                    printTestResult(true, "Edit survey option", "Tag was successfully removed, but no 'No tags available' message found.");
                }
            }
        } catch (Exception e) {
            printTestResult(false, "Remove and Save Tag", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
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

            WebElement TagsEdit = driver.findElement(By.cssSelector("input[placeholder='Enter tags']"));
            TagsEdit.click();
            TagsEdit.sendKeys("testTag");
            TagsEdit.sendKeys(ENTER);

            Thread.sleep(300);

            // 3) Click “Save Changes”
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            saveBtn.click();
            System.out.println("TEST: SAVE CHANGES BUTTON CLICKED");

            Thread.sleep(300);
            WebElement testTag = driver.findElement(By.xpath("//*[text()='testTag']"));

            System.out.println("Test Tag: " + testTag.getText());

            if (testTag.getText().equals("testTag")) {
                printTestResult(true, "Edit survey option", "Tag ‘testTag’ was successfully added.");
            } else {
                printTestResult(false, "Edit survey option", "Tag ‘testTag’ did not appear.");
            }

        } catch (Exception e) {
            printTestResult(false, "Edit survey option", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }

    }






} 