package com.flightbookinglab.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annarvekar on 7/8/14.
 */
public class Airport {
    private final String airportName;
    private List<Airport> outgoingAirports = new ArrayList<Airport>();

    public Airport(String airport) {
        this.airportName = airport;
    }

    public String getAirportName() {
        return airportName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Airport && ((Airport) obj).airportName.equals(airportName);
    }

    public void setOutgoingAirports(List<Airport> outgoingAirports) {
        this.outgoingAirports = outgoingAirports;
    }

    public List<Airport> getOutgoingAirports() {
        return outgoingAirports;
    }
}
