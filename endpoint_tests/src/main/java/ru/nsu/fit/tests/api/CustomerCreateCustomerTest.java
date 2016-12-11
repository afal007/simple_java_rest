package ru.nsu.fit.tests.api;

import io.codearte.jfairy.Fairy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.nsu.fit.services.rest.RestService;
import ru.nsu.fit.shared.AllureUtils;
import ru.nsu.fit.shared.JsonMapper;
import ru.nsu.fit.shared.classmock.Customer;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
@Title("Customer Create Customer Test")
public class CustomerCreateCustomerTest {
    private static final String CUSTOMER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"money\":\"%s\"\n" +
            "}";

    private Customer testCustomer;

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
    @Title("Customer create customer")
    @Description("Create customer as customer via REST API")
    @Severity(SeverityLevel.NORMAL)
    @Features("Authorization")
    @Stories("Customer auth")
    public void test() {
        authorize("admin", "setup");
        createCustomer();
        authorize(testCustomer.data.login, testCustomer.data.pass);
        String response = createCustomer();
        check(response);
    }

    @Step("Auth")
    private void authorize(String login, String password) {
        rest.configAuth(login, password);
    }

    @Step("Add customer")
    private String createCustomer() {
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

        return strResponse;
    }

    @Step("Check response")
    public void check(String response) {
        Assert.assertEquals(response, "You cannot access this resource");
    }
}
