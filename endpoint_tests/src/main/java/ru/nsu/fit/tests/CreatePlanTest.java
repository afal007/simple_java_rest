package ru.nsu.fit.tests;

import io.codearte.jfairy.Fairy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.nsu.fit.services.rest.RestService;
import ru.nsu.fit.shared.AllureUtils;
import ru.nsu.fit.shared.JsonMapper;
import ru.nsu.fit.shared.classmock.Plan;
import ru.nsu.fit.shared.classmock.Plan;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
@Title("Create Plan Test")
public class CreatePlanTest {
    private static final String PLAN_TEMPLATE = "{\n" +
            "\t\"name\":\"%s\",\n" +
            "    \"details\":\"%s\",\n" +
            "    \"maxSeats\":%s,\n" +
            "    \"feePerUnit\":%s,\n" +
            "    \"cost\":%s\n" +
            "}";

    private Plan testPlan;

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
    @Title("Admin create plan")
    @Description("Create plan as admin via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Plan subscription")
    @Stories("Create plan")
    public void test() {
        authorize();
        createPlan();
        check();
    }

    @Step("Auth")
    private void authorize() {
        rest.configAuth("admin", "setup");
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

    @Step("Check plan")
    public void check() {
        Response response = rest.getPlanData(testPlan.id.toString());
        Plan plan = JsonMapper.fromJson(response.readEntity(String.class), Plan.class);

        Assert.assertEquals(plan.data, testPlan.data);
    }
}
