package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;
import ru.nsu.fit.endpoint.service.database.exceptions.BadSubscriptionException;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Subscription {
    private UUID id;
    private UUID customerId;
    private UUID servicePlanId;
    private int maxSeats; //same restrictions as in ServicePlan (?)
    private int minSeats; //same restrictions as in ServicePlan (?)
    private int usedSeats; // maxSeats >= usedSeats >= minSeats
    
    public Subscription(int maxSeats, int minSeats, int usedSeats) throws BadSubscriptionException{
    	this.id = UUID.randomUUID();
    	this.customerId = UUID.randomUUID();
    	this.servicePlanId = UUID.randomUUID();
    	this.maxSeats = maxSeats;
    	this.minSeats = minSeats;
    	this.usedSeats = usedSeats;
    	
    	validate();
    }
    
    private void validate() throws BadSubscriptionException {
    	validateMaxSeats();
    	validateMinSeats();
    }
    
    private void validateMaxSeats() throws BadSubscriptionException {
        if(maxSeats > 999999) throw new BadSubscriptionException( BadSubscriptionException.BIG_MAXSEATS_MESSAGE );
        if(maxSeats < 1) throw new BadSubscriptionException( BadSubscriptionException.SMALL_MAXSEATS_MESSAGE );
        if(minSeats > maxSeats) throw new BadSubscriptionException(BadSubscriptionException.MAXSEATS_LESS_THAN_MINSEATS_MESSAGE);
        if(usedSeats > maxSeats) throw new BadSubscriptionException(BadSubscriptionException.MAXSEATS_LESS_THAN_USEDSEATS_MESSAGE);
    }

    private void validateMinSeats() throws BadSubscriptionException {
        if(minSeats > 999999) throw new BadSubscriptionException( BadSubscriptionException.BIG_MINSEATS_MESSAGE );
        if(minSeats < 1) throw new BadSubscriptionException( BadSubscriptionException.SMALL_MINSEATS_MESSAGE );
        if(minSeats > usedSeats) throw new BadSubscriptionException(BadSubscriptionException.MINSEATS_GREATER_THAN_USEDSEATS_MESSAGE);

    }
}