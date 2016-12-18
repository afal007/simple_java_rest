package ru.nsu.fit.tests.ui;

import io.codearte.jfairy.Fairy;
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
import ru.nsu.fit.shared.classmock.User;
import ru.nsu.fit.shared.screens.*;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class CreateNewUserTest {
    private static final Logger logger = LoggerFactory.getLogger("UI_TEST_LOGGER");
    private Browser browser = null;
    private Customer testCustomer;
    private User testUser;
    private Fairy testFairy;

    @BeforeClass
    public void beforeClass() {
        testFairy = Fairy.create();
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
    @Title("Create user")
    @Description("Create user via UI API")
    @Severity(SeverityLevel.CRITICAL)
    @Features("User management")
    public void test() {
        createCustomer();
        login();
        createUser();
        checkUser();
    }

    private void init() {
        LoginScreen.init(browser, logger);
        CustomerDashboardScreen.init(browser, logger);
        AddUserScreen.init(browser, logger);
    }

    @Step("Create customer")
    private void createCustomer() {
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

    @Step("Create user")
    private void createUser() {
        CustomerDashboardScreen.clickAddUser();

        logger.debug("Clicked Add user button");

        testUser = new User(
                new User.UserData(
                        testFairy.person().firstName(),
                        testFairy.person().lastName(),
                        testFairy.person().email(),
                        "123StrPass",
                        User.UserData.UserRole.USER),
                UUID.randomUUID(),
                testCustomer.id);

        AddUserScreen.fillFields(testUser.data.firstName, testUser.data.lastName, testUser.data.login, testUser.data.pass, testUser.data.userRole);
        AllureUtils.saveImageAttach("User fields: ", browser.makeScreenshot());

        logger.debug("Filled new customer fields");

        AddUserScreen.addUser();

        logger.debug("Clicked Create new user button");
    }

    @Step("Login")
    private void login() {
        logger.debug("User creation test start");

        LoginScreen.openPage();

        LoginScreen.fillFields(testCustomer.data.login, testCustomer.data.pass);

        logger.debug("Filled login fields");

        AllureUtils.saveImageAttach("Login fields: ", browser.makeScreenshot());
        LoginScreen.login();
    }

    @Step("Check user")
    private void checkUser() {
        UUID failed = new UUID(0L, 0L);
        UUID userId;

        RestService.configAuth(testCustomer.data.login, testCustomer.data.pass);
        Response response = RestService.getUserIdByLogin(testUser.data.login);

        userId = UUID.fromString(response.readEntity(String.class).split(":")[1].replace("\"", "").replace("}", ""));

        Assert.assertNotEquals(userId, failed);
    }
}
