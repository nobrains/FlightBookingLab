package com.flightbookinglab.model;

import com.flightbookinglab.exception.AirportNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annarvekar on 7/9/14.
 */
public class Airports{
    private static List<Airport> locallyCachedAirports = new ArrayList<Airport>();
    {
        //locallyCachedAirports. would be statically loaded during application bootstrap
    }

    public static Airport getAirport(String airportName) throws AirportNotFoundException {
        for (Airport airport : locallyCachedAirports) {
            if(airport.getAirportName().equals(airportName)){
                return airport;
            }
        }
        throw new AirportNotFoundException(airportName);
    }

    public static void add(Airport airport) {
        locallyCachedAirports.add(airport);
    }
}
