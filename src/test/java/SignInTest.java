import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

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

    //Sign In functionality test:
    try {

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
      // Close the browser.
      driver.quit();
    } catch (Exception e) {
      System.out.println("Test encountered an error: " + e.getMessage());
    }

  }
}
