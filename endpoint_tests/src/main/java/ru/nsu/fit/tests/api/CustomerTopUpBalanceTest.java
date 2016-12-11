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
@Title("Customer Top Up Balance Test")
public class CustomerTopUpBalanceTest {
    private static final String CUSTOMER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"money\":\"%s\"\n" +
            "}";

    private Customer testCustomer;
    private Integer amount;

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
    @Title("Customer top up balance")
    @Description("Top up balance as customer via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Balance management")
    @Stories("Top up balance")
    public void test() {
        authorize("admin", "setup");
        createCustomer();
        authorize(testCustomer.data.login, testCustomer.data.pass);
        topUpBalance();
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
                        testFairy.baseProducer().randomBetween(100, 1000)),
                UUID.randomUUID());

        Response response = rest.createCustomer(
                String.format(
                        CUSTOMER_TEMPLATE,
                        testCustomer.data.firstName,
                        testCustomer.data.lastName,
                        testCustomer.data.login,
                        testCustomer.data.pass,
                        testCustomer.data.money));

        String id = response.readEntity(String.class);
        testCustomer.id = UUID.fromString(id);

        AllureUtils.saveTextLog("Response:", id);
    }

    @Step("Top up balance")
    private void topUpBalance() {
        amount = 1000;//testFairy.baseProducer().randomBetween(100, 1000);
        Response response = rest.topUpBalance(testCustomer.id.toString(), amount);

        AllureUtils.saveTextLog("Response", response.readEntity(String.class));
    }

    @Step("Check user")
    public void check() {
        Response response = rest.getCustomerData(testCustomer.id.toString());
        Customer customer = JsonMapper.fromJson(response.readEntity(String.class), Customer.class);

        Assert.assertEquals(testCustomer.data.money + amount, customer.data.money);
    }
}
