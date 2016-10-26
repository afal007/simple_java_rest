package ru.nsu.fit.endpoint.rest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.glassfish.jersey.internal.util.Base64;

import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.shared.JsonMapper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.StringTokenizer;
import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Path("/rest")
public class RestService {
    @RolesAllowed("ADMIN")
    @POST
    @Path("/create_customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(String customerDataJson) {
        try {
            Customer.CustomerData customerData = JsonMapper.fromJson(customerDataJson, Customer.CustomerData.class);
            DBService.createCustomer(customerData);
            return Response.status(200).entity(customerData.toString()).build();
        } catch (BadCustomerException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
    
    @RolesAllowed("CUSTOMER")
    @POST
    @Path("/create_user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@HeaderParam("Authorization") String creatorCredentialsBase64, String userDataJson){
    	try{
    		User.UserData userData = JsonMapper.fromJson(userDataJson, User.UserData.class);
    		creatorCredentialsBase64 = creatorCredentialsBase64.replaceFirst("Basic ", ""); // get auth property from AuthenticationFilter
    		String creatorCredentials = new String(Base64.decode(creatorCredentialsBase64.getBytes()));
    		StringTokenizer tokenizer = new StringTokenizer(creatorCredentials, ":");
            String username = tokenizer.nextToken();
            System.err.println("Trying to create user");
            System.err.println(creatorCredentialsBase64);
            System.err.println(username);
            UUID customerId;
            if (!username.equals("admin")){
            	customerId = DBService.getCustomerIdByLogin(username);
            }
            else{
            	//f u admin
            	return Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build();
            }
    		DBService.createUser(userData, customerId);
    		return Response.status(200).entity(userData.toString()).build();
    	}
    	catch (BadUserException ex){
    		return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
    	}
    }

    @RolesAllowed("ADMIN")
    @GET
    @Path("/get_customer_id/{customer_login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerId(@PathParam("customer_login") String customerLogin) {
        try {
            UUID id = DBService.getCustomerIdByLogin(customerLogin);
            return Response.status(200).entity("{\"id\":\"" + id.toString() + "\"}").build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
}