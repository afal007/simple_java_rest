package ru.nsu.fit.endpoint.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.glassfish.jersey.internal.util.Base64;

import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.data.Subscription.SubscriptionData;
import ru.nsu.fit.endpoint.service.database.data.Subscription;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadPlanException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadSubscriptionException;
import ru.nsu.fit.endpoint.shared.JsonMapper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
    @Path("/create_plan")
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
    @Path("/delete_customer/{customer_login}")
    public Response deleteCustomer(@PathParam("customer_login") String customerLogin){
    	try{
    		DBService.deleteCustomer(customerLogin); //TODO add negative response if no customer found
    		return Response.status(200).entity("Customer " + customerLogin + " is now deleted").build();
    	} catch (RuntimeException ex){
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerData(@PathParam("customer_id") UUID customerId) {
        try {

            //TODO: Check if customer is allowed to fetch data for this id
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

    @RolesAllowed({"USER", "CUSTOMER"})
    @GET
    @Path("/get_user_id/{user_login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserId(@PathParam("user_login") String userLogin) {
        try {
            UUID id = DBService.getUserIdByLogin(userLogin);

            return Response.status(200).entity("{\"id\":\"" + id.toString() + "\"}").build();

        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed({"USER","CUSTOMER"})
    @GET
    @Path("/get_user_data/{user_id}")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserData(@PathParam("user_id") String userId) {
        try {
            User user = DBService.getUserById(userId);
            String response = JsonMapper.toJson(user, true);

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
            Integer response = DBService.updateCustomerMoney(customerId, amount);

            return Response.status(200).entity("money: " + response.toString()).build();
        } catch (IllegalArgumentException ex) {
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
            
            if(DBService.isPlanOwned(customerId.toString(), planId.toString()))
            	return Response.status(400).entity("Plan is already owned by this customer!").build();

            if(customer.getData().getMoney() < plan.getData().getCost())
                return Response.status(400).entity("Not enough money to complete purchase.").build();

            DBService.createSubscription(customerId, planId, plan.isExternal());
            DBService.updateCustomerMoney(customerId, - plan.getData().getCost());

            return Response.status(200).entity("Succesfully purchased plan " + planId.toString()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed("CUSTOMER")
    @PUT
    @Path("/subscribe_user/") //check customer's funds. if enough, subscribe user
    public Response subscribeUser(@HeaderParam("Authorization") String auth, @QueryParam("userId") String userId, @QueryParam("subscriptionId") String subscriptionId){
    	try{
    		String login = getLogin(auth);
            UUID customerId = DBService.getCustomerIdByLogin(login);
            Customer customer = DBService.getCustomerById(customerId);
            Subscription subscription = DBService.getSubscriptionById(subscriptionId);
            Plan plan = DBService.getPlanById(subscription.getServicePlanId());
            if(subscription.getData().getStatus() == SubscriptionData.Status.PROVISIONING)
            	return Response.status(Status.CONFLICT).entity("Can't assign provisioning subscriptions!").build();
            if(subscription.getData().getUsedSeats() >= plan.getData().getMaxSeats())
                return Response.status(400).entity("Subscription is full!").build();
            if(customer.getData().getMoney() < plan.getData().getFeePerUnit())
            	return Response.status(Status.EXPECTATION_FAILED).entity("Not enough money").build();
            DBService.updateCustomerMoney(customerId, -plan.getData().getFeePerUnit());
    		DBService.subscribeUser(userId, subscriptionId);
        	return Response.status(200).entity("Succesfully assigned user " + userId.toString() + " to subscription " + subscriptionId.toString()).build();
    	}catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
    
    @RolesAllowed("CUSTOMER")
    @PUT
    @Path("/unsubscribe_user/")
    public Response unsubscribeUser(@QueryParam("userId") String userId, @QueryParam("subscriptionId") String subscriptionId){
    	try{
    		DBService.unsubscribeUser(userId, subscriptionId);
        	return Response.status(200).entity("Succesfully freed user " + userId.toString() + " from subscription " + subscriptionId.toString()).build();
    	}catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
    
    private String getLogin(String auth) {
        String login = auth.split(" ")[1];

        return  Base64.decodeAsString(login).split(":")[0];

    }
}