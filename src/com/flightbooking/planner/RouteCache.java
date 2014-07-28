package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.List;

public interface RouteCache {
    List<Airport> get(String cacheKey);

    void put(String cacheKey, List<Airport> shortestRoute);

    String createCacheKey(String source, String destination);

    void flush();
}
