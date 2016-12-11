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
import ru.nsu.fit.shared.classmock.Plan;
import ru.nsu.fit.shared.classmock.Subscription;
import ru.nsu.fit.shared.classmock.User;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
@Title("Customer Add User To Subscription Test")
public class CustomerAddUserToSubscriptionTest {
    private static final String PLAN_TEMPLATE = "{\n" +
            "\t\"name\":\"%s\",\n" +
            "    \"details\":\"%s\",\n" +
            "    \"maxSeats\":%s,\n" +
            "    \"feePerUnit\":%s,\n" +
            "    \"cost\":%s\n" +
            "}";

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

    private Plan testPlan;
    private Customer testCustomer;
    private User testUser;
    private String subscriptionId;

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
    @Title("Customer subscribe user")
    @Description("Add user to subscription as customer via REST API")
    @Severity(SeverityLevel.CRITICAL)
    @Features("User management")
    @Stories("Subscribe user")
    public void test() {
        authorize("admin", "setup");
        createPlan();
        createCustomer();
        authorize(testCustomer.data.login, testCustomer.data.pass);
        buyPlan();
        createUser();
        String response = addUserToSubscription();
        check(response);
    }

    @Step("Auth")
    private void authorize(String login, String password) {
        rest.configAuth(login, password);
    }

    @Step("Add plan")
    private void createPlan() {
        testPlan = new Plan(
                new Plan.PlanData(
                        testFairy.company().name().replaceAll("[^A-Za-z0-9]", ""),
                        testFairy.textProducer().sentence(),
                        testFairy.baseProducer().randomBetween(10, 100),
                        testFairy.baseProducer().randomBetween(100, 500),
                        testFairy.baseProducer().randomInt(1000)),
                UUID.randomUUID());

        Response response = rest.createPlan(
                String.format(
                        PLAN_TEMPLATE,
                        testPlan.data.name,
                        testPlan.data.details,
                        testPlan.data.maxSeats,
                        testPlan.data.feePerUnit,
                        testPlan.data.cost));

        String responseText = response.readEntity(String.class);
        testPlan.id = UUID.fromString(responseText);

        AllureUtils.saveTextLog("Response", responseText);
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

    @Step("Buy plan")
    private void buyPlan() {
        Response response = rest.buyPlan(testPlan.id.toString());
        subscriptionId = response.readEntity(String.class);

        AllureUtils.saveTextLog("Response", subscriptionId);
    }

    @Step("Buy plan")
    private String addUserToSubscription() {
        Response response = rest.subscribeUser(testUser.id.toString(), subscriptionId);
        String responseText = response.readEntity(String.class);

        AllureUtils.saveTextLog("Response", responseText);
        return responseText;
    }

    @Step("Check response")
    public void check(String strResponse) {
        Assert.assertEquals(strResponse,
                "Succesfully assigned user " + testUser.id.toString() + " to subscription " + subscriptionId.toString());
    }
}
