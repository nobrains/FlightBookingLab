package com.flightbookinglab.routeplanner;

import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Route;

/**
 * Created by annarvekar on 7/8/14.
 */
public interface RoutePlanner {
    public Route plan(Airport source, Airport destination) throws RouteNotFoundException;
}
