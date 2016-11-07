package ru.nsu.fit.tests;

import io.codearte.jfairy.Fairy;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.nsu.fit.shared.AllureUtils;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import ru.nsu.fit.services.rest.RestService;;
/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Title("Build Verification Test")
public class BuildVerificationTest {
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
    
    private static final String PLAN_TEMPLATE = "{\n"+
    		"\"name\": \"%s\",\n"+
    		"\"details\": \"%s\",\n"+
    		"\"maxSeats\": %s,\n"+
    		"\"minSeats\": %s,\n"+
    		"\"feePerUnit\": %s,\n"+
    		"\"cost\":%s\n"+
"}";

    @Parameter("Created customer")
    Customer testCustomer = null;

    @Parameter("Created user")
    User testUser = null;
    
    @Parameter("Created plan id")
    UUID testPlanId = null;
    
    @Parameter("Created plan id")
    UUID testSubscriptionId = null;


    @Test(groups = "testsWithCustomer")
    @Title("Create customer")
    @Description("Create customer via REST API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Admin feature")
    public void createCustomer() {
        Fairy fairy = Fairy.create();

        testCustomer = new Customer(
                            UUID.randomUUID(),
                            fairy.person().firstName(),
                            fairy.person().lastName(),
                            fairy.person().email(),
                            "123StrPass",
                            10000);

        Response response = rest.configAuth("admin", "setup").createCustomer("{\n" +
                "\t\"firstName\":\"" + testCustomer.firstName + "\",\n" +
                "    \"lastName\":\"" + testCustomer.lastName +"\",\n" +
                "    \"login\":\"" + testCustomer.login +"\",\n" +
                "    \"pass\":\"" + testCustomer.pass + "\",\n" +
                "    \"money\":\"" + testCustomer.money + "\"\n" +
                "}");
        
        Assert.assertEquals(response.getStatus(), 200);
        testCustomer.id = UUID.fromString(response.readEntity(String.class));
        AllureUtils.saveTextLog("Response: " + testCustomer.id.toString());
    }

    @Test(dependsOnMethods = "createCustomer", groups = "testsWithCustomer")
    @Title("Check login")
    @Description("Get customer id by login")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void getCustomerIdByLogin() {
    	Response response = rest
			.configAuth(testCustomer.login, testCustomer.pass)
			.getCustomerIdByLogin(testCustomer.login);
    	
        saveTextLog("Response", response.readEntity(String.class));
        AllureUtils.saveTextLog("Response: " + testCustomer.id);
    }

    @Test(groups = "testsWithCustomer")
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
                        UUID.randomUUID(),
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
                "    \"userRole\":\"" + testUser.userRole + "\"\n" +
                "}", MediaType.APPLICATION_JSON));
        
        testUser.id = UUID.fromString(response.readEntity(String.class));
        Assert.assertEquals(response.getStatus(), 200);
        AllureUtils.saveTextLog("Created test user: " +  testUser.id.toString());
    }
    
    
    @Test(dependsOnMethods = "createCustomer", dependsOnGroups="testsWithCustomer") // last test with testCustomer
    @Title("delete Customer")
    @Description("delete Customer via REST service")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void deleteCustomer() {
        ClientConfig clientConfig = new ClientConfig();
        AllureUtils.saveTextLog("delete customer test: " + testCustomer.id.toString());

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "setup");
        clientConfig.register(feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("delete_customer").path(testCustomer.id.toString());

        Response response =	webTarget.request().delete(Response.class);
        Assert.assertEquals(response.getStatus(), 200);
        AllureUtils.saveTextLog("Response: " + response.readEntity(String.class));
    }
    
    @Test(groups="plan")
    @Title("Create Plan")
    @Description("Create plan via REST API")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Plan feature")
    public void createPlan(){
        ClientConfig clientConfig = new ClientConfig();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "setup");
        clientConfig.register( feature) ;
        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient( clientConfig );
        
        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("create_plan");

        Invocation.Builder invocationBuilder =	webTarget.request();
        
        Response response = invocationBuilder.post(Entity.entity(
        		String.format(PLAN_TEMPLATE, 
        				testFairy.person().firstName(),
        				testFairy.person().fullName(),
        				10, 1, 1, 10)
        		, MediaType.APPLICATION_JSON));
        
        String responseText = response.readEntity(String.class);
        testPlanId = UUID.fromString(responseText);
        
        Assert.assertEquals(response.getStatus(), 200);
        AllureUtils.saveTextLog("Create Plan Response: " + responseText);
    }
    
    @Test(dependsOnMethods = "createCustomer", groups = "testsWithCustomer")
    @Title("Create Plan (Customer)")
    @Description("Create plan via REST API with Customer credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Plan feature")
    public void createPlanAsCustomer(){
        ClientConfig clientConfig = new ClientConfig();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(testCustomer.login, testCustomer.pass);
        clientConfig.register( feature) ;
        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient( clientConfig );
        
        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("create_customer");

        Invocation.Builder invocationBuilder =	webTarget.request();
        
        Response response = invocationBuilder.post(Entity.entity(
        		String.format(PLAN_TEMPLATE, 
        				testFairy.person().firstName(),
        				testFairy.person().fullName(),
        				10, 1, 1, 10)
        		, MediaType.APPLICATION_JSON));
        Assert.assertEquals(response.getStatus(), 401);
        AllureUtils.saveTextLog("Response: " + response.readEntity(String.class));
    }
    
    @Test(dependsOnGroups="plan") // last test with testCustomer
    @Title("delete Plan")
    @Description("delete Plan via REST service")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Plan feature")
    public void deletePlan() {
        ClientConfig clientConfig = new ClientConfig();
        AllureUtils.saveTextLog("delete plan test: " + testPlanId.toString());

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "setup");
        clientConfig.register(feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("delete_plan").path(testPlanId.toString());

        Response response =	webTarget.request().delete(Response.class);
        Assert.assertEquals(response.getStatus(), 200);
        AllureUtils.saveTextLog("Response: " + response.readEntity(String.class));
    }
    
    @Test(dependsOnMethods={"createCustomer", "createPlan"},groups={"subscription", "plan"})
    @Title("buy Plan")
    @Description("Buy Test Plan as Test Customer")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void buyPlan(){
        ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(testCustomer.login, testCustomer.pass);
        clientConfig.register(feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path("buy_plan").path(testPlanId.toString());

        Response response =	webTarget.request().get();
        testSubscriptionId = UUID.fromString(response.readEntity(String.class));
        Assert.assertEquals(response.getStatus(), 200);
        AllureUtils.saveTextLog("Created test subscription: " + testSubscriptionId.toString());
    }
    
    @Test(dependsOnMethods={"createUser", "createPlan"}, groups={"user", "subscription", "plan"})
    @Title("subscribe User to Plan")
    @Description("delete Plan via REST service")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void subscribeUser(){
    	ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(testCustomer.login, testCustomer.pass);
        clientConfig.register(feature) ;

        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest")
        		.path("subscribe_user")
        		.path(testUser.id.toString())
        		.path(testSubscriptionId.toString());

        Response response =	webTarget.request().get();
       
        AllureUtils.saveTextLog("Assign test user: " + response.readEntity(String.class));
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Attachment(value="{0}",type="text/plain")
    public static String saveTextLog(String name, String msg) {
        return msg;
    }
}
