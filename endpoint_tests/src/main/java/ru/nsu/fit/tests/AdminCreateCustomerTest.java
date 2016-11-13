package ru.nsu.fit.tests;

import io.codearte.jfairy.Fairy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ru.nsu.fit.services.rest.RestService;

import java.util.UUID;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class AdminCreateCustomerTest {
    private class Customer {
        UUID id;
        String firstName;
        String lastName;
        String login;
        String pass;
        int money;

        public Customer(UUID id ,String firstName, String lastName, String login, String pass, int money) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.pass = pass;
            this.money = money;
        }
    }

    private Fairy testFairy;
    private RestService rest;
    @BeforeClass
    private void beforeClass(){
        testFairy = Fairy.create();
        rest = new RestService();
    }
    @BeforeMethod
    private void beforeMethod(){
        rest.configAuth("",""); //reset authorization
    }


}
