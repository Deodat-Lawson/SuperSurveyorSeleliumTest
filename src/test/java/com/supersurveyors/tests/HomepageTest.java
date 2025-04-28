//Completed

package com.supersurveyors.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class HomepageTest {
  public static void main(String[] args) {

    // System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
    WebDriver driver = new ChromeDriver();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Home page
    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/";
    driver.get(url);
    System.out.println("\n==== TEST: Homepage Elements Verification ====");
    System.out.println("→ Navigated to homepage: " + url);

    //Title
    // Verify that the page title is "SuperSurveyors"
    String expectedTitle = "SuperSurveyors";
    String actualTitle = driver.getTitle();
    if (expectedTitle.equals(actualTitle)) {
      System.out.println("✓ Title verification passed: " + actualTitle);
    } else {
      System.out.println("❌ Title verification failed: expected '" + expectedTitle + "', but got '" + actualTitle + "'");
    }

    // Check for the main header element that should contain "SuperSurveyors"
    try {
      WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h1[contains(text(), 'SuperSurveyors')]")));
      if (header.isDisplayed()) {
        System.out.println("✓ Header element verification passed");
      }
    } catch (Exception e) {
      System.out.println("❌ Header element verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking navigation buttons --");
    
    //Get Started Button
    try {
      WebElement getStartedBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Get Started')]")));
      if (getStartedBtn.isDisplayed()) {
        System.out.println("✓ 'Get Started' button is displayed");
      }
    } catch (Exception e) {
      System.out.println("❌ 'Get Started' button verification failed: " + e.getMessage());
    }

    //Sign In Button
    try {
      WebElement signInBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Sign In')]")));
      if (signInBtn.isDisplayed()) {
        System.out.println("✓ 'Sign In' button is displayed");
      }
    } catch (Exception e) {
      System.out.println("❌ 'Sign In' button verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Checking content sections --");
    
    // Platform Benefits Section
    try {
      WebElement benefitsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Platform Benefits')]")));
      if (benefitsSection.isDisplayed()) {
        System.out.println("✓ 'Platform Benefits' section is displayed");
      }
    } catch (Exception e) {
      System.out.println("❌ 'Platform Benefits' section verification failed: " + e.getMessage());
    }

    // Features section
    try {
      WebElement featuresHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Features')]")));
      if (featuresHeader.isDisplayed()) {
        System.out.println("✓ 'Features' section is displayed");
      }
    } catch (Exception e) {
      System.out.println("❌ 'Features' section verification failed: " + e.getMessage());
    }

    // How it works section
    try {
      WebElement howItWorksHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'How It Works')]")));
      if (howItWorksHeader.isDisplayed()) {
        System.out.println("✓ 'How It Works' section is displayed");
      }
    } catch (Exception e) {
      System.out.println("❌ 'How It Works' section verification failed: " + e.getMessage());
    }

    // Verify that the "Our Team" section is present.
    try {
      WebElement ourTeamHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Our Team')]")));
      if (ourTeamHeader.isDisplayed()) {
        System.out.println("✓ 'Our Team' section is displayed");
      }
    } catch (Exception e) {
      System.out.println("❌ 'Our Team' section verification failed: " + e.getMessage());
    }

    System.out.println("\n-- Verifying team members --");
    
    // Team members
    String[] teamMembers = {
            "Larry Cai",
            "Jianwei Chen",
            "Mia Jin",
            "Noah Park",
            "Xin Tan",
            "Jiayi Zhang"
    };

    boolean allTeamMembersPresent = true;
    for (String member : teamMembers) {
      try {
        WebElement teamMemberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[contains(text(), '" + member + "')]")
        ));
        if (teamMemberElement.isDisplayed()) {
          System.out.println("✓ Team member: " + member + " is displayed");
        }
      } catch (Exception e) {
        System.out.println("❌ Team member: " + member + " verification failed: " + e.getMessage());
        allTeamMembersPresent = false;
      }
    }

    if (allTeamMembersPresent) {
      System.out.println("\n✅ SUCCESS: All team members verified successfully!");
    } else {
      System.out.println("\n❌ FAILURE: Some team members failed verification.");
    }

    // ================================
    // Test for Sign In button navigation
    // ================================
    try {
      System.out.println("\n==== TEST: Sign In Button Navigation ====");
      
      // Refresh page to ensure we're starting from the homepage
      driver.get(url);
      System.out.println("→ Navigated back to homepage");
      
      // Locate and click the Sign In button using a more robust approach
      try {
        // Try a more specific XPath that uses class information
        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiButton-root') and contains(text(), 'Sign In')]")));
        
        System.out.println("→ Found 'Sign In' button, clicking...");
        
        // Scroll the button into view before clicking
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signInButton);
        
        // Add a small wait to ensure the button is fully in view
        Thread.sleep(500);
        
        // Click the button
        signInButton.click();
        System.out.println("→ Clicked 'Sign In' button");
      } catch (Exception ex) {
        // If regular click fails, try JavaScript executor as a last resort
        System.out.println("→ Using JavaScript executor to click the 'Sign In' button");
        WebElement element = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign In')]"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        System.out.println("→ Clicked 'Sign In' button using JavaScript");
      }
      
      // Verify navigation to sign in page
      wait.until(ExpectedConditions.urlContains("/login"));
      String expectedSignInUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
      String actualSignInUrl = driver.getCurrentUrl();
      
      if (actualSignInUrl.equals(expectedSignInUrl)) {
        System.out.println("✓ Navigation successful to: " + actualSignInUrl);
      } else {
        System.out.println("❌ Navigation failed. Expected: " + expectedSignInUrl + 
                           ", but got: " + actualSignInUrl);
      }
      
      // Check for sign in page elements
      WebElement signInHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Sign In')]")));
      if (signInHeader.isDisplayed()) {
        System.out.println("✓ Sign In page header is displayed");
        System.out.println("✅ SUCCESS: Sign In button navigation test passed!");
      }
      
    } catch (Exception e) {
      System.out.println("❌ FAILURE: Sign In button navigation test failed: " + e.getMessage());
    }
    
    // ================================
    // Test for Get Started (Sign Up) button navigation
    // ================================
    try {
      System.out.println("\n==== TEST: Get Started Button Navigation ====");
      
      // Refresh page to ensure we're starting from the homepage
      driver.get(url);
      System.out.println("→ Navigated back to homepage");
      
      // Locate and click the Get Started button using a more robust approach
      try {
        // Try a more specific XPath that uses class information
        WebElement getStartedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiButton-root') and contains(text(), 'Get Started')]")));
        
        System.out.println("→ Found 'Get Started' button, clicking...");
        
        // Scroll the button into view before clicking
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getStartedButton);
        
        // Add a small wait to ensure the button is fully in view
        Thread.sleep(500);
        
        // Click the button
        getStartedButton.click();
        System.out.println("→ Clicked 'Get Started' button");
      } catch (Exception ex) {
        // If regular click fails, try JavaScript executor as a last resort
        System.out.println("→ Using JavaScript executor to click the 'Get Started' button");
        WebElement element = driver.findElement(
                By.xpath("//button[contains(text(), 'Get Started')]"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        System.out.println("→ Clicked 'Get Started' button using JavaScript");
      }
      
      // Verify navigation to sign up page
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
        System.out.println("✓ Sign Up page header is displayed");
        System.out.println("✅ SUCCESS: Get Started button navigation test passed!");
      }
      
    } catch (Exception e) {
      System.out.println("❌ FAILURE: Get Started button navigation test failed: " + e.getMessage());
    }

    // ================================
    // Test for GitHub profile links for team members
    // ================================
    try {
      System.out.println("\n==== TEST: GitHub Profile Links ====");
      
      // Refresh page to ensure we're starting from the homepage
      driver.get(url);
      System.out.println("→ Navigated back to homepage");
      
      // Scroll to the team section to ensure elements are visible
      WebElement teamSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Our Team')]")));
      ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", teamSection);
      System.out.println("→ Scrolled to 'Our Team' section");
      
      // Define GitHub usernames and their expected profile URLs
      String[][] githubProfiles = {
        {"larrythelog", "https://github.com/larrythelog"},
        {"jchen362", "https://github.com/jchen362"},
        {"zhengyue4499", "https://github.com/zhengyue4499"},
        {"noahpark101", "https://github.com/noahpark101"},
        {"tanx3036", "https://github.com/tanx3036"},
        {"jiayizhang-evelynn", "https://github.com/jiayizhang-evelynn"}
      };
      
      // Keep track of test results
      boolean allGithubLinksValid = true;
      
      // Test each GitHub link
      for (String[] profile : githubProfiles) {
        String username = profile[0];
        String expectedUrl = profile[1];
        
        try {
          // Find the GitHub username button for the team member
          WebElement githubButton = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("//*[contains(text(), '" + username + "')]")));
          System.out.println("→ Found GitHub button for: " + username);
          
          // Get the href attribute to check if it points to the correct GitHub profile
          // Since this is a single-page application, we need to check the elements rather than clicking
          // which would navigate away from the page
          
          // First, find the parent element that might contain the link information
          WebElement parentElement = githubButton.findElement(By.xpath("./.."));
          String onclick = githubButton.getAttribute("onclick");
          String href = parentElement.getAttribute("href");
          
          // Extract URL information - either from direct href or from onclick handler
          boolean linkPointsToGithub = false;
          
          if (href != null && href.contains("github.com/" + username)) {
            linkPointsToGithub = true;
          } else if (onclick != null && onclick.contains("github.com/" + username)) {
            linkPointsToGithub = true;
          } else {
            // Another approach - check if parent is an anchor tag
            try {
              WebElement anchorParent = githubButton.findElement(By.xpath("./ancestor::a"));
              String anchorHref = anchorParent.getAttribute("href");
              if (anchorHref != null && anchorHref.contains("github.com/" + username)) {
                linkPointsToGithub = true;
              }
            } catch (Exception e) {
              // Parent is not an anchor tag, continue with other checks
            }
          }
          
          if (linkPointsToGithub) {
            System.out.println("✓ GitHub link verified for: " + username);
          } else {
            System.out.println("❌ GitHub link verification FAILED for: " + username + 
                               " - Link does not point to expected GitHub profile");
            allGithubLinksValid = false;
          }
          
        } catch (Exception e) {
          System.out.println("❌ GitHub link verification failed for " + username + ": " + e.getMessage());
          allGithubLinksValid = false;
        }
      }
      
      if (allGithubLinksValid) {
        System.out.println("\n✅ SUCCESS: All GitHub profile links verified successfully!");
      } else {
        System.out.println("\n❌ FAILURE: Some GitHub profile links failed verification.");
      }
      
    } catch (Exception e) {
      System.out.println("❌ FAILURE: GitHub profile links test failed: " + e.getMessage());
      e.printStackTrace();
    }

    // Optional: Pause for a few seconds so you can see the browser before it closes.
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // Handle the exception if needed.
    }

    // Clean up by closing the browser.
    driver.quit();
    System.out.println("\n==== All Homepage Tests Completed ====");
  }
}
