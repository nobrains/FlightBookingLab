package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.List;

/**
 * Created by naresh on 27/07/14.
 */
public interface RouteCache {
    List<Airport> get(String cacheKey);

    void put(String cacheKey, List<Airport> shortestRoute);

    String createCacheKey(Airport source, Airport destination);

    void flush();
}
