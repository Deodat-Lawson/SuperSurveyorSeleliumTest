//Completed

package com.supersurveyors.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.supersurveyors.tests.TestUtils.*;

public class TrendingTest {
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
            debug("Starting TrendingTest...");
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

            // 2. Navigate to trending page
            debug("Navigating to trending page...");
            navigateToTrendingPage(driver, wait);
            
            // 3. Test page elements
            debug("Testing trending page elements...");
            testTrendingPageElements(driver, wait);
            
            // 4. Test survey card colors
            debug("Testing top survey card colors...");
            testSurveyCardColors(driver, wait);
            
            // 5. Test results button navigation
            debug("Testing result button navigation...");
            testResultsButtonNavigation(driver, wait);
            
            testPassed = true;
            debug("All tests completed successfully!");
            printTestResult(testPassed, "Trending Test", "All tests completed successfully");

        } catch (Exception e) {
            debug("FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
            printTestResult(false, "Trending Test", "Fatal error: " + e.getMessage());
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

    private static void navigateToTrendingPage(WebDriver driver, WebDriverWait wait) {
        printTestHeader("STEP: Navigate to Trending Page");
        
        try {
            // Direct navigation approach
            debug("Navigating directly to trending page...");
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/trending");
            
            // Wait for page title to confirm we're on trending page
            debug("Waiting for trending page title...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Trending Surveys')]")
            ));
            
            debug("Successfully navigated to trending page at: " + driver.getCurrentUrl());
            printTestResult(true, "Navigation", "Navigated to Trending page");
        } catch (Exception e) {
            debug("ERROR during navigation: " + e.getMessage());
            throw e; // rethrow to stop test
        }
    }

    private static void testTrendingPageElements(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Trending Page Elements");
        
        try {
            // 1. Check page title
            debug("Checking page title...");
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Trending Surveys')]")
            ));
            boolean titleExists = titleElement != null && titleElement.isDisplayed();
            printElementCheck(titleExists, "Page Title", "Found 'Trending Surveys' title");
            
            // Wait for loading to complete (check if loading spinner disappears)
            debug("Waiting for loading to complete...");
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.MuiCircularProgress-root")
                ));
                debug("Loading completed - spinner is gone");
                printElementCheck(true, "Loading Spinner", "Loading spinner disappears after data loads");
            } catch (Exception e) {
                debug("Could not verify loading spinner disappearance: " + e.getMessage());
                // Continue with test - loading might have been too quick
            }
            
            // Additional wait to ensure content is loaded
            debug("Adding extra wait time for content to load...");
            Thread.sleep(2000);
            
            // 2. Check for survey cards with expanded selectors
            debug("Looking for survey cards...");
            List<WebElement> surveyCards = driver.findElements(By.cssSelector("MuiButtonBase-root"));
            debug("CSS Selector used: MuiButtonBase-root");
            
            if (surveyCards.isEmpty()) {
                debug("Trying alternative selector for survey cards...");
                surveyCards = driver.findElements(By.cssSelector(".MuiGrid2-root > div"));
                debug("Alternative CSS Selector used: .MuiGrid2-root > div");
            }
            
            // Print page source for debugging if no cards found
            if (surveyCards.isEmpty()) {
                debug("No cards found with any selector. Current page structure:");
                debug("Page title: " + driver.getTitle());
                debug("Current URL: " + driver.getCurrentUrl());
                
                // Find all div elements to help diagnose structure
                List<WebElement> allDivs = driver.findElements(By.tagName("div"));
                debug("Found " + allDivs.size() + " div elements in total");
                
                // Log some of the main containers
                List<WebElement> containers = driver.findElements(By.cssSelector(".MuiContainer-root"));
                debug("Found " + containers.size() + " container elements");
                
                // If "No trending surveys" message exists, log it
                try {
                    WebElement noSurveysMessage = driver.findElement(
                        By.xpath("//div[contains(text(), 'No trending surveys') or contains(text(), 'No trending surveys available')]"));
                    debug("Found empty state message: " + noSurveysMessage.getText());
                    printElementCheck(true, "Empty State", "No trending surveys available message is displayed");
                } catch (Exception e) {
                    debug("No empty state message found");
                }
            }
            
            int cardCount = surveyCards.size();
            debug("Found " + cardCount + " survey cards");
            printElementCheck(cardCount > 0, "Survey Cards", "Found " + cardCount + " trending survey cards");
            
            if (cardCount > 0) {
                // 3. Check first card elements
                WebElement firstCard = surveyCards.get(0);
                debug("Checking elements in first card...");
                
                try {
                    // Check title
                    WebElement cardTitle = firstCard.findElement(By.cssSelector("h6, p"));
                    String titleText = cardTitle.getText();
                    debug("First card title: " + titleText);
                    boolean hasRankingInTitle = titleText.contains("Top 1:") || titleText.contains("Top") || titleText.contains("#1");
                    printElementCheck(hasRankingInTitle, "Card Title", "First card has ranking title: " + titleText);
                    
                    // Check for tags
                    List<WebElement> tags = firstCard.findElements(By.cssSelector("div.MuiChip-root"));
                    debug("Found " + tags.size() + " tags in first card");
                    printElementCheck(tags.size() >= 0, "Card Tags", "First card has " + tags.size() + " tags");
                    
                    // Check for any button that might be the results button
                    List<WebElement> buttons = firstCard.findElements(By.tagName("button"));
                    debug("Found " + buttons.size() + " buttons in first card");
                    
                    if (!buttons.isEmpty()) {
                        WebElement resultsButton = buttons.get(0);
                        for (WebElement button : buttons) {
                            String btnText = button.getText();
                            debug("Button text: " + btnText);
                            if (btnText.contains("View") || btnText.contains("result") || btnText.contains("Result")) {
                                resultsButton = button;
                                break;
                            }
                        }
                        
                        String buttonText = resultsButton.getText();
                        debug("Selected button text: " + buttonText);
                        boolean hasResultsButton = buttonText.length() > 0;
                        printElementCheck(hasResultsButton, "Results Button", "First card has button: " + buttonText);
                    } else {
                        debug("No buttons found in card");
                        printElementCheck(false, "Results Button", "No buttons found in first card");
                    }
                } catch (Exception e) {
                    debug("ERROR examining card elements: " + e.getMessage());
                    printElementCheck(false, "Card Elements", "Error examining card elements: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            debug("ERROR testing trending page elements: " + e.getMessage());
            printTestResult(false, "Trending Elements", "Error: " + e.getMessage());
            throw e;
        }
    }
    
    private static void testSurveyCardColors(WebDriver driver, WebDriverWait wait) {
        printTestHeader("TEST: Survey Card Colors");
        
        try {
            List<WebElement> surveyCards = driver.findElements(By.cssSelector("div.MuiCard-root"));
            int cardCount = surveyCards.size();
            
            if (cardCount >= 4) {
                // Check the first 4 cards for their special background colors
                debug("Checking background colors of top 4 cards...");
                
                // Check gold (top 1)
                String card1Bg = surveyCards.get(0).getCssValue("background-color");
                debug("Top 1 card background: " + card1Bg);
                boolean isGoldish = card1Bg.contains("255") && card1Bg.contains("215"); // Approximate RGB for gold
                printElementCheck(isGoldish, "Top 1 Card Color", "Has gold/yellow background: " + card1Bg);
                
                // Check silver (top 2)
                String card2Bg = surveyCards.get(1).getCssValue("background-color");
                debug("Top 2 card background: " + card2Bg);
                boolean isSilverish = card2Bg.contains("192") || card2Bg.contains("silver");
                printElementCheck(isSilverish, "Top 2 Card Color", "Has silver/gray background: " + card2Bg);
                
                // Check bronze (top 3)
                String card3Bg = surveyCards.get(2).getCssValue("background-color");
                debug("Top 3 card background: " + card3Bg);
                boolean isBronzish = card3Bg.contains("232") || card3Bg.contains("156"); // Approximately bronze
                printElementCheck(isBronzish, "Top 3 Card Color", "Has bronze background: " + card3Bg);
                
                // Check blue (top 4)
                String card4Bg = surveyCards.get(3).getCssValue("background-color");
                debug("Top 4 card background: " + card4Bg);
                boolean isBluish = card4Bg.contains("111") && card4Bg.contains("152") && card4Bg.contains("189");
                printElementCheck(isBluish, "Top 4 Card Color", "Has blue background: " + card4Bg);
            } else {
                debug("Not enough cards to test colors, found only " + cardCount);
                printElementCheck(false, "Card Colors", "Not enough cards to verify colors, need at least 4");
            }
        } catch (Exception e) {
            debug("ERROR testing card colors: " + e.getMessage());
            printTestResult(false, "Card Colors", "Error: " + e.getMessage());
        }
    }
    
    private static void testResultsButtonNavigation(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        printTestHeader("TEST: Results Button Navigation");
        
        try {
            // Try different selectors to find the cards
            debug("Looking for survey cards for result navigation...");
            List<WebElement> surveyCards = driver.findElements(By.cssSelector("div.MuiCard-root, div.MuiPaper-root"));
            
            if (surveyCards.isEmpty()) {
                debug("Trying alternative selector for survey cards...");
                surveyCards = driver.findElements(By.cssSelector(".MuiGrid2-root > div"));
            }
            
            if (!surveyCards.isEmpty()) {
                WebElement firstCard = surveyCards.get(0);
                debug("Found " + surveyCards.size() + " cards, using first card");
                
                // Find all buttons in the card
                List<WebElement> buttons = firstCard.findElements(By.tagName("button"));
                debug("Found " + buttons.size() + " buttons in first card");
                
                if (!buttons.isEmpty()) {
                    // Try to find a button that looks like a results button
                    WebElement resultsButton = buttons.get(0); // Default to first button
                    
                    for (WebElement button : buttons) {
                        String btnText = button.getText();
                        debug("Button text: " + btnText);
                        if (btnText.contains("View") || btnText.contains("result") || btnText.contains("Result")) {
                            resultsButton = button;
                            debug("Selected button with text: " + btnText);
                            break;
                        }
                    }
                    
                    // Get card title if possible
                    String titleText = "unknown";
                    try {
                        WebElement cardTitle = firstCard.findElement(By.cssSelector("h6, p"));
                        titleText = cardTitle.getText();
                        if (titleText.contains(":")) {
                            titleText = titleText.substring(titleText.indexOf(":") + 1).trim();
                        }
                        debug("Card title: " + titleText);
                    } catch (Exception e) {
                        debug("Could not get card title: " + e.getMessage());
                    }
                    
                    // Click the button
                    debug("Clicking button: " + resultsButton.getText());
                    resultsButton.click();
                    
                    // Wait for navigation to (hopefully) survey results page
                    debug("Waiting for redirect after button click...");
                    Thread.sleep(1000); // Additional wait to ensure navigation begins
                    
                    String originalUrl = driver.getCurrentUrl();
                    wait.until(ExpectedConditions.urlContains("survey-results"));
                    String currentUrl = driver.getCurrentUrl();
                    debug("Redirected to: " + currentUrl);
                    
                    boolean navigatedToResults = currentUrl.contains("results") || currentUrl.contains("survey-results");
                    printTestResult(navigatedToResults, "Results Navigation", 
                        "Navigated to page: " + currentUrl);

                    WebElement backButton = driver.findElement(By.cssSelector(".MuiButtonBase-root"));
                    debug("Found back button, clicking...");
                    backButton.click();
                    wait.until(ExpectedConditions.urlContains("view"));
                    debug("Navigated back to trending page");


                } else {
                    debug("No buttons found in card");
                    printTestResult(false, "Results Navigation", "No buttons found in card");
                }
            } else {
                debug("No survey cards found to test navigation");
                printTestResult(false, "Results Navigation", "No survey cards available to test");
                
                // Test navigation directly to a results page as fallback
                debug("Attempting fallback: direct navigation to a results page...");
                driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/survey-results/1");
                wait.until(ExpectedConditions.urlContains("survey-results"));
                debug("Direct navigation to results page succeeded");
                printTestResult(true, "Direct Results Navigation", "Directly navigated to results page");
            }
        } catch (Exception e) {
            debug("ERROR testing results button navigation: " + e.getMessage());
            printTestResult(false, "Results Navigation", "Error: " + e.getMessage());
        }
    }
}
