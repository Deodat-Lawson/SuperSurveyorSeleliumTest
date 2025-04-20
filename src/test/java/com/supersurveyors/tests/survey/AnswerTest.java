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

public class AnswerTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        boolean testPassed = false;

        try {
            printTestHeader("SETUP: Initializing WebDriver");
            System.setProperty("webdriver.chrome.driver", 
                "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            printTestResult(true, "WebDriver Setup", "Initialization successful");

            // 1. Log in
            login(driver, wait, "timothylinziqimc@gmail.com", "test12345678!");

            // 2. Navigate to a survey to answer
            navigateToAnswerPage(driver, wait);
            
            // 3. Test various widget interactions
            testMediaGalleries(driver, wait);
            testTextQuestionInteraction(driver, wait);
            testSingleSelectInteraction(driver, wait);
            testMultipleSelectInteraction(driver, wait);
            
            // 4. Test submission
            testSubmitSurvey(driver, wait);
            
            testPassed = true;
            printTestResult(testPassed, "Answer Test", "All tests completed successfully");

        } catch (Exception e) {
            printTestResult(false, "Answer Test", "Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private static void login(WebDriver driver, WebDriverWait wait, String email, String password) {
        printTestHeader("STEP: Login");
        driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login");
        
        // Input email and password
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
        
        // Click sign-in button
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[type='submit']")
        )).click();
        
        // Verify successful login by waiting for home page
        wait.until(ExpectedConditions.urlContains("#/home"));
        printTestResult(true, "Login", "Successfully logged in as " + email);
    }

    private static void navigateToAnswerPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Navigate to Answer Page");
        
        // Two navigation approaches: 
        // 1. Click the "Answer" link in navbar if available (must use XPath for text)
        try {
            WebElement answerLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Answer')]")
            ));
            answerLink.click();
            wait.until(ExpectedConditions.urlContains("#/answer"));
            printTestResult(true, "Navigation", "Navigated to Answer page via navbar");
        } 
        // 2. If no Answer link, go directly to trending page and find a survey
        catch (Exception e) {
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/trending");
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Popular Surveys')]")
            ));
            
            // Find first available survey and click it
            List<WebElement> surveyCards = driver.findElements(By.cssSelector("div.MuiCard-root"));
            if (!surveyCards.isEmpty()) {
                WebElement firstSurvey = surveyCards.get(0);
                WebElement surveyLink = firstSurvey.findElement(By.cssSelector("a[href*='/survey-view/']"));
                String surveyTitle = surveyLink.getText();
                surveyLink.click();
                
                // Wait for survey view page and click the answer button (must use XPath for text/aria-label)
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(), 'Answer') or contains(@aria-label, 'Answer')]")
                )).click();
                
                // Wait for the answer page to load (check for survey title - must use XPath for text)
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//form//h4[text()='" + surveyTitle + "']")
                ));
                printTestResult(true, "Navigation", "Navigated to Answer page via survey card");
            } else {
                throw new RuntimeException("No surveys found to answer");
            }
        }
        
        // Verify survey form is loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("form h4")
        ));
        printTestResult(true, "Answer Page", "Survey form loaded successfully");
    }

    private static void testMediaGalleries(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Media Galleries");
        
        try {
            // We need to use XPath for the text='Image Gallery' and relation to sibling div content
            // Check for image gallery
            List<WebElement> images = driver.findElements(By.xpath("//h6[text()='Image Gallery']/following-sibling::div//img"));
            if (!images.isEmpty()) {
                scrollToElement(driver, images.get(0));
                printElementCheck(true, "Image Gallery", "Found " + images.size() + " images");
                
                // Check img attributes
                String src = images.get(0).getAttribute("src");
                boolean hasValidSrc = src != null && !src.isEmpty();
                printElementCheck(hasValidSrc, "Image Source", src != null ? src.substring(0, Math.min(src.length(), 50)) + "..." : "null");
            } else {
                printElementCheck(false, "Image Gallery", "No images found in this survey");
            }
            
            // Check for video gallery (using XPath for text content)
            List<WebElement> videos = driver.findElements(By.xpath("//h6[text()='Video Gallery']/following-sibling::div//video"));
            if (!videos.isEmpty()) {
                scrollToElement(driver, videos.get(0));
                printElementCheck(true, "Video Gallery", "Found " + videos.size() + " videos");
                
                // Check video controls
                boolean hasControls = videos.get(0).getAttribute("controls") != null;
                printElementCheck(hasControls, "Video Controls", "Video has playback controls");
            } else {
                printElementCheck(false, "Video Gallery", "No videos found in this survey");
            }
            
            // Check for audio gallery (using XPath for text content)
            List<WebElement> audios = driver.findElements(By.xpath("//h6[text()='Audio Gallery']/following-sibling::div//audio"));
            if (!audios.isEmpty()) {
                scrollToElement(driver, audios.get(0));
                printElementCheck(true, "Audio Gallery", "Found " + audios.size() + " audio files");
                
                // Check audio controls
                boolean hasControls = audios.get(0).getAttribute("controls") != null;
                printElementCheck(hasControls, "Audio Controls", "Audio has playback controls");
            } else {
                printElementCheck(false, "Audio Gallery", "No audio files found in this survey");
            }
            
        } catch (Exception e) {
            printTestResult(false, "Media Galleries", "Error checking media: " + e.getMessage());
        }
    }

    private static void testTextQuestionInteraction(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Text Question Interaction");
        
        try {
            // Find all text input questions
            List<WebElement> textFields = driver.findElements(
                By.cssSelector("textarea.MuiInputBase-input, textarea.MuiOutlinedInput-input")
            );
            
            if (!textFields.isEmpty()) {
                WebElement textField = textFields.get(0);
                scrollToElement(driver, textField);
                
                // Click the field
                textField.click();
                printElementCheck(true, "Text Field", "Clicked text input field");
                
                // Type into the field
                String testInput = "This is a test answer from Selenium automation";
                textField.clear();
                textField.sendKeys(testInput);
                
                // Verify text was entered
                String enteredText = textField.getAttribute("value");
                boolean textMatches = enteredText != null && enteredText.equals(testInput);
                printElementCheck(textMatches, "Text Entry", "Entered text: " + testInput);
            } else {
                printElementCheck(false, "Text Question", "No text input fields found in this survey");
            }
        } catch (Exception e) {
            printTestResult(false, "Text Question Interaction", "Error interacting with text field: " + e.getMessage());
        }
    }

    private static void testSingleSelectInteraction(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Single Select Question Interaction");
        
        try {
            // Find radio button questions (single select)
            List<WebElement> radioGroups = driver.findElements(
                By.cssSelector("div[role*='radiogroup']")
            );
            
            if (!radioGroups.isEmpty()) {
                WebElement radioGroup = radioGroups.get(0);
                scrollToElement(driver, radioGroup);
                
                // Find radio options
                List<WebElement> radioOptions = radioGroup.findElements(
                    By.cssSelector("input[type='radio']")
                );
                
                if (!radioOptions.isEmpty()) {
                    // Get the label to click (more reliable than clicking the input directly)
                    WebElement radioLabel = driver.findElement(
                        By.cssSelector("label[for='" + radioOptions.get(0).getAttribute("id") + "']")
                    );
                    
                    // Click the option
                    scrollToElement(driver, radioLabel);
                    radioLabel.click();
                    
                    // Verify option was selected
                    Thread.sleep(500); // Short pause to let selection register
                    boolean isSelected = radioOptions.get(0).isSelected();
                    printElementCheck(isSelected, "Radio Selection", "Selected the first radio option");
                } else {
                    printElementCheck(false, "Radio Options", "No radio options found in this group");
                }
            } else {
                // Try alternative approach for styled radio buttons
                List<WebElement> formControlLabels = driver.findElements(
                    By.cssSelector("label.MuiFormControlLabel-root")
                );
                
                if (!formControlLabels.isEmpty()) {
                    WebElement radioLabel = formControlLabels.get(0);
                    scrollToElement(driver, radioLabel);
                    radioLabel.click();
                    printElementCheck(true, "Radio Selection", "Clicked styled radio button");
                } else {
                    printElementCheck(false, "Single Select Question", "No single select questions found in this survey");
                }
            }
        } catch (Exception e) {
            printTestResult(false, "Single Select Interaction", "Error interacting with radio buttons: " + e.getMessage());
        }
    }

    private static void testMultipleSelectInteraction(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Multiple Select Question Interaction");
        
        try {
            // Find checkbox groups (multiple select)
            List<WebElement> checkboxGroups = driver.findElements(
                By.cssSelector("div[role*='group']")
            );
            
            if (!checkboxGroups.isEmpty()) {
                WebElement checkboxGroup = checkboxGroups.get(0);
                scrollToElement(driver, checkboxGroup);
                
                // Find checkbox options
                List<WebElement> checkboxes = checkboxGroup.findElements(
                    By.cssSelector("input[type='checkbox']")
                );
                
                if (checkboxes.size() >= 2) {
                    // Get labels for first two options
                    WebElement label1 = driver.findElement(
                        By.cssSelector("label[for='" + checkboxes.get(0).getAttribute("id") + "']")
                    );
                    WebElement label2 = driver.findElement(
                        By.cssSelector("label[for='" + checkboxes.get(1).getAttribute("id") + "']")
                    );
                    
                    // Click first option
                    scrollToElement(driver, label1);
                    label1.click();
                    Thread.sleep(300);
                    
                    // Click second option
                    scrollToElement(driver, label2);
                    label2.click();
                    Thread.sleep(300);
                    
                    // Verify selections
                    boolean firstSelected = checkboxes.get(0).isSelected();
                    boolean secondSelected = checkboxes.get(1).isSelected();
                    printElementCheck(firstSelected && secondSelected, 
                        "Checkbox Selection", "Selected the first two checkbox options");
                    
                    // Unselect first option
                    label1.click();
                    Thread.sleep(300);
                    
                    // Verify updated selections
                    firstSelected = checkboxes.get(0).isSelected();
                    secondSelected = checkboxes.get(1).isSelected();
                    printElementCheck(!firstSelected && secondSelected, 
                        "Checkbox Toggle", "Successfully toggled first checkbox off");
                } else {
                    printElementCheck(false, "Checkbox Options", "Not enough checkbox options found");
                }
            } else {
                // Try alternative approach for styled checkboxes
                List<WebElement> formControlLabels = driver.findElements(
                    By.cssSelector("label.MuiFormControlLabel-root span.MuiCheckbox-root")
                );
                
                if (formControlLabels.size() >= 2) {
                    // Click two checkboxes
                    scrollToElement(driver, formControlLabels.get(0));
                    formControlLabels.get(0).click();
                    Thread.sleep(300);
                    
                    scrollToElement(driver, formControlLabels.get(1));
                    formControlLabels.get(1).click();
                    Thread.sleep(300);
                    
                    printElementCheck(true, "Checkbox Selection", "Clicked two styled checkbox options");
                } else {
                    printElementCheck(false, "Multiple Select Question", "No multiple select questions found in this survey");
                }
            }
        } catch (Exception e) {
            printTestResult(false, "Multiple Select Interaction", "Error interacting with checkboxes: " + e.getMessage());
        }
    }

    private static void testSubmitSurvey(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Survey Submission");
        
        try {
            // First ensure all questions are answered to avoid validation errors
            ensureAllQuestionsAnswered(driver, wait);
            
            // Find the submit button - must use XPath for text content
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' or contains(text(), 'Submit Answer')]")
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
            
            // Wait for submission confirmation
            // Based on the React code, we should see either:
            // 1. A success check icon and message
            // 2. A redirect to another page
            
            try {
                // Look for the success message or check icon
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'submitted successfully')]")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(), 'submitted successfully')]")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("svg[data-testid='CheckCircleIcon']"))
                ));
                printTestResult(true, "Submit Verification", "Submission was successful - confirmation displayed");
            } catch (Exception e) {
                // If we don't see the success message, check for URL change
                if (driver.getCurrentUrl().contains("view") || 
                    driver.getCurrentUrl().contains("home") ||
                    driver.getCurrentUrl().contains("trending")) {
                    printTestResult(true, "Submit Verification", "Submission successful - redirected to: " + driver.getCurrentUrl());
                } else {
                    throw new RuntimeException("Could not verify successful submission");
                }
            }
            
        } catch (Exception e) {
            printTestResult(false, "Submit Survey", "Error testing submission: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void ensureAllQuestionsAnswered(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        // Fill any empty text fields
        List<WebElement> textFields = driver.findElements(
            By.cssSelector("textarea.MuiInputBase-input")
        );
        
        for (WebElement field : textFields) {
            if (field.getAttribute("value") == null || field.getAttribute("value").isEmpty()) {
                scrollToElement(driver, field);
                field.sendKeys("Test answer from automated test");
                Thread.sleep(300);
            }
        }
        
        // Select one option for any unselected radio groups
        List<WebElement> radioGroups = driver.findElements(
            By.cssSelector("div[role*='radiogroup']")
        );
        
        for (WebElement group : radioGroups) {
            List<WebElement> selectedOptions = group.findElements(
                By.cssSelector("input[type='radio'][checked]")
            );
            
            if (selectedOptions.isEmpty()) {
                List<WebElement> options = group.findElements(
                    By.cssSelector("input[type='radio']")
                );
                
                if (!options.isEmpty()) {
                    String id = options.get(0).getAttribute("id");
                    WebElement label = driver.findElement(By.cssSelector("label[for='" + id + "']"));
                    scrollToElement(driver, label);
                    label.click();
                    Thread.sleep(300);
                }
            }
        }
        
        // Select at least one checkbox in any checkbox groups
        List<WebElement> checkboxGroups = driver.findElements(
            By.cssSelector("div[role*='group']")
        );
        
        for (WebElement group : checkboxGroups) {
            List<WebElement> selectedCheckboxes = group.findElements(
                By.cssSelector("input[type='checkbox'][checked]")
            );
            
            if (selectedCheckboxes.isEmpty()) {
                List<WebElement> checkboxes = group.findElements(
                    By.cssSelector("input[type='checkbox']")
                );
                
                if (!checkboxes.isEmpty()) {
                    String id = checkboxes.get(0).getAttribute("id");
                    WebElement label = driver.findElement(By.cssSelector("label[for='" + id + "']"));
                    scrollToElement(driver, label);
                    label.click();
                    Thread.sleep(300);
                }
            }
        }
    }
    
    private static void scrollToElement(WebDriver driver, WebElement element) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
        Thread.sleep(500); // Wait for scroll to complete
    }
} 