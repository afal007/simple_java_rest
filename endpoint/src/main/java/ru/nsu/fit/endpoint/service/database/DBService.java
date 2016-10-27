package ru.nsu.fit.endpoint.service.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.exceptions.BadPlanException;

import java.sql.*;
import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 * @author Konstantin Vysotski
 */
public class DBService {
    // Constants
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(id, first_name, last_name, login, pass, money) values ('%s', '%s', '%s', '%s', '%s', %s)";
    private static final String SELECT_CUSTOMER_ID = "SELECT id FROM CUSTOMER WHERE login='%s'";
    private static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER WHERE id='%s'";
    private static final String SELECT_CUSTOMER_BY_LOGIN = "SELECT * FROM CUSTOMER WHERE login='%s'";
    private static final String DELETE_CUSTOMER = "DELETE FROM CUSTOMER WHERE login='%s'";

    private static final String INSERT_PLAN = "INSERT INTO PLAN(id, name, details, min_seats, max_seats, fee_per_seat) values ('%s', '%s', '%s', %s, %s, %s)";
    private static final String DELETE_PLAN = "DELETE FROM PLAN WHERE id='%s'";
    
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
    
    private enum QueryIndex {ID, LOGIN};
    public static Customer getCustomerBy(QueryIndex index, String key){
    	synchronized (generalMutex){
    		try {
                Statement statement = connection.createStatement();
                String query = new String();
    			switch(index){
    			case LOGIN:{
    				query = String.format(SELECT_CUSTOMER_BY_LOGIN, key);
    				break;
    			}
    			case ID:{
    				query = String.format(SELECT_CUSTOMER_BY_ID, key);
    				break;
    			}
    			}
                ResultSet rs = statement.executeQuery(query);
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
                    throw new IllegalArgumentException("Customer was not found");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            } catch (BadCustomerException ex) {
                throw new RuntimeException("This should never happen, because we create customer from database data which was already verified");
            }
    	}
    }
    
    public static UUID getCustomerIdByLogin(String customerLogin) {
    	Customer customer = getCustomerBy(QueryIndex.LOGIN, customerLogin);
    	return customer.getId();
    }

    public static Customer getCustomerById(UUID customerId) {
        return getCustomerBy(QueryIndex.ID, customerId.toString());
    }
    
    public static void createPlan(Plan.PlanData planData) throws BadPlanException{
    	synchronized (generalMutex) {
            logger.info("Try to create plan");
            logger.debug("plan data: " + planData.toString());

            Plan plan = new Plan(planData, UUID.randomUUID());
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_PLAN,
                                plan.getId(),
                                plan.getData().getName(),
                                plan.getData().getDetails(),
                                plan.getData().getMinSeats(),
                                plan.getData().getMaxSeats(),
                                plan.getData().getFeePerUnit()));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }
    
    public static void deletePlan(String planId){
    	synchronized (generalMutex) {
            logger.info("Trying to delete plan with id "+planId);
            try {
            	Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                DELETE_PLAN,
                                planId));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
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
