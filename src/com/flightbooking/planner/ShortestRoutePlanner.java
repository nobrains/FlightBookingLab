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
//        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(source, destination);
//        List<Airport> cachedRoute = ShortestRouteCache.get(cacheKey);
//        if(cachedRoute != null)
//            return cachedRoute;
        List<Airport> shortestRoute = computeShortestRoute(source, destination);
        Collections.reverse(shortestRoute);
//        ShortestRouteCache.put(cacheKey,shortestRoute);
        return shortestRoute;
    }
    private List<Airport> computeShortestRoute(Airport source, Airport destination) {
        List<Airport> shortestRoute = new ArrayList<Airport>();
        for (Airport outgoingAirport : source.getOutgoingAirports()) {
            if (destination.equals(outgoingAirport)) {
                List<Airport> p = new ArrayList<Airport>();
                p.add(outgoingAirport);
                return p;
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