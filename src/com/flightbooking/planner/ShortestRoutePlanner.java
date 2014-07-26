package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by annarvekar on 7/26/14.
 */
public class ShortestRoutePlanner implements RoutePlanner {

    @Override
    public List<Airport> plan(Airport source, Airport destination) {

        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(source, destination);
        List<Airport> shortestRouteFromCache = ShortestRouteCache.get(cacheKey);
        if(shortestRouteFromCache!=null){
            return shortestRouteFromCache;
        }
        List<Airport> shortestRoute = Collections.emptyList();
        Map<Airport, Blah> parentMapping = new ConcurrentHashMap<Airport, Blah>();
        Queue<Airport> queueForPerformingBreadthFirstSearch = new LinkedList<Airport>();
        queueForPerformingBreadthFirstSearch.add(source);
        loop:
        while (!queueForPerformingBreadthFirstSearch.isEmpty()) {
            Airport currentAirport = queueForPerformingBreadthFirstSearch.poll();
            for (Airport outgoingAirport : currentAirport.getOutgoingAirports()) {

                add(outgoingAirport, currentAirport, parentMapping);
                if (outgoingAirport.equals(destination)) {
                    shortestRoute = compute(destination, parentMapping);
                    break loop;
                }
                queueForPerformingBreadthFirstSearch.add(outgoingAirport);
            }
        }
        ShortestRouteCache.put(cacheKey,shortestRoute);
        return shortestRoute;
    }

    private void add(Airport airport, Airport parentAirport, Map<Airport, Blah> parentMapping) {
        Blah airportIntegerMap = parentMapping.get(airport);

        if (airportIntegerMap == null) {
            parentMapping.put(airport, new Blah(parentAirport, 1));
        } else {
            Blah airportIntegerMap1 = parentMapping.get(parentAirport);
            if (airportIntegerMap1.getC() < airportIntegerMap.getC()) {
                airportIntegerMap1.incrementC();
            }
        }
    }

    private List<Airport> compute(Airport destinationAirport, Map<Airport, Blah> parentMapping) {
        List<Airport> a = new LinkedList<Airport>();
        Airport l = destinationAirport;
        a.add(destinationAirport);
        while (parentMapping.containsKey(l)) {
            Airport e = parentMapping.get(l).getA();
            a.add(e);
            l = e;
        }
        a.remove(a.size() - 1);
        Collections.reverse(a);
        return a;
    }
    private final class Blah {
        private Airport a;
        private Integer c;

        public Blah(Airport a, Integer c) {
            this.a = a;

            this.c = c;
        }

        public Airport getA() {
            return a;
        }

        public Integer getC() {
            return c;
        }

        public void incrementC() {
            this.c += 1;
        }
    }
}

