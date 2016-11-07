package ru.nsu.fit.endpoint.service.database;

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.*;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.data.Subscription;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadUserException;

public class DBServiceTest {
 
 private User testUser;
 private Customer testCustomer;
 private Plan testPlan;
 private Subscription testSubscription;
 
 private Fairy fairy = Fairy.create();
	 
 @BeforeClass
 public void setUp() {
	Person personUser = fairy.person();
	Person personCustomer = fairy.person();
	try{
	testUser = new User(new User.UserData(personUser.firstName(),
					personUser.lastName(),
					personUser.email(), 
					"321StrPass", 
					User.UserData.UserRole.USER), UUID.randomUUID(), UUID.randomUUID());
	
	testCustomer = new Customer(new Customer.CustomerData(personCustomer.firstName(),
					personCustomer.lastName(), 
					personCustomer.email(), 
					"321StrPass", 
					100), UUID.randomUUID());
	}
	catch(BadUserException ex){
		System.err.println("Test user not cool");
	}
	catch(BadCustomerException ex){
		System.err.println("Test customer not cool");
	}
 }
 

 
 @Test(expectedExceptions = IllegalArgumentException.class)
 public void getUserIdNonExistent() {
	 DBService.getUserById((new UUID(0L, 0L)).toString());
 }
 
 @Test
 public void aSlowTest() {
    System.out.println("Slow test");
 }
 
}