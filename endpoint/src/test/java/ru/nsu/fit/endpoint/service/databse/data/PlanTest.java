package ru.nsu.fit.endpoint.service.databse.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.fit.endpoint.service.database.data.Plan;
import ru.nsu.fit.endpoint.service.database.exceptions.*;

public class PlanTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void PlanOK() throws BadPlanException {
		new Plan("name", "details", 10, 1, 999);
	}
	
	/*
	 * Name tests
	 */
	@Test
	public void NameLongEnough() throws BadPlanException{
		new Plan("aa", "details", 10, 1, 999);
		String longstr = new String(new char[128]).replace('\0', 'a');
		new Plan(longstr, "details", 10, 1, 999);
	}
	
	@Test
	public void NameShort() throws BadPlanException{
		expectedEx.expect(BadPlanNameException.class);
        expectedEx.expectMessage(BadPlanNameException.SHORT_NAME_MESSAGE);
		new Plan("a", "details", 10, 1, 999);
	}
	
	@Test
	public void NameLong() throws BadPlanException{
		expectedEx.expect(BadPlanNameException.class);
        expectedEx.expectMessage(BadPlanNameException.LONG_NAME_MESSAGE);
        String toolongstr = new String(new char[129]).replace('\0', 'a');
		new Plan(toolongstr, "details", 10, 1, 999);
	}
	
	@Test
	public void NameSpecialSymbols() throws BadPlanException{
		expectedEx.expect(BadPlanNameException.class);
        expectedEx.expectMessage(BadPlanNameException.WRONG_SYMBOLS_MESSAGE);
		new Plan("!aasd", "details", 10, 1, 999);
	}
	
	/*
	 * Details tests
	 */
	@Test
	public void DetailsLongEnough() throws BadPlanException{
        String longstr = new String(new char[1024]).replace('\0', 'a');
		new Plan("name", "d", 10, 1, 999);
		new Plan("name", longstr, 10, 1, 999);
	}
	
	@Test
	public void DetailsShort() throws BadPlanException{
		expectedEx.expect(BadPlanDetailsException.class);
        expectedEx.expectMessage(BadPlanDetailsException.SHORT_DETAILS_MESSAGE);
		new Plan("name", "", 10, 1, 999);
	}
	
	@Test
	public void DetailsLong() throws BadPlanException{
		expectedEx.expect(BadPlanDetailsException.class);
        expectedEx.expectMessage(BadPlanDetailsException.LONG_DETAILS_MESSAGE);
        String longstr = new String(new char[1025]).replace('\0', 'a');
		new Plan("name", longstr, 10, 1, 999);
	}
	
	/*
	 * Max seats tests
	 */
	@Test
	public void MaxSeatsEnough() throws BadPlanException{
		new Plan("name", "details", 1, 1, 999);
		new Plan("name", "details", 999999, 1, 999);
	}
	
	@Test
	public void MaxSeatsTooFew() throws BadPlanException{
		expectedEx.expect(BadPlanMaxSeatsException.class);
        expectedEx.expectMessage(BadPlanMaxSeatsException.SMALL_MAXSEATS_MESSAGE);
		new Plan("name", "details", 0, 1, 999);
	}
	
	@Test
	public void MaxSeatsTooMuch() throws BadPlanException{
		expectedEx.expect(BadPlanMaxSeatsException.class);
        expectedEx.expectMessage(BadPlanMaxSeatsException.BIG_MAXSEATS_MESSAGE);
		new Plan("name", "details", 1000000, 1, 999);
	}
	
	@Test
	public void MaxSeatsLessThanMinSeats() throws BadPlanException{
		expectedEx.expect(BadPlanMaxSeatsException.class);
        expectedEx.expectMessage(BadPlanMaxSeatsException.LESS_THAN_MINSEATS_MESSAGE);
		new Plan("name", "details", 1, 2, 999);
	}
	
	/*
	 * Min seats tests
	 */
	@Test
	public void MinSeatsEnough() throws BadPlanException{
		new Plan("name", "details", 1, 1, 999);
		new Plan("name", "details", 999999, 1, 999);
	}
	
	@Test
	public void MinSeatsTooFew() throws BadPlanException{
		expectedEx.expect(BadPlanMinSeatsException.class);
        expectedEx.expectMessage(BadPlanMinSeatsException.SMALL_MINSEATS_MESSAGE);
		new Plan("name", "details", 1, 0, 999);
	}
	
	/* max seats ex is thrown first, cant implement
	@Test
	public void MinSeatsTooMuch() throws BadPlanException{
		expectedEx.expect(BadPlanMinSeatsException.class);
        expectedEx.expectMessage(BadPlanMinSeatsException.SMALL_MINSEATS_MESSAGE);
		new Plan("name", "details", 1000000, 1000000, 999);
	}
	*/
	
	/*
	 * Fee tests
	 */
	
	@Test
	public void FeeEnough() throws BadPlanException{
		new Plan("name", "details", 10, 1, 0);
		new Plan("name", "details", 10, 1, 999999);
	}
	
	@Test
	public void FeeNegative() throws BadPlanException{
		expectedEx.expect(BadPlanFeeException.class);
        expectedEx.expectMessage(BadPlanFeeException.NEGATIVE_FEE_MESSAGE);
		new Plan("name", "details", 10, 1, -1);
	}
	
	@Test
	public void FeeTooHigh() throws BadPlanException{
		expectedEx.expect(BadPlanFeeException.class);
        expectedEx.expectMessage(BadPlanFeeException.BIG_FEE_MESSAGE);
		new Plan("name", "details", 10, 1, 1000000);
	}
	
	/*
	@Test
	public void  throws BadPlanException{
		expectedEx.expect(BadPlan_Exception.class);
        expectedEx.expectMessage(BadPlan_Exception);
		new Plan("name", "details", 10, 1, 999);
	}
	*/
}
