import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignUpTest {
  public static void main(String[] args) {
    // Set the path to the ChromeDriver executable (update the path accordingly)
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Initialize the ChromeDriver.
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/signup"; 
    driver.get(url);

    // --- Page Title Verification ---
    String expectedTitle = "SuperSurveyors";
    String actualTitle = driver.getTitle();
    if (expectedTitle.equals(actualTitle)) {
      System.out.println("Page title verification passed: " + actualTitle);
    } else {
      System.out.println("Page title verification failed: expected '"
              + expectedTitle + "', but got '" + actualTitle + "'");
    }

    // --- Header Verification ---
    // Verify that the header displays the SuperSurveyors logo/title.
    try {
      WebElement headerTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
      ));
      System.out.println("Header with 'SuperSurveyors' is displayed.");
    } catch (Exception e) {
      System.out.println("Header verification failed: " + e.getMessage());
    }

    // Verify that the "Back to Login" button is present.
    try {
      WebElement backToLoginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Back to Login')]")
      ));
      System.out.println("'Back to Login' button is displayed.");
    } catch (Exception e) {
      System.out.println("'Back to Login' button verification failed: " + e.getMessage());
    }

    // --- Main Content Verification ---
    // Verify that the main heading "Create Account" is displayed.
    try {
      WebElement mainHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//h1[contains(text(),'Create Account')]")
      ));
      System.out.println("Main heading 'Create Account' is displayed.");
    } catch (Exception e) {
      System.out.println("Main heading verification failed: " + e.getMessage());
    }

    // Verify that the tagline "Join SuperSurveyors today" is displayed.
    try {
      WebElement tagline = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//main//p[contains(text(),'Join SuperSurveyors today')]")
      ));
      System.out.println("Tagline 'Join SuperSurveyors today' is displayed.");
    } catch (Exception e) {
      System.out.println("Tagline verification failed: " + e.getMessage());
    }

    // --- Form Fields Verification ---
    // Check that the Display Name label and input field are present.
    try {
      WebElement displayNameLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]")
      ));
      WebElement displayNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("displayName")
      ));
      System.out.println("Display Name label and input are displayed.");
    } catch (Exception e) {
      System.out.println("Display Name field verification failed: " + e.getMessage());
    }

    // Check that the Email Address label and input field are present.
    try {
      WebElement emailLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Email Address')]")
      ));
      WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("email")
      ));
      System.out.println("Email Address label and input are displayed.");
    } catch (Exception e) {
      System.out.println("Email Address field verification failed: " + e.getMessage());
    }

    // Check that the Password label and input field are present.
    try {
      WebElement passwordLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Password')]")
      ));
      WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("password")
      ));
      System.out.println("Password label and input are displayed.");
    } catch (Exception e) {
      System.out.println("Password field verification failed: " + e.getMessage());
    }

    // --- Button Verification ---
    // Verify that the primary "Create Account" button (of type submit) is displayed.
    try {
      WebElement createAccountButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Create Account') and @type='submit']")
      ));
      System.out.println("'Create Account' button is displayed.");
    } catch (Exception e) {
      System.out.println("'Create Account' button verification failed: " + e.getMessage());
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
      System.out.println("Sign in prompt and button are displayed.");
    } catch (Exception e) {
      System.out.println("Sign in prompt verification failed: " + e.getMessage());
    }

    // --- Optional: Verify Alert Container ---
    // Optionally verify that the error alert container exists (even if hidden by default).
    try {
      WebElement errorAlert = driver.findElement(By.xpath(
              "//div[@role='alert' and contains(@class, 'MuiAlert-colorError')]"));
      System.out.println("Error alert container is present in the DOM.");
    } catch (Exception e) {
      System.out.println("Error alert container verification failed: " + e.getMessage());
    }

    // Close the browser.
    driver.quit();
  }
}
