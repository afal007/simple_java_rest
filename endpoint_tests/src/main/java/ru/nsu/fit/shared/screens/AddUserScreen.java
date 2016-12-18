package ru.nsu.fit.shared.screens;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.shared.classmock.User.UserData.UserRole;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class AddUserScreen {
    private static Browser browser = null;
    private static Logger logger = null;

    public static void init(Browser _browser, Logger _logger) {
        browser = _browser;
        logger = _logger;
    }

    public static void addUser() {
        browser.waitForElement(By.id("create_user_id"));
        browser.click(By.id("create_user_id"));
    }

    public static void fillFields(String firstName, String lastName, String login, String pass, UserRole userRole) {
        browser.waitForElement(By.id("first_name_id"));
        browser.typeText(By.id("first_name_id"), firstName);

        browser.waitForElement(By.id("last_name_id"));
        browser.typeText(By.id("last_name_id"), lastName);

        browser.waitForElement(By.id("email_id"));
        browser.typeText(By.id("email_id"), login);

        browser.waitForElement(By.id("password_id"));
        browser.typeText(By.id("password_id"), pass);

        browser.waitForElement(By.id("role_id"));
        Select select = new Select(browser.getElement(By.id("role_id")));
        switch (userRole) {
            case USER:
                select.selectByIndex(0);
                break;
            case COMPANY_ADMINISTRATOR:
                select.selectByIndex(1);
                break;
            case BILLING_ADMINISTRATOR:
                select.selectByIndex(2);
                break;
            case TECHNICAL_ADMINISTRATOR:
                select.selectByIndex(3);
                break;
            default:
                select.selectByIndex(0);
        }
    }
}
