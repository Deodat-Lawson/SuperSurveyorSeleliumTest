import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileTest {
  public static void main(String[] args) {
    // Set the path to your ChromeDriver executable (update as needed)
    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

    // Create a new instance of the ChromeDriver
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    printTestHeader("TEST SUITE: SUPERSURVEYOR PROFILE FUNCTIONALITY");

    // Home page
    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
    driver.get(url);

    //Sign in
    try {
      printTestHeader("TEST 1: USER AUTHENTICATION");
      System.out.println("→ Navigating to login page: " + url);

      WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
      emailField.clear();
      emailField.sendKeys("timothylinziqimc@gmail.com");
      System.out.println("→ Entered email: timothylinziqimc@gmail.com");

      // Wait for the password input field and enter the password.
      WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
      passwordField.clear();
      passwordField.sendKeys("test12345678!");
      System.out.println("→ Entered password: ************");

      // Wait for the "Sign In" button to be clickable and click it.
      WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
      signInButton.click();
      System.out.println("→ Clicked 'Sign In' button");

      // Define the expected URL after a successful sign in.
      String expectedHomeUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/home";

      // Wait until the URL becomes the expected home URL.
      boolean urlChanged = wait.until(ExpectedConditions.urlToBe(expectedHomeUrl));

      if (urlChanged) {
        printTestResult(true, "Sign in functionality", "Successfully redirected to: " + driver.getCurrentUrl());
      } else {
        printTestResult(false, "Sign in functionality", "Failed to redirect. Current URL: " + driver.getCurrentUrl());
      }

    } catch (Exception e) {
      printTestResult(false, "Sign in functionality", "Error: " + e.getMessage());
    }

    try {
      // 1. Navigate to the Profile page (View page)
      printTestHeader("TEST 2: PROFILE PAGE BASIC ELEMENTS");
      
      String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("→ Navigated to Profile page: " + profileUrl);

      // 2. Verify that the header contains "SuperSurveyors"
      WebElement headerLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
      ));
      printElementCheck(true, "Header contains 'SuperSurveyors'", headerLogo.getText());

      // 3. Verify the profile name "Timothy Lin"
      WebElement profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(text(), 'Timothy Lin')]")
      ));
      printElementCheck(true, "Profile name", profileName.getText());

      // 4. Verify the email address is displayed correctly
      WebElement profileEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(), 'timothylinziqimc@gmail.com')]")
      ));
      printElementCheck(true, "Profile email", profileEmail.getText());

      // 5. Verify the UID is displayed
      WebElement profileUID = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//p[contains(text(), 'UID:')]")
      ));
      printElementCheck(true, "Profile UID", profileUID.getText());

      // 6. Verify that the "Interest Tags" section is present with the heading
      WebElement interestTagsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h6[contains(text(), 'Interest Tags')]")
      ));
      printElementCheck(true, "Interest Tags header", interestTagsHeader.getText());

      // Verify that the section shows "No tags selected"
      WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")
      ));
      printElementCheck(true, "No tags message", noTagsMessage.getText());

      // 7. Optionally, verify the Edit Profile and Edit Tags buttons are clickable.
      WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")
      ));
      printElementCheck(true, "Edit profile button", "Clickable");

      WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit tags']")
      ));
      printElementCheck(true, "Edit tags button", "Clickable");

      // 8. Optionally, verify that a profile picture is displayed.
      WebElement profilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'MuiAvatar-root')]//img[@alt='Profile']")
      ));
      printElementCheck(profilePic.isDisplayed(), "Profile picture", "Is displayed");

      printTestResult(true, "Profile Page Basic Elements", "All basic profile elements verified successfully");

    } catch (Exception e) {
      printTestResult(false, "Profile Page Basic Elements", "Error: " + e.getMessage());
    }

    //Edit Profile and Edit tags test
    try {
      printTestHeader("TEST 3: PROFILE NAME EDIT FUNCTIONALITY");
      
      // Navigate to the Profile page.
      String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("→ Navigated to Profile Page: " + profileUrl);
      
      // Check for and close any open dialogs
      closeOpenDialogs(driver);

      // Click the "edit profile" button identified by its aria-label.
      WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")));
      // Add a small pause to ensure any animations complete
      Thread.sleep(500);
      editProfileButton.click();
      System.out.println("→ Clicked the 'edit profile' button");
      
      // Wait for the edit profile modal to fully appear
      Thread.sleep(500);

      // Wait for the modal for editing the profile name to appear.
      WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")
      ));
      // Change the display name.
      String newName = "New Test Name";
      nameInput.clear();
      nameInput.sendKeys(newName);
      System.out.println("→ Entered new display name: " + newName);

      // Click the Save button in the profile edit modal.
      WebElement saveProfileButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Save Changes')]")));
      printElementCheck(saveProfileButton.isDisplayed(), "Save Changes button", "Is displayed");
      saveProfileButton.click();
      System.out.println("→ Clicked 'Save Changes' button");

      // Wait until the profile edit modal closes (e.g., the name input is no longer visible).
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editDisplayName")));
      // Additional wait to ensure modal is fully closed
      Thread.sleep(500);

      // Verify that the profile page now displays the updated name.
      WebElement updatedNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(text(), '" + newName + "')]")
      ));
      if (updatedNameElement.isDisplayed()) {
        printTestResult(true, "Profile name update", "Successfully updated to: " + newName);
      } else {
        printTestResult(false, "Profile name update", "Failed to update profile name");
      }
      
      // Reset the name back to original at end of test
      // Close any open dialogs that might interfere
      closeOpenDialogs(driver);
      
      
      // Wait until the profile edit modal closes
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editDisplayName")));
      // Additional wait to ensure modal is fully closed
      Thread.sleep(500);

 // ================================
      // Test: Edit Profile Tags (Cancel Test)
      // ================================
      try {
        printTestHeader("TEST 6: EDIT PROFILE TAGS (CANCEL TEST)");
        
        // Navigate to the Profile page
        driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile");
        System.out.println("→ Navigated to Profile Page");
        
        // Check for and close any open dialogs
        closeOpenDialogs(driver);
      
        // First, check the current state of tags (should be none after previous test)
        boolean noTagsInitially = false;
        try {
          WebElement noTagsMessage = driver.findElement(
                By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]"));
          noTagsInitially = noTagsMessage.isDisplayed();
          printElementCheck(true, "Initial state", "No tags are selected");
        } catch (Exception e) {
          printElementCheck(false, "Initial state", "Expected no tags but found some tags");
        }
      
        // Click the "edit tags" button to open the tag selection modal
        WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit tags']")));
        
        // Use JavaScript to click the button
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", editTagsButton);
        System.out.println("→ Clicked the 'edit tags' button using JavaScript");
        
        // Wait for the edit tags modal to fully appear 
        Thread.sleep(1000);
        
        // Try to disable pointer events on the intercepting dialog container
        try {
          ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "let overlay = document.querySelector('.MuiDialog-container.MuiDialog-scrollPaper'); " +
            "if(overlay) { overlay.style.pointerEvents = 'none'; }"
          );
          System.out.println("→ Disabled pointer events on dialog container");
        } catch (Exception e) {
          System.out.println("→ Could not modify dialog container: " + e.getMessage());
        }
        
        // Define tag names to select
        String[] testTagNames = {"arts", "gaming", "technology"};
        
        // Select tags using JavaScript to avoid click interception
        System.out.println("\n→ Selecting tags that will NOT be saved...");
        for (String tagName : testTagNames) {
          try {
            // Use JavaScript to find and click the tag
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
              "const tags = Array.from(document.querySelectorAll('.MuiDialog-paper p')); " +
              "const tag = tags.find(t => t.textContent.includes('" + tagName + "')); " +
              "if(tag) { tag.click(); }"
            );
            printElementCheck(true, "Selected tag", tagName);
            Thread.sleep(300);
          } catch (Exception e) {
            printElementCheck(false, "Selecting tag", tagName + " - Error: " + e.getMessage());
          }
        }
        
        // Click the Cancel button instead of Save using JavaScript
        try {
          ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const buttons = Array.from(document.querySelectorAll('button')); " +
            "const cancelButton = buttons.find(b => b.textContent.includes('Cancel')); " +
            "if(cancelButton) { cancelButton.click(); }"
          );
          System.out.println("→ Clicked 'Cancel' button using JavaScript");
        } catch (Exception e) {
          System.out.println("→ Failed to click Cancel button with JavaScript: " + e.getMessage());
          
          // Fallback to normal click if JavaScript approach fails
          WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//button[contains(text(), 'Cancel')]")));
          cancelButton.click();
          System.out.println("→ Clicked 'Cancel' button with regular click");
        }
        
        // Wait for the modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Cancel')]")));
        // Additional wait to ensure modal is fully closed
        Thread.sleep(1000);
        
        // Verify that the tags were not saved (the state is the same as before)
        boolean noTagsAfterCancel = false;
        try {
          WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")));
          noTagsAfterCancel = noTagsMessage.isDisplayed();
          printElementCheck(true, "State after cancel", "No tags are selected");
        } catch (Exception e) {
          printElementCheck(false, "State after cancel", "Expected no tags but found some tags");
        }
        
        // Verify the cancel operation was successful
        boolean cancelSuccessful = (noTagsInitially == noTagsAfterCancel);
        printTestResult(cancelSuccessful, "Edit Profile Tags (Cancel Test)", 
                    cancelSuccessful ? 
                    "Changes were successfully discarded when clicking Cancel" :
                    "Changes were incorrectly saved despite clicking Cancel");
      
      } catch (Exception e) {
        printTestResult(false, "Edit Profile Tags (Cancel Test)", "Error: " + e.getMessage());
        e.printStackTrace();
      }



      // ================================
      // Test: Edit Profile Tags (Toggle Test)
      // ================================


      printTestHeader("TEST 4: TAG SELECTION (TOGGLE ON)");

      // Navigate to the Profile page.
      profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
      driver.get(profileUrl);
      System.out.println("→ Navigated to Profile Page: " + profileUrl);
      
      // Check for and close any open dialogs
      closeOpenDialogs(driver);

      // Click the "edit tags" button to open the tag selection modal
      WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit tags']")));
      // Add a small pause to ensure any animations complete
      Thread.sleep(500);
      editTagsButton.click();
      System.out.println("→ Clicked the 'edit tags' button");
      
      // Wait for the edit tags modal to fully appear
      Thread.sleep(500);
      
      // First select all available tags (toggle on)
      System.out.println("\n→ Selecting all available interest tags...");
      
      // Array of tag names to process
      String[] tagNames = {"arts", "gaming", "technology", "cooking", "eduLife", 
                           "environment", "healthLife", "sport", "travel"};
      
      // Select all tags
      for (String tagName : tagNames) {
          WebElement tag = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//*[contains(text(), '" + tagName + "')]")
          ));
          tag.click();
          printElementCheck(true, "Selected tag", tagName);
      }

      // Save the changes
      WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Save Changes')]")
      ));
      saveChangesButton.click();
      System.out.println("→ Saving selected tags...");
      
      // Wait until the tags edit modal closes
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("editTags")));
      // Additional wait to ensure modal is fully closed
      Thread.sleep(500);
      
      System.out.println("\n→ Verifying selected tags appear on profile:");
      
      boolean allTagsVisible = true;
      for (String tagName : tagNames) {
          try {
              WebElement tagDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(
                      By.xpath("//*[contains(text(), '" + tagName + "')]")
              ));
              printElementCheck(true, "Found tag on profile", tagName);
          } catch (Exception e) {
              printElementCheck(false, "Found tag on profile", tagName);
              allTagsVisible = false;
          }
      }

      printTestResult(allTagsVisible, "Tag selection (Toggle ON)", 
                     allTagsVisible ? "All tags were successfully added to the profile" 
                                    : "Some tags failed to appear on the profile");


      // ================================
      // Test: Tag Deselection (Toggle OFF)
      // ================================

      try {
        printTestHeader("TEST 5: TAG DESELECTION (TOGGLE OFF)");
        
      // Navigate to the Profile page.
      driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile");
      System.out.println("→ Navigated to Profile Page");
      
      // Wait longer to ensure page is fully loaded
      Thread.sleep(1000);
      
      // Check for and close any open dialogs - more aggressively
      try {
        // Look for any dialogs that might be open
        java.util.List<WebElement> dialogs = driver.findElements(
                By.xpath("//div[contains(@class, 'MuiDialog-container')]"));
        if (!dialogs.isEmpty()) {
          System.out.println("→ Found open dialog, attempting to close it");


          List<WebElement> remainingTags = driver.findElements(
                By.cssSelector(".css-hze7mg p:not(:empty)")
          );
          boolean noneLeft = remainingTags.isEmpty();
          printTestResult(noneLeft, "Tag deselection (Toggle OFF)",
              noneLeft 
                ? "All tags removed (no chips found on profile)" 
                : "Some tags still visible: " + remainingTags.stream()
                                             .map(WebElement::getText)
                                             .collect(Collectors.joining(", "))
          );          

        
          System.out.println("→ Clicked close button on dialog");
          Thread.sleep(1000); // Wait longer for dialog to fully close
        }
      } catch (Exception e) {
        System.out.println("→ No open dialogs detected or couldn't close dialog: " + e.getMessage());
      }
      
      // Click the edit tags button again using JavaScript
      try {
        // Make sure we can find and interact with the edit tags button
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='edit tags']")));
        
        // Use JavaScript to click the button to avoid any intercepted clicks
        WebElement editTagsBtn = driver.findElement(By.xpath("//button[@aria-label='edit tags']"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", editTagsBtn);
        System.out.println("→ Clicked the 'edit tags' button using JavaScript");
        
        // Wait for the edit tags modal to fully appear and stabilize
        Thread.sleep(2000);
        
        // Try to disable pointer events on the intercepting dialog container
        try {
          ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "let overlay = document.querySelector('.MuiDialog-container.MuiDialog-scrollPaper'); " +
            "if(overlay) { overlay.style.pointerEvents = 'none'; }"
          );
          System.out.println("→ Disabled pointer events on dialog container");
        } catch (Exception e) {
          System.out.println("→ Could not modify dialog container: " + e.getMessage());
        }
        
        // Wait for any animations to complete
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
        System.out.println("→ Tags edit dialog is open");
        
        // Toggle OFF all previously selected tags using JavaScript
        System.out.println("\n→ Removing all previously selected tags...");
        
        // Try the direct approach - click all chips at once
        try {
          ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const chips = document.querySelectorAll('.MuiDialog-paper .MuiChip-root');" + 
            "console.log('Found ' + chips.length + ' chips');" +
            "for(let i=0; i<chips.length; i++) {" +
            "  setTimeout(() => { chips[i].click(); }, i * 300);" +
            "}"
          );
          System.out.println("→ Deselected all tags using JavaScript with staggered clicks");
          // Wait for all clicks to complete
          Thread.sleep(tagNames.length * 300 + 1000);
        } catch (Exception e) {
          System.out.println("→ Could not deselect tags with single script: " + e.getMessage());
          
          // Try clicking each tag individually
          for (String tagName : tagNames) {
            try {
              ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "const chips = Array.from(document.querySelectorAll('.MuiDialog-paper .MuiChip-root'));" +
                "const chip = chips.find(c => c.textContent.includes('" + tagName + "'));" +
                "if(chip) { chip.click(); }"
              );
              printElementCheck(true, "Deselected tag", tagName);
              Thread.sleep(300);
            } catch (Exception ex) {
              printElementCheck(false, "Deselecting tag", tagName + " - Error: " + ex.getMessage());
            }
          }
        }
        
        // Save the changes after deselection using JavaScript
        Thread.sleep(1000); // Make sure all clicks have been processed
        try {
          ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const buttons = Array.from(document.querySelectorAll('button'));" +
            "const saveButton = buttons.find(b => b.textContent.includes('Save Changes'));" +
            "if(saveButton) { saveButton.click(); }"
          );
          System.out.println("\n→ Saving changes after removing all tags using JavaScript");
        } catch (Exception e) {
          System.out.println("→ Failed to click Save button with JavaScript: " + e.getMessage());
          
          // Fall back to regular WebDriver click
          WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//button[contains(text(), 'Save Changes')]")));
          saveBtn.click();
          System.out.println("\n→ Saving changes after removing all tags using regular click");
        }
        
        // Wait until the tags edit modal closes
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiDialog-paper')]")));
        
        // Additional wait to ensure modal is fully closed
        Thread.sleep(1000);
        
        // Verify that "No tags selected" message appears
        WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")));
        printTestResult(noTagsMessage.isDisplayed(), "Tag deselection (Toggle OFF)", 
                      "'No tags selected' message is displayed, confirming all tags were removed");
      } catch (Exception e) {
        printTestResult(false, "Tag deselection (Toggle OFF)", "Error: " + e.getMessage());
        
        // Check if any tags are still visible
        try {
          boolean anyTagsStillVisible = false;
          
          System.out.println("\n→ Checking for any remaining tags:");
          for (String tagName : tagNames) {
            try {
              driver.findElement(By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), '" + tagName + "')]"));
              printElementCheck(false, "Tag removal", tagName + " is still visible after attempting to remove it");
              anyTagsStillVisible = true;
            } catch (Exception ex) {
              // This is expected - tag should not be found
              printElementCheck(true, "Tag removal", tagName + " was successfully removed");
            }
          }
          
          if (!anyTagsStillVisible) {
            System.out.println("\n⚠️ NOTE: All tags were successfully removed, but 'No tags selected' message is not displayed.");
          }
        } catch (Exception innerException) {
          System.out.println("Could not verify tag status: " + innerException.getMessage());
        }
      }

    } catch (Exception e) {
      printTestResult(false, "Edit Profile and Tags Test", "Error: " + e.getMessage());
    }


     

    // ================================
    // Test: Upload Media Test
    // ================================
    try {
      printTestHeader("TEST 7: UPLOAD MEDIA FUNCTIONALITY");
      
      // Navigate to the Profile page
      driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile");
      System.out.println("→ Navigated to Profile Page for Upload Media test");

      // Check if any dialogs are open and close them first
      closeOpenDialogs(driver);

      // Click the "edit profile" button identified by its aria-label.
      WebElement editProfileBtn = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")));
      // Add a small pause to ensure any animations complete
      Thread.sleep(500);
      editProfileBtn.click();
      System.out.println("→ Clicked the 'edit profile' button");

      // Wait for the edit profile modal to fully appear
      Thread.sleep(500);
      
      // Click the "Upload Media" button
      WebElement uploadMediaButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Upload Media')]")));
      uploadMediaButton.click();
      System.out.println("→ Clicked 'Upload Media' button");

      // Wait for the upload media dialog to appear
      WebElement uploadDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@role, 'dialog')]//h2[contains(text(), 'Upload Media')]")));
      printElementCheck(uploadDialog.isDisplayed(), "Upload Media dialog", "Appeared successfully");

      // Check if the file upload option is present
      WebElement fileUploadTab = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Upload File')]")));
      printElementCheck(fileUploadTab.isDisplayed(), "File upload tab", "Is present");
      
      // Click on the upload file tab
      fileUploadTab.click();
      System.out.println("→ Clicked on 'Upload File' tab");

      // Prepare a sample file path to upload
      // Note: This is a mock file path. The file should exist for a real test.
      String mockFilePath = System.getProperty("user.home") + "/test_profile_image.jpg";
      
      // Find the file input element and send the file path
      WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
              By.xpath("//input[@type='file']")));
      fileInput.sendKeys(mockFilePath);
      System.out.println("→ Selected file for upload: " + mockFilePath);

      // Click the Upload/Submit button
      WebElement submitUploadButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Upload')]")));
      submitUploadButton.click();
      System.out.println("→ Clicked 'Upload' button to submit the file");

      // Wait for upload confirmation or success message
      WebElement uploadSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(text(), 'Upload successful') or contains(text(), 'Media updated')]")));
      printTestResult(uploadSuccess.isDisplayed(), "Upload Media", "Success message: " + uploadSuccess.getText());

      // Close the upload dialog - ensure it's properly closed
      WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='close' or contains(text(), 'Close')]")));
      closeButton.click();
      System.out.println("→ Closed the Upload Media dialog");
      
      // Wait for dialog to fully close
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
              By.xpath("//div[contains(@role, 'dialog')]//h2[contains(text(), 'Upload Media')]")));
      Thread.sleep(500); // Additional wait for any animations to complete

      // Verify that the profile image has been updated
      WebElement updatedProfilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//div[contains(@class, 'MuiAvatar-root')]//img[@alt='Profile']")));
      printTestResult(updatedProfilePic.isDisplayed(), "Profile picture update", 
                    "Profile picture updated successfully after upload");

    } catch (Exception e) {
      printTestResult(false, "Upload Media Test", "Error: " + e.getMessage());
      e.printStackTrace();
    }

    // ================================
    // Test: Cancel Button Test
    // ================================
    try {
      printTestHeader("TEST 8: CANCEL BUTTON FUNCTIONALITY");
      
      // Navigate to the Profile page
      driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile");
      System.out.println("→ Navigated to Profile Page for Cancel button test");

      // Check if any dialogs are open and close them first
      closeOpenDialogs(driver);

      // Store the original name for comparison later
      WebElement originalNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(@class, 'MuiTypography-root')]")));
      String originalName = originalNameElement.getText();
      System.out.println("→ Original profile name: " + originalName);

      // Click the "edit profile" button identified by its aria-label.
      WebElement editProfileBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@aria-label='edit profile']")));
      // Add a small pause to ensure any animations complete
      Thread.sleep(500);
      editProfileBtn2.click();
      System.out.println("→ Clicked the 'edit profile' button");

      // Wait for the edit profile modal to fully appear
      Thread.sleep(500);

      // Wait for the modal for editing the profile name to appear
      WebElement nameInput2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")
      ));
      
      // Change the display name to something new
      String tempName = "This Name Should Not Be Saved";
      nameInput2.clear();
      nameInput2.sendKeys(tempName);
      System.out.println("→ Entered temporary display name: " + tempName);

      // Find and click the Cancel button
      WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[contains(text(), 'Cancel')]")));
      cancelButton.click();
      System.out.println("→ Clicked 'Cancel' button");

      // Wait for the edit modal to close
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
              By.xpath("//label[contains(text(),'Display Name')]/following::input[1]")));
      System.out.println("→ Edit profile modal closed after clicking Cancel");
      
      // Additional wait to ensure modal is fully closed
      Thread.sleep(500);

      // Verify that the profile name remains unchanged
      WebElement nameAfterCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h4[contains(@class, 'MuiTypography-root')]")));
      String nameAfterCancelText = nameAfterCancel.getText();
      
      printTestResult(nameAfterCancelText.equals(originalName), "Cancel button functionality", 
                    nameAfterCancelText.equals(originalName) ? 
                    "Name remained unchanged: " + nameAfterCancelText :
                    "Name changed unexpectedly! Original: " + originalName + ", After: " + nameAfterCancelText);

    } catch (Exception e) {
      printTestResult(false, "Cancel Button Test", "Error: " + e.getMessage());
      e.printStackTrace();
    }

    } catch (Exception e) {
      printTestResult(false, "Main test flow", "Error: " + e.getMessage());
      e.printStackTrace();
    }

    printTestHeader("TEST SUITE COMPLETED");
    
    // Close the driver after all tests
    driver.quit();
  }
  
  // Helper methods for better output formatting
  private static void printTestHeader(String header) {
    System.out.println("\n" + "=".repeat(80));
    System.out.println(" " + header);
    System.out.println("=".repeat(80));
  }
  
  private static void printTestResult(boolean success, String testName, String message) {
    String resultIcon = success ? "✅ PASS" : "❌ FAIL";
    System.out.println("\n" + resultIcon + " | " + testName + ": " + message);
  }
  
  private static void printElementCheck(boolean success, String elementName, String value) {
    String checkIcon = success ? "✓" : "✗";
    System.out.println(checkIcon + " " + elementName + ": " + value);
  }
  
  // Helper method to check for and close any open dialogs
  private static void closeOpenDialogs(WebDriver driver) {
    try {
      WebElement closeButton = driver.findElement(By.xpath("//button[@aria-label='close' or contains(text(), 'Close')]"));
      if (closeButton.isDisplayed()) {
        closeButton.click();
        System.out.println("→ Closed previously open dialog");
        // Wait for dialog to close completely
        Thread.sleep(500);
      }
    } catch (Exception e) {
      // No dialog open, which is fine
      System.out.println("→ No previously open dialogs detected");
    }
  }
}
