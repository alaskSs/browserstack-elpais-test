package org.assignment.utils;

import io.restassured.response.Response;
import org.assignment.pages.ELPaisPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HelperMethods {
    public static void handlePopup(WebDriver driver) {
        try {
            List<WebElement> acceptButton = driver.findElements(ELPaisPage.ACCEPT_BTN);
            if (!acceptButton.isEmpty() && acceptButton.get(0).isDisplayed()) {
                acceptButton.get(0).click();
            }
        } catch (Exception e) {
            Assert.fail("Fail to click on accept button");
        }
    }

    public static void downloadImage(String imageUrl) {
        try {
            if (imageUrl == null) {
                throw new NullPointerException("Image Url is Null");
            }
            String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            String imageDir = System.getProperty("user.dir") + "\\src\\test\\resources\\images\\" + imageName.substring(0, imageName.indexOf('?'));
            URL url = new URL(imageUrl);
            try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(imageDir)) {
                // Reading Image From Connection
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                // Saving Image
                while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println("Image downloaded successfully: " + imageDir);
            } catch (IOException e) {
                Assert.fail(e.getMessage());
            }
        } catch (MalformedURLException | NullPointerException e) {
            Assert.fail(e.getMessage());
        }
    }

    public static String translateText(String text) {
        String requestBody = String.format("{\"from\":\"auto\", \"to\":\"en\", \"text\":\"%s\"}", text);
        Response response = given()
                .header("Content-Type", "application/json")
                .header("x-rapidapi-key", ConfigReader.getProperty("translate.api.key"))
                .header("x-rapidapi-host", ConfigReader.getProperty("translate.api.host"))
                .body(requestBody)
                .when()
                .post(ConfigReader.getProperty("translate.api.url"));
        if (response.getStatusCode() == 200) {
            return response.jsonPath().getString("trans");
        } else {
            throw new RuntimeException("Translation failed: " + response.getBody().asString());
        }
    }

    public static void analyzeWords(List<String> titles) {
        Map<String, Integer> wordCount = new HashMap<>();
        for (String title : titles) {
            for (String word : title.toLowerCase().replaceAll("[.,!?]", "").split(" ")) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
        System.out.println("\nRepeated Words:");
        wordCount.entrySet().stream().filter(entry -> entry.getValue() > 2).forEach(entry ->
                System.out.println("\t" + entry.getKey() + ": " + entry.getValue()));
    }
}
