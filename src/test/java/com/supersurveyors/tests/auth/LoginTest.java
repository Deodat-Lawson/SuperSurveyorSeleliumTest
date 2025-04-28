//Completed

package com.supersurveyors.tests.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

// Assuming helper methods are accessible or replicated
import static com.supersurveyors.tests.TestUtils.*; 

public class LoginTest {
    
    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            // Setup WebDriver     
            // System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
       
            driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();

            printTestHeader("TEST: USER AUTHENTICATION");
            
            // Navigate to login page
            String url = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login";
            driver.get(url);
            System.out.println("→ Navigating to login page: " + url);

            // Enter credentials
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys("timothylinziqimc@gmail.com");
            System.out.println("→ Entered email: timothylinziqimc@gmail.com");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys("test12345678!");
            System.out.println("→ Entered password: ************");

            // Click Sign In
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
            signInButton.click();
            System.out.println("→ Clicked 'Sign In' button");

            // Verify redirect
            String expectedHomeUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/home";
            boolean urlChanged = wait.until(ExpectedConditions.urlToBe(expectedHomeUrl));

            if (urlChanged) {
                printTestResult(true, "Sign in functionality", "Successfully redirected to: " + driver.getCurrentUrl());
            } else {
                printTestResult(false, "Sign in functionality", "Failed to redirect. Current URL: " + driver.getCurrentUrl());
            }

        } catch (Exception e) {
            printTestResult(false, "Sign in functionality", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
} 