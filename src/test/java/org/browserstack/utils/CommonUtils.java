package org.browserstack.utils;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.*;
import java.net.URL;

public class CommonUtils {

    public static void downloadImage(String imageUrl) throws IOException {
        // Open a connection to the image URL
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        String imageDir = System.getProperty("user.dir") + "\\src\\test\\resources\\" + imageName.substring(0, imageName.indexOf('?'));
        URL url = new URL(imageUrl);
        try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(imageDir)) {
            // reading image from connection
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            // saving image
            while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Image downloaded successfully: " + imageDir);
        }
    }

    public static String translateTextSpanishToEnglish(String text) {
        // Base URL
        RestAssured.baseURI = "https://rapid-translate-multi-traduction.p.rapidapi.com";

        // Create payload
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("from", "es");
        requestParams.addProperty("to", "en");
        requestParams.addProperty("q", text);

        // Send the POST request and get the response
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("x-rapidapi-host", "rapid-translate-multi-traduction.p.rapidapi.com")
                .header("x-rapidapi-key", "2fd895934dmsha7137b777be3630p15c25fjsn9e900f4ce8fa")
                .body(requestParams.toString())
                .when()
                .post("/t");
        if (response.getStatusCode() == 200) {
            return response.getBody().jsonPath().getList("").get(0).toString();
        } else {
            throw new RuntimeException("Post API failed with status: " + response.getStatusCode());
        }
    }
}
