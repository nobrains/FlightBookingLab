package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by annarvekar on 7/26/14.
 */
public class ShortestRoutePlanner implements RoutePlanner {

    @Override
    public List<Airport> plan(Airport source, Airport destination) {
        List<Airport> shortestRoute = computeShortestRoute(source, destination);
        return reverse(shortestRoute);
    }

    private List<Airport> reverse(List<Airport> shortestRoute) {
        Collections.reverse(shortestRoute);
        return shortestRoute;
    }

    private List<Airport> computeShortestRoute(Airport source, Airport destination) {
        List<Airport> shortestRoute = new ArrayList<Airport>();
        for (Airport outgoingAirport : source.getOutgoingAirports()) {
            if (destination.equals(outgoingAirport)) {
                List<Airport> route = new ArrayList<Airport>();
                route.add(outgoingAirport);
                return route;
            }
            List<Airport> localShortestRoute = computeShortestRoute(outgoingAirport, destination);
            localShortestRoute.add(outgoingAirport);

            shortestRoute = smallerOf(shortestRoute, localShortestRoute);
        }
        return shortestRoute;
    }

    private List<Airport> smallerOf(List<Airport> shortestRoute, List<Airport> localShortestRoute) {
        return (shortestRoute.isEmpty() || localShortestRoute.size() < shortestRoute.size())?localShortestRoute:shortestRoute;
    }
}