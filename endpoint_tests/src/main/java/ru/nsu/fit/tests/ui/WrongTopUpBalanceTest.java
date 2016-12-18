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
import ru.nsu.fit.shared.screens.BalanceScreen;
import ru.nsu.fit.shared.screens.CustomerDashboardScreen;
import ru.nsu.fit.shared.screens.LoginScreen;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class WrongTopUpBalanceTest {
    private static final Logger logger = LoggerFactory.getLogger("UI_TEST_LOGGER");
    private Browser browser = null;
    private Customer testCustomer;
    private Integer firstBalance;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        org.apache.log4j.BasicConfigurator.configure();
        init();
    }

    @AfterClass
    public void afterClass() {
        if (browser != null)
            browser.close();
    }

    @Test
    @Title("Wrong top up balance")
    @Description("Wrong top up balance via UI API")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Balance management")
    public void test() {
        createCustomer();
        login();
        topUpBalance();
        checkBalance();
    }

    private void init() {
        LoginScreen.init(browser, logger);
        CustomerDashboardScreen.init(browser, logger);
        BalanceScreen.init(browser, logger);
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

    @Step("Top up balance")
    private void topUpBalance() {
        CustomerDashboardScreen.clickBalance();

        logger.debug("Clicked Balance button");

        firstBalance = BalanceScreen.getBalance();
        BalanceScreen.fillBalance(-100);

        AllureUtils.saveImageAttach("Balance field: ", browser.makeScreenshot());

        logger.debug("Filled balance field");

        BalanceScreen.accept();

        logger.debug("Clicked Accept button");
    }

    @Step("Login")
    private void login() {
        logger.debug("Top up balance test start");

        LoginScreen.openPage();

        LoginScreen.fillFields(testCustomer.data.login, testCustomer.data.pass);

        logger.debug("Filled login fields");

        AllureUtils.saveImageAttach("Login fields: ", browser.makeScreenshot());
        LoginScreen.login();
    }

    @Step("Check balance")
    private void checkBalance() {
        browser.waitForElement(By.className("validation"));
        AllureUtils.saveImageAttach("Balance: ", browser.makeScreenshot());

        Assert.assertTrue(BalanceScreen.isValidationPresent());
    }
}
