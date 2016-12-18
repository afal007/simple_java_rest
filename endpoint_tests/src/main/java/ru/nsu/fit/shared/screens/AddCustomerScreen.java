package ru.nsu.fit.shared.screens;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import ru.nsu.fit.services.browser.Browser;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class AddCustomerScreen {
    private static Browser browser = null;
    private static Logger logger = null;

    public static void init(Browser _browser, Logger _logger) {
        browser = _browser;
        logger = _logger;
    }

    public static void fillFields(String firstName, String lastName, String login, String pass) {
        browser.waitForElement(By.id("first_name_id"));
        browser.typeText(By.id("first_name_id"), firstName);

        browser.waitForElement(By.id("last_name_id"));
        browser.typeText(By.id("last_name_id"), lastName);

        browser.waitForElement(By.id("email_id"));
        browser.typeText(By.id("email_id"), login);

        browser.waitForElement(By.id("password_id"));
        browser.typeText(By.id("password_id"), pass);
    }

    public static void addCustomer() {
        browser.waitForElement(By.id("create_customer_id"));
        browser.click(By.id("create_customer_id"));
    }
}
