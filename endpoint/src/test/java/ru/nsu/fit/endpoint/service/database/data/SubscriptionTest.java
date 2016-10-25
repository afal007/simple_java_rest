package ru.nsu.fit.endpoint.service.database.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ru.nsu.fit.endpoint.service.database.data.Subscription;
import ru.nsu.fit.endpoint.service.database.exceptions.BadSubscriptionException;

public class SubscriptionTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void SubscriptionOk() throws BadSubscriptionException {
		new Subscription.SubscriptionData(Subscription.SubscriptionData.Status.PROVISIONING);
	}
}
