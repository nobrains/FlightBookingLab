package com.flightbookinglab.cache;

import com.flightbookinglab.model.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by annarvekar on 7/10/14.
 */
public class DJShortestRouteCache {
    private static Map<String, Route> map = new HashMap<String, Route>();

    public Route get(String s) {
        return map.get(s);
    }

    public void put(String s, Route route) {
        map.put(s,route);
    }
}
