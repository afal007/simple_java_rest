package ru.nsu.fit.endpoint.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.exception.ExceptionUtils;
import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.shared.JsonMapper;
import ru.nsu.fit.endpoint.utils.JsonConverter;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @RolesAllowed("CUSTOMER")
    @GET
    @Path("/get_customer_data/{customer_id}")
    @Consumes(MediaType.APPLICATION_JSON)
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
}