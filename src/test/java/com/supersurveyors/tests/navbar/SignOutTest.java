package com.supersurveyors.tests.navbar;

//Complete
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignOutTest {
    public static void main(String[] args) {
        // Set the path to your ChromeDriver executable (update as needed)
        // System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");

        // Create a new instance of the ChromeDriver
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Sign in first
        try {
            // Navigate to login page
            String loginUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
            driver.get(loginUrl);
            System.out.println("Navigated to login page: " + loginUrl);

            // Login with test credentials
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys("timothylinziqimc@gmail.com");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys("test12345678!");

            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
            signInButton.click();

            // Wait for redirect to home page
            String expectedHomeUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/home";
            boolean urlChanged = wait.until(ExpectedConditions.urlToBe(expectedHomeUrl));

            if (urlChanged) {
                System.out.println("Sign in successful! Redirected to: " + driver.getCurrentUrl());
            } else {
                System.out.println("Sign in failed. Current URL: " + driver.getCurrentUrl());
                driver.quit();
                return;
            }

        } catch (Exception e) {
            System.out.println("Login test encountered an error: " + e.getMessage());
            e.printStackTrace();
            driver.quit();
            return;
        }

        // ================================
        // Test 1: NavBar Navigation Test
        // ================================
        try {
            // Navigate to the View page first
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/view");
            System.out.println("Navigated to View page");
            
            // Verify we're on the View page
            WebElement viewPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                     By.xpath("//*[contains(text(), 'Your Surveys')]")));
            if (viewPageTitle.isDisplayed()) {
                System.out.println("View page loaded successfully");
            }

            Thread.sleep(1000);
            WebElement avatarImg = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//img[contains(@class,'MuiAvatar-img')]")
                    )
            );
            avatarImg.click();

            // 2. Now the dropdown is visible—click the menu item you want
            //    (e.g. “Profile” or “Logout”; replace the text below accordingly)
            WebElement profileOption = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//li[normalize-space(.)='Sign Out']")
                    )
            );
            profileOption.click();

            
            // Verify navigation to login page
            wait.until(ExpectedConditions.urlContains("/login"));
           
            Thread.sleep(1000);
            
            System.out.println("Sign out test completed successfully!");

        } catch (Exception e) {
            System.out.println("Sign out test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the browser window
            driver.quit();
            System.out.println("Test complete. Browser closed.");
        }
    }
}
