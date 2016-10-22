package ru.nsu.fit.endpoint.service.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.service.database.data.Customer;

import java.sql.*;
import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class DBService {
    // Constants
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(id, first_name, last_name, login, pass, money) values ('%s', '%s', '%s', '%s', '%s', %s)";
    private static final String SELECT_CUSTOMER = "SELECT id FROM CUSTOMER WHERE login='%s'";

    private static final Logger logger = LoggerFactory.getLogger("DB_LOG");
    private static final Object generalMutex = new Object();
    private static Connection connection;

    static {
        init();
    }

    public static void createCustomer(Customer.CustomerData customerData) {
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

    public static UUID getCustomerIdByLogin(String customerLogin) {
        synchronized (generalMutex) {
            logger.info("Try to create customer");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(
                        String.format(
                                SELECT_CUSTOMER,
                                customerLogin));
                if (rs.next()) {
                    return UUID.fromString(rs.getString(1));
                } else {
                    throw new IllegalArgumentException("Customer with login '" + customerLogin + " was not found");
                }
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
