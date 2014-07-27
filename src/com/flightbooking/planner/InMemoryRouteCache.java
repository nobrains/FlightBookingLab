package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRouteCache implements RouteCache {
    private static RouteCache instance = new InMemoryRouteCache();
    private Map<String, List<Airport>> cache = new ConcurrentHashMap<String, List<Airport>>();

    private InMemoryRouteCache() {
    }

    @Override
    public List<Airport> get(String cacheKey) {
        return cache.get(cacheKey);
    }

    @Override
    public void put(String cacheKey, List<Airport> shortestRoute) {
        cache.put(cacheKey, shortestRoute);
    }

    @Override
    public String createCacheKey(Airport source, Airport destination) {
        return source.getAirportName() + "-" + destination.getAirportName();
    }

    @Override
    public void flush() {
        cache.clear();
    }

    public static RouteCache getInstance() {
        return instance;
    }
}
