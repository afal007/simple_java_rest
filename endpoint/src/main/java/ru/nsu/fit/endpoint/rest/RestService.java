package ru.nsu.fit.endpoint.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.glassfish.jersey.internal.util.Base64;

import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.data.Subscription;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.shared.JsonMapper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
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
    @POST
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

    @RolesAllowed("CUSTOMER")
    @GET
    @Path("/get_customer_data/{customer_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerData(@PathParam("customer_id") UUID customerId) {
        try {
            Customer customer = DBService.getCustomerById(customerId);
            String response = JsonMapper.toJson(customer, true);

            return Response.status(200).entity(response).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        } catch (JsonProcessingException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        } catch (IOException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed("CUSTOMER")
    @POST
    @Path("/top_up_balance/{customer_id}/{amount}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response topUpCustomerBalance(@PathParam("customer_id") UUID customerId, @PathParam("amount") Integer amount) {
        try {
            Integer response = DBService.addMoney(customerId, amount);

            return Response.status(200).entity("money: " + response.toString()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed("CUSTOMER")
    @GET
    @Path("/get_plan_id/{plan_name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlanId(@PathParam("plan_name") String planName) {
        try {
            ArrayList<UUID> id_arr = DBService.getPlanIdByName(planName);
            String response = JsonMapper.toJson(id_arr, true);

            return Response.status(200).entity(response).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        } catch (IOException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed("CUSTOMER")
    @POST
    @Path("/buy_plan/{plan_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyPlanById(@HeaderParam("Authorization") String auth, @PathParam("plan_id") UUID planId) {
        try {
            String login = getLogin(auth);
            UUID customerId = DBService.getCustomerIdByLogin(login);
            Customer customer = DBService.getCustomerById(customerId);
            Plan plan = DBService.getPlanById(planId);

            if(customer.getData().getMoney() < plan.getData().getCost())
                return Response.status(400).entity("Not enough money to complete purchase.").build();

            DBService.createSubscription(customerId, planId, plan.isExternal());

            return Response.status(200).entity("Succesfully purchased plan " + planId.toString()).build();
        } catch (IllegalArgumentException ex) {
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


    private String getLogin(String auth) {
        String login = auth.split(" ")[1];

        return  Base64.decodeAsString(login).split(":")[0];
    }
}