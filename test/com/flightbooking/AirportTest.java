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
    private RouteCache routeCache = mock(InMemoryRouteCache.class);
    private Airport bombayAirport= new Airport("bom");
    private Airport delhiAirport= new Airport("del");
    private Airport cochinAirport= new Airport("cok");
    private Airport bangaloreAirport= new Airport("blr");
    private Airport mysoreAirport= new Airport("mah");
    private Airport goaAirport= new Airport("goi");

    @Test
    public void returnsCachedValueIfExists() {
        bombayAirport= new Airport("bom", routeCache);
        String cacheKey = routeCache.createCacheKey(bombayAirport, delhiAirport);
        List<Airport> expectedRoute = Collections.emptyList();
        when(routeCache.get(cacheKey)).thenReturn(expectedRoute);

        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        assertSame(expectedRoute, actualRoute);
    }

    @Test
    public void cacheValuesForFuture() throws Exception {
        bombayAirport= new Airport("bom", routeCache);
        String cacheKey = routeCache.createCacheKey(bombayAirport, delhiAirport);
        when(routeCache.get(cacheKey)).thenReturn(null);
        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        verify(routeCache).put(cacheKey, actualRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenDestinationAirportIsDirectlyConnected() {
        bombayAirport.addFlightTo(delhiAirport);
        List<Airport> actualShortestRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenSourceHasMultipleOutgoingAirportsOneOfThemBeingTheDestinationAirport() {
        bombayAirport.addFlightTo(cochinAirport, delhiAirport);
        List<Airport> actualShortestRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereIsOneIntermediateHopsBetweenSourceAndDestination() {
        bombayAirport.addFlightTo(delhiAirport);
        bangaloreAirport.addFlightTo(bombayAirport);
        List<Airport> actualShortestRoute = bangaloreAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(bombayAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultipleIntermediateHopsBetweenSourceAndDestination() {
        bombayAirport.addFlightTo(delhiAirport);
        bangaloreAirport.addFlightTo(bombayAirport);
        cochinAirport.addFlightTo(bangaloreAirport);
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = asList(bangaloreAirport, bombayAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultipleRoutesToDestinationAvailable() {
        bombayAirport.addFlightTo(delhiAirport);
        bangaloreAirport.addFlightTo(delhiAirport, bombayAirport);
        cochinAirport.addFlightTo(bangaloreAirport);

        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);

        assertEquals(asList(bangaloreAirport, delhiAirport), actualShortestRoute);
    }

    @Test
    public void shouldReturnEmptyListIfThereIsNoRouteAvailable() {
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = Collections.emptyList();
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRoute() {
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
    public void testEquals() {
        Airport del = new Airport("del");
        assertEquals(del, new Airport("del"));
        assertFalse(new Airport("bom").equals(del));
    }

    @After
    public void cleanUp(){
        InMemoryRouteCache.getInstance().flush();
    }
}
