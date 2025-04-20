//Missing widget tests: Upload, TagDelete



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
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBtn);
            Thread.sleep(300);
            addBtn.click();

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


            addOptionsToQuestion(driver, wait, new String[]{"VS Code", "IntelliJ IDEA", "Eclipse"});
            
        } catch (Exception e) {
            printTestResult(false, "Add Single Select Q", "Failed: " + e.getMessage());
            throw e;
        }
    }

    private static void addMultipleSelectQuestion(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Add Multiple Select Question (Q3)");
        try {
            // Use index 1 as per user's change, click 'Add Question'
            WebElement addBtn = findAddQuestionButton(driver, wait, 1); 
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBtn);
            Thread.sleep(300);
            addBtn.click();

            // Find the new question input field (assuming it's the next one)
            WebElement qInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[normalize-space(text())='Question']/following-sibling::*//input")
                )
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", qInput);
            Thread.sleep(300);
            qInput.clear();
            qInput.sendKeys("Which languages do you know?");
            printElementCheck(true, "Question 3 Text", "Entered Q3 text.");

            // Find all dropdown triggers
            List<WebElement> triggers = driver.findElements(
                By.cssSelector("div.MuiSelect-select")
            );
            System.out.println("DEBUG: Found " + triggers.size() + " dropdown triggers (MuiSelect-select) for Q3.");
            for (int i = 0; i < triggers.size(); i++) {
                System.out.println("DEBUG: Trigger[" + i + "] text='" + triggers.get(i).getText() + "'");
            }

            // Use the *second* trigger for Q3 (index 1)
            if (triggers.size() < 2) { 
                throw new RuntimeException("Expected at least 2 dropdown triggers for Q3, found " + triggers.size());
            }
            WebElement responseType = triggers.get(0); // Index 1 for the second dropdown (Q3)
            scrollAndClick(driver, responseType, "Q3 Response Type");

            // Click the 'Multiple Select' option
            WebElement multiOpt = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("li[role='option'][data-value='checkbox']") // data-value for multiple select
                )
            );
            multiOpt.click();
            wait.until(ExpectedConditions.textToBePresentInElement(responseType, "Multiple Select"));
            printElementCheck(true, "Question 3 Type", "Set to Multiple Select");

            // Add options
            addOptionsToQuestion(driver, wait, new String[]{"Java", "Python", "JavaScript"});

            //Add Question
            addBtn = findAddQuestionButton(driver, wait, 1); 
            scrollAndClick(driver, addBtn, "Add Question (for Q4)");
        } catch (Exception e) {
            printTestResult(false, "Add Multiple Select Q", "Failed: " + e.getMessage());
            throw e;
        }
    }
    
    private static void addOptionsToQuestion(WebDriver driver, WebDriverWait wait, String[] opts) throws InterruptedException {
        printTestHeader("--> Adding Options");
    
        for (String optionText : opts) {
            // 1) wait for the "Enter option" input to be visible
            WebElement optionInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[placeholder='Enter option']")
                )
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", optionInput);
            optionInput.clear();
            optionInput.sendKeys(optionText);
            printElementCheck(true, "Enter Option", "Entered '" + optionText + "'");
    
            // 2) wait for the "Add" button to be clickable, then click
            WebElement addBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space(text())='Add']")
                )
            );
            scrollAndClick(driver, addBtn, "Add option '" + optionText + "'");
    
            // 3) verify the new option shows up in the list
            By addedLocator = By.xpath("//span[normalize-space(text())='" + optionText + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(addedLocator));
            printElementCheck(true, "Verify Option", "Verified '" + optionText + "' added.");
        }
    }
    
    
    

    private static WebElement findAddQuestionButton(WebDriver driver, WebDriverWait wait, int idx) {
        String xp = String.format("(//button[contains(text(),'Add Question')])[%d]", idx);
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xp)));
    }

    private static void manageSurveyTags(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Manage Survey Tags");
        try {
            // 1) Locate the tags <div> (last MUI select)
            List<WebElement> selects = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.cssSelector("div.MuiSelect-select")));
            WebElement tagSelect = selects.get(selects.size() - 1);
    
            // — open via JS click —
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                tagSelect
            );
            printElementCheck(true, "Open Tags Dropdown", "Fired mousedown on Select");

            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('mousedown',{bubbles:true,cancelable:true}));",
                tagSelect
            );
    
            // 2) Choose up to 3 tags
            List<WebElement> options = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.cssSelector("ul[role='listbox'] li[role='option']")));
            int toAdd = Math.min(8, options.size());
    
            for (int i = 1; i < toAdd; i++) {
                WebElement opt = options.get(i);
                String tagName = opt.getText();
    
                // JS‑click option
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", opt);
                Thread.sleep(200);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
                printElementCheck(true, "Select Tag", tagName + " via JS");
    
                // verify chip appeared
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[text()='" + tagName + "']")));
                printElementCheck(true, "Verify Tag Added", tagName + " chip visible.");


                if(i != toAdd - 1) {
    
                // re-open for next pick
                selects = driver.findElements(By.cssSelector("div.MuiSelect-select"));
                tagSelect = selects.get(selects.size() - 1);

                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    tagSelect
                );
                printElementCheck(true, "Open Tags Dropdown", "Fired mousedown on Select");
    
                Thread.sleep(300);
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mousedown',{bubbles:true,cancelable:true}));",
                    tagSelect
                );

                options = wait.until(ExpectedConditions
                    .visibilityOfAllElementsLocatedBy(By.cssSelector("ul[role='listbox'] li[role='option']")));
                }
            }
    
            // 3) Remove each chip normally
        //     List<WebElement> chips = driver.findElements(By.cssSelector("MuiSvgIcon-root"));
        //     for (WebElement chip : chips) {
        //         String text = chip.findElement(By.cssSelector("span")).getText();
        //         WebElement del = chip.findElement(By.cssSelector("svg.MuiChip-deleteIcon"));
        //         ((JavascriptExecutor) driver).executeScript(
        //             "arguments[0].scrollIntoView({block:'center'});",
        //             del
        //         );
        //         printElementCheck(true, "Open Tags Dropdown", "Fired mousedown on Select");
    
        //         Thread.sleep(300);
        //         ((JavascriptExecutor) driver).executeScript(
        //             "arguments[0].dispatchEvent(new MouseEvent('mousedown',{bubbles:true,cancelable:true}));",
        //             del
        //         );

        //         printElementCheck(true, "Verify Tag Removed", text + " removed.");
        //     }
    
            printTestResult(true, "Manage Survey Tags", "Passed with JS clicks.");
        } catch (Exception e) {
            printTestResult(false, "Manage Survey Tags", "Failed: " + e.getMessage());
            throw e;
        }
    }
    

    private static void submitSurvey(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Submit Survey");
            
        // Find the submit button - must use XPath for text content
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[@type='submit' or contains(text(), 'Submit Survey')]")
        ));
        
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                submitButton
            );
            printElementCheck(true, "Open Tags Dropdown", "Fired mousedown on Select");

            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('mousedown',{bubbles:true,cancelable:true}));",
                submitButton
            );

        printElementCheck(true, "Submit Button", "Found the submit button");
        
        // Actually click the submit button
        printElementCheck(true, "Submit Action", "Clicking the submit button");
        submitButton.click();
        
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
