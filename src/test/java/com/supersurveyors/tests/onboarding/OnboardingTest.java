package com.supersurveyors.tests.onboarding;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class OnboardingTest {
  public static void main(String[] args) {
    // Set the path to the ChromeDriver executable (update the path accordingly)
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Initialize the ChromeDriver.
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    
    try {
      // First sign up, then navigate to onboarding
      String signupUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/signup";
      driver.get(signupUrl);
      System.out.println("\n==== TEST: Sign Up to Reach Onboarding ====");
      System.out.println("→ Navigated to sign up page: " + signupUrl);

      // Sign Up to get to the onboarding page
      try {
        // Generate a random email to ensure it's always new
        String baseEmail = "test.user";
        String domain = "@example.com";
        Random random = new Random();
        String randomChars = String.valueOf(System.currentTimeMillis()) + 
                              String.valueOf(random.nextInt(10000));
        String randomEmail = baseEmail + "+" + randomChars + domain;
        
        // Fill out the form
        // 1. Display Name
        WebElement displayNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("displayName")));
        displayNameField.clear();
        displayNameField.sendKeys("Test User");
        System.out.println("→ Entered display name: Test User");
        
        // 2. Email Address with random component
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("email")));
        emailField.clear();
        emailField.sendKeys(randomEmail);
        System.out.println("→ Entered random email: " + randomEmail);
        
        // 3. Password
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("password")));
        passwordField.clear();
        String password = "SecurePassword123!";
        passwordField.sendKeys(password);
        System.out.println("→ Entered password");
        
        // Submit the form
        WebElement createAccountButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Create Account') and @type='submit']")));
        System.out.println("→ Clicking 'Create Account' button...");
        
        // Scroll button into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", createAccountButton);
        Thread.sleep(500);
        
        // Click the button
        createAccountButton.click();
        
        // Check for successful submission - should redirect to onboarding page
        try {
          // Wait for redirect to onboarding page
          wait.until(ExpectedConditions.urlContains("/onboarding"));
          String onboardingUrl = driver.getCurrentUrl();
          
          if (onboardingUrl.contains("/onboarding")) {
            System.out.println("✓ Successfully created account and redirected to: " + onboardingUrl);
            System.out.println("\n✅ SUCCESS: Reached onboarding page after signup");
          } else {
            System.out.println("❌ Not redirected to onboarding page. Current URL: " + onboardingUrl);
            
            // If we got redirected to home instead, manually navigate to onboarding
            if (onboardingUrl.contains("/home")) {
              System.out.println("→ Was redirected to home page, manually navigating to onboarding");
              driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/onboarding");
            }
          }
        } catch (Exception ex) {
          // If we're still on the signup page, check for error messages
          if (driver.getCurrentUrl().contains("/signup")) {
            try {
              WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                      By.xpath("//div[@role='alert']")));
              System.out.println("❌ Form submission error: " + errorMessage.getText());
            } catch (Exception e) {
              System.out.println("❌ Form submission failed but no error message was displayed");
            }
            System.out.println("\n❌ FAILURE: Account creation failed");
            
            // Manually navigate to onboarding page to continue tests
            System.out.println("→ Manually navigating to onboarding page to continue tests");
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/onboarding");
          }
        }
        
      } catch (Exception e) {
        System.out.println("\n❌ FAILURE: Sign up test failed: " + e.getMessage());
        e.printStackTrace();
        
        // Manually navigate to onboarding page to continue tests
        System.out.println("→ Manually navigating to onboarding page to continue tests");
        driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/onboarding");
      }

      System.out.println("\n==== TEST: Onboarding Page Elements Verification ====");
      
      // Wait for the page to load
      try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[contains(text(), 'Select Your Interests')]")));
      } catch (Exception e) {
        System.out.println("❌ Onboarding page did not load as expected. Current URL: " + driver.getCurrentUrl());
      }

      // --- Page Title Verification ---
      String expectedTitle = "SuperSurveyors";
      String actualTitle = driver.getTitle();
      if (expectedTitle.equals(actualTitle)) {
        System.out.println("✓ Page title verification passed: " + actualTitle);
      } else {
        System.out.println("❌ Page title verification failed: expected '"
                + expectedTitle + "', but got '" + actualTitle + "'");
      }

      System.out.println("\n-- Checking header elements --");
      
      // --- Header Verification ---
      // Verify that the header displays the SuperSurveyors logo/title.
      try {
        WebElement headerTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
        ));
        System.out.println("✓ Header with 'SuperSurveyors' logo is displayed");
      } catch (Exception e) {
        System.out.println("❌ Header verification failed: " + e.getMessage());
      }

      // --- Navigation Menu Buttons Verification ---
      System.out.println("\n-- Checking navigation menu buttons --");
      
      String[] navButtons = {"View", "Trending", "Create", "Answer"};
      
      for (String buttonText : navButtons) {
        try {
          WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("//a[contains(text(), '" + buttonText + "')]")
          ));
          System.out.println("✓ '" + buttonText + "' navigation button is displayed");
        } catch (Exception e) {
          System.out.println("❌ '" + buttonText + "' navigation button verification failed: " + e.getMessage());
        }
      }

      // --- Main Content Verification ---
      System.out.println("\n-- Checking main content elements --");
      
      // Verify the heading "Select Your Interests"
      try {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Select Your Interests')]")
        ));
        System.out.println("✓ 'Select Your Interests' heading is displayed");
      } catch (Exception e) {
        System.out.println("❌ Heading verification failed: " + e.getMessage());
      }



      // Verify the progress bar
      try {
        WebElement progressBar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@role='progressbar']")
        ));
        System.out.println("✓ Progress bar is displayed");
      } catch (Exception e) {
        System.out.println("❌ Progress bar verification failed: " + e.getMessage());
      }

      // Verify the "Required topics" indicator
      try {
        WebElement requiredTopics = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(), 'Selected Topics')]")
        ));
        System.out.println("✓ Required topics indicator is displayed");
      } catch (Exception e) {
        System.out.println("❌ Required topics indicator verification failed: " + e.getMessage());
      }

      // --- Interest Cards Verification ---
      System.out.println("\n-- Checking interest cards --");
      
      try {
        List<WebElement> interestCards = driver.findElements(By.xpath("//div[contains(@class, 'MuiCard-root')]"));
        System.out.println("✓ Found " + interestCards.size() + " interest topic cards");
        
        // Check a few specific cards
        String[] expectedTopics = {"arts", "cooking", "technology", "travel", "gaming"};
        for (String topic : expectedTopics) {
          WebElement topicCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("//p[contains(text(), '" + topic + "')]/ancestor::div[contains(@class, 'MuiCard-root')]")
          ));
          System.out.println("✓ '" + topic + "' topic card is displayed");
        }
      } catch (Exception e) {
        System.out.println("❌ Interest cards verification failed: " + e.getMessage());
      }

      // Verify the continue button (initially disabled)
      try {
        WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(text(), 'Select 5 more topics')]")
        ));
        boolean isDisabled = !continueButton.isEnabled();
        
        if (isDisabled) {
          System.out.println("✓ Continue button is correctly disabled initially");
        } else {
          System.out.println("❌ Continue button should be disabled initially but is enabled");
        }
      } catch (Exception e) {
        System.out.println("❌ Continue button verification failed: " + e.getMessage());
      }
      
      // ================================
      // Test: Toggle All Interest Topic Cards
      // ================================
      System.out.println("\n==== TEST: Toggle All Interest Topic Cards ====");
      
      try {
        
        // Wait for the page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[contains(text(), 'Select Your Interests')]")));
        
        // Find all topic cards
        List<WebElement> allCards = driver.findElements(By.xpath("//div[contains(@class, 'MuiCard-root')]"));
        int cardCount = allCards.size();
        System.out.println("→ Total number of interest topic cards: " + cardCount);
        
        // Array to store topic names
        String[] topicNames = new String[cardCount];
        
        // Step 1: Toggle ON all cards one by one
        System.out.println("\n-- Step 1: Toggle ON all topic cards --");
        
        for (int i = 0; i < cardCount; i++) {
          WebElement card = allCards.get(i);
          
          // Get the topic name
          WebElement topicText = card.findElement(By.xpath(".//p"));
          topicNames[i] = topicText.getText().trim();
          
          // Scroll to the card - ensure it's centered and wait for it to be fully visible
          Actions actions = new Actions(driver);
          actions.moveToElement(card).perform();
          
          // Wait for animations to complete and ensure element is clickable
          wait.until(ExpectedConditions.elementToBeClickable(card));
          Thread.sleep(700);
          
          try {
            // Try direct click first
            card.click();
          } catch (Exception clickException) {
            try {
              // If direct click fails, try JavaScript click
              System.out.println("→ Using JavaScript click fallback for topic #" + (i+1));
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            } catch (Exception jsException) {
              // If both methods fail, try with actions
              System.out.println("→ Using Actions click fallback for topic #" + (i+1));
              actions.moveToElement(card).click().perform();
            }
          }
          
          System.out.println("→ Selected topic #" + (i+1) + ": " + topicNames[i]);
          
          // Wait for progress counter to update
          Thread.sleep(200);
          
          // Verify progress counter updates
          try {
            WebElement progressCounter = driver.findElement(
                    By.xpath("//*[contains(text(), '" + (i+1) + " of 5')]"));
            System.out.println("✓ Progress counter updated to " + (i+1) + " of 5");
          } catch (Exception e) {
            // If counter doesn't update as expected (e.g., after 5 selections)
            System.out.println("ℹ️ Progress counter might have reached maximum");
          }
          
          // Check if the continue button is enabled after selecting 5 topics
          if (i == 4) {
            try {
              WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                      By.xpath("//button[contains(text(), 'Complete Selection')]")));
              System.out.println("✓ Continue button is correctly enabled after selecting 5 topics");
            } catch (Exception e) {
              System.out.println("❌ Continue button should be enabled after selecting 5 topics but is not");
            }
          }
        }
        
        // Step 2: Toggle OFF all cards in reverse order
        System.out.println("\n-- Step 2: Toggle OFF all topic cards --");
        
        for (int i = cardCount - 1; i >= 0; i--) {
          WebElement card = allCards.get(i);
          
          // Scroll to the card - ensure it's centered and wait for it to be fully visible
          Actions actions = new Actions(driver);
          actions.moveToElement(card).perform();
          
          // Wait for animations to complete and ensure element is clickable
          wait.until(ExpectedConditions.elementToBeClickable(card));
          Thread.sleep(700);
          
          try {
            // Try direct click first
            card.click();
          } catch (Exception clickException) {
            try {
              // If direct click fails, try JavaScript click
              System.out.println("→ Using JavaScript click fallback for topic #" + (i+1));
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            } catch (Exception jsException) {
              // If both methods fail, try with actions
              System.out.println("→ Using Actions click fallback for topic #" + (i+1));
              actions.moveToElement(card).click().perform();
            }
          }
          
          System.out.println("→ Unselected topic #" + (i+1) + ": " + topicNames[i]);
          
          // Wait for interface to update
          Thread.sleep(200);
          
          // If we're unselecting below 5 topics, verify the continue button disabled state
          if (i == 4) {
            try {
              WebElement disabledButton = driver.findElement(
                      By.xpath("//button[contains(text(), 'Select 1 more topic')]"));
              boolean isDisabled = !disabledButton.isEnabled();
              
              if (isDisabled) {
                System.out.println("✓ Continue button correctly disabled after dropping below 5 selections");
              } else {
                System.out.println("❌ Continue button should be disabled after dropping below 5 selections");
              }
            } catch (Exception e) {
              System.out.println("❌ Continue button state verification failed: " + e.getMessage());
            }
          }
          

        }
        
        // Step 3: Toggle ON specific cards to test individual selection
        System.out.println("\n-- Step 3: Toggle ON specific cards --");
        
        // Define a subset of cards to toggle (3 specific cards)
        String[] specificTopics = {"arts", "gaming", "technology"};
        
        for (String topic : specificTopics) {
          // Find the specific card
          WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//p[contains(text(), '" + topic + "')]/ancestor::div[contains(@class, 'MuiCard-root')]")));
          
          // Scroll to the card - ensure it's centered and wait for it to be fully visible
          Actions actions = new Actions(driver);
          actions.moveToElement(card).perform();
          
          // Wait for animations to complete and ensure element is clickable
          wait.until(ExpectedConditions.elementToBeClickable(card));
          Thread.sleep(700);
          
          try {
            // Try direct click first
            card.click();
          } catch (Exception clickException) {
            try {
              // If direct click fails, try JavaScript click
              System.out.println("→ Using JavaScript click fallback for topic #" + topic);
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            } catch (Exception jsException) {
              // If both methods fail, try with actions
              System.out.println("→ Using Actions click fallback for topic #" + topic);
              actions.moveToElement(card).click().perform();
            }
          }
          
          System.out.println("→ Selected specific topic: " + topic);
          Thread.sleep(200);
        }


        String[] additionalTopics = {"travel", "cooking"};
        
        for (String topic : additionalTopics) {
          // Find the specific card
          WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//p[contains(text(), '" + topic + "')]/ancestor::div[contains(@class, 'MuiCard-root')]")));
          
          // Scroll to the card - ensure it's centered and wait for it to be fully visible
          Actions actions = new Actions(driver);
          actions.moveToElement(card).perform();
          
          // Wait for animations to complete and ensure element is clickable
          wait.until(ExpectedConditions.elementToBeClickable(card));
          Thread.sleep(700);
          
          try {
            // Try direct click first
            card.click();
          } catch (Exception clickException) {
            try {
              // If direct click fails, try JavaScript click
              System.out.println("→ Using JavaScript click fallback for topic #" + topic);
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            } catch (Exception jsException) {
              // If both methods fail, try with actions
              System.out.println("→ Using Actions click fallback for topic #" + topic);
              actions.moveToElement(card).click().perform();
            }
          }
          
          System.out.println("→ Selected additional topic: " + topic);
          Thread.sleep(200);
        }
        
        // Verify the button is enabled with 5 selections
        try {
          WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//button[contains(text(), 'Complete Selection')]")));
          System.out.println("✓ Continue button is correctly enabled after selecting 5 specific topics");
          System.out.println("\n✅ SUCCESS: All interest topic cards toggle functionality works correctly");
        } catch (Exception e) {
          System.out.println("❌ Continue button should be enabled but is not: " + e.getMessage());
          System.out.println("\n❌ FAILURE: Interest topic cards toggle functionality has issues");
        }
        
      } catch (Exception e) {
        System.out.println("\n❌ FAILURE: Toggle interest cards test failed: " + e.getMessage());
        e.printStackTrace();
      }
      
      
      // ================================
      // Test 3: Continue Button Functionality
      // ================================
      System.out.println("\n==== TEST: Continue Button Functionality ====");
      
      try {
     
        
        // Find and click the continue button
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Complete Selection')]")
        ));
        
        System.out.println("→ Clicking 'Continue' button");
        
        // Scroll to the button to ensure it's visible
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", continueButton);
        Thread.sleep(500);
        
        // Click the button
        continueButton.click();
        
        // Wait for navigation
        wait.until(ExpectedConditions.urlContains("/profile"));
        
        // Verify navigation to home page
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("/home")) {
          System.out.println("✓ Successfully navigated to home page: " + currentUrl);
          System.out.println("\n✅ SUCCESS: Continue button functionality works correctly");
        } else {
          System.out.println("❌ Continue button navigation failed. Expected URL to contain '/home', but got: " + currentUrl);
          System.out.println("\n❌ FAILURE: Continue button functionality has issues");
        }
        
      } catch (Exception e) {
        System.out.println("\n❌ FAILURE: Continue button test failed: " + e.getMessage());
        e.printStackTrace();
      }
      
    } finally {
      // Close the browser.
      driver.quit();
      System.out.println("\n==== All Onboarding Page Tests Completed ====");
    }
  }
} 