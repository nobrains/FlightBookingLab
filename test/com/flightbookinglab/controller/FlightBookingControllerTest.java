package com.flightbookinglab.controller;

import com.flightbookinglab.exception.AirportNotFoundException;
import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Airports;
import com.flightbookinglab.model.Route;
import com.flightbookinglab.routeplanner.RoutePlanner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by annarvekar on 7/8/14.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlightBookingControllerTest {

    @Mock
    private Airports airports;
    @Mock
    private RoutePlanner routePlanner;
    @InjectMocks
    private FlightBookingController bookingController = new FlightBookingController();
    private static List<Airport> bombayOutgoingAirports = new ArrayList<Airport>();
    private static List<Airport> delhiOutgoingAirports = new ArrayList<Airport>();
    private static Airport delhiAirport;
    private static Airport bombayAirport;

    @BeforeClass
    public static void initialize() {
        delhiAirport = new Airport("del");
        delhiAirport.setOutgoingAirports(delhiOutgoingAirports);
        bombayAirport = new Airport("bom");
        bombayAirport.setOutgoingAirports(bombayOutgoingAirports);
        bombayOutgoingAirports.add(delhiAirport);
        delhiOutgoingAirports.add(bombayAirport);
    }

    @Test
    public void shouldReturnFlightsGivenSourceAndDestination() throws RouteNotFoundException, AirportNotFoundException {

        stub(airports.getAirport("bom")).toReturn(bombayAirport);
        stub(airports.getAirport("del")).toReturn(delhiAirport);

        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport("bom"));
        expectedShortestRoute.add(new Airport("del"));
        stub(routePlanner.plan(bombayAirport, delhiAirport)).toReturn(expectedShortestRoute);
        Route actualShortestRoute = bookingController.getShortestRoute("bom", "del");
        verifyMethodsCalledOnStubs();
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test(expected = RouteNotFoundException.class)
    public void shouldThrowExceptionIfNoPossibleRouteIsFound() throws RouteNotFoundException, AirportNotFoundException {
        bombayOutgoingAirports.add(delhiAirport);
        delhiOutgoingAirports.remove(bombayAirport);
        stub(airports.getAirport("bom")).toReturn(bombayAirport);
        stub(airports.getAirport("del")).toReturn(delhiAirport);
        stub(routePlanner.plan(delhiAirport, bombayAirport)).toThrow(new RouteNotFoundException());

        bookingController.getShortestRoute("del", "bom");
        verifyMethodsCalledOnStubs();
    }

    @Test(expected = AirportNotFoundException.class)
    public void shouldThrowAirportNotFoundExceptionWhenInvalidAirportIsPassed() throws RouteNotFoundException, AirportNotFoundException {
        stub(airports.getAirport("hyd")).toThrow(new AirportNotFoundException("hyd"));
        bookingController.getShortestRoute("hyd", "bom");
        verify(airports, times(1)).getAirport("hyd");
    }

    private void verifyMethodsCalledOnStubs() throws RouteNotFoundException, AirportNotFoundException {
        verify(routePlanner, times(1)).plan(bombayAirport, delhiAirport);
        verify(airports, times(1)).getAirport("bom");
        verify(airports, times(1)).getAirport("del");
    }
}
