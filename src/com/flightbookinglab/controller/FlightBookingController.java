package com.flightbookinglab.controller;

import com.flightbookinglab.model.Route;
import com.flightbookinglab.exception.AirportNotFoundException;
import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.routeplanner.RoutePlanner;
import com.flightbookinglab.model.Airports;

/**
 * Created by annarvekar on 7/8/14.
 */
public class FlightBookingController {
    private RoutePlanner routePlanner;
    private Airports airports;

    public Route getShortestRoute(String source, String destination) throws RouteNotFoundException, AirportNotFoundException {
        return routePlanner.plan(airports.getAirport(source), airports.getAirport(destination));
    }
}
