package org.browserstack.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebUtils {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> waitThread = new ThreadLocal<>();

    public static boolean isVisible(By locator) {
        try {
            getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void clickOn(By locator) {
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(locator));
            getDriver().findElement(locator).click();
        } catch (Exception e) {
            Assert.fail("Fail to click: " + e.getMessage());
        }
    }

    public static void jsClick(By locator) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            WebElement element = getDriver().findElement(locator);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Assert.fail("Fail to click: " + e.getMessage());
        }
    }

    public static boolean verifyText(By locator, String value) {
        try {
            getWait().until(ExpectedConditions.textToBe(locator, value));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void openApplication(String url, String title) {
        try {
            // setting driver and wait based on browser
            setDriverAndWait(10);
            // opening web page
            getDriver().get(url);
            // maximizing browser
            //getDriver().manage().window().maximize();
            // wait for page to load
            getWait().until(ExpectedConditions.and(
                    ExpectedConditions.urlToBe(url),
                    ExpectedConditions.titleIs(title)));
        } catch (Exception e) {
            Assert.fail("Failed to open web page: " + e.getMessage());
        }
    }


    public static void closeApplication() {
        WebDriver driverInstant = driverThread.get();
        if (driverInstant != null) {
            driverInstant.close();
            driverThread.remove();
        }
    }


    // Driver and Wait's Getter Setter
    public static void setDriverAndWait(int seconds) {
        String browser = System.getProperty("browser");
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                driverThread.set(new ChromeDriver());
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                driverThread.set(new FirefoxDriver());
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                driverThread.set(new EdgeDriver());
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        WebDriverWait webDriverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
        waitThread.set(webDriverWait);
    }

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            throw new IllegalStateException("Driver is not initialized");
        }
        return driverThread.get();
    }

    public static WebDriverWait getWait() {
        if (waitThread.get() == null) {
            throw new IllegalStateException("Wait is not initialized");
        }
        return waitThread.get();
    }

//    @Before()
//    @SuppressWarnings("unchecked")
//    public void setUp() throws Exception {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("start-maximized");
//        driverThread.set(new ChromeDriver(options));
//    }
//
//    @After()
//    public void tearDown() throws Exception {
//        getDriver().quit();
//    }
}