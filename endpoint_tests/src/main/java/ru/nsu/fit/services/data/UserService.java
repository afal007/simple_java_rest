package ru.nsu.fit.services.data;

import ru.nsu.fit.services.data.exceptions.CustomerServiceException;
import ru.nsu.fit.services.data.exceptions.UserServiceException;
import ru.nsu.fit.services.rest.RestService;
import ru.nsu.fit.shared.classmock.User;

import javax.ws.rs.core.Response;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class UserService {
    private static final String USER_TEMPLATE = "{\n" +
            "\t\"firstName\":\"%s\",\n" +
            "    \"lastName\":\"%s\",\n" +
            "    \"login\":\"%s\",\n" +
            "    \"pass\":\"%s\",\n" +
            "    \"userRole\":\"%s\"\n" +
            "}";

    private static String login = null;
    private static String pass = null;

    public static void configAuth(String _login, String _pass) {
        login = _login;
        pass = _pass;
    }

    public static void createUser(String firstName, String lastName, String uLogin, String uPass, User.UserData.UserRole userRole) throws UserServiceException {
        RestService.configAuth(login, pass);
        Response response = RestService.createUser(
                String.format(
                        USER_TEMPLATE,
                        firstName,
                        lastName,
                        uLogin,
                        uPass,
                        userRole));

        if(response.getStatus() != 200) {
            throw new UserServiceException(response.readEntity(String.class));
        }
    }
}
