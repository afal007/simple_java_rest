package ru.nsu.fit.services.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

public class RestService {
	private static ClientConfig clientConfig;
    private static String username;
    private static String password;

    private static Client client;
    private static WebTarget webTarget;
    
    static {
    	clientConfig = new ClientConfig();
    	HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().build();
    	clientConfig.register(feature);
    	clientConfig.register(JacksonFeature.class);
    	client = ClientBuilder.newClient( clientConfig );
    	webTarget = client.target("http://localhost:8080/endpoint/rest");
    }

    public static void configAuth(String _username, String _password){
    	username = _username;
    	password = _password;
    }

    public static Response createCustomer(String customerJson){
		Response response = webTarget.path("create_customer").request(MediaType.APPLICATION_JSON)
    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
    		.post(Entity.entity(customerJson, MediaType.APPLICATION_JSON));
		return response;
	}

	public static Response createUser(String userJson){
		Response response = webTarget.path("create_user").request(MediaType.APPLICATION_JSON)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.post(Entity.entity(userJson, MediaType.APPLICATION_JSON));
		return response;
	}
	
	public static Response getCustomerIdByLogin(String login){
		Response response = webTarget
				.path("get_customer_id").path(login).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.get();
		return response;
	}

	public static Response getUserIdByLogin(String login) {
		Response response = webTarget
				.path("get_user_id").path(login).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.get();
		return response;
	}

	public static Response getCustomerData(String id) {
		Response response = webTarget
				.path("get_customer_data").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.get();
		return response;
	}

	public static Response getUserData(String id) {
		Response response = webTarget
				.path("get_user_data").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.get();
		return response;
	}

	public static Response deleteCustomer(String id){
		Response response = webTarget
				.path("delete_customer").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.delete(Response.class);
		return response;
	}
	
	public static Response createPlan(String planJson){
		Response response = webTarget.path("create_plan").request(MediaType.APPLICATION_JSON)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.post(Entity.entity(planJson, MediaType.APPLICATION_JSON));
		return response;
	}
	
	public static Response deletePlan(String id){
		Response response = webTarget.path("delete_plan").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.delete(Response.class);
		return response;
	}

	public static Response getPlanData(String id) {
		Response response = webTarget
				.path("get_plan_data").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.get();
		return response;
	}

	public static Response getSubscriptionData(String id) {
		Response response = webTarget
				.path("get_subscription_data").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.get();
		return response;
	}

	public static Response buyPlan(String id){
		Response response = webTarget.path("buy_plan").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.get();
		return response;
	}

	public static Response topUpBalance(String customerId, Integer amount){
		Response response = webTarget.path("top_up_balance").path(customerId).path(amount.toString()).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
				.post(Entity.entity("", MediaType.APPLICATION_JSON));
		return response;
	}
	
	public static Response subscribeUser(String userId, String subscriptionId){
		Response response = webTarget.path("subscribe_user").path(userId).path(subscriptionId).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.get();
		return response;
	}
	
	public static Response unsubscribeUser(String userId, String subscriptionId){
		Response response = webTarget.path("unsubscribe_user").path(userId).path(subscriptionId).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.get();
		return response;
	}
}
