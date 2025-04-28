package com.supersurveyors.tests.profile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static com.supersurveyors.tests.TestUtils.*;

public class ProfileViewTest {

    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        try {
            // Setup WebDriver
            // System.setProperty("webdriver.chrome.driver", "/Users/timothylin/Downloads/chromedriver-mac-arm64 2/chromedriver");
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();

            // Login first (assuming LoginTest isn't run prior)
            driver.get("https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/login");
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys("timothylinziqimc@gmail.com");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys("test12345678!");
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Sign In') and @type='submit']")));
            signInButton.click();
            wait.until(ExpectedConditions.urlContains("#/home"));

            // Test: PROFILE PAGE BASIC ELEMENTS
            printTestHeader("TEST: PROFILE PAGE BASIC ELEMENTS");
            
            String profileUrl = "https://jhu-oose-f24.github.io/Team-SuperSurveyors/#/profile";
            driver.get(profileUrl);
            System.out.println("→ Navigated to Profile page: " + profileUrl);

            // Verify header contains "SuperSurveyors"
            WebElement headerLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//header//h6[contains(text(), 'SuperSurveyors')]")
            ));
            printElementCheck(true, "Header contains 'SuperSurveyors'", headerLogo.getText());

            // Verify profile name 
            WebElement profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h4[contains(text(), 'Timothy Lin')]") // Use expected name or make dynamic
            ));
            printElementCheck(true, "Profile name", profileName.getText());

            // Verify the email address 
            WebElement profileEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(), 'timothylinziqimc@gmail.com')]") // Use expected email
            ));
            printElementCheck(true, "Profile email", profileEmail.getText());

            // Verify the UID 
            WebElement profileUID = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(), 'UID:')]")
            ));
            printElementCheck(true, "Profile UID", profileUID.getText());

            // Verify "Interest Tags" section header
            WebElement interestTagsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h6[contains(text(), 'Interest Tags')]")
            ));
            printElementCheck(true, "Interest Tags header", interestTagsHeader.getText());

            // Verify the section shows "No tags selected" (initial state or after test cleanup)
            WebElement noTagsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'css-hze7mg')]//p[contains(text(), 'No tags selected')]")
            ));
            printElementCheck(true, "No tags message", noTagsMessage.getText());

            // Verify Edit Profile and Edit Tags buttons are clickable
            WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@aria-label='edit profile']")
            ));
            printElementCheck(true, "Edit profile button", "Clickable");

            WebElement editTagsButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@aria-label='edit tags']")
            ));
            printElementCheck(true, "Edit tags button", "Clickable");

            // Verify profile picture is displayed
            WebElement profilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'MuiAvatar-root')]//img[@alt='Profile']")
            ));
            printElementCheck(profilePic.isDisplayed(), "Profile picture", "Is displayed");

            printTestResult(true, "Profile Page Basic Elements", "All basic profile elements verified successfully");

        } catch (Exception e) {
            printTestResult(false, "Profile Page Basic Elements", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
} 