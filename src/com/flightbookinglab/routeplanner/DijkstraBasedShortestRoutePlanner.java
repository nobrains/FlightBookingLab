package com.flightbookinglab.routeplanner;

import com.flightbookinglab.cache.DJShortestRouteCache;
import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Route;

import java.util.*;

/**
 * Created by annarvekar on 7/8/14.
 */
public class DijkstraBasedShortestRoutePlanner implements RoutePlanner {

    @Override
    public Route plan(Airport source, Airport destination) throws RouteNotFoundException {
        String cacheKey = DJShortestRouteCacheUtil.getCacheKey(source, destination);
        Route shortestRoute = DJShortestRouteCache.get(cacheKey);
        if (shortestRoute != null) {
            return shortestRoute;
        }
        DJNode sourceNode = new DJNode(source);
        DJNode destinationNode = new DJNode(destination);
        sourceNode.setJumps(0);
        Queue<DJNode> djNodes = new LinkedList<DJNode>();
        djNodes.add(sourceNode);
        loop:
        while (!djNodes.isEmpty()) {
            DJNode djNode = djNodes.poll();
            for (DJNode e : djNode.getConnectedAirportNodes()) {
                int distanceThroughU = djNode.getJumps() + 1;
                if (distanceThroughU < e.getJumps()) {
                    djNodes.remove(e);

                    e.setJumps(distanceThroughU);
                    e.setPreviousAirport(djNode);
                    djNodes.add(e);

                }
                if (e.equals(destinationNode)) {
                    destinationNode = e;
                    break loop;
                }
            }
        }
        shortestRoute = getShortestPathTo(destinationNode);
        if (shortestRoute.size() == 1) {
            throw new RouteNotFoundException(source,destination);
        }
        DJShortestRouteCache.put(cacheKey, shortestRoute);
        return shortestRoute;
    }

    private Route getShortestPathTo(DJNode target) {
        Route route = new Route();
        for (DJNode node = target; node != null; node = node.getPreviousAirport())
            route.add(node.getAirport());

        Collections.reverse(route);
        return route;
    }
}
