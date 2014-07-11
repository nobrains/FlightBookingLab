package com.flightbookinglab.model;

import java.util.List;

/**
 * Created by annarvekar on 7/8/14.
 */
public class Airport {
    private final String airportName;
    public List<Airport> outgoingAirports;
    public List<Runway> runways;

    public Airport(String airport, List<Airport> outgoingAirports) {
        this.airportName = airport;
        this.outgoingAirports = outgoingAirports;
    }

    public String getAirportName() {
        return airportName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Airport && ((Airport) obj).airportName.equals(airportName);
    }
}
