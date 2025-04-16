import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class HomepageTest {
  public static void main(String[] args) {

    System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
    WebDriver driver = new ChromeDriver();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Home page
    String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/";
    driver.get(url);

    //Title
    // Verify that the page title is "SuperSurveyors"
    String expectedTitle = "SuperSurveyors";
    String actualTitle = driver.getTitle();
    if (expectedTitle.equals(actualTitle)) {
      System.out.println("Title verification passed: " + actualTitle);
    } else {
      System.out.println("Title verification failed: expected '" + expectedTitle + "', but got '" + actualTitle + "'");
    }

    // Check for the main header element that should contain "SuperSurveyors"
    try {
      WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h1[contains(text(), 'SuperSurveyors')]")));
      if (header.isDisplayed()) {
        System.out.println("Header element verification passed!");
      }
    } catch (Exception e) {
      System.out.println("Header element verification failed: " + e.getMessage());
    }

    //Get Started Button
    try {
      WebElement getStartedBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Get Started')]")));
      if (getStartedBtn.isDisplayed()) {
        System.out.println("\"Get Started\" button verification passed!");
      }
    } catch (Exception e) {
      System.out.println("\"Get Started\" button verification failed: " + e.getMessage());
    }

    //Sign In Button
    try {
      WebElement signInBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//button[contains(text(), 'Sign In')]")));
      if (signInBtn.isDisplayed()) {
        System.out.println("\"Sign In\" button verification passed!");
      }
    } catch (Exception e) {
      System.out.println("\"Sign In\" button verification failed: " + e.getMessage());
    }

    // Platform Benefits Section
    try {
      WebElement benefitsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Platform Benefits')]")));
      if (benefitsSection.isDisplayed()) {
        System.out.println("\"Platform Benefits\" section verification passed!");
      }
    } catch (Exception e) {
      System.out.println("\"Platform Benefits\" section verification failed: " + e.getMessage());
    }

    // Features section
    try {
      WebElement featuresHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Features')]")));
      if (featuresHeader.isDisplayed()) {
        System.out.println("\"Features\" section verification passed!");
      }
    } catch (Exception e) {
      System.out.println("\"Features\" section verification failed: " + e.getMessage());
    }

    // How it works section
    try {
      WebElement howItWorksHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'How It Works')]")));
      if (howItWorksHeader.isDisplayed()) {
        System.out.println("\"How It Works\" section verification passed!");
      }
    } catch (Exception e) {
      System.out.println("\"How It Works\" section verification failed: " + e.getMessage());
    }

    // Verify that the "Our Team" section is present.
    try {
      WebElement ourTeamHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[contains(text(), 'Our Team')]")));
      if (ourTeamHeader.isDisplayed()) {
        System.out.println("\"Our Team\" section verification passed!");
      }
    } catch (Exception e) {
      System.out.println("\"Our Team\" section verification failed: " + e.getMessage());
    }

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
          System.out.println(member + " is present.");
        }
      } catch (Exception e) {
        System.out.println("Team member " + member + " verification failed: " + e.getMessage());
        allTeamMembersPresent = false;
      }
    }

    if (allTeamMembersPresent) {
      System.out.println("All team members verified successfully!");
    } else {
      System.out.println("Some team members failed the verification.");
    }


    // Optional: Pause for a few seconds so you can see the browser before it closes.
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // Handle the exception if needed.
    }

    // Clean up by closing the browser.
    driver.quit();
  }
}
