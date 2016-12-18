package ru.nsu.fit.shared.screens;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import ru.nsu.fit.services.browser.Browser;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class CustomerDashboardScreen {
    private static Browser browser = null;
    private static Logger logger = null;

    public static void init(Browser _browser, Logger _logger) {
        browser = _browser;
        logger = _logger;
    }

    public static void clickAddUser() {
        browser.waitForElement(By.id("add_user"));
        browser.click(By.id("add_user"));
    }

    public static void clickBalance() {
        browser.waitForElement(By.id("balance"));
        browser.click(By.id("balance"));
    }
}
