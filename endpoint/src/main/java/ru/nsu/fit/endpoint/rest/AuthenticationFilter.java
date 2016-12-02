package ru.nsu.fit.endpoint.rest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.internal.util.Base64;
import ru.nsu.fit.endpoint.shared.Globals;

import ru.nsu.fit.endpoint.service.database.DBService;

/**
 * This filter verify the access permissions for a user
 * based on username and password provided in request
 *
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    
    public String getAuthenticationScheme(){
    	return AUTHENTICATION_SCHEME;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if (!method.isAnnotationPresent(PermitAll.class)) {
            //Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).entity("Access blocked for all users !!").build();
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            System.err.println(headers.toString());
            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if (authorization == null || authorization.isEmpty()) {
                Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build();
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            String username = null;
            String password = null;

            if (authorization != null && authorization.size() == 1) {
                //Get encoded username and password
                final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

                //Decode username and password
                String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

                //Split username and password tokens
                final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                username = tokenizer.nextToken();
                password = tokenizer.nextToken();
            }

            //Verifying Username and password
            System.out.println("User: " + username);
            System.out.println("Pass: " + password);

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if (!isUserAllowed(username, password, rolesSet, requestContext)) {
                    Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build();
                    requestContext.abortWith(ACCESS_DENIED);
                }
            }
        }
    }

    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet, ContainerRequestContext requestContext) {
        //Step 1. Fetch password from database and match with password in argument
        //If both match then get the defined role for user from database and continue; else return isAllowed [false]
        //Access the database and do this part yourself
        //String userRole = userMgr.getUserRole(username);
        //Base64(admin:setup) = YWRtaW46c2V0dXA=

        String userRole = Roles.UNKNOWN;
        System.out.println("USER TRYING TO LOGIN: " + username + " " + password);

        if (StringUtils.equals(username, Globals.ADMIN_LOGIN) && StringUtils.equals(password, Globals.ADMIN_PASS)) {
            System.out.println("Role is admin");
            userRole = "ADMIN";
        }
        else { // this is bad
	        UUID id;
	        id = DBService.getCustomerIdByLogin(username);
	        if (!id.equals(new UUID(0L, 0L))){
	        	String customerPass = DBService.getCustomerById(id).getData().getPass();
	        	System.out.println(id.toString());
	        	if (StringUtils.equals(password, customerPass))
	        		userRole = "CUSTOMER";
	        }
	        else {
	        	id = DBService.getUserIdByLogin(username);
	        	if (!id.equals(new UUID(0L, 0L))){
		        	String userPass = DBService.getUserById(id.toString()).getData().getPass();
		        	System.out.println(id.toString());
		        	if (StringUtils.equals(password, userPass))
		        		userRole = "USER";
		        }
	        }
        }

        //Step 2. Verify user role
        System.out.println(userRole);
        requestContext.setProperty("ROLE", userRole);
        return rolesSet.contains(userRole);
    }
}