package org.assignment.tests;

import org.assignment.base.BaseTest;
import org.assignment.pages.ELPaisPage;
import org.assignment.utils.HelperMethods;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ELPaisTest extends BaseTest {
    @BeforeMethod
    @Parameters({"browser", "os", "osVersion", "browserVersion", "buildName"})
    public void setup(String browser, String os, String osVersion, String browserVersion, String buildName) throws MalformedURLException {
        setupDriver(browser, os, osVersion, browserVersion, buildName);
    }

    @Test
    public void elPaisTest() throws InterruptedException {

        // Navigate to the Opinion section of the website.
        driver.get("https://elpais.com/opinion/");
        Thread.sleep(5000);
        HelperMethods.handlePopup(driver);

        // Fetch the first five articles in this section.
        List<WebElement> articles = driver.findElements(ELPaisPage.FIRST_5_ARTICLES);
        List<String> translatedTitles = new ArrayList<>();

        for (WebElement article : articles) {
            // Print the title and content of each article in Spanish.
            WebElement titleElement = article.findElement(ELPaisPage.ARTICLE_TITLE);
            String title = titleElement.getText();
            System.out.println("Title: " + title);

            WebElement paragraph = article.findElement(ELPaisPage.PARAGRAPHS);
            System.out.println("Content: " + paragraph.getText());

            // If available, download and save the cover image of each article to your local machine.
            List<WebElement> figures = article.findElements(ELPaisPage.IMAGE);
            if (!figures.isEmpty()) {
                String imageUrl = figures.get(0).getDomAttribute("src");
                HelperMethods.downloadImage(imageUrl);
            } else {
                System.out.println("No cover image available for this article!");
            }

            // Translate the title of each article to English.
            String translatedTitle = HelperMethods.translateText(title);
            // Print the translated headers.
            System.out.println("Translated Title: " + translatedTitle);

            // For Analyzing
            translatedTitles.add(translatedTitle);
        }
        // Analyze Translated Headers
        HelperMethods.analyzeWords(translatedTitles);
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();
    }
}

