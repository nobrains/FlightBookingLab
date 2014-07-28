package com.flightbooking.planner;

import com.flightbooking.Airport;

import java.util.List;

public interface RoutePlanner {
    List<Airport> best(List<Airport> winner, List<Airport> contender);

    RoutePlanner SHORTEST = new RoutePlanner() {
        @Override
        public List<Airport> best(List<Airport> winner, List<Airport> contender) {
            return (winner.isEmpty() || contender.size() < winner.size()) ? contender : winner;
        }
    };
}
