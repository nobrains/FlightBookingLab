package com.flightbookinglab.exception;

import com.flightbookinglab.model.Airport;

/**
 * Created by annarvekar on 7/8/14.
 */
public class RouteNotFoundException extends Exception {
    public RouteNotFoundException(Airport source, Airport destination) {
        super(String.format("Route between %s and %s not found", source.getAirportName(),destination.getAirportName()));
    }
}
