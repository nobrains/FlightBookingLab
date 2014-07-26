package com.flightbooking.planner;

/**
 * Created by annarvekar on 7/26/14.
 */
public class RoutePlanningStrategyProvider {

    private static RoutePlanner shortestRouteStrategy = new ShortestRoutePlanner();

    public static RoutePlanner getShortestRouteStrategy() {
        return shortestRouteStrategy;
    }
}
