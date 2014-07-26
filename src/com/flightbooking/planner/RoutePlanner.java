package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.List;

/**
 * Created by annarvekar on 7/26/14.
 */
public interface RoutePlanner {
    public List<Airport> plan(Airport source,Airport destination);
}
