package com.flightbookinglab.controller;

import com.flightbookinglab.exception.AirportNotFoundException;
import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Airports;
import com.flightbookinglab.model.Route;
import com.flightbookinglab.routeplanner.RoutePlanner;
import com.flightbookinglab.routeplanner.RoutePlannerFactory;
import com.flightbookinglab.routeplanner.RoutePlannerType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by annarvekar on 7/8/14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FlightBookingController.class, Airports.class, RoutePlannerFactory.class})
public class FlightBookingControllerTest {

    @Mock
    private RoutePlanner routePlanner;
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

    @Before
    public void setUp() throws AirportNotFoundException {
        mockStatic(Airports.class);
        mockStatic(RoutePlannerFactory.class);
        when(Airports.getAirport("bom")).thenReturn(bombayAirport);
        when(Airports.getAirport("del")).thenReturn(delhiAirport);
        when(RoutePlannerFactory.getRoutePlanner(RoutePlannerType.SHORTEST)).thenReturn(routePlanner);
    }

    @Test
    public void shouldCallRoutePlannerFactoryForAppropriatePlannerImplementation() throws RouteNotFoundException, AirportNotFoundException {
        bookingController.getShortestRoute("bom", "del");
        verifyStatic(times(1));
        RoutePlannerFactory.getRoutePlanner(RoutePlannerType.SHORTEST);
    }

    @Test
    public void shouldCallAirportsHoldersToGetAppropriateAirportFromInputString() throws RouteNotFoundException, AirportNotFoundException {
        bookingController.getShortestRoute("bom", "del");
        verifyStatic(times(1));
        Airports.getAirport("bom");
        verifyStatic(times(1));
        Airports.getAirport("del");
    }

    @Test
    public void shouldMakeCallToRoutePlannerReturnedFromRoutePlannerFactory() throws RouteNotFoundException, AirportNotFoundException {
        bookingController.getShortestRoute("bom", "del");
        verify(routePlanner, times(1)).plan(bombayAirport, delhiAirport);
    }

    @Test
    public void shouldReturnFlightsGivenSourceAndDestination() throws RouteNotFoundException, AirportNotFoundException {
        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport("bom"));
        expectedShortestRoute.add(new Airport("del"));
        when(routePlanner.plan(bombayAirport, delhiAirport)).thenReturn(expectedShortestRoute);
        Route actualShortestRoute = bookingController.getShortestRoute("bom", "del");
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test(expected = RouteNotFoundException.class)
    public void shouldThrowExceptionIfRoutePlannerFindsNoRoute() throws RouteNotFoundException, AirportNotFoundException {
        bombayOutgoingAirports.add(delhiAirport);
        delhiOutgoingAirports.remove(bombayAirport);
        when(routePlanner.plan(delhiAirport, bombayAirport)).thenThrow(new RouteNotFoundException(delhiAirport,bombayAirport));
        bookingController.getShortestRoute("del", "bom");
    }

    @Test(expected = AirportNotFoundException.class)
    public void shouldThrowExceptionWhenAirportsHolderCantFindAirport() throws RouteNotFoundException, AirportNotFoundException {
        mockStatic(Airports.class);
        when(Airports.getAirport("hyd")).thenThrow(new AirportNotFoundException("hyd"));
        bookingController.getShortestRoute("hyd", "bom");
    }
}
