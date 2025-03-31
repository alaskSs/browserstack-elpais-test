package org.assignment.base;


import org.assignment.utils.ConfigReader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;
    private static final String BROWSERSTACK_URL = ConfigReader.getProperty("browserstack.url");

    public void setupDriver(String browser, String os, String osVersion, String browserVersion, String buildName) throws MalformedURLException {
        Map<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("osVersion", osVersion);
        browserstackOptions.put("browserVersion", browserVersion);
        browserstackOptions.put("buildName", buildName);

        MutableCapabilities options = switch (browser.toLowerCase()) {
            case "chrome", "chromium" -> {
                if (browser.equalsIgnoreCase("chromium")) {
                    browserstackOptions.put("deviceName", os);
                }
                yield new ChromeOptions();
            }
            case "edge" -> new EdgeOptions();
            case "safari" -> {
                browserstackOptions.put("deviceName", os);
                yield new SafariOptions();
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        options.setCapability("bstack:options", browserstackOptions);
        driver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), options);

        // Common driver settings
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }


    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
