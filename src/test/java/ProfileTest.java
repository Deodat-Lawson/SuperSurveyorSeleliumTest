import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProfileTest {
  public static void main(String[] args) {
    // Set the path to your ChromeDriver executable (update as needed)
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Create a new instance of the ChromeDriver
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Home page
    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
    driver.get(url);

    //Sign in
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




    try {
      // 1. Navigate to the Profile page (View page)
      String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("Navigated to Profile page: " + profileUrl);

      // 2. Verify that the header contains "SuperSurveyors"
      WebElement headerLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
      ));
      System.out.println("Header verification passed: " + headerLogo.getText());

      // 3. Verify the profile name "Timothy Lin"
      WebElement profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(text(), 'Timothy Lin')]")
      ));
      System.out.println("Profile name is displayed: " + profileName.getText());

      // 4. Verify the email address is displayed correctly
      WebElement profileEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(), 'timothylinziqimc@gmail.com')]")
      ));
      System.out.println("Profile email is displayed: " + profileEmail.getText());

      // 5. Verify the UID is displayed
      WebElement profileUID = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(), 'UID:')]")
      ));
      System.out.println("Profile UID is displayed: " + profileUID.getText());

      // 6. Verify that the "Interest Tags" section is present with the heading
      WebElement interestTagsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h6[contains(text(), 'Interest Tags')]")
      ));
      System.out.println("Interest Tags header is displayed: " + interestTagsHeader.getText());

      // Verify that the section shows "No tags selected"
      WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")
      ));
      System.out.println("No tags message is displayed: " + noTagsMessage.getText());

      // 7. Optionally, verify the Edit Profile and Edit Tags buttons are clickable.
      WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")
      ));
      System.out.println("Edit profile button is clickable.");

      WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit tags']")
      ));
      System.out.println("Edit tags button is clickable.");

      // 8. Optionally, verify that a profile picture is displayed.
      WebElement profilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'MuiAvatar-root')]//img[@alt='Profile']")
      ));
      if (profilePic.isDisplayed()) {
        System.out.println("Profile picture is displayed.");
      }

    } catch (Exception e) {
      System.out.println("Profile Page test failed: " + e.getMessage());
    }


    //Edit Profile and Edit tags test


    try {
      // Navigate to the Profile page.
      String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("Navigated to Profile Page: " + profileUrl);

      // Click the "edit profile" button identified by its aria-label.
      WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")));
      editProfileButton.click();
      System.out.println("Clicked the 'edit profile' button.");



      // Wait for the modal for editing the profile name to appear.
      // Assume the input field for the name has the id "editDisplayName".
      WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")
      ));
      // Change the display name.
      String newName = "New Test Name";
      nameInput.clear();
      nameInput.sendKeys(newName);
      System.out.println("Entered new display name: " + newName);

      // Click the Save button in the profile edit modal.
      // Assume the save button has the id "saveProfileButton".
      WebElement saveProfileButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Save Changes')]")));
      if (saveProfileButton.isDisplayed()) {
        System.out.println("\"Save Changes\" button verification passed!");
      }
      saveProfileButton.click();
      System.out.println("Clicked 'Save Profile' button.");

      // Wait until the profile edit modal closes (e.g., the name input is no longer visible).
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editDisplayName")));

      // Verify that the profile page now displays the updated name.
      WebElement updatedNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(text(), '" + newName + "')]")
      ));
      if (updatedNameElement.isDisplayed()) {
        System.out.println("Profile name updated successfully to: " + newName);
      } else {
        System.out.println("Profile name update failed.");
      }

      // ================================
      // Test 2: Edit Profile Tags (Edit Text)
      // ================================

      // Click the "edit tags" button. Assume it is identified by its aria-label.
      WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit tags']")));
      editTagsButton.click();
      System.out.println("Clicked the 'edit tags' button.");

      // Wait for the tags edit modal to appear.
      // Assume the input field for tags has the id "editTags".
      WebElement tagsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.id("editTags")
      ));
      String newTags = "Technology, Science";
      tagsInput.clear();
      tagsInput.sendKeys(newTags);
      System.out.println("Entered new tags: " + newTags);

      // Click the Save button for tags.
      // Assume this save button has the id "saveTagsButton".
      WebElement saveTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.id("saveTagsButton")
      ));
      saveTagsButton.click();
      System.out.println("Clicked 'Save Tags' button.");

      // Wait until the tags edit modal closes.
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editTags")));

      // Verify that the updated tags are displayed in the "Interest Tags" section.
      WebElement tagsDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), '" + newTags + "')]")
      ));

      if (tagsDisplay.isDisplayed()) {
        System.out.println("Tags updated successfully to: " + newTags);
      } else {
        System.out.println("Tags update failed.");
      }

    } catch (Exception e) {
      System.out.println("Edit Profile test failed: " + e.getMessage());
    }




  }
}
