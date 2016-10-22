package ru.nsu.fit.tests;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.testng.annotations.Test;
import ru.nsu.fit.shared.AllureUtils;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Title("BuildVerificationTest")
public class BuildVerificationTest {
    @Test
    @Title("Create customer")
    @Description("Create customer via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Customer feature")
    public void createCustomer() {
        ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "setup");
        clientConfig.register( feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient( clientConfig );

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("create_customer");

        Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity("{\n" +
                "\t\"firstName\":\"Johnds\",\n" +
                "    \"lastName\":\"Weakasd\",\n" +
                "    \"login\":\"helloworld123@login.com\",\n" +
                "    \"pass\":\"password123\",\n" +
                "    \"money\":\"100\"\n" +
                "}", MediaType.APPLICATION_JSON));
        AllureUtils.saveTextLog("Response: " + response.readEntity(String.class));
    }

    @Test(dependsOnMethods = "createCustomer")
    @Title("Check login")
    @Description("Get customer id by login")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void login() {

    }
}
