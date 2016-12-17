package ru.nsu.fit.services.data;

import ru.nsu.fit.services.data.exceptions.CustomerServiceException;
import ru.nsu.fit.services.rest.RestService;

import javax.ws.rs.core.Response;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class CustomerService {
    private static final String CUSTOMER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"money\":\"%s\"\n" +
            "}";

    public static void createCustomer(String firstName, String lastName, String login, String pass, Integer money) throws CustomerServiceException {
        RestService.configAuth("admin", "setup");
        Response response = RestService.createCustomer(
                String.format(
                        CUSTOMER_TEMPLATE,
                        firstName,
                        lastName,
                        login,
                        pass,
                        money));

        if(response.getStatus() != 200) {
            throw new CustomerServiceException(response.readEntity(String.class));
        }
    }
}
