package ru.nsu.fit.tests;

import io.codearte.jfairy.Fairy;
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
import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Title("Build Verification Test")
public class BuildVerificationTest {
    private class Customer {
        UUID id;
        String firstName;
        String lastName;
        String login;
        String pass;
        int money;

        public Customer(UUID id ,String firstName, String lastName, String login, String pass, int money) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.money = money;
        }
    }
    private static class User {
        UUID customerId;
        UUID[] subscriptionIds;
        UUID id;
        String firstName;
        String lastName;
        String login;
        String pass;
        UserRole userRole;

        public User(UUID id, UUID customerId, UUID[] subscriptionIds, String firstName, String lastName, String login, String pass, UserRole userRole) {
            this.id = id;
            this.customerId = customerId;
            this.subscriptionIds = subscriptionIds;
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.userRole = userRole;
        }

        public static enum UserRole {
            COMPANY_ADMINISTRATOR("Company administrator"),
            TECHNICAL_ADMINISTRATOR("Technical administrator"),
            BILLING_ADMINISTRATOR("Billing administrator"),
            USER("User");

            private String roleName;

            UserRole(String roleName) {
                this.roleName = roleName;
            }

            public String getRoleName() {
                return roleName;
            }

            public static UserRole fromString(String text) {
                if (text != null) {
                    for (UserRole b : UserRole.values()) {
                        if (text.equalsIgnoreCase(b.roleName)) {
                            return b;
                        }
                    }
                }
                return null;
            }
        }
    }

    @Parameter("Created customer")
    Customer testCustomer = null;

    @Parameter("Created user")
    User testUser = null;


    @Test
    @Title("Create customer")
    @Description("Create customer via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Admin feature")
    public void createCustomer() {
        ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "setup");
        clientConfig.register( feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient( clientConfig );

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("create_customer");

        Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
        Fairy fairy = Fairy.create();

        testCustomer = new Customer(
                            new UUID(0L, 0L),
                            fairy.person().firstName(),
                            fairy.person().lastName(),
                            fairy.person().email(),
                            "123StrPass",
                            10000);

        Response response = invocationBuilder.post(Entity.entity("{\n" +
                "\t\"firstName\":\"" + testCustomer.firstName + "\",\n" +
                "    \"lastName\":\"" + testCustomer.lastName +"\",\n" +
                "    \"login\":\"" + testCustomer.login +"\",\n" +
                "    \"pass\":\"" + testCustomer.pass + "\",\n" +
                "    \"money\":\"" + testCustomer.money + "\"\n" +
                "}", MediaType.APPLICATION_JSON));
        AllureUtils.saveTextLog("Response: " + response.readEntity(String.class));
    }

    @Test(dependsOnMethods = "createCustomer")
    @Title("Check login")
    @Description("Get customer id by login")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void getCustomerIdByLogin() {
        ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(testCustomer.login, testCustomer.pass);
        clientConfig.register(feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient( clientConfig );

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("get_customer_id/" + testCustomer.login);

        Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        //response looks like {"id":"00000-0000-0000-0000"}
        //testCustomer.id = (UUID.fromString(response.readEntity(String.class).
        //                                            split(":")[1].
        //                                            replace("\"" , "").
        //                                           replace("}" , "")));

        saveTextLog("Response", response.readEntity(String.class));
        AllureUtils.saveTextLog("Response: " + testCustomer.id);
    }

    @Test
    @Title("Create user")
    @Description("Create user via REST API")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void createUser() {
        ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(testCustomer.login, testCustomer.pass);
        clientConfig.register(feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("create_user");

        Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
        Fairy fairy = Fairy.create();
        testUser = new User(
                        new UUID(0L, 0L),
                        testCustomer.id,
                        null,
                        fairy.person().firstName(),
                        fairy.person().lastName(),
                        fairy.person().email(),
                        "123StrPass",
                        User.UserRole.USER);

        Response response = invocationBuilder.post(Entity.entity("{\n" +
                "\t\"firstName\":\"" + testUser.firstName + "\",\n" +
                "    \"lastName\":\"" + testUser.lastName +"\",\n" +
                "    \"login\":\"" + testUser.login +"\",\n" +
                "    \"pass\":\"" + testUser.pass + "\",\n" +
                "    \"role\":\"" + testUser.userRole + "\"\n" +
                "}", MediaType.APPLICATION_JSON));

        AllureUtils.saveTextLog("Response: " + response.readEntity(String.class));
    }

    @Attachment(value="{0}",type="text/plain")
    public static String saveTextLog(String name, String msg) {
        return msg;
    }
}
