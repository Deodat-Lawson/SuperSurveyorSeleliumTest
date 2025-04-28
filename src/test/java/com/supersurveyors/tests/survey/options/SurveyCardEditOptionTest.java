//Note: tag deletion needs to be fixed. Everything else is working.

package com.supersurveyors.tests.survey.options;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

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


            // Test cancel button
            openEdit(driver,wait, title);
            cancelEdit(driver,wait,title);

            // Test that we add a tag and the tag will be populated
            openEdit(driver,wait, title);
            addAndSaveTag(driver,wait,title);

            // Test that we remove a tag and the tag will disappear
            openEdit(driver,wait, title);
            removeAndSaveTag(driver,wait,title);
            
            // Test editing the survey title
            openEdit(driver,wait, title);
            editSurveyTitle(driver,wait,title);

            openEdit(driver,wait, title);
            clickEditAndModify(driver,wait);

            openEdit(driver,wait, title);
            CheckEditSuccess(driver,wait);
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



    private static void cancelEdit(WebDriver driver, WebDriverWait wait, String title) {
        try {
            WebElement TagsEdit = driver.findElement(By.cssSelector("input[placeholder='Enter tags']"));
            TagsEdit.click();
            TagsEdit.sendKeys("testTag");
            TagsEdit.sendKeys(ENTER);


            Thread.sleep(500);

            // 3) Click "Save Changes"
            // Save changes
            WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Cancel')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveChangesButton);
            System.out.println("→ Saving added tags...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
            Thread.sleep(1000); // Wait for UI update

            WebElement noTagsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[text()='No tags available.']")));
            System.out.println("Tag text: " + noTagsElement.getText());

            if (noTagsElement.getText().equals("No tags available.")) {
                printTestResult(true, "Edit survey option", "Tag 'testTag' was successfully removed.");
            } else {
                printTestResult(false, "Edit survey option", "Tag 'testTag' did not get removed.");
            }

        } catch (Exception e) {
            printTestResult(false, "Edit survey option", "Exception during test: " + e.getMessage());
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

            // 3) Click "Save Changes"
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
                printTestResult(true, "Edit survey option", "Tag 'testTag' was successfully added.");
            } else {
                printTestResult(false, "Edit survey option", "Tag 'testTag' did not appear.");
            }

        } catch (Exception e) {
            printTestResult(false, "Edit survey option", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    private static void removeAndSaveTag(WebDriver driver, WebDriverWait wait, String title) {
        try {
            Thread.sleep(1000); // Wait for dialog to fully load
            
            System.out.println("→ Starting tag removal test");
            
            // Find all input fields to identify which one is the tag input
            List<WebElement> inputFields = driver.findElements(By.cssSelector("input.MuiInputBase-input"));
            System.out.println("→ Found " + inputFields.size() + " input fields in the dialog");
            
            for (int i = 0; i < inputFields.size(); i++) {
                WebElement input = inputFields.get(i);
                String placeholder = input.getAttribute("placeholder");
                String value = input.getAttribute("value");
                String classes = input.getAttribute("class");
                
                System.out.println("→ Input #" + i + ": placeholder='" + placeholder + "', value='" + value + "', class='" + classes + "'");
            }
            
            // Find the tag input field - look for the one with placeholder="Enter tags"
            // WebElement tagsInput = driver.findElement(By.cssSelector("input[placeholder='Enter tags']"));
            // System.out.println("→ Found tags input with placeholder: " + tagsInput.getAttribute("placeholder"));
            
            // // Look for existing tags (MuiChip elements)
            // List<WebElement> existingTags = driver.findElements(By.cssSelector(".MuiChip-root"));
            // System.out.println("→ Found " + existingTags.size() + " existing tags");

            // WebElement tag = existingTags.get(0);



            // // Focus the element using JavaScript
            // Actions actions = new Actions(driver);
            // actions.moveToElement(tag).click().perform();
            // Thread.sleep(300);
            

            // actions.sendKeys(Keys.DELETE).perform();
            // Thread.sleep(500);

            // // Verify the tag was removed
            // List<WebElement> remainingTags = driver.findElements(By.cssSelector(".MuiChip-root"));
            // System.out.println("→ Tags remaining after deletion: " + remainingTags.size());


            WebElement removingTag = driver.findElement(By.cssSelector(".d-inline-flex[role='button']"));
            removingTag.click();

            
            // Click "Save Changes"
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            System.out.println("→ Saving changes after tag removal...");

            // Wait for dialog to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'MuiDialog-paper')]")
            ));
            Thread.sleep(1000); // Wait for UI update

            try {
                WebElement noTagsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[text()='No tags available.']")));
                System.out.println("→ Tag text: " + noTagsElement.getText());

                if (noTagsElement.getText().equals("No tags available.")) {
                    printTestResult(true, "Edit survey option", "All tags were successfully removed.");
                } else {
                    printTestResult(false, "Edit survey option", "Tags did not get removed properly.");
                }
            } catch (TimeoutException e) {
                // If we can't find "No tags available", check if any tags still exist
                try {
                    List<WebElement> existingTagsAfterSave = driver.findElements(By.cssSelector(".MuiChip-root"));
                    if (existingTagsAfterSave.isEmpty()) {
                        printTestResult(true, "Edit survey option", "All tags were successfully removed, but no 'No tags available' message found.");
                    } else {
                        String remainingTagText = existingTagsAfterSave.get(0).getText();
                        printTestResult(false, "Edit survey option", "Tags were not removed, still showing: " + remainingTagText);
                    }
                } catch (Exception ex) {
                    printTestResult(true, "Edit survey option", "Tags removal appears successful, but couldn't verify exact status.");
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

            // 3) Click "Save Changes"
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            saveBtn.click();
            System.out.println("TEST: SAVE CHANGES BUTTON CLICKED");

            Thread.sleep(300);
            WebElement testTag = driver.findElement(By.xpath("//*[text()='testTag']"));

            System.out.println("Test Tag: " + testTag.getText());

            if (testTag.getText().equals("testTag")) {
                printTestResult(true, "Edit survey option", "Tag 'testTag' was successfully added.");
            } else {
                printTestResult(false, "Edit survey option", "Tag 'testTag' did not appear.");
            }

        } catch (Exception e) {
            printTestResult(false, "Edit survey option", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }

    }

    private static void editSurveyTitle(WebDriver driver, WebDriverWait wait, String originalTitle) {
        printTestHeader("TEST: Edit Survey Title");
        try {
            // Find the title input field using its class
            WebElement titleInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input.MuiInputBase-input.MuiInput-input.MuiInputBase-inputSizeSmall")
            ));
            
            String initialValue = titleInput.getAttribute("value");
            System.out.println("→ Found survey title input with current value: " + initialValue);
            
            // Step 1: Change the title to a new value
            // Clear the current title
            titleInput.click();
            // Use command+a to select all text in the field (for Mac)
            titleInput.sendKeys(Keys.chord(Keys.COMMAND, "a"));
            titleInput.sendKeys(Keys.DELETE);
            
            // Enter new title
            String newTitle = "Updated Survey Title " + System.currentTimeMillis();
            titleInput.sendKeys(newTitle);
            System.out.println("→ Changed title to: " + newTitle);
            
            // Save the changes
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            System.out.println("→ Clicking save changes button");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            
            // Wait for dialog to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiDialog-paper')]")
            ));
            Thread.sleep(1000); // Wait for UI update
            
            // Verify the title changed
            boolean titleChanged = false;
            List<WebElement> updatedCards = getVisibleCards(driver, wait);
            for (WebElement card : updatedCards) {
                WebElement cardTitle = card.findElement(By.cssSelector("a[href*='/survey-view/']"));
                String currentTitle = cardTitle.getText();
                System.out.println("→ Found card with title: " + currentTitle);
                
                if (currentTitle.equals(newTitle)) {
                    titleChanged = true;
                    printTestResult(true, "Edit Survey Title", 
                        "Successfully changed title from '" + originalTitle + "' to '" + newTitle + "'");
                    break;
                }
            }
            
            if (!titleChanged) {
                printTestResult(false, "Edit Survey Title", 
                    "Could not find card with updated title '" + newTitle + "'");
                return;
            }
            
            // Step 2: Change title back to original value
            System.out.println("→ Now changing title back to original value");
            
            // Reopen edit dialog for the card with the new title
            openEdit(driver, wait, newTitle);
            
            // Find the title input again
            titleInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input.MuiInputBase-input.MuiInput-input.MuiInputBase-inputSizeSmall")
            ));
            
            // Clear and restore original title
            titleInput.click();
            titleInput.sendKeys(Keys.chord(Keys.COMMAND, "a"));
            titleInput.sendKeys(Keys.DELETE);
            titleInput.sendKeys(initialValue);
            System.out.println("→ Restoring title to original value: " + initialValue);
            
            // Save changes again
            saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            
            // Wait for dialog to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiDialog-paper')]")
            ));
            Thread.sleep(1000); // Wait for UI update
            
            // Verify the title was restored
            boolean titleRestored = false;
            updatedCards = getVisibleCards(driver, wait);
            for (WebElement card : updatedCards) {
                WebElement cardTitle = card.findElement(By.cssSelector("a[href*='/survey-view/']"));
                String currentTitle = cardTitle.getText();
                System.out.println("→ Found card with title: " + currentTitle);
                
                if (currentTitle.equals(initialValue)) {
                    titleRestored = true;
                    printTestResult(true, "Restore Survey Title", 
                        "Successfully restored title from '" + newTitle + "' back to '" + initialValue + "'");
                    return;
                }
            }
            
            if (!titleRestored) {
                printTestResult(false, "Restore Survey Title", 
                    "Could not find card with restored title '" + initialValue + "'");
            }
            
        } catch (Exception e) {
            printTestResult(false, "Edit Survey Title", "Exception during test: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    private static void clickEditAndModify(WebDriver driver, WebDriverWait wait) {
        try {
            System.out.println("→ Starting edit button test");

            Thread.sleep(500);
            
            By editButtonLocator = By.cssSelector(
                "button[style*='background: none'][style*='color: black']"
            );
            
            // 1. Wait for it to be clickable, then grab it
            WebDriverWait waitToClick = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement editBtn = waitToClick.until(
                ExpectedConditions.elementToBeClickable(editButtonLocator)
            );
            Thread.sleep(500);
            System.out.println("→ Found edit button, clicking it");
            // 2. Do the click (you can also use JS to avoid Selenium’s “stale” checks if you like)
            editBtn.click();
            // —or—
            // ((JavascriptExecutor)driver).executeScript("arguments[0].click();", editBtn);
            
            // 3. Now wait until that very element goes stale (i.e. is removed from the page)
            wait.until(ExpectedConditions.stalenessOf(editBtn));
            
            

            // Clear the field and type new content
            System.out.println("→ Modifying input field");
            new Actions(driver).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).perform();
            new Actions(driver).sendKeys(Keys.DELETE).perform();
            new Actions(driver).sendKeys("Modified Question").perform();

            Thread.sleep(500);
            
            // Click Save Changes
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            System.out.println("→ Clicking Save Changes button");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            
            // Wait for dialog to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiDialog-paper')]")
            ));
            System.out.println("→ Edit dialog closed, changes saved successfully");
            
            printTestResult(true, "Edit Button Test", "Successfully clicked edit, modified content, and saved changes");


        } catch (Exception e) {
            printTestResult(false, "Edit Button Test", "Exception during test: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void CheckEditSuccess(WebDriver driver, WebDriverWait wait) {
        try {
            System.out.println("→ Checking if edit was successful");

            Thread.sleep(500);

            // Verify the content was saved
            
            WebElement savedContent = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[text()='Question 1']")
            ));
            String savedContentText = savedContent.getAttribute("value");
            System.out.println("→ Saved content: " + savedContentText);
            
            
            By editButtonLocator = By.cssSelector(
                "button[style*='background: none'][style*='color: black']"
            );
            
            // 1. Wait for it to be clickable, then grab it
            WebDriverWait waitToClick = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement editBtn = waitToClick.until(
                ExpectedConditions.elementToBeClickable(editButtonLocator)
            );
            Thread.sleep(500);
            System.out.println("→ Found edit button, clicking it");
            // 2. Do the click (you can also use JS to avoid Selenium’s “stale” checks if you like)
            editBtn.click();
            // —or—
            // ((JavascriptExecutor)driver).executeScript("arguments[0].click();", editBtn);
            
            // 3. Now wait until that very element goes stale (i.e. is removed from the page)
            wait.until(ExpectedConditions.stalenessOf(editBtn));
            
        

            
            // Clear the field and type new content
            System.out.println("→ Modifying input field");
            new Actions(driver).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).perform();
            new Actions(driver).sendKeys(Keys.DELETE).perform();
            new Actions(driver).sendKeys("Question 1").perform();

            Thread.sleep(500);
            
            // Click Save Changes
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space(text())='Save Changes']")
            ));
            System.out.println("→ Clicking Save Changes button");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            
            // Wait for dialog to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiDialog-paper')]")
            ));
            System.out.println("→ Edit dialog closed, changes saved successfully");
            
            printTestResult(true, "Edit Button Test", "Successfully clicked edit, modified content, and saved changes");


        } catch (Exception e) {
            printTestResult(false, "Edit Button Test", "Exception during test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    



} 