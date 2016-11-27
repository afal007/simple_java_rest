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
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
@Title("Admin Create Customer Test")
public class AdminCreateCustomerTest {
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
    @Title("Admin create customer")
    @Description("Create customer as admin via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Authorization")
    @Stories("Admin auth")
    public void test() {
        authorize();
        createCustomer();
        check();
    }

    @Step("Auth")
    private void authorize() {
        rest.configAuth("admin", "setup");
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

        String responseText = response.readEntity(String.class);
        testCustomer.id = UUID.fromString(responseText);

        AllureUtils.saveTextLog("Response", responseText);
    }

    @Step("Check customer")
    public void check() {
        Response response = rest.getCustomerData(testCustomer.id.toString());
        Customer customer = JsonMapper.fromJson(response.readEntity(String.class), Customer.class);

        Assert.assertEquals(customer.data.firstName, testCustomer.data.firstName);
        Assert.assertEquals(customer.data.lastName, testCustomer.data.lastName);
        Assert.assertEquals(customer.data.login, testCustomer.data.login);
        Assert.assertEquals(customer.data.pass, testCustomer.data.pass);
        Assert.assertEquals(customer.data.money, testCustomer.data.money);
    }
}
