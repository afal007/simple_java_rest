package ru.nsu.fit.endpoint.service.databse.data;

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
		new Subscription(10, 1, 1);
	}
	
	@Test
	public void MaxSeatsEnough() throws BadSubscriptionException{
		new Subscription(1, 1, 1);
		new Subscription(999999, 1, 1);
	}
	
	@Test
	public void MinSeatsEnough() throws BadSubscriptionException{
		new Subscription(999999, 999999, 999999);
	}
	
	@Test
	public void MaxSeatsTooFew() throws BadSubscriptionException{
		expectedEx.expect(BadSubscriptionException.class);
        expectedEx.expectMessage(BadSubscriptionException.SMALL_MAXSEATS_MESSAGE);
		new Subscription(0, 0, 0);
	}
	
	@Test
	public void MaxSeatsTooMuch() throws BadSubscriptionException{
		expectedEx.expect(BadSubscriptionException.class);
        expectedEx.expectMessage(BadSubscriptionException.BIG_MAXSEATS_MESSAGE);
		new Subscription(1000000, 1, 1);
	}
	
	@Test
	public void MaxSeatsLessThanMinSeats() throws BadSubscriptionException{
		expectedEx.expect(BadSubscriptionException.class);
        expectedEx.expectMessage(BadSubscriptionException.MAXSEATS_LESS_THAN_MINSEATS_MESSAGE);
		new Subscription(10, 11, 11);
	}
	
	@Test
	public void MaxSeatsLessThanUsedSeats() throws BadSubscriptionException{
		expectedEx.expect(BadSubscriptionException.class);
        expectedEx.expectMessage(BadSubscriptionException.MAXSEATS_LESS_THAN_USEDSEATS_MESSAGE);
		new Subscription(10, 1, 11);
	}
	
	@Test
	public void MinSeatsTooFew() throws BadSubscriptionException{
		expectedEx.expect(BadSubscriptionException.class);
        expectedEx.expectMessage(BadSubscriptionException.SMALL_MINSEATS_MESSAGE);
		new Subscription(10, 0, 1);
	}
	
	@Test
	public void MinSeatsGreaterThanUsedSeats() throws BadSubscriptionException{
		expectedEx.expect(BadSubscriptionException.class);
        expectedEx.expectMessage(BadSubscriptionException.MINSEATS_GREATER_THAN_USEDSEATS_MESSAGE);
		new Subscription(10, 2, 1);
	}
}
