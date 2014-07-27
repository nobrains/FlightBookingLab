package com.flightbooking;

import com.flightbooking.planner.InMemoryRouteCache;
import com.flightbooking.planner.RouteCache;
import com.flightbooking.planner.ShortestRoutePlanner;

import java.util.*;

/**
 * Created by annarvekar on 7/25/14.
 */
public class Airport {
    private final String airportName;
    private final RouteCache routeCache;
    private final List<Airport> outgoingAirports = new ArrayList<Airport>();

    public Airport(String airportName) {
        this(airportName, InMemoryRouteCache.getInstance());
    }

    public Airport(String airportName, RouteCache routeCache) {
        this.airportName = airportName;
        this.routeCache = routeCache;
    }

    public void addFlightTo(Airport... airports) {
        this.outgoingAirports.addAll(Arrays.asList(airports));
    }

    public List<Airport> getShortestRouteTo(Airport destinationAirport) {

        String cacheKey = routeCache.createCacheKey(this, destinationAirport);
        List<Airport> cachedRoute = routeCache.get(cacheKey);
        if (cachedRoute != null)
            return cachedRoute;
        List<Airport> shortestRoute = new ShortestRoutePlanner().plan(this, destinationAirport);
        routeCache.put(cacheKey, shortestRoute);
        return shortestRoute;
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

    @Override
    public String toString() {
        return airportName;
    }
}
