package com.flightbooking;

import com.flightbooking.planner.*;
import org.junit.After;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AirportTest {
    public static final String BOM = "bom";
    public static final String DEL = "del";
    private RouteCache routeCache = mock(InMemoryRouteCache.class);
    private Airport bombayAirport= new Airport(BOM);
    private Airport delhiAirport= new Airport(DEL);
    private Airport cochinAirport= new Airport("cok");
    private Airport bangaloreAirport= new Airport("blr");
    private Airport mysoreAirport= new Airport("mah");
    private Airport goaAirport= new Airport("goi");

    @Test
    public void returnsCachedValueIfExists() {
        bombayAirport= new Airport(BOM, routeCache);
        String cacheKey = routeCache.createCacheKey(BOM, DEL);
        List<Airport> expectedRoute = Collections.emptyList();
        when(routeCache.get(cacheKey)).thenReturn(expectedRoute);

        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        assertSame(expectedRoute, actualRoute);
    }

    @Test
    public void cacheValuesForFuture() throws Exception {
        bombayAirport= new Airport(BOM, routeCache);
        String cacheKey = routeCache.createCacheKey(BOM, DEL);
        when(routeCache.get(cacheKey)).thenReturn(null);
        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        verify(routeCache).put(cacheKey, actualRoute);
    }

    @Test
    public void shortestRouteIsDirectFlightsToNeighbouringAirport() {
        bombayAirport.addFlightTo(delhiAirport);
        List<Airport> actualShortestRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shortestRouteIsDirectFlightsEvenIfThereAreOtherMultiStopFlights() {
        bombayAirport.addFlightTo(cochinAirport, delhiAirport);
        List<Airport> actualShortestRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shortestRouteIsOneStopFlightWhenDirectFlightsDoNotExist() {
        bombayAirport.addFlightTo(delhiAirport);
        bangaloreAirport.addFlightTo(bombayAirport);
        List<Airport> actualShortestRoute = bangaloreAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(bombayAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shortestRouteWithMultipleStopsWhenDirectFlightsDoNotExist() {
        bombayAirport.addFlightTo(delhiAirport);
        bangaloreAirport.addFlightTo(bombayAirport);
        cochinAirport.addFlightTo(bangaloreAirport);
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(bangaloreAirport, bombayAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shortestRouteIsTheOneWithLeastNumberOfStops() {
        bombayAirport.addFlightTo(delhiAirport);
        bangaloreAirport.addFlightTo(delhiAirport, bombayAirport);
        cochinAirport.addFlightTo(bangaloreAirport);

        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);

        assertEquals(asList(bangaloreAirport, delhiAirport), actualShortestRoute);
    }

    @Test
    public void notAllAirportsAreReachable() {
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        assertEquals(Collections.<Airport>emptyList(), actualShortestRoute);
    }

    @Test
    public void multiStopFlightIsTheShortestFlightWhenDirectFlightsDoNotExist() {
        mysoreAirport.addFlightTo(goaAirport, cochinAirport);
        goaAirport.addFlightTo(bombayAirport);
        bombayAirport.addFlightTo(delhiAirport);
        cochinAirport.addFlightTo(delhiAirport);
        delhiAirport.addFlightTo(bangaloreAirport);
        List<Airport> actualShortestRoute = mysoreAirport.getShortestRouteTo(bangaloreAirport);
        List<Airport> expectedShortestRoute = asList(cochinAirport, delhiAirport, bangaloreAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void weCheckAirportCodeToVerifyIfItsTheSameAirport() {
        Airport del = new Airport(DEL);
        assertEquals(del, new Airport(DEL));
        assertFalse(new Airport(BOM).equals(del));
    }

    @After
    public void cleanUp(){
        InMemoryRouteCache.getInstance().flush();
    }
}
