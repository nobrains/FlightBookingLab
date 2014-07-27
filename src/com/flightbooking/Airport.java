package com.flightbooking;

import com.flightbooking.planner.InMemoryRouteCache;
import com.flightbooking.planner.RouteCache;
import com.flightbooking.planner.RoutePlanner;

import java.util.*;

import static java.util.Arrays.asList;

public class Airport {
    private final String airportCode;
    private final RouteCache routeCache;
    private final RoutePlanner routePlanner;
    private final List<Airport> neighbours = new ArrayList<Airport>();

    public Airport(String airportCode) {
        this(airportCode, InMemoryRouteCache.getInstance(), RoutePlanner.SHORTEST);
    }

    public Airport(String airportCode, RouteCache routeCache, RoutePlanner routePlanner) {
        this.airportCode = airportCode;
        this.routeCache = routeCache;
        this.routePlanner = routePlanner;
    }

    public void addFlightTo(Airport... airports) {
        this.neighbours.addAll(Arrays.asList(airports));
    }

    public List<Airport> getBestRouteTo(Airport destinationAirport) {
        String cacheKey = routeCache.createCacheKey(this.airportCode, destinationAirport.airportCode);
        List<Airport> cachedRoute = routeCache.get(cacheKey);
        if (cachedRoute != null)
            return cachedRoute;
        List<Airport> bestRoute = computeBestRoute(this, destinationAirport);
        Collections.reverse(bestRoute);
        routeCache.put(cacheKey, bestRoute);
        return bestRoute;
    }

    private List<Airport> computeBestRoute(Airport source, Airport destination) {
        List<Airport> bestRoute = new ArrayList<Airport>();
        for (Airport neighbour : source.neighbours) {
            if (destination.equals(neighbour))
                return new ArrayList<Airport>(asList(neighbour));
            List<Airport> localBestRoute = computeBestRoute(neighbour, destination);
            localBestRoute.add(neighbour);

            bestRoute = routePlanner.best(bestRoute, localBestRoute);
        }
        return bestRoute;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Airport && ((Airport) obj).airportCode.equals(this.airportCode);
    }

    @Override
    public String toString() {
        return airportCode;
    }
}
