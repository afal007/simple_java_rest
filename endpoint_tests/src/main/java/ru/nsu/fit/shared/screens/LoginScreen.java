package ru.nsu.fit.shared.screens;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import ru.nsu.fit.services.browser.Browser;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class LoginScreen {

    private static Browser browser = null;
    private static Logger logger = null;

    public static void init(Browser _browser, Logger _logger) {
        browser = _browser;
        logger = _logger;
    }

    public static void fillFields (String login, String password) {
        browser.openPage("http://localhost:8080/endpoint");
        browser.waitForElement(By.id("email"));

        browser.typeText(By.id("email"), "admin");
        //browser.getElement(By.id("email")).sendKeys("admin");
        browser.typeText(By.id("password"), "setup");
        //browser.getElement(By.id("password")).sendKeys("setup");
    }

    public static void login () {
        browser.waitForElement(By.id("login"));
        browser.click(By.id("login"));
        //browser.getElement(By.id("login")).click();

        logger.debug("Clicked \"Login\" button");
    }
}
