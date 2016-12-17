package ru.nsu.fit.tests.ui;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.services.browser.BrowserService;
import ru.nsu.fit.shared.classmock.Customer;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger("UI_TEST_LOGGER");
    private Browser browser = null;
    private Customer testCustomer;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        org.apache.log4j.BasicConfigurator.configure();
    }

    @Test
    @Title("Create customer")
    @Description("Create customer via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Customer feature")
    public void createCustomer() {
        // login to admin cp
        logger.debug("Create customer test");


        // create customer
        browser.getElement(By.id("add_new_customer")).click();

        logger.debug("Clicked \"Add user\" button");

        browser.getElement(By.id("first_name_id")).sendKeys("John");
        browser.getElement(By.id("last_name_id")).sendKeys("Weak");
        browser.getElement(By.id("email_id")).sendKeys("email@example.com");
        browser.getElement(By.id("password_id")).sendKeys("strongpass");

        logger.debug("Filled fields");

        browser.getElement(By.id("create_customer_id")).click();

        logger.debug("Clicked \"Create customer\" button");
    }

    @Test(dependsOnMethods = "createCustomer")
    @Title("Check login")
    @Description("Get customer id by login")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Customer feature")
    public void checkCustomer() {
       // TODO: need implement
    }

    @AfterClass
    public void afterClass() {
        if (browser != null)
            browser.close();
    }
}
