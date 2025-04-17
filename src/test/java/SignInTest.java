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

    // --- Basic Page and Header Verification ---

    // Verify the page title is "SuperSurveyors"
    String expectedTitle = "SuperSurveyors";
    String actualTitle = driver.getTitle();
    if (expectedTitle.equals(actualTitle)) {
      System.out.println("Page title verification passed: " + actualTitle);
    } else {
      System.out.println("Page title verification failed: expected '" + expectedTitle
              + "', but got '" + actualTitle + "'");
    }

    // Verify the header contains the logo/title "SuperSurveyors"
    try {
      WebElement headerLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
      ));
      System.out.println("Header link with 'SuperSurveyors' is displayed.");
    } catch (Exception e) {
      System.out.println("Header link verification failed: " + e.getMessage());
    }

    // --- Main Content Verification ---

    // Verify the main heading "Welcome Back"
    try {
      WebElement welcomeHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//h1[contains(text(), 'Welcome Back')]")
      ));
      System.out.println("Main heading 'Welcome Back' is displayed.");
    } catch (Exception e) {
      System.out.println("Welcome heading verification failed: " + e.getMessage());
    }

    // Verify the tagline "Sign in to SuperSurveyors"
    try {
      WebElement tagline = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//p[contains(text(), 'Sign in to SuperSurveyors')]")
      ));
      System.out.println("Tagline 'Sign in to SuperSurveyors' is displayed.");
    } catch (Exception e) {
      System.out.println("Tagline verification failed: " + e.getMessage());
    }

    // --- Form Fields Verification ---

    // Check that the Email Address label and input field are present
    try {
      WebElement emailLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(), 'Email Address')]")
      ));
      WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("email")
      ));
      System.out.println("Email label and input field are displayed.");
    } catch (Exception e) {
      System.out.println("Email field verification failed: " + e.getMessage());
    }

    // Check that the Password label and input field are present
    try {
      WebElement passwordLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(), 'Password')]")
      ));
      WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("password")
      ));
      System.out.println("Password label and input field are displayed.");
    } catch (Exception e) {
      System.out.println("Password field verification failed: " + e.getMessage());
    }

    // --- Buttons Verification ---

    // Verify the primary "Sign In" button (submit type)
    try {
      WebElement signInButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Sign In') and @type='submit']")
      ));
      System.out.println("\"Sign In\" button is displayed.");
    } catch (Exception e) {
      System.out.println("\"Sign In\" button verification failed: " + e.getMessage());
    }

    // Verify the "Login with Google" button is present
    try {
      WebElement googleLoginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Login with')]")
      ));
      System.out.println("\"Login with Google\" button is displayed.");
    } catch (Exception e) {
      System.out.println("\"Login with Google\" button verification failed: " + e.getMessage());
    }

    // --- Sign Up Prompt Verification ---

    // Verify that the prompt "Don't have an account?" and the Sign Up button exist.
    try {
      WebElement signUpPrompt = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(), \"Don't have an account?\")]")
      ));
      WebElement signUpButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Sign up')]")
      ));
      System.out.println("Sign up prompt and button are displayed.");
    } catch (Exception e) {
      System.out.println("Sign up prompt verification failed: " + e.getMessage());
    }

    // ================================
    // Test 1: Password Visibility Toggle
    // ================================
    try {
      System.out.println("\n--- Testing Password Visibility Toggle ---");
      
      // Navigate back to login page
      driver.get(url);
      
      // Enter a password in the password field
      WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
      passwordField.clear();
      String testPassword = "TestPassword123!";
      passwordField.sendKeys(testPassword);
      System.out.println("Entered password into the field");
      
      // Check that password is initially masked (type="password")
      String passwordType = passwordField.getAttribute("type");
      if ("password".equals(passwordType)) {
        System.out.println("Password is initially masked (type='password') as expected");
      } else {
        System.out.println("Password is NOT masked initially. Current type: " + passwordType);
      }
      
      // Find and click the eye button to toggle password visibility
      // Using a simpler approach that doesn't rely on the SVG path directly
      WebElement visibilityToggle = null;
      try {
        // Try finding the button by its aria-label (most reliable)
        visibilityToggle = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='toggle password visibility']")));
        System.out.println("Found visibility toggle button by aria-label");
      } catch (Exception ex) {
        try {
          // Try finding the button that's near the password field
          visibilityToggle = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//div[.//input[@id='password']]//button[contains(@class, 'MuiIconButton-root')]")));
          System.out.println("Found visibility toggle button near password field");
        } catch (Exception e) {
          // Last resort - find any button with the MuiIconButton class near the end of the form
          visibilityToggle = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//form//button[contains(@class, 'MuiIconButton-root')]")));
          System.out.println("Found visibility toggle button using generic selector");
        }
      }
      
      System.out.println("Found the password visibility toggle button");
      
      // Scroll the button into view before clicking
      ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibilityToggle);
      Thread.sleep(500);
      
      visibilityToggle.click();
      System.out.println("Clicked the visibility toggle button (eye icon)");
      
      // Check that password is now visible (type="text")
      passwordType = passwordField.getAttribute("type");
      if ("text".equals(passwordType)) {
        System.out.println("Password is now visible (type='text') as expected after toggle");
      } else {
        System.out.println("Password visibility toggle failed. Current type: " + passwordType);
      }
      
      // Click the toggle button again to hide the password
      // Scroll the button into view again before clicking
      ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", visibilityToggle);
      Thread.sleep(500);
      
      visibilityToggle.click();
      System.out.println("Clicked the visibility toggle button again (eye icon with slash)");
      
      // Check that password is masked again
      passwordType = passwordField.getAttribute("type");
      if ("password".equals(passwordType)) {
        System.out.println("Password is masked again (type='password') as expected");
        System.out.println("\n✅ SUCCESS: Password visibility toggle test PASSED! Toggle functionality works correctly.");
      } else {
        System.out.println("Password is not masked as expected. Current type: " + passwordType);
        System.out.println("\n❌ FAILURE: Password visibility toggle test FAILED! Toggle functionality is not working properly.");
      }
    } catch (Exception e) {
      System.out.println("Password visibility toggle test failed: " + e.getMessage());
      e.printStackTrace();
    }
    
    // ================================
    // Test 2: Google Login Window
    // ================================
    try {
      System.out.println("\n--- Testing Google Login Window ---");
      
      // Navigate back to login page
      driver.get(url);
      
      // Get current window handle before clicking Google login
      String mainWindowHandle = driver.getWindowHandle();
      System.out.println("Main window handle: " + mainWindowHandle);
      
      // Find and click the Google login button
      WebElement googleLoginButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Login with Google') or contains(text(),'Login with')]")));
      System.out.println("Found the Google login button");
      googleLoginButton.click();
      System.out.println("Clicked the Google login button");
      
      // Wait for the new window/tab to open (Google login)
      wait.until(ExpectedConditions.numberOfWindowsToBe(2));
      
      // Get all window handles and switch to the new window
      Set<String> windowHandles = driver.getWindowHandles();
      System.out.println("Number of open windows: " + windowHandles.size());
      
      String googleWindowHandle = null;
      for (String handle : windowHandles) {
        if (!handle.equals(mainWindowHandle)) {
          googleWindowHandle = handle;
          driver.switchTo().window(googleWindowHandle);
          System.out.println("Switched to Google login window: " + googleWindowHandle);
          break;
        }
      }
      
      // Verify we're on a Google authentication page
      // We don't need to actually log in, just verify the window opened properly
      wait.until(ExpectedConditions.urlContains("accounts.google.com"));
      String googleUrl = driver.getCurrentUrl();
      System.out.println("Google login URL: " + googleUrl);
      
      if (googleUrl.contains("accounts.google.com")) {
        System.out.println("Google login window verification PASSED!");
      } else {
        System.out.println("Google login window verification FAILED! Current URL: " + googleUrl);
      }
      
    } catch (Exception e) {
      System.out.println("Google login window test failed: " + e.getMessage());
      e.printStackTrace();
    }
    
    // ================================
    // Test 3: Sign Up Link Navigation
    // ================================
    try {
      System.out.println("\n--- Testing Sign Up Link Navigation ---");
      
      // Navigate back to login page
      driver.get(url);
      
      // Find and click the Sign Up button/link using a more specific selector with the class information
      try {
        // Try a more specific XPath that uses the class information
        WebElement signUpLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiButton-root') and contains(@class, 'MuiButton-textPrimary') and contains(text(), 'Sign up')]")));
        
        System.out.println("Found the Sign up button");
        
        // Scroll the button into view before clicking to ensure it's visible
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpLink);
        
        // Add a small wait to ensure the button is fully in view
        Thread.sleep(500);
        
        // Click the button
        signUpLink.click();
        System.out.println("Clicked the Sign up button");
      } catch (Exception ex) {
        // If the regular click fails, try JavaScript executor as a last resort
        System.out.println("→ Using JavaScript executor to click the Sign Up button");
        WebElement element = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign up')]"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        System.out.println("Clicked the Sign up button using JavaScript");
      }
      
      // Verify navigation to signup page
      wait.until(ExpectedConditions.urlContains("/signup"));
      String expectedSignUpUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/signup";
      String actualSignUpUrl = driver.getCurrentUrl();
      
      if (actualSignUpUrl.equals(expectedSignUpUrl)) {
        System.out.println("Sign up link navigation test PASSED! Redirected to: " + actualSignUpUrl);
      } else {
        System.out.println("Sign up link navigation test FAILED. Expected: " + expectedSignUpUrl + 
                          ", but got: " + actualSignUpUrl);
      }
      
      // Check for sign up page elements
      WebElement signUpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Create Account')]")));
      if (signUpHeader.isDisplayed()) {
        System.out.println("Sign Up page header verification passed!");
      }
      
    } catch (Exception e) {
      System.out.println("Sign up link navigation test failed: " + e.getMessage());
      e.printStackTrace();
    }

    // Sign In functionality test:
    try {
      // Navigate back to login page
      driver.get(url);
      System.out.println("\n--- Testing Sign In Functionality ---");

      WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
      emailField.clear();
      emailField.sendKeys("timothylinziqimc@gmail.com");

      // Wait for the password input field and enter the password.
      WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
      passwordField.clear();
      passwordField.sendKeys("test12345678!");

      // Wait for the "Sign In" button to be clickable and click it.
      WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
      signInButton.click();

      // Define the expected URL after a successful sign in.
      String expectedHomeUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/home";

      // Wait until the URL becomes the expected home URL.
      boolean urlChanged = wait.until(ExpectedConditions.urlToBe(expectedHomeUrl));

      if (urlChanged) {
        System.out.println("Sign in functionality test passed! Redirected to: " + driver.getCurrentUrl());
      } else {
        System.out.println("Sign in functionality test failed. Current URL: " + driver.getCurrentUrl());
      }
      
    } catch (Exception e) {
      System.out.println("Sign in functionality test failed: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Close the browser.
      driver.quit();
      System.out.println("\n--- All tests completed. Browser closed. ---");
    }
  }
}
