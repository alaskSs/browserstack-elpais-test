package org.assignment.pages;

import org.openqa.selenium.By;

public class ELPaisPage {
    public static final By ACCEPT_BTN = By.xpath("//button//span[text()='Accept']");
    public static final By FIRST_5_ARTICLES = By.xpath("(//article)[position() <= 5]");
    public static final By ARTICLE_TITLE = By.xpath(".//h2//a");
    public static final By PARAGRAPHS = By.cssSelector("p");
    public static final By IMAGE = By.xpath(".//figure//img");
}
