package com.flightbooking.planner;

import com.flightbooking.Airport;

/**
 * Created by annarvekar on 7/26/14.
 */
public class ShortestRouteCacheKeyUtil {
    public static String getCacheKey(Airport sourceAirport, Airport destinationAirport) {
        return sourceAirport.getAirportName()+"-"+destinationAirport.getAirportName();
    }
}
