//Completed

package com.supersurveyors.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import java.util.Random;

public class SignUpTest {
  public static void main(String[] args) {
    // Set the path to the ChromeDriver executable (update the path accordingly)
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Initialize the ChromeDriver.
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/signup"; 
    driver.get(url);
    System.out.println("\n==== TEST: Sign Up Page Elements Verification ====");
    System.out.println("→ Navigated to sign up page: " + url);

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

    // Verify that the "Back to Login" button is present.
    try {
      WebElement backToLoginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Back to Login')]")
      ));
      System.out.println("✓ 'Back to Login' button is displayed");
    } catch (Exception e) {
      System.out.println("❌ 'Back to Login' button verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking main content elements --");
    
    // --- Main Content Verification ---
    // Verify that the main heading "Create Account" is displayed.
    try {
      WebElement mainHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//h1[contains(text(),'Create Account')]")
      ));
      System.out.println("✓ Main heading 'Create Account' is displayed");
    } catch (Exception e) {
      System.out.println("❌ Main heading verification failed: " + e.getMessage());
    }

    // Verify that the tagline "Join SuperSurveyors today" is displayed.
    try {
      WebElement tagline = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//p[contains(text(),'Join SuperSurveyors today')]")
      ));
      System.out.println("✓ Tagline 'Join SuperSurveyors today' is displayed");
    } catch (Exception e) {
      System.out.println("❌ Tagline verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking form fields --");
    
    // --- Form Fields Verification ---
    // Check that the Display Name label and input field are present.
    try {
      WebElement displayNameLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]")
      ));
      WebElement displayNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("displayName")
      ));
      System.out.println("✓ Display Name field and label are displayed");
    } catch (Exception e) {
      System.out.println("❌ Display Name field verification failed: " + e.getMessage());
    }

    // Check that the Email Address label and input field are present.
    try {
      WebElement emailLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Email Address')]")
      ));
      WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("email")
      ));
      System.out.println("✓ Email Address field and label are displayed");
    } catch (Exception e) {
      System.out.println("❌ Email Address field verification failed: " + e.getMessage());
    }

    // Check that the Password label and input field are present.
    try {
      WebElement passwordLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Password')]")
      ));
      WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("password")
      ));
      System.out.println("✓ Password field and label are displayed");
    } catch (Exception e) {
      System.out.println("❌ Password field verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking buttons and navigation options --");
    
    // --- Button Verification ---
    // Verify that the primary "Create Account" button (of type submit) is displayed.
    try {
      WebElement createAccountButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Create Account') and @type='submit']")
      ));
      System.out.println("✓ 'Create Account' button is displayed");
    } catch (Exception e) {
      System.out.println("❌ 'Create Account' button verification failed: " + e.getMessage());
    }

    // --- Sign In Prompt Verification ---
    // Verify that the "Already have an account?" prompt and its "Sign in" button are present.
    try {
      WebElement signInPrompt = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(),\"Already have an account?\")]")
      ));
      WebElement signInButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Sign in')]")
      ));
      System.out.println("✓ 'Already have an account?' prompt and 'Sign in' button are displayed");
    } catch (Exception e) {
      System.out.println("❌ Sign in prompt verification failed: " + e.getMessage());
    }

    // --- Optional: Verify Alert Container ---
    // Optionally verify that the error alert container exists (even if hidden by default).
    try {
      WebElement errorAlert = driver.findElement(By.xpath(
              "//div[@role='alert' and contains(@class, 'MuiAlert-colorError')]"));
      System.out.println("✓ Error alert container is present in the DOM");
    } catch (Exception e) {
      System.out.println("❌ Error alert container verification failed: " + e.getMessage());
    }

    // ================================
    // Test 1: Password Visibility Toggle
    // ================================
    try {
      System.out.println("\n==== TEST: Password Visibility Toggle ====");
      
      // Navigate back to signup page
      driver.get(url);
      System.out.println("→ Navigated to sign up page");
      
      // Enter a password in the password field
      WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
      passwordField.clear();
      String testPassword = "TestPassword123!";
      passwordField.sendKeys(testPassword);
      System.out.println("→ Entered test password into the field");
      
      // Check that password is initially masked (type="password")
      String passwordType = passwordField.getAttribute("type");
      if ("password".equals(passwordType)) {
        System.out.println("✓ Password is initially masked (type='password') as expected");
      } else {
        System.out.println("❌ Password is NOT masked initially. Current type: " + passwordType);
      }
      
      // Find and click the eye button to toggle password visibility
      WebElement visibilityToggle = null;
      try {
        // Try finding the button by its aria-label (most reliable)
        visibilityToggle = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='toggle password visibility']")));
        System.out.println("→ Found visibility toggle by aria-label");
      } catch (Exception ex) {
        try {
          // Try finding the button that's near the password field
          visibilityToggle = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//div[.//input[@id='password']]//button[contains(@class, 'MuiIconButton-root')]")));
          System.out.println("→ Found visibility toggle near password field");
        } catch (Exception e) {
          // Last resort - find any button with the MuiIconButton class near the end of the form
          visibilityToggle = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//form//button[contains(@class, 'MuiIconButton-root')]")));
          System.out.println("→ Found visibility toggle using generic selector");
        }
      }
      
      // Scroll the button into view before clicking
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibilityToggle);
      Thread.sleep(500);
      
      // Step 1: Make password visible
      visibilityToggle.click();
      System.out.println("→ Clicked eye icon to show password");
      
      // Check that password is now visible (type="text")
      passwordType = passwordField.getAttribute("type");
      if ("text".equals(passwordType)) {
        System.out.println("✓ Password is now visible (type='text')");
      } else {
        System.out.println("❌ Password visibility toggle failed. Current type: " + passwordType);
      }
      
      // Step 2: Hide password again
      // Scroll the button into view again before clicking
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibilityToggle);
      Thread.sleep(500);
      
      visibilityToggle.click();
      System.out.println("→ Clicked eye-slash icon to hide password");
      
      // Check that password is masked again
      passwordType = passwordField.getAttribute("type");
      if ("password".equals(passwordType)) {
        System.out.println("✓ Password is masked again (type='password')");
        System.out.println("\n✅ SUCCESS: Password visibility toggle works correctly in both directions");
      } else {
        System.out.println("❌ Password is not masked as expected. Current type: " + passwordType);
        System.out.println("\n❌ FAILURE: Password visibility toggle is not working properly");
      }
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Password visibility test failed: " + e.getMessage());
      e.printStackTrace();
    }

    

    // ================================
    // Test 3: "Sign In" Navigation
    // ================================
    try {
      System.out.println("\n==== TEST: Sign In Navigation ====");
      
      // Navigate back to signup page
      driver.get(url);
      System.out.println("→ Navigated to sign up page");
      
      // Find and click the "Sign in" button
      try {
        // Try finding the button with class info first for more specificity
        WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiButton-root') and contains(@class, 'MuiButton-textPrimary') and contains(text(), 'Sign in')]")));
        
        System.out.println("→ Found 'Sign in' button, clicking...");
        
        // Scroll button into view before clicking to ensure visibility
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signInLink);
        
        // Add a small wait to ensure the button is fully in view
        Thread.sleep(500);
        
        // Click the button
        signInLink.click();
      } catch (Exception ex) {
        // If the regular click fails, try using JavaScript as fallback
        System.out.println("→ Using JavaScript to click the 'Sign in' button");
        WebElement element = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign in')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
      }
      
      // Verify navigation to login page
      wait.until(ExpectedConditions.urlContains("/login"));
      String expectedLoginUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
      String actualLoginUrl = driver.getCurrentUrl();
      
      if (actualLoginUrl.equals(expectedLoginUrl)) {
        System.out.println("✓ Navigation successful to: " + actualLoginUrl);
      } else {
        System.out.println("❌ Navigation failed. Expected: " + expectedLoginUrl + 
                          ", but got: " + actualLoginUrl);
      }
      
      // Check for login page elements to confirm successful navigation
      WebElement loginHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h1[contains(text(), 'Welcome Back')]")));
      if (loginHeading.isDisplayed()) {
        System.out.println("✓ Login page 'Welcome Back' header is displayed");
        System.out.println("\n✅ SUCCESS: Sign in navigation works correctly");
      } else {
        System.out.println("\n❌ FAILURE: Login page header not found");
      }
      
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Sign in navigation test failed: " + e.getMessage());
      e.printStackTrace();
    }

    // ================================
    // Test 4: "Back to Login" Button Functionality
    // ================================
    try {
      System.out.println("\n==== TEST: Back to Login Navigation ====");
      
      // Navigate back to signup page
      driver.get(url);
      System.out.println("→ Navigated to sign up page");
      
      // Find and click the "Back to Login" button
      try {
        // Try finding the button with class info first for more specificity
        WebElement backToLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiButton-root') and contains(text(), 'Back to Login')]")));
        
        System.out.println("→ Found 'Back to Login' button, clicking...");
        
        // Scroll button into view before clicking to ensure visibility
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", backToLoginButton);
        
        // Add a small wait to ensure the button is fully in view
        Thread.sleep(500);
        
        // Click the button
        backToLoginButton.click();
      } catch (Exception ex) {
        // If the regular click fails, try using JavaScript as fallback
        System.out.println("→ Using JavaScript to click the 'Back to Login' button");
        WebElement element = driver.findElement(
                By.xpath("//button[contains(text(), 'Back to Login')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
      }
      
      // Verify navigation to login page
      wait.until(ExpectedConditions.urlContains("/login"));
      String expectedLoginUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
      String actualLoginUrl = driver.getCurrentUrl();
      
      if (actualLoginUrl.equals(expectedLoginUrl)) {
        System.out.println("✓ Navigation successful to: " + actualLoginUrl);
      } else {
        System.out.println("❌ Navigation failed. Expected: " + expectedLoginUrl + 
                          ", but got: " + actualLoginUrl);
      }
      
      // Check for login page elements to confirm successful navigation
      WebElement loginHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h1[contains(text(), 'Welcome Back')]")));
      if (loginHeading.isDisplayed()) {
        System.out.println("✓ Login page 'Welcome Back' header is displayed");
        System.out.println("\n✅ SUCCESS: Back to Login navigation works correctly");
      } else {
        System.out.println("\n❌ FAILURE: Login page header not found");
      }
      
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Back to Login navigation test failed: " + e.getMessage());
      e.printStackTrace();
    } 


    // ================================
    // Test 2: Form Submission with Random Email
    // ================================
    try {
      System.out.println("\n==== TEST: Sign Up Form Submission ====");
      
      // Navigate back to signup page
      driver.get(url);
      System.out.println("→ Navigated to sign up page");
      
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
      
      // Check for successful submission - should redirect to home page
      try {
        // Wait for redirect to home page
        wait.until(ExpectedConditions.urlContains("/home"));
        String homeUrl = driver.getCurrentUrl();
        
        if (homeUrl.contains("/home")) {
          System.out.println("✓ Successfully created account and redirected to: " + homeUrl);
          
          // Verify we're on the home page by looking for a home page element
          WebElement homeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("//h1[contains(text(), 'Dashboard') or contains(text(), 'Welcome')]")));
          System.out.println("✓ Home page dashboard element is displayed");
          System.out.println("\n✅ SUCCESS: Account creation successful");
        } else {
          System.out.println("❌ Not redirected to home page. Current URL: " + homeUrl);
          System.out.println("\n❌ FAILURE: Account creation may have failed");
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
        }
      }
      
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Form submission test failed: " + e.getMessage());
      e.printStackTrace();
    }finally {
      // Close the browser.
      driver.quit();
      System.out.println("\n==== All Sign Up Page Tests Completed ====");
    }
  }
}
