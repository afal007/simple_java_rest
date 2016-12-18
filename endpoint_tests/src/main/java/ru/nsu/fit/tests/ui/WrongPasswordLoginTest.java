package ru.nsu.fit.tests.ui;

import io.codearte.jfairy.Fairy;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.services.browser.BrowserService;
import ru.nsu.fit.services.data.CustomerService;
import ru.nsu.fit.services.data.exceptions.CustomerServiceException;
import ru.nsu.fit.shared.AllureUtils;
import ru.nsu.fit.shared.classmock.Customer;
import ru.nsu.fit.shared.screens.LoginScreen;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class WrongPasswordLoginTest {
    private static final Logger logger = LoggerFactory.getLogger("UI_TEST_LOGGER");
    private Browser browser = null;
    private Customer testCustomer;

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
    @Title("Wrong password login")
    @Description("Wrong password login via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Login")
    public void test() {
        init();
        createCustomer();
        login();
        checkLogin();
    }

    private void init() {
        LoginScreen.init(browser, logger);
    }

    @Step("Create customer")
    private void createCustomer() {
        Fairy testFairy = Fairy.create();

        testCustomer = new Customer(
                new Customer.CustomerData(
                        testFairy.person().firstName(),
                        testFairy.person().lastName(),
                        testFairy.person().email(),
                        "123StrPass",
                        10000),
                UUID.randomUUID());

        try {
            CustomerService.createCustomer(
                    testCustomer.data.firstName,
                    testCustomer.data.lastName,
                    testCustomer.data.login,
                    testCustomer.data.pass,
                    testCustomer.data.money);
        } catch (CustomerServiceException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Login")
    private void login() {
        logger.debug("Wrong password login test start");

        LoginScreen.openPage();

        LoginScreen.fillFields(testCustomer.data.login, "1234");

        logger.debug("Filled fields");

        AllureUtils.saveImageAttach("Fields: ", browser.makeScreenshot());
        LoginScreen.login();
    }

    @Step("Check login")
    private void checkLogin() {
        browser.waitForElement(By.className("validation"));

        logger.debug("Page is loaded");

        AllureUtils.saveImageAttach("Wrong password: ", browser.makeScreenshot());
        Assert.assertEquals(browser.getText(By.className("validation")), "Wrong password.");
    }
}
