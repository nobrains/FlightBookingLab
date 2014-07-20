package com.flightbookinglab.routeplanner;

import com.flightbookinglab.exception.RoutePlannerNotFoundException;

/**
 * Created by annarvekar on 7/20/14.
 */
public class RoutePlannerFactory {
    private static RoutePlanner shortestRoutePlanner = new DijkstraBasedShortestRoutePlanner();
    public static RoutePlanner getRoutePlanner(RoutePlannerType plannerType){
        switch (plannerType){
        case SHORTEST:
            return shortestRoutePlanner;
        default:
            return shortestRoutePlanner;
        }
    }
}
