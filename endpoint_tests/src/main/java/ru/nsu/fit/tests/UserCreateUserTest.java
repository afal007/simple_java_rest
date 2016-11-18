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
@Title("User Create User Test")
public class UserCreateUserTest {
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
    @Title("User create user")
    @Description("Create user as customer via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Authorization")
    @Stories("User auth")
    public void test() {
        authorize("admin", "setup");
        createCustomer();
        authorize(testCustomer.data.login, testCustomer.data.pass);
        createUser();
        authorize(testUser.data.login, testUser.data.pass);
        String response = createUser();
        check(response);
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
        testCustomer.id = UUID.fromString(strResponse);

        AllureUtils.saveTextLog("Response:", strResponse);
    }

    @Step("Add user")
    private String createUser() {
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

        String strResponse = response.readEntity(String.class);

        if(!strResponse.equals("You cannot access this resource"))
            testUser.id = UUID.fromString(strResponse);

        AllureUtils.saveTextLog("Response:", strResponse);

        return strResponse;
    }

    @Step("Check response")
    public void check(String response) {
        Assert.assertEquals(response, "You cannot access this resource");
    }
}
