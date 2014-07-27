package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by annarvekar on 7/26/14.
 */
public class ShortestRouteCache {
    private static Map<String, List<Airport>> cache = new ConcurrentHashMap<String,List<Airport>>();
    public static List<Airport> get(String cacheKey) {
        return cache.get(cacheKey);
    }

    public static void put(String cacheKey, List<Airport> shortestRoute) {
        cache.put(cacheKey,shortestRoute);
    }

    public static String createCacheKey(Airport source, Airport destination) {
        return source.getAirportName()+"-"+destination.getAirportName();
    }
}
