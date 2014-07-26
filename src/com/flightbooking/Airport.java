package com.flightbooking;

import com.flightbooking.planner.RoutePlanner;
import com.flightbooking.planner.RoutePlanningStrategyProvider;

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
        RoutePlanner shortestRouteStrategy = RoutePlanningStrategyProvider.getShortestRouteStrategy();
        List<Airport> shortestRoute = shortestRouteStrategy.plan(this, destinationAirport);
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
