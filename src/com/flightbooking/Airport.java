package com.flightbooking;

import com.flightbooking.planner.ShortestRouteCache;
import com.flightbooking.planner.ShortestRoutePlanner;

import java.util.*;

/**
 * Created by annarvekar on 7/25/14.
 */
public class Airport {
    private final String airportName;
    private List<Airport> outgoingAirports = new ArrayList<Airport>();

    public Airport(String airportName) {
        this.airportName = airportName;
    }

    public List<Airport> getShortestRouteTo(Airport destinationAirport) {

        String cacheKey = ShortestRouteCache.createCacheKey(this, destinationAirport);
        List<Airport> cachedRoute = ShortestRouteCache.get(cacheKey);
        if (cachedRoute != null)
            return cachedRoute;
        List<Airport> shortestRoute = new ShortestRoutePlanner().plan(this, destinationAirport);
        ShortestRouteCache.put(cacheKey,shortestRoute);
        return shortestRoute;
    }

    public void setOutgoingAirports(List<Airport> outgoingAirports) {
        this.outgoingAirports = outgoingAirports;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Airport && ((Airport) obj).airportName.equals(this.airportName);
    }

    public List<Airport> getOutgoingAirports() {
        return outgoingAirports;
    }

    public String getAirportName() {
        return airportName;
    }
}
