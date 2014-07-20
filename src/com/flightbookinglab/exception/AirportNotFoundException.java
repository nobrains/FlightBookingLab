package com.flightbookinglab.exception;

/**
 * Created by annarvekar on 7/9/14.
 */
public class AirportNotFoundException extends Exception {
    public AirportNotFoundException(String airportName) {
        super(airportName+ " airport not found");
    }
}
