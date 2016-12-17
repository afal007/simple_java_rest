package ru.nsu.fit.tests.ui;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.services.browser.BrowserService;
import ru.nsu.fit.shared.AllureUtils;
import ru.nsu.fit.shared.screens.LoginScreen;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class AdminLoginTest {
    private static final Logger logger = LoggerFactory.getLogger("UI_TEST_LOGGER");
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        org.apache.log4j.BasicConfigurator.configure();
    }

    @AfterClass
    public void afterClass() {
        if (browser != null)
            browser.close();
    }

    @Test
    @Title("Administrator login")
    @Description("Administrator login via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Login")
    public void test() {
        init();
        login();
        checkLogin();
    }

    private void init() {
        LoginScreen.init(browser, logger);
    }

    @Step("Login")
    private void login() {
        logger.debug("Login test start");

        LoginScreen.fillFields("admin", "setup");

        logger.debug("Filled fields");

        AllureUtils.saveImageAttach("Fields: ", browser.makeScreenshot());
        LoginScreen.login();
    }

    @Step("Check login")
    private void checkLogin() {
        browser.waitForElement(By.id("add_new_customer"));

        logger.debug("Page is loaded");

        AllureUtils.saveImageAttach("Admin dashboard: ", browser.makeScreenshot());
        Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/endpoint/customers.html?login=admin&pass=setup&role=ADMIN"));
    }
}
