package org.browserstack.implementation;

import org.browserstack.locators.ElPaisLocators;
import org.browserstack.utils.CommonUtils;
import org.browserstack.utils.WebUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.*;

public class ElPaisImplementation extends WebUtils {

    public static final List<String> headers = Collections.synchronizedList(new ArrayList<>());
    public static final List<String> translatedHeaders = Collections.synchronizedList(new ArrayList<>());

    public static void openElPaisWebPage() {
        openApplication("https://elpais.com/?ed=es", "EL PAÍS: el periódico global");
        checkCookieAlert();
    }

    public static void checkCookieAlert() {
        if (isVisible(ElPaisLocators.cookieWindow)) {
            clickOn(ElPaisLocators.cookieAgreeBTN);
        }if (isVisible(ElPaisLocators.acceptAndContinueBTN)) {
            clickOn(ElPaisLocators.acceptAndContinueBTN);
        }
    }

    public static void verifyThatPageTextIsSpanish() {
        if (!verifyText(ElPaisLocators.languageHeader, "ESPAÑA")) {
            jsClick(ElPaisLocators.hamburgerBTN);
            jsClick(ElPaisLocators.hamburgerLanguageOption);
            if (isVisible(ElPaisLocators.hamburgerCloseBTN)) {
                clickOn(ElPaisLocators.hamburgerCloseBTN);
            }
            // Verify ESPAÑA is selected
            Assert.assertTrue("España Not selected", verifyText(ElPaisLocators.languageHeader, "ESPAÑA"));
        }
    }

    public static void navigateToOpinionSection() {
        clickOn(ElPaisLocators.opinionBTN);
        Assert.assertTrue("Opinión page not loaded successfully", isVisible(ElPaisLocators.opinionHeader));
    }

    public static void fetchAndSaveArticles() {
        checkCookieAlert();
        synchronized (headers) {
            List<WebElement> articleElements = getDriver().findElements(By.xpath("//article")).subList(0, 5);
            for (WebElement article : articleElements) {
                String title = article.findElement(By.xpath(".//header/h2/a")).getText();
                System.out.println("Title: " + title);
                headers.add(title);
                String content = article.findElement(By.xpath(".//p")).getText();
                System.out.println("Content: " + content);
                try {
                    WebElement figure = article.findElement(By.xpath(".//figure//img"));
                    CommonUtils.downloadImage(Objects.requireNonNull(figure.getDomAttribute("src")));
                } catch (NoSuchElementException e) {
                    System.out.println("No cover image available!");
                } catch (IOException e) {
                    throw new RuntimeException("Fail to save Image" + e.getMessage());
                }
            }
        }
    }

    public static void translateArticleHeaders() {
        synchronized (headers) {
            for (String value : headers) {
                String translated = CommonUtils.translateTextSpanishToEnglish(value);
                System.out.println("Translated Header: " + translated);
                translatedHeaders.add(translated);
            }
        }
    }

    public static void analyzeTranslatedHeaders() {
        synchronized (translatedHeaders) {
            HashMap<String, Integer> occurrences = new HashMap<>();
            String[] allWords = String.join(" ", translatedHeaders).toLowerCase().replaceAll("[^a-zA-Z0-9 /s]", "").split(" ");
            // checking occurrences
            for (String value : allWords) {
                if (occurrences.containsKey(value)) {
                    occurrences.put(value, occurrences.get(value) + 1);
                } else {
                    occurrences.put(value, 1);
                }
            }
            // Printing Repeated words
            occurrences.forEach((key, value) -> {
                if (value > 1) {
                    System.out.println("Repeated Word: " + key + "= " + value);
                }
            });
        }
    }

    public static void closeElPaisWebPage() {
        closeApplication();
    }
}
