import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreateSurveyTest {
  public static void main(String[] args) {
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Initialize ChromeDriver and WebDriverWait.
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    try {
      // Navigate to the Create Survey page.
      String createSurveyUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
      driver.get(createSurveyUrl);


      //Sign In:
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

      } catch (Exception e) {
        System.out.println("Test encountered an error: " + e.getMessage());
      }

      WebElement createNavLink = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//a[@href='#/create' and contains(text(),'Create')]")
      ));
      createNavLink.click();
      System.out.println("Clicked on the 'Create' button in the top navigation.");


      //Wait until it is in view page
      String expectedCreateUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/create";

      // Wait until the URL becomes the expected home URL.
      boolean urlChanged2 = wait.until(ExpectedConditions.urlToBe(expectedCreateUrl));

      // --- Page Load Verification ---
      // Wait for the header "Create a Survey" to ensure the page is loaded.
      WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(text(),'Create a Survey')]")
      ));
      System.out.println("Page header 'Create a Survey' is visible.");

      // --- Fill Out the Survey Form ---

      // Fill in the Survey Title.
      WebElement surveyTitleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("surveyTitle")
      ));
      surveyTitleInput.clear();
      surveyTitleInput.sendKeys("Selenium Test Survey");
      System.out.println("Survey Title filled.");

      // Fill in the Question.

      WebElement questionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//input[@placeholder='Enter your question']")
      ));
      questionInput.clear();
      questionInput.sendKeys("What is your favorite programming language?");
      System.out.println("Question filled.");

      // Verify that the Response Type is defaulted to "Free Response".
      WebElement responseTypeDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[@role='combobox' and contains(text(),'Free Response')]")
      ));
      System.out.println("Response type defaulted to: " + responseTypeDiv.getText());

      //Add Question
      WebElement addQuestionButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Add Question')]")
      ));
      addQuestionButton.click();
      System.out.println("Clicked 'Add Question' to add a second question.");



      // Optionally, you might check other fields or interactions (e.g., uploading media or adding additional questions)
      // For this test, we proceed to submit the survey.

      // Click the "Submit Survey" button.
      WebElement submitSurveyButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Submit Survey')]")
      ));
      submitSurveyButton.click();
      System.out.println("Submit Survey button clicked.");

      // --- Verification After Submission ---
      // If your application displays a success message or redirects upon successful submission,
      // wait for that condition. For example, wait until the "Submit Survey" button is no longer visible,
      // or a confirmation message appears. Here, we wait for the submit button to disappear:
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
              By.xpath("//button[contains(text(),'Submit Survey')]")
      ));
      System.out.println("Survey creation appears to be processed successfully.");

    } catch (Exception e) {
      System.out.println("Create Survey test failed: " + e.getMessage());
    } finally {
      // Close the browser.
      driver.quit();
    }
  }
}
