package ru.nsu.fit.endpoint.service.databse.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.fit.endpoint.service.database.data.ServicePlan;
import ru.nsu.fit.endpoint.service.database.exceptions.*;

public class ServicePlanTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void ServicePlanOK() throws BadServicePlanException {
		new ServicePlan("name", "details", 10, 1, 999);
	}
	
	/*
	 * Name tests
	 */
	@Test
	public void NameLongEnough() throws BadServicePlanException{
		new ServicePlan("aa", "details", 10, 1, 999);
		String longstr = new String(new char[128]).replace('\0', 'a');
		new ServicePlan(longstr, "details", 10, 1, 999);
	}
	
	@Test
	public void NameShort() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanNameException.class);
        expectedEx.expectMessage(BadServicePlanNameException.SHORT_NAME_MESSAGE);
		new ServicePlan("a", "details", 10, 1, 999);
	}
	
	@Test
	public void NameLong() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanNameException.class);
        expectedEx.expectMessage(BadServicePlanNameException.LONG_NAME_MESSAGE);
        String toolongstr = new String(new char[129]).replace('\0', 'a');
		new ServicePlan(toolongstr, "details", 10, 1, 999);
	}
	
	@Test
	public void NameSpecialSymbols() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanNameException.class);
        expectedEx.expectMessage(BadServicePlanNameException.WRONG_SYMBOLS_MESSAGE);
		new ServicePlan("!aasd", "details", 10, 1, 999);
	}
	
	/*
	 * Details tests
	 */
	@Test
	public void DetailsLongEnough() throws BadServicePlanException{
        String longstr = new String(new char[1024]).replace('\0', 'a');
		new ServicePlan("name", "d", 10, 1, 999);
		new ServicePlan("name", longstr, 10, 1, 999);
	}
	
	@Test
	public void DetailsShort() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanDetailsException.class);
        expectedEx.expectMessage(BadServicePlanDetailsException.SHORT_DETAILS_MESSAGE);
		new ServicePlan("name", "", 10, 1, 999);
	}
	
	@Test
	public void DetailsLong() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanDetailsException.class);
        expectedEx.expectMessage(BadServicePlanDetailsException.LONG_DETAILS_MESSAGE);
        String longstr = new String(new char[1025]).replace('\0', 'a');
		new ServicePlan("name", longstr, 10, 1, 999);
	}
	
	/*
	 * Max seats tests
	 */
	@Test
	public void MaxSeatsEnough() throws BadServicePlanException{
		new ServicePlan("name", "details", 1, 1, 999);
		new ServicePlan("name", "details", 999999, 1, 999);
	}
	
	@Test
	public void MaxSeatsTooFew() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanMaxSeatsException.class);
        expectedEx.expectMessage(BadServicePlanMaxSeatsException.SMALL_MAXSEATS_MESSAGE);
		new ServicePlan("name", "details", 0, 1, 999);
	}
	
	@Test
	public void MaxSeatsTooMuch() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanMaxSeatsException.class);
        expectedEx.expectMessage(BadServicePlanMaxSeatsException.BIG_MAXSEATS_MESSAGE);
		new ServicePlan("name", "details", 1000000, 1, 999);
	}
	
	@Test
	public void MaxSeatsLessThanMinSeats() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanMaxSeatsException.class);
        expectedEx.expectMessage(BadServicePlanMaxSeatsException.LESS_THAN_MINSEATS_MESSAGE);
		new ServicePlan("name", "details", 1, 2, 999);
	}
	
	/*
	 * Min seats tests
	 */
	@Test
	public void MinSeatsEnough() throws BadServicePlanException{
		new ServicePlan("name", "details", 1, 1, 999);
		new ServicePlan("name", "details", 999999, 1, 999);
	}
	
	@Test
	public void MinSeatsTooFew() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanMinSeatsException.class);
        expectedEx.expectMessage(BadServicePlanMinSeatsException.SMALL_MINSEATS_MESSAGE);
		new ServicePlan("name", "details", 1, 0, 999);
	}
	
	/* max seats ex is thrown first, cant implement
	@Test
	public void MinSeatsTooMuch() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanMinSeatsException.class);
        expectedEx.expectMessage(BadServicePlanMinSeatsException.SMALL_MINSEATS_MESSAGE);
		new ServicePlan("name", "details", 1000000, 1000000, 999);
	}
	*/
	
	/*
	 * Fee tests
	 */
	
	@Test
	public void FeeEnough() throws BadServicePlanException{
		new ServicePlan("name", "details", 10, 1, 0);
		new ServicePlan("name", "details", 10, 1, 999999);
	}
	
	@Test
	public void FeeNegative() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanFeeException.class);
        expectedEx.expectMessage(BadServicePlanFeeException.NEGATIVE_FEE_MESSAGE);
		new ServicePlan("name", "details", 10, 1, -1);
	}
	
	@Test
	public void FeeTooHigh() throws BadServicePlanException{
		expectedEx.expect(BadServicePlanFeeException.class);
        expectedEx.expectMessage(BadServicePlanFeeException.BIG_FEE_MESSAGE);
		new ServicePlan("name", "details", 10, 1, 1000000);
	}
	
	/*
	@Test
	public void  throws BadServicePlanException{
		expectedEx.expect(BadServicePlan_Exception.class);
        expectedEx.expectMessage(BadServicePlan_Exception);
		new ServicePlan("name", "details", 10, 1, 999);
	}
	*/
}
