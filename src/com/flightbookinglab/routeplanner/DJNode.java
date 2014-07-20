package com.flightbookinglab.routeplanner;

import com.flightbookinglab.model.Airport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annarvekar on 7/9/14.
 */
public class DJNode
{
    private int jumps = Integer.MAX_VALUE;
    private DJNode previousAirport;
    private Airport airport;
    public DJNode(Airport airport) {
        this.airport = airport;
    }

    public List<DJNode> getConnectedAirportNodes() {
        List<DJNode> djNodes = new ArrayList<DJNode>(airport.getOutgoingAirports().size());
        for (Airport outgoingAirport : airport.getOutgoingAirports()) {
            djNodes.add(new DJNode(outgoingAirport));
        }
        return djNodes;
    }

    public Airport getAirport() {
        return airport;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DJNode && ((DJNode) obj).airport.getAirportName().equals(this.airport.getAirportName());
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public int getJumps() {
        return jumps;
    }

    public void setPreviousAirport(DJNode previousAirport) {
        this.previousAirport = previousAirport;
    }

    public DJNode getPreviousAirport() {
        return previousAirport;
    }
}
