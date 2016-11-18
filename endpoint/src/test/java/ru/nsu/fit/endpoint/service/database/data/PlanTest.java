package ru.nsu.fit.endpoint.service.database.data;

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
		new Plan.PlanData("name", "details", 10, 999, 1);
	}
	
	/*
	 * Name tests
	 */
	@Test
	public void NameLongEnough() throws BadPlanException{
		new Plan.PlanData("aa", "details", 10, 999, 1);
		String longstr = new String(new char[128]).replace('\0', 'a');
		new Plan.PlanData(longstr, "details", 10, 999, 1);
	}
	
	@Test
	public void NameShort() throws BadPlanException{
		expectedEx.expect(BadPlanNameException.class);
        expectedEx.expectMessage(BadPlanNameException.SHORT_NAME_MESSAGE);
		new Plan.PlanData("a", "details", 10, 999, 1);
	}
	
	@Test
	public void NameLong() throws BadPlanException{
		expectedEx.expect(BadPlanNameException.class);
        expectedEx.expectMessage(BadPlanNameException.LONG_NAME_MESSAGE);
        String toolongstr = new String(new char[129]).replace('\0', 'a');
		new Plan.PlanData(toolongstr, "details", 10, 999, 1);
	}
	
	@Test
	public void NameSpecialSymbols() throws BadPlanException{
		expectedEx.expect(BadPlanNameException.class);
        expectedEx.expectMessage(BadPlanNameException.WRONG_SYMBOLS_MESSAGE);
		new Plan.PlanData("!aasd", "details", 10, 999, 1);
	}
	
	/*
	 * Details tests
	 */
	@Test
	public void DetailsLongEnough() throws BadPlanException{
        String longstr = new String(new char[1024]).replace('\0', 'a');
		new Plan.PlanData("name", "d", 10, 999, 1);
		new Plan.PlanData("name", longstr, 10, 999, 1);
	}
	
	@Test
	public void DetailsShort() throws BadPlanException{
		expectedEx.expect(BadPlanDetailsException.class);
        expectedEx.expectMessage(BadPlanDetailsException.SHORT_DETAILS_MESSAGE);
		new Plan.PlanData("name", "", 10, 999, 1);
	}
	
	@Test
	public void DetailsLong() throws BadPlanException{
		expectedEx.expect(BadPlanDetailsException.class);
        expectedEx.expectMessage(BadPlanDetailsException.LONG_DETAILS_MESSAGE);
        String longstr = new String(new char[1025]).replace('\0', 'a');
		new Plan.PlanData("name", longstr, 10, 999, 1);
	}
	
	/*
	 * Max seats tests
	 */
	@Test
	public void MaxSeatsEnough() throws BadPlanException{
		new Plan.PlanData("name", "details", 1, 999, 1);
		new Plan.PlanData("name", "details", 999999, 999, 1);
	}
	
	@Test
	public void MaxSeatsTooFew() throws BadPlanException{
		expectedEx.expect(BadPlanMaxSeatsException.class);
        expectedEx.expectMessage(BadPlanMaxSeatsException.SMALL_MAXSEATS_MESSAGE);
		new Plan.PlanData("name", "details", 0, 999, 1);
	}
	
	@Test
	public void MaxSeatsTooMuch() throws BadPlanException{
		expectedEx.expect(BadPlanMaxSeatsException.class);
        expectedEx.expectMessage(BadPlanMaxSeatsException.BIG_MAXSEATS_MESSAGE);
		new Plan.PlanData("name", "details", 1000000, 999, 1);
	}

	/*
	 * Fee tests
	 */
	
	@Test
	public void FeeEnough() throws BadPlanException{
		new Plan.PlanData("name", "details", 10, 0, 1);
		new Plan.PlanData("name", "details", 10, 999999, 1);
	}
	
	@Test
	public void FeeNegative() throws BadPlanException{
		expectedEx.expect(BadPlanFeeException.class);
        expectedEx.expectMessage(BadPlanFeeException.NEGATIVE_FEE_MESSAGE);
		new Plan.PlanData("name", "details", 10, -1, 1);
	}
	
	@Test
	public void FeeTooHigh() throws BadPlanException{
		expectedEx.expect(BadPlanFeeException.class);
        expectedEx.expectMessage(BadPlanFeeException.BIG_FEE_MESSAGE);
		new Plan.PlanData("name", "details", 10, 1000000, 1);
	}
	
	/*
	@Test
	public void  throws BadPlanException{
		expectedEx.expect(BadPlan_Exception.class);
        expectedEx.expectMessage(BadPlan_Exception);
		new Plan.PlanData("name", "details", 10, 1, 999);
	}
	*/
}
