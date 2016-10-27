package ru.nsu.fit.endpoint.service.database;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.data.Subscription;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.data.User.UserData.UserRole;
import ru.nsu.fit.endpoint.service.database.exceptions.BadPlanException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.exceptions.BadPlanException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Fal (falalexandr007@gmail.com)
 * @author Konstantin Vysotski
 */
public class DBService {
    // Constants
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(id, first_name, last_name, login, pass, money) values ('%s', '%s', '%s', '%s', '%s', %s)";

    private static final String SELECT_CUSTOMER = "SELECT * FROM customer WHERE id='%s'";
    private static final String SELECT_CUSTOMER_ID = "SELECT id FROM CUSTOMER WHERE login='%s'";
    private static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER WHERE id='%s'";
    private static final String SELECT_CUSTOMER_BY_LOGIN = "SELECT * FROM CUSTOMER WHERE login='%s'";
    private static final String SELECT_CUSTOMER_MONEY = "SELECT money FROM CUSTOMER WHERE id='%s'";

    private static final String DELETE_CUSTOMER = "DELETE FROM CUSTOMER WHERE login='%s'";


    private static final String INSERT_USER = "INSERT INTO USER(id, customer_id, first_name, last_name, login, pass, user_role) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')";

    private static final String SELECT_USER_ID = "SELECT id FROM USER WHERE login='%s'";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM USER WHERE id='%s'";
    private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM USER WHERE login='%s'";
    private static final String SELECT_USER_SUBSCRIPTIONS_BY_ID = "SELECT subscription_id FROM USER_ASSIGNMENT WHERE user_id='%s'";
    private static final String SELECT_USER_COUNT_SUBSCRIPTIONS = "SELECT COUNT(subscription_id) FROM USER_ASSIGNMENT WHERE user_id='%s'";

    private static final String INSERT_PLAN = "INSERT INTO PLAN(id, name, details, min_seats, max_seats, fee_per_seat) values ('%s', '%s', '%s', %s, %s, %s)";

    private static final String SELECT_PLAN_ID_BY_NAME = "SELECT id FROM plan WHERE name='%s'";
    private static final String SELECT_PLAN = "SELECT * FROM plan WHERE id='%s'";

    private static final String DELETE_PLAN = "DELETE FROM PLAN WHERE id='%s'";


    private static final String INSERT_SUBSCRIPTION = "INSERT INTO subscription(id, plan_id, customer_id, used_seats, status) VALUES ('%s', '%s', '%s', '%s', '%s')";

    private static final String INSERT_USER_ASSIGNMENT = "INSERT INTO USER_ASSIGNMENT(user_id, subscription_id) values ('%s', '%s')";

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

    public static void createSubscription(UUID customerId, UUID planId, boolean isExternal) {
        synchronized (generalMutex) {
            logger.info("Try to create subscription");

            Subscription subscription = new Subscription(
                    new Subscription.SubscriptionData(isExternal ? Subscription.SubscriptionData.Status.PROVISIONING : Subscription.SubscriptionData.Status.DONE),
                    UUID.randomUUID(),
                    customerId,
                    planId);

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_SUBSCRIPTION,
                                subscription.getId(),
                                subscription.getCustomerId(),
                                subscription.getServicePlanId(),
                                0,
                                subscription.getData().getStatus()));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
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

    public static UUID getCustomerIdByLogin(String customerLogin) {
    	try{
    		Customer customer = getCustomerBy(QueryIndex.LOGIN, customerLogin);
    		return customer.getId();
    	}catch (IllegalArgumentException ex){ // thrown when user not found
    		return new UUID(0L, 0L);
    	}
    }

    public static Customer getCustomerById(UUID customerId) {
        return getCustomerBy(QueryIndex.ID, customerId.toString());
    }

    public static int addCustomerMoney(UUID customerId, Integer amount) {
        synchronized (generalMutex) {
            logger.info("Try to add money to Customer by id");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(
                        String.format(
                                SELECT_CUSTOMER_MONEY,
                                customerId.toString()));
                if(rs.next()) {
                    int total = rs.getInt(1) + amount;
                    rs.updateInt(1, total);
                    rs.updateRow();

                    return total;
                }
                else {
                    throw new IllegalArgumentException("Customer with id '" + customerId + " was not found");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public static ArrayList<UUID> getPlanIdByName(String planName) {
        synchronized (generalMutex) {
            logger.info("Try to get Plan id by name");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(
                        String.format(
                                SELECT_PLAN_ID_BY_NAME,
                                planName));

                ArrayList<UUID> id_arr = new ArrayList<>();
                while(rs.next())
                    id_arr.add(UUID.fromString(rs.getString(1)));

                return id_arr;
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public static Plan getPlanById(UUID planId) {
        synchronized (generalMutex) {
            logger.info("Try to get Plan by id");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(
                        String.format(
                                SELECT_PLAN,
                                planId.toString()));
                if(rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String name = rs.getString("name");
                    String details = rs.getString("details");
                    Integer minSeats = rs.getInt("min_seats");
                    Integer maxSeats = rs.getInt("max_seats");
                    Integer fee = rs.getInt("fee_per_seat");
                    Integer cost = rs.getInt("cost");

                    return new Plan(new Plan.PlanData(name, details, minSeats, maxSeats, fee, cost), id);
                }
                else {
                    throw new IllegalArgumentException("Plan with id '" + planId + " was not found");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            } catch (BadPlanException ex) {
                throw new RuntimeException("This should never happen, because we create plan from database data which was already verified");
            }
        }
    }

    public static User getUserById(UUID userId) {
        return getUserBy(QueryIndex.ID, userId.toString());
    }

    public static UUID getUserIdByLogin(String userLogin) {
        try{
            User user = getUserBy(QueryIndex.LOGIN, userLogin);
            return user.getId();
        }catch (IllegalArgumentException ex){ // thrown when user not found
            return new UUID(0L, 0L);
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

    public static void deletePlan(String planId){
        synchronized (generalMutex) {
            logger.info("Trying to delete plan with id "+ planId);
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

    private enum QueryIndex {ID, LOGIN}
    private static Customer getCustomerBy(QueryIndex index, String key){
        synchronized (generalMutex){
            try {
                Statement statement = connection.createStatement();
                String query = "";
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
    private static User getUserBy(QueryIndex index, String key){
        synchronized (generalMutex){
            try {
                Statement statement = connection.createStatement();
                String query = "";
                switch(index){
                    case LOGIN:{
                        query = String.format(SELECT_USER_BY_LOGIN, key);
                        break;
                    }
                    case ID:{
                        query = String.format(SELECT_USER_BY_ID, key);
                        break;
                    }
                }
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    UUID customerId = UUID.fromString(rs.getString("customer_id"));
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String login = rs.getString("login");
                    String pass = rs.getString("pass");
                    UserRole userRole = UserRole.fromString(rs.getString("user_role"));

                    User user = new User(new User.UserData(firstName, lastName, login, pass, userRole), id, customerId);

                    //fetch subscriptions
                    rs = statement.executeQuery(String.format(SELECT_USER_COUNT_SUBSCRIPTIONS, id));
                    int count = 0;
                    if (rs.next())
                        count = rs.getInt(1);
                    if (count > 0) {
                        rs = statement.executeQuery(String.format(SELECT_USER_SUBSCRIPTIONS_BY_ID, id));
                        UUID[] subscriptionIds = new UUID[count];
                        int i = 0;
                        while(rs.next()){
                            subscriptionIds[i] = (UUID.fromString(rs.getString("subscription_id")));
                            i++;
                        }
                        user.setSubscriptionIds(subscriptionIds);
                    }

                    return user;
                }
                else {
                    throw new IllegalArgumentException("User was not found");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            } catch (BadUserException ex) {
                throw new RuntimeException("This should never happen, because we create customer from database data which was already verified");
            }
        }
    }
    
    public static void subscribeUser(String userId, String subscriptionId) throws IllegalArgumentException{
    	synchronized(generalMutex){
	    	User user = getUserById(UUID.fromString(userId));
	    	if(ArrayUtils.contains(user.getSubscriptionIds(), subscriptionId))
	    		throw new IllegalArgumentException("user is already subscribed to this");
	    	else{
	    		try{
	    			Statement statement = connection.createStatement();
	    			statement.executeUpdate(
						String.format(INSERT_USER_ASSIGNMENT, 
								userId,
								subscriptionId));
	    		}catch(SQLException ex){
	    			logger.debug(ex.getMessage(), ex);
	                throw new RuntimeException(ex);
	    		}
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
