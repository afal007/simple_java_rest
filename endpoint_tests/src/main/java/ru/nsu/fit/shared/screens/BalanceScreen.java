package ru.nsu.fit.shared.screens;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import ru.nsu.fit.services.browser.Browser;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class BalanceScreen {
    private static Browser browser = null;
    private static Logger logger = null;

    public static void init(Browser _browser, Logger _logger) {
        browser = _browser;
        logger = _logger;
    }

    public static void fillBalance(Integer amount) {
        browser.waitForElement(By.id("amount"));
        browser.typeText(By.id("amount"), amount.toString());
    }

    public static void accept() {
        browser.waitForElement(By.id("accept"));
        browser.click(By.id("accept"));
    }

    public static Integer getBalance() {
        Integer balance;

        browser.waitForElement(By.id("cur_balance"));
        balance = Integer.valueOf(browser.getText(By.id("cur_balance")));

        return balance;
    }

    public static boolean isValidationPresent() {
        browser.waitForElement(By.className("validation"));
        return browser.isElementPresent(By.className("validation"));
    }
}
