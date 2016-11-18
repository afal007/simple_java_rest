package ru.nsu.fit.tests;

import io.codearte.jfairy.Fairy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.nsu.fit.services.rest.RestService;
import ru.nsu.fit.shared.AllureUtils;
import ru.nsu.fit.shared.JsonMapper;
import ru.nsu.fit.shared.classmock.Customer;
import ru.nsu.fit.shared.classmock.User;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
@Title("Customer Create User Test")
public class CustomerCreateUserTest {
    private static final String CUSTOMER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"money\":\"%s\"\n" +
            "}";

    private static final String USER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"userRole\":\"%s\"\n" +
            "}";

    private Customer testCustomer;
    private User testUser;

    private Fairy testFairy;
    private RestService rest;
    @BeforeClass
    private void beforeClass(){
        testFairy = Fairy.create();
        rest = new RestService();
    }
    @BeforeMethod
    private void beforeMethod(){
        rest.configAuth("",""); //reset authorization
    }

    @Test
    @Title("Create user")
    @Description("Create user as customer via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Authorization")
    public void test() {
        authorize("admin", "setup");
        createCustomer();
        authorize(testCustomer.data.login, testCustomer.data.pass);
        createUser();
        check();
    }

    @Step("Auth")
    private void authorize(String login, String password) {
        rest.configAuth(login, password);
    }

    @Step("Add customer")
    private void createCustomer() {
        testCustomer = new Customer(
                new Customer.CustomerData(
                        testFairy.person().firstName(),
                        testFairy.person().lastName(),
                        testFairy.person().email(),
                        "123StrPass",
                        10000),
                UUID.randomUUID());

        Response response = rest.createCustomer(
                String.format(
                        CUSTOMER_TEMPLATE,
                        testCustomer.data.firstName,
                        testCustomer.data.lastName,
                        testCustomer.data.login,
                        testCustomer.data.pass,
                        testCustomer.data.money));

        String strResponse = response.readEntity(String.class);

        AllureUtils.saveTextLog("Response:", strResponse);
    }

    @Step("Add user")
    private void createUser() {
        testUser = new User(
                new User.UserData(
                        testFairy.person().firstName(),
                        testFairy.person().lastName(),
                        testFairy.person().email(),
                        "123StrPass",
                        User.UserData.UserRole.USER),
                UUID.randomUUID(),
                testCustomer.id);

        Response response = rest.createUser(
                String.format(
                        USER_TEMPLATE,
                        testUser.data.firstName,
                        testUser.data.lastName,
                        testUser.data.login,
                        testUser.data.pass,
                        testUser.data.userRole));

        String id = response.readEntity(String.class);
        testUser.id = UUID.fromString(id);

        AllureUtils.saveTextLog("Response:", id);
    }

    @Step("Check user")
    public void check() {
        Response response = rest.getUserData(testUser.id.toString());
        User user = JsonMapper.fromJson(response.readEntity(String.class), User.class);

        Assert.assertEquals(testUser.data, user.data);
    }
}
