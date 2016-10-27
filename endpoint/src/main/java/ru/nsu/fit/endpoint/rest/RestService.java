package ru.nsu.fit.endpoint.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.glassfish.jersey.internal.util.Base64;

import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadPlanException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.shared.JsonMapper;
import ru.nsu.fit.endpoint.utils.JsonConverter;

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
    
    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/delete_customer/{customer_login}")
    public Response deleteCustomer(@PathParam("customer_login") String customerLogin){
    	try{
    		DBService.deleteCustomer(customerLogin); //TODO add negative response if no customer found
    		return Response.status(200).entity("Customer " + customerLogin + " is now deleted").build();
    	} catch (RuntimeException ex){
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
    @POST
    @Path("create_plan/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlan(String planDataJson){
        try {
            Plan.PlanData planData = JsonMapper.fromJson(planDataJson, Plan.PlanData.class);
            DBService.createPlan(planData);
            return Response.status(200).entity(planData.toString()).build();
        } catch (BadPlanException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
    
    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/delete_plan/{plan_id}")
    public Response deletePlan(@PathParam("plan_id") String planId){
    	try{
    		DBService.deletePlan(planId); //TODO add negative response if no customer found
    		return Response.status(200).entity("Plan " + planId + " is now deleted").build();
    	} catch (RuntimeException ex){
    		return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
    	}
    }

    @RolesAllowed({"ADMIN", "CUSTOMER"})
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

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @GET
    @Path("/get_customer_data/{customer_id}")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerData(@PathParam("customer_id") UUID customerId) {
        try {
            Customer customer = DBService.getCustomerById(customerId);
            String response = JsonConverter.toJSON(customer);

            return Response.status(200).entity(response).build();

        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        } catch (JsonProcessingException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
    
    @RolesAllowed({"USER","CUSTOMER"})
    @GET
    @Path("/get_user_data/{user_id}")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserData(@PathParam("user_id") UUID userId) {
        try {
            User user = DBService.getUserById(userId);
            String response = JsonConverter.toJSON(user);

            return Response.status(200).entity(response).build();

        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        } catch (JsonProcessingException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
    
    @RolesAllowed({"USER", "CUSTOMER"})
    @GET
    @Path("/get_user_id/{user_login}")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserId(@PathParam("user_login") String userLogin) {
        try {
            UUID id = DBService.getUserIdByLogin(userLogin);

            return Response.status(200).entity("{\"id\":\"" + id.toString() + "\"}").build();

        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
}