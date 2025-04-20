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

    // Add debugging flag to easily enable/disable detailed logging
    private static final boolean DEBUG_MODE = true;
    
    // Helper method for debug logging
    private static void debug(String message) {
        if (DEBUG_MODE) {
            System.out.println("DEBUG: " + message);
        }
    }

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        boolean testPassed = false;

        try {
            debug("Starting AnswerTest...");
            printTestHeader("SETUP: Initializing WebDriver");
            System.setProperty("webdriver.chrome.driver", 
                "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            debug("Chrome driver initialized with 15 second wait timeout");
            printTestResult(true, "WebDriver Setup", "Initialization successful");

            // 1. Log in
            debug("Attempting login...");
            login(driver, wait, "timothylinziqimc@gmail.com", "test12345678!");

            // 2. Navigate to a survey to answer
            debug("Navigating to answer page...");
            navigateToAnswerPage(driver, wait);
            
            // 3. Test survey switch button
            debug("Testing survey switch button...");
            testSwitchSurveyButton(driver, wait);
            
            // 4. Test various widget interactions
            debug("Testing media galleries...");
            testMediaGalleries(driver, wait);
            debug("Testing text question interaction...");
            testTextQuestionInteraction(driver, wait);
            debug("Testing single select interaction...");
            testSingleSelectInteraction(driver, wait);
            debug("Testing multiple select interaction...");
            testMultipleSelectInteraction(driver, wait);
            
            // 5. Test submission
            debug("Testing survey submission...");
            testSubmitSurvey(driver, wait);
            
            testPassed = true;
            debug("All tests completed successfully!");
            printTestResult(testPassed, "Answer Test", "All tests completed successfully");

        } catch (Exception e) {
            debug("FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
            printTestResult(false, "Answer Test", "Fatal error: " + e.getMessage());
        } finally {
            debug("Cleaning up and quitting driver...");
            if (driver != null) {
                driver.quit();
                debug("Driver quit successfully");
            }
        }
    }

    private static void login(WebDriver driver, WebDriverWait wait, String email, String password) {
        printTestHeader("STEP: Login");
        debug("Navigating to login page...");
        driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login");
        debug("Current URL: " + driver.getCurrentUrl());
        
        try {
            // Input email and password
            debug("Waiting for email field...");
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            debug("Email field found, entering: " + email);
            emailField.sendKeys(email);
            
            debug("Waiting for password field...");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            debug("Password field found, entering password");
            passwordField.sendKeys(password);
            
            // Click sign-in button
            debug("Looking for submit button...");
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit']")
            ));
            debug("Submit button found, clicking...");
            signInButton.click();
            
            // Verify successful login by waiting for home page
            debug("Waiting for redirect to home page...");
            wait.until(ExpectedConditions.urlContains("#/home"));
            debug("Successfully redirected to: " + driver.getCurrentUrl());
            printTestResult(true, "Login", "Successfully logged in as " + email);
        } catch (Exception e) {
            debug("ERROR during login: " + e.getMessage());
            throw e; // rethrow to stop test
        }
    }

    private static void navigateToAnswerPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("STEP: Navigate to Answer Page");
        
        // Two navigation approaches: 
        debug("Attempting to find 'Answer' link in navbar...");
        // 1. Click the "Answer" link in navbar if available (must use XPath for text)
        try {
            WebElement answerLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Answer')]")
            ));
            debug("Answer link found in navbar, clicking...");
            answerLink.click();
            debug("Waiting for URL to contain '#/answer'...");
            wait.until(ExpectedConditions.urlContains("#/answer"));
            debug("Successfully navigated to: " + driver.getCurrentUrl());
            printTestResult(true, "Navigation", "Navigated to Answer page via navbar");
        } 
        // 2. If no Answer link, go directly to trending page and find a survey
        catch (Exception e) {
            debug("Answer link not found in navbar: " + e.getMessage());
            debug("Trying alternative approach - navigating to trending page...");
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/trending");
            debug("Waiting for trending page to load...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Popular Surveys')]")
            ));
            debug("Trending page loaded at: " + driver.getCurrentUrl());
            
            // Find first available survey and click it
            debug("Looking for survey cards...");
            List<WebElement> surveyCards = driver.findElements(By.cssSelector("div.MuiCard-root"));
            debug("Found " + surveyCards.size() + " survey cards");
            
            if (!surveyCards.isEmpty()) {
                WebElement firstSurvey = surveyCards.get(0);
                WebElement surveyLink = firstSurvey.findElement(By.cssSelector("a[href*='/survey-view/']"));
                String surveyTitle = surveyLink.getText();
                debug("Selected survey: '" + surveyTitle + "', clicking...");
                surveyLink.click();
                
                // Wait for survey view page and click the answer button (must use XPath for text/aria-label)
                debug("Waiting for Answer button on survey view page...");
                WebElement answerButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(), 'Answer') or contains(@aria-label, 'Answer')]")
                ));
                debug("Answer button found, clicking...");
                answerButton.click();
                
                // Wait for the answer page to load (check for survey title - must use XPath for text)
                debug("Waiting for survey title to appear on answer page...");
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//form//h4[text()='" + surveyTitle + "']")
                ));
                debug("Survey title found on answer page");
                printTestResult(true, "Navigation", "Navigated to Answer page via survey card");
            } else {
                debug("ERROR: No survey cards found on trending page");
                throw new RuntimeException("No surveys found to answer");
            }
        }
        
        // Verify survey form is loaded
        debug("Verifying survey form is loaded...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("form h4")
        ));
        debug("Survey form loaded at: " + driver.getCurrentUrl());
        printTestResult(true, "Answer Page", "Survey form loaded successfully");
    }

    private static void testMediaGalleries(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Media Galleries");
        
        try {
            debug("Looking for image gallery...");
            // We need to use XPath for the text='Image Gallery' and relation to sibling div content
            // Check for image gallery
            List<WebElement> images = driver.findElements(By.xpath("//h6[text()='Image Gallery']/following-sibling::div//img"));
            debug("Found " + images.size() + " images in image gallery");
            
            if (!images.isEmpty()) {
                debug("Scrolling to first image...");
                scrollToElement(driver, images.get(0));
                printElementCheck(true, "Image Gallery", "Found " + images.size() + " images");
                
                // Check img attributes
                String src = images.get(0).getAttribute("src");
                debug("First image src: " + (src != null ? src.substring(0, Math.min(src.length(), 50)) + "..." : "null"));
                boolean hasValidSrc = src != null && !src.isEmpty();
                printElementCheck(hasValidSrc, "Image Source", src != null ? src.substring(0, Math.min(src.length(), 50)) + "..." : "null");
            } else {
                debug("No images found in this survey");
                printElementCheck(false, "Image Gallery", "No images found in this survey");
            }
            
            debug("Looking for video gallery...");
            // Check for video gallery (using XPath for text content)
            List<WebElement> videos = driver.findElements(By.xpath("//h6[text()='Video Gallery']/following-sibling::div//video"));
            debug("Found " + videos.size() + " videos in video gallery");
            
            if (!videos.isEmpty()) {
                debug("Scrolling to first video...");
                scrollToElement(driver, videos.get(0));
                printElementCheck(true, "Video Gallery", "Found " + videos.size() + " videos");
                
                // Check video controls
                boolean hasControls = videos.get(0).getAttribute("controls") != null;
                debug("Video controls present: " + hasControls);
                printElementCheck(hasControls, "Video Controls", "Video has playback controls");
            } else {
                debug("No videos found in this survey");
                printElementCheck(false, "Video Gallery", "No videos found in this survey");
            }
            
            debug("Looking for audio gallery...");
            // Check for audio gallery (using XPath for text content)
            List<WebElement> audios = driver.findElements(By.xpath("//h6[text()='Audio Gallery']/following-sibling::div//audio"));
            debug("Found " + audios.size() + " audio files in audio gallery");
            
            if (!audios.isEmpty()) {
                debug("Scrolling to first audio element...");
                scrollToElement(driver, audios.get(0));
                printElementCheck(true, "Audio Gallery", "Found " + audios.size() + " audio files");
                
                // Check audio controls
                boolean hasControls = audios.get(0).getAttribute("controls") != null;
                debug("Audio controls present: " + hasControls);
                printElementCheck(hasControls, "Audio Controls", "Audio has playback controls");
            } else {
                debug("No audio files found in this survey");
                printElementCheck(false, "Audio Gallery", "No audio files found in this survey");
            }
            
        } catch (Exception e) {
            debug("ERROR testing media galleries: " + e.getMessage());
            printTestResult(false, "Media Galleries", "Error checking media: " + e.getMessage());
        }
    }

    private static void testTextQuestionInteraction(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Text Question Interaction");
        
        try {
            // Find all text input questions - both textareas and regular text inputs
            List<WebElement> textFields = driver.findElements(
                By.cssSelector("textarea.MuiInputBase-input, textarea.MuiOutlinedInput-input, input[type='text'].form-control")
            );
            debug("Found " + textFields.size() + " text input fields");
            
            if (!textFields.isEmpty()) {
                WebElement textField = textFields.get(0);
                scrollToElement(driver, textField);
                
                // Click the field
                textField.click();
                debug("Clicked text field: " + textField.getTagName() + ", placeholder: " + textField.getAttribute("placeholder"));
                printElementCheck(true, "Text Field", "Clicked text input field");
                
                // Type into the field
                String testInput = "This is a test answer from Selenium automation";
                textField.clear();
                textField.sendKeys(testInput);
                debug("Entered text: " + testInput);
                
                // Verify text was entered
                String enteredText = textField.getAttribute("value");
                debug("Actual value in field: " + enteredText);
                boolean textMatches = enteredText != null && enteredText.equals(testInput);
                printElementCheck(textMatches, "Text Entry", "Entered text: " + testInput);
            } else {
                printElementCheck(false, "Text Question", "No text input fields found in this survey");
            }
        } catch (Exception e) {
            debug("ERROR in testTextQuestionInteraction: " + e.getMessage());
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
            debug("Ensuring all questions are answered...");
            ensureAllQuestionsAnswered(driver, wait);
            
            // Find the submit button - must use XPath for text content
            debug("Looking for submit button...");
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' or contains(text(), 'Submit')]")
            ));
            debug("Submit button found, scrolling into view...");
            
            // Scroll to make sure submit button is in view
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                submitButton
            );
            debug("Button text: '" + submitButton.getText() + "'");
            debug("Button enabled: " + submitButton.isEnabled());
            debug("Button displayed: " + submitButton.isDisplayed());
            
            // Sometimes buttons need a mousedown event before clicking
            debug("Firing mousedown event on submit button...");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('mousedown',{bubbles:true,cancelable:true}));",
                submitButton
            );
            Thread.sleep(300);

            debug("About to click submit button...");
            submitButton.click();
            debug("Submit button clicked!");
            
            // Wait for submission confirmation
            debug("Waiting for submission confirmation...");
            
            try {
                debug("Looking for success indicators...");
                // Look for the success message or check icon
                Boolean success = wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'submitted successfully')]")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(), 'submitted successfully')]")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("svg[data-testid='CheckCircleIcon']"))
                ));
                debug("Success indicator found: " + success);
                printTestResult(true, "Submit Verification", "Submission was successful - confirmation displayed");
            } catch (Exception e) {
                debug("Success indicators not found: " + e.getMessage());
                debug("Checking for URL change instead...");
                // If we don't see the success message, check for URL change
                String currentUrl = driver.getCurrentUrl();
                debug("Current URL after submission: " + currentUrl);
                
                if (currentUrl.contains("view") || 
                    currentUrl.contains("home") ||
                    currentUrl.contains("trending")) {
                    debug("URL indicates successful submission via redirect");
                    printTestResult(true, "Submit Verification", "Submission successful - redirected to: " + currentUrl);
                } else {
                    debug("Could not verify successful submission - URL: " + currentUrl);
                    throw new RuntimeException("Could not verify successful submission");
                }
            }
            
        } catch (Exception e) {
            debug("ERROR testing submission: " + e.getMessage());
            e.printStackTrace();
            printTestResult(false, "Submit Survey", "Error testing submission: " + e.getMessage());
        }
    }
    
    private static void ensureAllQuestionsAnswered(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        debug("Starting to ensure all questions are answered...");
        // Fill any empty text fields (both textareas and regular text inputs)
        List<WebElement> textFields = driver.findElements(
            By.cssSelector("textarea.MuiInputBase-input, input[type='text'].form-control")
        );
        debug("Found " + textFields.size() + " text input fields to check");
        
        for (int i = 0; i < textFields.size(); i++) {
            WebElement field = textFields.get(i);
            String value = field.getAttribute("value");
            if (value == null || value.isEmpty()) {
                debug("Text field #" + i + " is empty, filling it... (id: " + field.getAttribute("id") + ", type: " + field.getTagName() + ")");
                scrollToElement(driver, field);
                field.sendKeys("Test answer from automated test");
                debug("Text entered into field #" + i);
                Thread.sleep(300);
            } else {
                debug("Text field #" + i + " already has value: '" + value + "'");
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
        debug("Scrolling element into view: " + element.getTagName());
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
        debug("Waiting after scroll...");
        Thread.sleep(500); // Wait for scroll to complete
        debug("Scroll complete");
    }

    private static void testSwitchSurveyButton(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Survey Switch Button");
        
        try {
            // Get the current survey title
            debug("Getting current survey title before switch...");
            WebElement surveyTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("form h4")
            ));
            String initialSurveyTitle = surveyTitleElement.getText();
            debug("Initial survey title: '" + initialSurveyTitle + "'");
            
            // Find the "NOT INTERESTED?" button using XPath for text content
            debug("Looking for 'NOT INTERESTED? ANSWER A DIFFERENT SURVEY' button...");
            WebElement switchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'NOT INTERESTED') and contains(text(), 'ANSWER A DIFFERENT SURVEY')]")
            ));
            debug("Switch button found: '" + switchButton.getText() + "'");
            
            // Click the button
            debug("Scrolling to and clicking the switch button...");
            scrollToElement(driver, switchButton);
            switchButton.click();
            debug("Switch button clicked, waiting 500ms...");
            
            // Wait as specified
            Thread.sleep(500);
            
            // Check that the survey title has changed
            debug("Getting new survey title after switch...");
            WebElement newSurveyTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("form h4")
            ));
            String newSurveyTitle = newSurveyTitleElement.getText();
            debug("New survey title: '" + newSurveyTitle + "'");
            
            // Verify the title has changed
            boolean titleChanged = !initialSurveyTitle.equals(newSurveyTitle);
            debug("Title changed: " + titleChanged + " (old: '" + initialSurveyTitle + "', new: '" + newSurveyTitle + "')");
            printTestResult(titleChanged, "Survey Switch", 
                "Successfully switched from '" + initialSurveyTitle + "' to '" + newSurveyTitle + "'");
        } catch (Exception e) {
            debug("ERROR testing survey switch button: " + e.getMessage());
            printTestResult(false, "Survey Switch", "Error: " + e.getMessage());
        }
    }
} 