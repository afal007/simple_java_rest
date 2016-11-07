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
	private ClientConfig clientConfig;
    private String username;
    private String password;

    private Client client;
    private WebTarget webTarget;
    
    public RestService(){
    	clientConfig = new ClientConfig();
    	HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().build();
    	clientConfig.register(feature);
    	clientConfig.register(JacksonFeature.class);
    	client = ClientBuilder.newClient( clientConfig );
    	webTarget = client.target("http://localhost:8080/endpoint/rest");
    }
    public RestService configAuth(String username, String password){
    	this.username = username;
    	this.password = password;
    	//webTarget.request()
    	//	.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
    	//	.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password);
    	return this;
    }
	public Response createCustomer(String customerJson){
		Response response = webTarget.path("create_customer").request(MediaType.APPLICATION_JSON)
    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
    		.post(Entity.entity(customerJson, MediaType.APPLICATION_JSON));
		return response;
	}
	
	public Response getCustomerIdByLogin(String login){
		Response response = webTarget
				.path("get_customer_id").path(login).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.get();
		return response;
	}
	
	public Response deleteCustomer(String id){
		Response response = webTarget
				.path("delete_customer").path(id).request()
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
	    		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
	    		.delete(Response.class);
		return response;
	}
}
