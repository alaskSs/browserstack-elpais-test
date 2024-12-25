package org.browserstack.locators;

import org.openqa.selenium.By;

public class ElPaisLocators {
    public static By cookieWindow = By.xpath("//div[@id='didomi-notice']");
    public static By cookieAgreeBTN = By.xpath("//div[@id='didomi-notice']//button[@id='didomi-notice-agree-button']");
    public static By languageHeader = By.xpath("//li[@id='edition_head' and @class='ed_a']");
    public static By hamburgerBTN = By.xpath("//button[@id='btn_open_hamburger']");
    public static By hamburgerCloseBTN = By.xpath("//button[@id='btn_toggle_hamburger']");
    public static By hamburgerLanguageOption = By.xpath("//li[@data-edition='el-pais']//a[span[text()='España']]");
    public static By opinionBTN = By.xpath("//a[@cmp-ltrk='portada_menu' and text()='Opinión']");
    public static By opinionHeader = By.xpath("//h1/a[@title='Opinión']");
}