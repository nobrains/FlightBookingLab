package com.flightbookinglab.model;

import com.flightbookinglab.exception.AirportNotFoundException;

import java.util.ArrayList;

/**
 * Created by annarvekar on 7/9/14.
 */
public class Airports extends ArrayList<Airport>{

    public Airport getAirport(String airportName) throws AirportNotFoundException {
        for (Airport airport : this) {
            if(airport.getAirportName().equals(airportName)){
                return airport;
            }
        }
        throw new AirportNotFoundException(airportName);
    }
}
