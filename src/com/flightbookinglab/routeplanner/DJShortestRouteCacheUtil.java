package com.flightbookinglab.routeplanner;

import com.flightbookinglab.model.Airport;

/**
 * Created by annarvekar on 7/11/14.
 */
public class DJShortestRouteCacheUtil {

    public static String getCacheKey(Airport sourceAirport, Airport destinationAirport) {
        return sourceAirport.getAirportName()+"-"+destinationAirport.getAirportName();
    }
}
