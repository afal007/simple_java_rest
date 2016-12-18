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
import ru.nsu.fit.services.rest.RestService;
import ru.nsu.fit.shared.AllureUtils;
import ru.nsu.fit.shared.classmock.Customer;
import ru.nsu.fit.shared.screens.AddCustomerScreen;
import ru.nsu.fit.shared.screens.CustomersScreen;
import ru.nsu.fit.shared.screens.LoginScreen;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class CreateNewCustomerTest {
    private static final Logger logger = LoggerFactory.getLogger("UI_TEST_LOGGER");
    private Browser browser = null;
    private Customer testCustomer;

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
    @Title("Create customer")
    @Description("Create customer via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Customer management")
    public void test() {
        login();
        createCustomer();
        checkCustomer();
    }

    private void init() {
        LoginScreen.init(browser, logger);
        CustomersScreen.init(browser, logger);
        AddCustomerScreen.init(browser, logger);
    }

    @Step("Create customer")
    private void createCustomer() {
        CustomersScreen.clickAddCustomer();

        logger.debug("Clicked Add new customer button");

        Fairy testFairy = Fairy.create();

        testCustomer = new Customer(
                new Customer.CustomerData(
                        testFairy.person().firstName(),
                        testFairy.person().lastName(),
                        testFairy.person().email(),
                        "123StrPass",
                        10000),
                UUID.randomUUID());

        AddCustomerScreen.fillFields(testCustomer.data.firstName, testCustomer.data.lastName, testCustomer.data.login, testCustomer.data.pass);
        AllureUtils.saveImageAttach("Customer fields: ", browser.makeScreenshot());

        logger.debug("Filled new customer fields");

        AddCustomerScreen.addCustomer();

        logger.debug("Clicked Create new customer button");
    }

    @Step("Login")
    private void login() {
        logger.debug("Customer creation test start");

        LoginScreen.openPage();

        LoginScreen.fillFields("admin", "setup");

        logger.debug("Filled login fields");

        AllureUtils.saveImageAttach("Login fields: ", browser.makeScreenshot());
        LoginScreen.login();
    }

    @Step("Check customer")
    private void checkCustomer() {
        UUID failed = new UUID(0L, 0L);
        UUID customerId;

        RestService.configAuth("admin", "setup");
        Response response = RestService.getCustomerIdByLogin(testCustomer.data.login);

        customerId = UUID.fromString(response.readEntity(String.class).split(":")[1].replace("\"", "").replace("}", ""));

        Assert.assertNotEquals(customerId, failed);
    }
}
