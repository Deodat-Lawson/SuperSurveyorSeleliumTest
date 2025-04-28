# SuperSurveyors Selenium Test Suite

This repository contains Selenium tests for the SuperSurveyors web application. The tests verify the functionality of various widgets and actions within the application.

## Prerequisites

- Java JDK 11 or higher
- Gradle
- Chrome browser
- ChromeDriver (matching your Chrome version)

## Setup

1. Clone this repository
2. Update the ChromeDriver path in the test files to match your local setup:
   ```java
   System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
   ```

## Running Tests

After setting your chrome webdriver, open this up in Intellij, Click the green run button beside each test to run it. 

### Additional Notes:
- The `CreateSurveyTest.java` must have enough coins for us to create a Survey. Ensure you have at least 5 coins when running this test. In the case where coins are not enough, run `AnswerTest.java` to get more coins by sumbitting responses.

- All the Suvery Option Tests require a survey to exist. Run `CreateSurveyTest.java` if a survey does not exist.

## Test Suite Overview

### Authentication Tests
- `SignUpTest.java`: Tests the user registration process
- `SignInTest.java`: Tests the login functionality
- `LoginTest.java`: Alternative login test with different scenarios
- `SignOutTest.java`: Tests the sign-out functionality

### Navigation Tests
- `HomepageTest.java`: Verifies elements on the homepage and navigation to other pages
- `NavBarTest.java`: Tests navigation bar functionality
- `SurveyCardNavigationTest.java`: Tests navigation between survey cards

### User Profile Tests
- `ProfileViewTest.java`: Tests viewing user profiles
- `ProfileEditNameTest.java`: Tests editing user names
- `ProfileEditTagsTest.java`: Tests adding/removing user tags
- `ProfileUploadMediaTest.java`: Tests uploading profile media

### Survey Management Tests
- `CreateSurveyTest.java`: Tests survey creation functionality
- `AnswerTest.java`: Tests answering surveys
- `SurveyListViewTest.java`: Tests viewing survey lists
- `TrendingTest.java`: Tests trending survey section

### Survey Options Tests
- `SurveyCardEditOptionTest.java`: Tests editing survey cards
- `SurveyCardDeleteOptionTest.java`: Tests deleting survey cards
- `SurveyCardShareOptionTest.java`: Tests sharing survey cards
- `SurveyCardViewResultsOptionTest.java`: Tests viewing survey results

### Onboarding Tests
- `OnboardingTest.java`: Tests the user onboarding process

## Utilities

- `TestUtils.java`: Contains helper methods used across multiple tests

## Coverage



## Notes

- These tests are designed to run against the deployed application at: https://jhu-oose-f24.github.io/Team-SuperSurveyors/
- Some tests require authentication - they will automatically log in with test credentials
- Tests print detailed logs to the console showing which checks passed or failed 