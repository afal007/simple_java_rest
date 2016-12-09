package ru.nsu.fit.endpoint.service.database;

import java.util.UUID;

import org.testng.annotations.*;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.data.Subscription;
import ru.nsu.fit.endpoint.service.database.data.User;
import ru.nsu.fit.endpoint.service.database.exceptions.BadCustomerException;
import ru.nsu.fit.endpoint.service.database.exceptions.BadPlanException;
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
	testCustomer = new Customer(new Customer.CustomerData(personCustomer.firstName(),
					personCustomer.lastName(), 
					personCustomer.email(), 
					"321StrPass", 
					100), UUID.randomUUID());
	
	testUser = new User(new User.UserData(personUser.firstName(),
					personUser.lastName(),
					personUser.email(), 
					"321StrPass", 
					User.UserData.UserRole.USER), UUID.randomUUID(), testCustomer.getId());

	testPlan = new Plan(new Plan.PlanData(fairy.person().firstName(), 
					fairy.textProducer().latinSentence(), 
					999, 1, 10), UUID.randomUUID());
	
	testSubscription = new Subscription(new Subscription.SubscriptionData(Subscription.SubscriptionData.Status.DONE), 
					UUID.randomUUID(), 
					testCustomer.getId(), 
					testPlan.getId());
	}
	catch(BadUserException ex){
		System.err.println("Test user not cool");
	}
	catch(BadCustomerException ex){
		System.err.println("Test customer not cool");
	}
	catch(BadPlanException ex){
		System.err.println("Test plan not cool");
	}
 }
 
 @Test
 public void createCustomer() throws BadCustomerException{
	 //add test customer to db and give it real id
	 System.out.println("Simple test-method Six. Thread id is: " + Thread.currentThread().getId());
	 testCustomer.setId(DBService.createCustomer(testCustomer.getData()));
	 
 }
 
 @Test(expectedExceptions = RuntimeException.class, dependsOnMethods={"createCustomer"})
 public void createCustomerWithSameLogin() throws BadCustomerException{
	 //just try to add test customer again
	 System.out.println("Simple test-method Five. Thread id is: " + Thread.currentThread().getId());
	 DBService.createCustomer(testCustomer.getData());
	 
 }
 
 @Test(dependsOnMethods={"createCustomer"})
 public void createUser() throws BadUserException{
	 System.out.println("Simple test-method Four. Thread id is: " + Thread.currentThread().getId());
	 testUser.setId(DBService.createUser(testUser.getData(), testCustomer.getId()));
	 
 }
 
 @Test(expectedExceptions = RuntimeException.class, dependsOnMethods={"createUser"})
 public void createUserWithSameLogin() throws BadUserException{
	 //just try to add test user again
	 System.out.println("Simple test-method Three. Thread id is: " + Thread.currentThread().getId());
	 DBService.createUser(testUser.getData(), testCustomer.getId());
	 
 }
 
 @Test(expectedExceptions = IllegalArgumentException.class)
 public void getUserIdNonExistent() {
	 System.out.println("Simple test-method Two. Thread id is: " + Thread.currentThread().getId());
	 DBService.getUserById((new UUID(0L, 0L)).toString());
	 
 }
 
 @Test(expectedExceptions = IllegalArgumentException.class)
 public void getCustomerIdNonExistent() {
	 System.out.println("Simple test-method One. Thread id is: " + Thread.currentThread().getId());
	 DBService.getCustomerById((new UUID(0L, 0L)));
	 
 }
 
}