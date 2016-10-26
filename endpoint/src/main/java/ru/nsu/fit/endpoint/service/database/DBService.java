package ru.nsu.fit.endpoint.service.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;

import java.sql.*;
import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 * @author Konstantin Vysotski
 */
public class DBService {
    // Constants
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(id, firstName, lastName, login, pass, money) values ('%s', '%s', '%s', '%s', '%s', %s)";
    private static final String SELECT_CUSTOMER_ID = "SELECT id FROM CUSTOMER WHERE login='%s'";
    private static final String SELECT_CUSTOMER = "SELECT * FROM CUSTOMER WHERE id='%s'";
    private static final String DELETE_CUSTOMER = "DELETE FROM CUSTOMER WHERE login='%s'";

    private static final String INSERT_USER = "INSERT INTO USER(id, customer_id, first_name, last_name, login, pass, user_role) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String SELECT_USER = "SELECT id FROM USER WHERE login='%s'";


    private static final Logger logger = LoggerFactory.getLogger("DB_LOG");
    private static final Object generalMutex = new Object();
    private static Connection connection;

    static {
        init();
    }

    public static void createUser(User.UserData userData, UUID customerId) throws BadUserException{
    	synchronized(generalMutex){
    		logger.info("Trying to create user");

            User user = new User(userData, UUID.randomUUID(), customerId);
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_USER,
                                user.getId(),
                                user.getCustomerId(),
                                user.getData().getFirstName(),
                                user.getData().getLastName(),
                                user.getData().getLogin(),
                                user.getData().getPass(),
                                user.getData().getUserRole()));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
    	}
    }
    public static void createCustomer(Customer.CustomerData customerData) throws BadCustomerException {
    	logger.info("info log level works");
    	logger.debug("debug log level works");
        synchronized (generalMutex) {
            logger.info("Try to create customer");

            Customer customer = new Customer(customerData, UUID.randomUUID());
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_CUSTOMER,
                                customer.getId(),
                                customer.getData().getFirstName(),
                                customer.getData().getLastName(),
                                customer.getData().getLogin(),
                                customer.getData().getPass(),
                                customer.getData().getMoney()));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public static void deleteCustomer(String customerLogin){
    	synchronized (generalMutex) {
            logger.info("Try to delete customer");

            try {
            	Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                DELETE_CUSTOMER,
                                customerLogin));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public static UUID getCustomerIdByLogin(String customerLogin) {
        synchronized (generalMutex) {
            logger.info("Try to create customer");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(
                        String.format(
                                SELECT_CUSTOMER_ID,
                                customerLogin));
                if (rs.next()) {
                    return UUID.fromString(rs.getString(1));
                } else {
                    //throw new IllegalArgumentException("Customer with login '" + customerLogin + " was not found");
                	return new UUID(0L, 0L); // "00000000-0000-0000-0000-000000000000"
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public static Customer getCustomerById(UUID customerId) {
        synchronized (generalMutex) {
            logger.info("Try to get Customer by id");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(
                        String.format(
                                SELECT_CUSTOMER,
                                customerId.toString()));
                if(rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String login = rs.getString("login");
                    Integer money = rs.getInt("money");
                    String pass = rs.getString("pass");

                    return new Customer(new Customer.CustomerData(firstName, lastName, login, pass, money), id);
                }
                else {
                    throw new IllegalArgumentException("Customer with id '" + customerId + " was not found");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            } catch (BadCustomerException ex) {
                throw new RuntimeException("This should never happen, because we create customer from database data which was already verified");
            }
        }
    }

    private static void init() {
        logger.debug("-------- MySQL JDBC Connection Testing ------------");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            logger.debug("Where is your MySQL JDBC Driver?", ex);
            throw new RuntimeException(ex);
        }

        logger.debug("MySQL JDBC Driver Registered!");

        try {
            // 178.49.4.144 MySQL server home, user: test_methods_remote_user, pass: 1q2w3e
            connection = DriverManager
                    .getConnection(
                            "jdbc:mysql://localhost:3306/testmethods?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false",
                            "user",
                            "user");
        } catch (SQLException ex) {
            logger.debug("Connection Failed! Check output console", ex);
            throw new RuntimeException(ex);
        }

        if (connection != null) {
            logger.debug("You made it, take control your database now!");
        } else {
            logger.debug("Failed to make connection!");
        }
    }
}
