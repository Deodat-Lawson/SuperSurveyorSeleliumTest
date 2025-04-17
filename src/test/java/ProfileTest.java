import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;

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
      // Test 2: Edit Profile Tags (Toggle Test)
      // ================================

      // Click the "edit tags" button to open the tag selection modal
      WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit tags']")));
      editTagsButton.click();
      System.out.println("Clicked the 'edit tags' button.");

      // First select two tags (toggle on): 'arts' and 'gaming'
      System.out.println("\n==== TEST: Tag Selection (Toggle ON) ====");
      System.out.println("Selecting all available interest tags...");
      
      // Click on the 'arts' tag to select it
      WebElement artsTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'arts')]")
      ));
      artsTag.click();
      System.out.println("✓ Selected: 'arts' tag");

      // Click on the 'gaming' tag to select it
      WebElement gamingTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'gaming')]")
      ));
      gamingTag.click();
      System.out.println("✓ Selected: 'gaming' tag");

      // Click on the 'technology' tag to select it
      WebElement technologyTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'technology')]")
      ));
      technologyTag.click();
      System.out.println("✓ Selected: 'technology' tag");
      
      // Click on the 'cooking' tag to select it
      WebElement cookingTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'cooking')]")
      ));
      cookingTag.click();
      System.out.println("✓ Selected: 'cooking' tag");
      
      // Click on the 'eduLife' tag to select it
      WebElement eduLifeTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'eduLife')]")
      ));
      eduLifeTag.click();
      System.out.println("✓ Selected: 'eduLife' tag");
      
      // Click on the 'environment' tag to select it
      WebElement environmentTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'environment')]")
      ));
      environmentTag.click();
      System.out.println("✓ Selected: 'environment' tag");
      
      // Click on the 'healthLife' tag to select it
      WebElement healthLifeTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'healthLife')]")
      ));
      healthLifeTag.click();
      System.out.println("✓ Selected: 'healthLife' tag");
      
      // Click on the 'sport' tag to select it
      WebElement sportTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'sport')]")
      ));
      sportTag.click();
      System.out.println("✓ Selected: 'sport' tag");
      
      // Click on the 'travel' tag to select it
      WebElement travelTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'travel')]")
      ));
      travelTag.click();
      System.out.println("✓ Selected: 'travel' tag");

      // Save the changes
      WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Save Changes')]")
      ));
      saveChangesButton.click();
      System.out.println("→ Saving selected tags...");
      
      // Wait until the tags edit modal closes
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editTags")));
      System.out.println("→ Tag selection modal closed");
      
      // Verify that the selected tags are displayed
      System.out.println("\nVerifying selected tags appear on profile:");
      
      WebElement artsTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'arts')]")
      ));
      System.out.println("✓ Found: 'arts' tag on profile");

      WebElement cookingTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//*[contains(text(), 'cooking')]")
        ));     
      System.out.println("✓ Found: 'cooking' tag on profile");

      WebElement gamingTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'gaming')]")
      ));
      System.out.println("✓ Found: 'gaming' tag on profile");
      
      WebElement technologyTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'technology')]")
      ));
      System.out.println("✓ Found: 'technology' tag on profile");
      
      // Additional tag buttons verification
      WebElement eduLifeTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'eduLife')]")
      ));
      System.out.println("✓ Found: 'eduLife' tag on profile");
      
      WebElement environmentTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'environment')]")
      ));
      System.out.println("✓ Found: 'environment' tag on profile");
      
      WebElement healthLifeTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'healthLife')]")
      ));
      System.out.println("✓ Found: 'healthLife' tag on profile");
      
      WebElement sportTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'sport')]")
      ));
      System.out.println("✓ Found: 'sport' tag on profile");
      
      WebElement travelTagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'travel')]")
      ));
      System.out.println("✓ Found: 'travel' tag on profile");

      if (artsTagDisplay.isDisplayed() && gamingTagDisplay.isDisplayed() && technologyTagDisplay.isDisplayed() &&
          cookingTagDisplay.isDisplayed() && eduLifeTagDisplay.isDisplayed() && environmentTagDisplay.isDisplayed() && 
          healthLifeTagDisplay.isDisplayed() && sportTagDisplay.isDisplayed() && travelTagDisplay.isDisplayed()) {
        System.out.println("\n✅ SUCCESS: All tags were successfully added to the profile!");
      } else {
        System.out.println("\n❌ FAILURE: Some tags failed to appear on the profile!");
      }

      // Now test toggle OFF functionality for all tags
      System.out.println("\n==== TEST: Tag Deselection (Toggle OFF) ====");
      
      // Click the edit tags button again
      editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit tags']")));
      editTagsButton.click();
      System.out.println("→ Opening tag selection modal for deselection test");
      
      // Wait for the tags edit modal to appear again
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editTags")));
      System.out.println("→ Tag selection modal opened successfully");
      
      // Toggle OFF all previously selected tags
      System.out.println("\nRemoving all previously selected tags:");
      
      // Click on the 'arts' tag to deselect it
      artsTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'arts')]")));
      artsTag.click();
      System.out.println("✓ Deselected: 'arts' tag");
      
      // Click on the 'gaming' tag to deselect it
      gamingTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'gaming')]")));
      gamingTag.click();
      System.out.println("✓ Deselected: 'gaming' tag");
      
      // Click on the 'technology' tag to deselect it
      technologyTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'technology')]")));
      technologyTag.click();
      System.out.println("✓ Deselected: 'technology' tag");
      
      // Click on the 'cooking' tag to deselect it
      cookingTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'cooking')]")));
      cookingTag.click();
      System.out.println("✓ Deselected: 'cooking' tag");
      
      // Click on the 'eduLife' tag to deselect it
      eduLifeTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'eduLife')]")));
      eduLifeTag.click();
      System.out.println("✓ Deselected: 'eduLife' tag");
      
      // Click on the 'environment' tag to deselect it
      environmentTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'environment')]")));
      environmentTag.click();
      System.out.println("✓ Deselected: 'environment' tag");
      
      // Click on the 'healthLife' tag to deselect it
      healthLifeTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'healthLife')]")));
      healthLifeTag.click();
      System.out.println("✓ Deselected: 'healthLife' tag");
      
      // Click on the 'sport' tag to deselect it
      sportTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'sport')]")));
      sportTag.click();
      System.out.println("✓ Deselected: 'sport' tag");
      
      // Click on the 'travel' tag to deselect it
      travelTag = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//div[contains(@class, 'css-12vffkv')]//p[contains(text(), 'travel')]")));
      travelTag.click();
      System.out.println("✓ Deselected: 'travel' tag");
      
      // Save the changes after deselection
      saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Save Changes')]")));
      saveChangesButton.click();
      System.out.println("\n→ Saving changes after removing all tags...");
      
      // Wait until the tags edit modal closes
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editTags")));
      System.out.println("→ Tag selection modal closed");
      
      // Verify that "No tags selected" message appears
      try {
        WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")));
        if (noTagsMessage.isDisplayed()) {
          System.out.println("\n✅ SUCCESS: 'No tags selected' message is displayed, confirming all tags were removed!");
        }
      } catch (Exception e) {
        System.out.println("\n❌ FAILURE: 'No tags selected' message is not displayed after removing all tags!");
        
        // Check if any tags are still visible
        try {
          String[] tagNames = {"arts", "gaming", "technology", "cooking", "eduLife", 
                               "environment", "healthLife", "sport", "travel"};
          boolean anyTagsStillVisible = false;
          
          System.out.println("\nChecking for any remaining tags:");
          for (String tagName : tagNames) {
            try {
              driver.findElement(By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), '" + tagName + "')]"));
              System.out.println("⚠️ WARNING: Tag '" + tagName + "' is still visible after attempting to remove it!");
              anyTagsStillVisible = true;
            } catch (Exception ex) {
              // This is expected - tag should not be found
              System.out.println("✓ Tag '" + tagName + "' was successfully removed");
            }
          }
          
          if (!anyTagsStillVisible) {
            System.out.println("\n⚠️ NOTE: All tags were successfully removed, but 'No tags selected' message is not displayed.");
          }
        } catch (Exception ex) {
          // Ignore nested exception
        }
      }
      
      System.out.println("\n==== Tag Toggle Functionality Test Complete ====");
      
    } catch (Exception e) {
      System.out.println("Edit Profile test failed: " + e.getMessage());
    }

    // Test Cancel Button 




    // ================================
    // Test 3: Upload Media Test
    // ================================
    try {
      // Navigate to the Profile page
      String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("Navigated to Profile Page for Upload Media test");

      // Click the "edit profile" button to open edit modal
      WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")));
      editProfileButton.click();
      System.out.println("Clicked the 'edit profile' button for Upload Media test.");

      // Click the "Upload Media" button
      WebElement uploadMediaButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Upload Media')]")));
      uploadMediaButton.click();
      System.out.println("Clicked 'Upload Media' button.");

      // Wait for the upload media dialog to appear
      WebElement uploadDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@role, 'dialog')]//h2[contains(text(), 'Upload Media')]")));
      System.out.println("Upload Media dialog appeared successfully.");

      // Check if the file upload option is present
      WebElement fileUploadTab = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Upload File')]")));
      System.out.println("File upload tab is present.");
      
      // Click on the upload file tab
      fileUploadTab.click();
      System.out.println("Clicked on 'Upload File' tab.");

      // Prepare a sample file path to upload
      // Note: This is a mock file path. The file should exist for a real test.
      String mockFilePath = System.getProperty("user.home") + "/test_profile_image.jpg";
      
      // Find the file input element and send the file path
      WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
              By.xpath("//input[@type='file']")));
      fileInput.sendKeys(mockFilePath);
      System.out.println("Selected file for upload: " + mockFilePath);

      // Click the Upload/Submit button
      WebElement submitUploadButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Upload')]")));
      submitUploadButton.click();
      System.out.println("Clicked 'Upload' button to submit the file.");

      // Wait for upload confirmation or success message
      WebElement uploadSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(text(), 'Upload successful') or contains(text(), 'Media updated')]")));
      System.out.println("Upload Media test passed with success message: " + uploadSuccess.getText());

      // Close the upload dialog
      WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='close' or contains(text(), 'Close')]")));
      closeButton.click();
      System.out.println("Closed the Upload Media dialog.");

      // Verify that the profile image has been updated
      WebElement updatedProfilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'MuiAvatar-root')]//img[@alt='Profile']")));
      if (updatedProfilePic.isDisplayed()) {
        System.out.println("Profile picture updated successfully after upload.");
      }

    } catch (Exception e) {
      System.out.println("Upload Media test failed: " + e.getMessage());
      e.printStackTrace();
    }

    // ================================
    // Test 4: Cancel Button Test
    // ================================
    try {
      // Navigate to the Profile page
      String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("Navigated to Profile Page for Cancel button test");

      // Store the original name for comparison later
      WebElement originalNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(@class, 'MuiTypography-root')]")));
      String originalName = originalNameElement.getText();
      System.out.println("Original profile name: " + originalName);

      // Click the "edit profile" button to open edit modal
      WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")));
      editProfileButton.click();
      System.out.println("Clicked the 'edit profile' button for Cancel button test.");

      // Wait for the modal for editing the profile name to appear
      WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")));
      
      // Change the display name to something new
      String tempName = "This Name Should Not Be Saved";
      nameInput.clear();
      nameInput.sendKeys(tempName);
      System.out.println("Entered temporary display name for cancel test: " + tempName);

      // Find and click the Cancel button
      WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Cancel')]")));
      cancelButton.click();
      System.out.println("Clicked 'Cancel' button.");

      // Wait for the edit modal to close
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")));
      System.out.println("Edit profile modal closed after clicking Cancel.");

      // Verify that the profile name remains unchanged
      WebElement nameAfterCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(@class, 'MuiTypography-root')]")));
      String nameAfterCancelText = nameAfterCancel.getText();
      
      if (nameAfterCancelText.equals(originalName)) {
        System.out.println("Cancel button test passed! Name remained unchanged: " + nameAfterCancelText);
      } else {
        System.out.println("Cancel button test failed! Original name: " + originalName + 
                          ", Name after cancel: " + nameAfterCancelText);
      }

    } catch (Exception e) {
      System.out.println("Cancel Button test failed: " + e.getMessage());
      e.printStackTrace();
    }

    // Close the driver after all tests
    driver.quit();
  }
}
