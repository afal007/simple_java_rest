package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Subscription {
    private UUID id;
    private UUID customerId;
    private UUID servicePlanId;
    private int usedSeats;
}
