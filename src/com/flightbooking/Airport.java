package com.flightbooking;

import com.flightbooking.planner.InMemoryRouteCache;
import com.flightbooking.planner.RouteCache;
import com.flightbooking.planner.ShortestRoutePlanner;

import java.util.*;

public class Airport {
    private final String airportCode;
    private final RouteCache routeCache;
    private final List<Airport> outgoingAirports = new ArrayList<Airport>();

    public Airport(String airportCode) {
        this(airportCode, InMemoryRouteCache.getInstance());
    }

    public Airport(String airportCode, RouteCache routeCache) {
        this.airportCode = airportCode;
        this.routeCache = routeCache;
    }

    public void addFlightTo(Airport... airports) {
        this.outgoingAirports.addAll(Arrays.asList(airports));
    }

    public List<Airport> getShortestRouteTo(Airport destinationAirport) {

        String cacheKey = routeCache.createCacheKey(this.airportCode, destinationAirport.airportCode);
        List<Airport> cachedRoute = routeCache.get(cacheKey);
        if (cachedRoute != null)
            return cachedRoute;
        List<Airport> shortestRoute = new ShortestRoutePlanner().plan(this, destinationAirport);
        routeCache.put(cacheKey, shortestRoute);
        return shortestRoute;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Airport && ((Airport) obj).airportCode.equals(this.airportCode);
    }

    public List<Airport> getOutgoingAirports() {
        return outgoingAirports;
    }

    @Override
    public String toString() {
        return airportCode;
    }
}
