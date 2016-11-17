package ru.nsu.fit.tests;

import io.codearte.jfairy.Fairy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ru.nsu.fit.services.rest.RestService;
import ru.nsu.fit.shared.ClassMockUtils;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class AdminCreateCustomerTest {
    private static final String CUSTOMER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"money\":\"%s\"\n" +
            "}";

    private ClassMockUtils.Customer testCustomer;

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

    private void authorize() {
        rest.configAuth("admin", "setup");
    }

    private void createCustomer() {
        testCustomer = new ClassMockUtils.Customer(
                UUID.randomUUID(),
                testFairy.person().firstName(),
                testFairy.person().lastName(),
                testFairy.person().email(),
                "123StrPass",
                10000);

        Response response = rest.createCustomer(
                String.format(
                    CUSTOMER_TEMPLATE,
                    testCustomer.firstName,
                    testCustomer.lastName,
                    testCustomer.login,
                    testCustomer.pass,
                    testCustomer.money));

        String responseText = response.readEntity(String.class);
        testCustomer.id = UUID.fromString(responseText.split(":")[1].replace("\"",""));
    }

    public void check() {}
}
