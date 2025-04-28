//Completed

package com.supersurveyors.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.JavascriptExecutor;

public class SignInTest {
  public static void main(String[] args) {
    // Set the path to the ChromeDriver executable. Update the path if necessary.
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Initialize the ChromeDriver.
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
    driver.get(url);
    System.out.println("\n==== TEST: Sign In Page Elements Verification ====");
    System.out.println("→ Navigated to sign in page: " + url);

    // --- Basic Page and Header Verification ---

    // Verify the page title is "SuperSurveyors"
    String expectedTitle = "SuperSurveyors";
    String actualTitle = driver.getTitle();
    if (expectedTitle.equals(actualTitle)) {
      System.out.println("✓ Page title verification passed: " + actualTitle);
    } else {
      System.out.println("❌ Page title verification failed: expected '" + expectedTitle
              + "', but got '" + actualTitle + "'");
    }

    // Verify the header contains the logo/title "SuperSurveyors"
    try {
      WebElement headerLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
      ));
      System.out.println("✓ Header with 'SuperSurveyors' logo is displayed");
    } catch (Exception e) {
      System.out.println("❌ Header verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking main content elements --");
    
    // Verify the main heading "Welcome Back"
    try {
      WebElement welcomeHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//h1[contains(text(), 'Welcome Back')]")
      ));
      System.out.println("✓ 'Welcome Back' heading is displayed");
    } catch (Exception e) {
      System.out.println("❌ Welcome heading verification failed: " + e.getMessage());
    }

    // Verify the tagline "Sign in to SuperSurveyors"
    try {
      WebElement tagline = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//p[contains(text(), 'Sign in to SuperSurveyors')]")
      ));
      System.out.println("✓ 'Sign in to SuperSurveyors' tagline is displayed");
    } catch (Exception e) {
      System.out.println("❌ Tagline verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking form fields --");
    
    // Check that the Email Address label and input field are present
    try {
      WebElement emailLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(), 'Email Address')]")
      ));
      WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("email")
      ));
      System.out.println("✓ Email field and label are displayed");
    } catch (Exception e) {
      System.out.println("❌ Email field verification failed: " + e.getMessage());
    }

    // Check that the Password label and input field are present
    try {
      WebElement passwordLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(), 'Password')]")
      ));
      WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("password")
      ));
      System.out.println("✓ Password field and label are displayed");
    } catch (Exception e) {
      System.out.println("❌ Password field verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking buttons and navigation options --");
    
    // Verify the primary "Sign In" button (submit type)
    try {
      WebElement signInButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Sign In') and @type='submit']")
      ));
      System.out.println("✓ 'Sign In' button is displayed");
    } catch (Exception e) {
      System.out.println("❌ 'Sign In' button verification failed: " + e.getMessage());
    }

    // Verify the "Login with Google" button is present
    try {
      WebElement googleLoginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Login with')]")
      ));
      System.out.println("✓ 'Login with Google' button is displayed");
    } catch (Exception e) {
      System.out.println("❌ 'Login with Google' button verification failed: " + e.getMessage());
    }

    // Verify that the prompt "Don't have an account?" and the Sign Up button exist.
    try {
      WebElement signUpPrompt = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(), \"Don't have an account?\")]")
      ));
      WebElement signUpButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Sign up')]")
      ));
      System.out.println("✓ 'Don't have an account?' prompt and 'Sign up' button are displayed");
    } catch (Exception e) {
      System.out.println("❌ Sign up prompt verification failed: " + e.getMessage());
    }

    // ================================
    // Test 1: Password Visibility Toggle
    // ================================
    try {
      System.out.println("\n==== TEST: Password Visibility Toggle ====");
      
      // Navigate back to login page
      driver.get(url);
      System.out.println("→ Navigated to sign in page");
      
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
      // Using a simpler approach that doesn't rely on the SVG path directly
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
      ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibilityToggle);
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
      ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibilityToggle);
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
    // Test 2: Google Login Window
    // ================================
    try {
      System.out.println("\n==== TEST: Google Login Integration ====");
      
      // Navigate back to login page
      driver.get(url);
      System.out.println("→ Navigated to sign in page");
      
      // Get current window handle before clicking Google login
      String mainWindowHandle = driver.getWindowHandle();
      System.out.println("→ Main application window handle: " + mainWindowHandle);
      
      // Find and click the Google login button
      WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Login with Google') or contains(text(),'Login with')]")));
      System.out.println("→ Found 'Login with Google' button, clicking...");
      googleLoginButton.click();
      
      // Wait for the new window/tab to open (Google login)
      wait.until(ExpectedConditions.numberOfWindowsToBe(2));
      
      // Get all window handles and switch to the new window
      Set<String> windowHandles = driver.getWindowHandles();
      System.out.println("→ " + windowHandles.size() + " browser windows are now open");
      
      String googleWindowHandle = null;
      for (String handle : windowHandles) {
        if (!handle.equals(mainWindowHandle)) {
          googleWindowHandle = handle;
          driver.switchTo().window(googleWindowHandle);
          System.out.println("→ Switched to Google authentication window");
          break;
        }
      }
      
      // Verify we're on a Google authentication page
      wait.until(ExpectedConditions.urlContains("accounts.google.com"));
      String googleUrl = driver.getCurrentUrl();
      
      if (googleUrl.contains("accounts.google.com")) {
        System.out.println("✓ Google authentication page loaded: " + googleUrl);
        System.out.println("\n✅ SUCCESS: Google login integration works correctly");
      } else {
        System.out.println("❌ Not on Google authentication page. Current URL: " + googleUrl);
        System.out.println("\n❌ FAILURE: Google login integration failed");
      }
      
      // Switch back to main window for next test
      driver.switchTo().window(mainWindowHandle);
      System.out.println("→ Switched back to main application window");
      
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Google login test failed: " + e.getMessage());
      e.printStackTrace();
    }
    
    // ================================
    // Test 3: Sign Up Link Navigation
    // ================================
    try {
      System.out.println("\n==== TEST: Sign Up Navigation ====");
      
      // Navigate back to login page
      driver.get(url);
      System.out.println("→ Navigated to sign in page");
      
      // Find and click the Sign Up button/link using a more specific selector with the class information
      try {
        // Try a more specific XPath that uses the class information
        WebElement signUpLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiButton-root') and contains(@class, 'MuiButton-textPrimary') and contains(text(), 'Sign up')]")));
        
        System.out.println("→ Found 'Sign up' button, clicking...");
        
        // Scroll the button into view before clicking to ensure it's visible
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpLink);
        
        // Add a small wait to ensure the button is fully in view
        Thread.sleep(500);
        
        // Click the button
        signUpLink.click();
      } catch (Exception ex) {
        // If the regular click fails, try JavaScript executor as a last resort
        System.out.println("→ Using JavaScript to click the 'Sign up' button");
        WebElement element = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign up')]"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
      }
      
      // Verify navigation to signup page
      wait.until(ExpectedConditions.urlContains("/signup"));
      String expectedSignUpUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/signup";
      String actualSignUpUrl = driver.getCurrentUrl();
      
      if (actualSignUpUrl.equals(expectedSignUpUrl)) {
        System.out.println("✓ Navigation successful to: " + actualSignUpUrl);
      } else {
        System.out.println("❌ Navigation failed. Expected: " + expectedSignUpUrl + 
                          ", but got: " + actualSignUpUrl);
      }
      
      // Check for sign up page elements
      WebElement signUpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Create Account')]")));
      if (signUpHeader.isDisplayed()) {
        System.out.println("✓ Sign Up page 'Create Account' header is displayed");
        System.out.println("\n✅ SUCCESS: Sign up navigation works correctly");
      } else {
        System.out.println("\n❌ FAILURE: Sign up page header not found");
      }
      
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Sign up navigation test failed: " + e.getMessage());
      e.printStackTrace();
    }

    // ================================
    // Test 4: Sign In Functionality
    // ================================
    try {
      System.out.println("\n==== TEST: Sign In Authentication ====");
      
      // Navigate back to login page
      driver.get(url);
      System.out.println("→ Navigated to sign in page");

      // Enter email
      WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
      emailField.clear();
      emailField.sendKeys("timothylinziqimc@gmail.com");
      System.out.println("→ Entered test email address");

      // Enter password
      WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
      passwordField.clear();
      passwordField.sendKeys("test12345678!");
      System.out.println("→ Entered test password");

      // Click the Sign In button
      WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
      System.out.println("→ Clicking 'Sign In' button...");
      signInButton.click();

      // Verify navigation to home page after login
      String expectedHomeUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/home";
      boolean urlChanged = wait.until(ExpectedConditions.urlToBe(expectedHomeUrl));

      if (urlChanged) {
        System.out.println("✓ Successfully signed in and redirected to: " + driver.getCurrentUrl());
        System.out.println("\n✅ SUCCESS: Sign in authentication works correctly");
      } else {
        System.out.println("❌ Sign in failed. Current URL: " + driver.getCurrentUrl());
        System.out.println("\n❌ FAILURE: Sign in authentication failed");
      }
      
    } catch (Exception e) {
      System.out.println("\n❌ FAILURE: Sign in authentication test failed: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Close the browser.
      driver.quit();
      System.out.println("\n==== All Sign In Page Tests Completed ====");
    }
  }
}
