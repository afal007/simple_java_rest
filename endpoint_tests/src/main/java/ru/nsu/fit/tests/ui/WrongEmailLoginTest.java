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
public class WrongEmailLoginTest {
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
    @Title("Wrong email login")
    @Description("Wrong email login via UI API")
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
        logger.debug("Wrong email login test start");

        LoginScreen.openPage();

        LoginScreen.fillFields("email", "password");

        logger.debug("Filled fields");

        AllureUtils.saveImageAttach("Fields: ", browser.makeScreenshot());
        LoginScreen.login();
    }

    @Step("Check login")
    private void checkLogin() {
        browser.waitForElement(By.className("validation"));

        logger.debug("Page is loaded");

        AllureUtils.saveImageAttach("Wrong e-mail: ", browser.makeScreenshot());
        Assert.assertEquals(browser.getText(By.className("validation")), "Wrong e-mail address.");
    }
}
