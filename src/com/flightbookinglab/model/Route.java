package com.flightbookinglab.model;

import java.util.LinkedList;

/**
 * Created by annarvekar on 7/8/14.
 */
public class Route extends LinkedList<Airport> {
    @Override
    public boolean equals(Object o) {
        if (o instanceof Route) {
            Route route = (Route) o;
            if (this.size() != route.size())
                return false;
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).equals(route.get(i)))
                    return false;
            }
            return true;
        }
        return false;
    }
}