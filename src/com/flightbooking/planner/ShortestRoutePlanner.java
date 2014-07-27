package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class ShortestRoutePlanner {

    public List<Airport> plan(Airport source, Airport destination) {
        List<Airport> shortestRoute = computeShortestRoute(source, destination);
        Collections.reverse(shortestRoute);
        return shortestRoute;
    }

    private List<Airport> computeShortestRoute(Airport source, Airport destination) {
        List<Airport> shortestRoute = new ArrayList<Airport>();
        for (Airport outgoingAirport : source.getOutgoingAirports()) {
            if (destination.equals(outgoingAirport))
                return new ArrayList<Airport>(asList(outgoingAirport));
            List<Airport> localShortestRoute = computeShortestRoute(outgoingAirport, destination);
            localShortestRoute.add(outgoingAirport);

            shortestRoute = smallerOf(shortestRoute, localShortestRoute);
        }
        return shortestRoute;
    }

    private List<Airport> smallerOf(List<Airport> shortestRoute, List<Airport> localShortestRoute) {
        return (shortestRoute.isEmpty() || localShortestRoute.size() < shortestRoute.size()) ? localShortestRoute : shortestRoute;
    }
}