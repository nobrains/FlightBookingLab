package com.flightbooking.planner;

import com.flightbooking.Airport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by annarvekar on 7/26/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ShortestRoutePlanner.class,ShortestRouteCache.class})
public class ShortestRoutePlannerTest{
    private Airport bombayAirport;
    private Airport delhiAirport;
    private Airport cochinAirport;
    private Airport bangaloreAirport;
    private Airport mysoreAirport;
    private Airport goaAirport;
    private ShortestRoutePlanner routePlanner;

    @Before
    public void setUp() {
        bombayAirport = new Airport("bom");
        delhiAirport = new Airport("del");
        cochinAirport = new Airport("cok");
        bangaloreAirport = new Airport("blr");
        mysoreAirport = new Airport("mah");
        goaAirport = new Airport("goi");
        routePlanner = new ShortestRoutePlanner();
        PowerMockito.mockStatic(ShortestRouteCache.class);
    }

    @Test
    public void shouldCheckIfRouteExistsInCache(){
//        PowerMockito.mockStatic(ShortestRouteCache.class);
        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(bombayAirport, delhiAirport);
        when(ShortestRouteCache.get(cacheKey)).thenReturn(null);

        bombayAirport.getShortestRouteTo(delhiAirport);

        verifyStatic(times(1));
        ShortestRouteCache.get(cacheKey);
    }

    @Test
    public void shouldReturnValueInCacheIfExists() {
//        PowerMockito.mockStatic(ShortestRouteCache.class);
        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(bombayAirport, delhiAirport);
        List<Airport> expectedRoute = Collections.<Airport>emptyList();
        when(ShortestRouteCache.get(cacheKey)).thenReturn(expectedRoute);

        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        assertEquals(expectedRoute,actualRoute);
    }

    @Test
    public void shouldInsertIntoCacheIfNotPresentAlready() throws Exception {
//        PowerMockito.mockStatic(ShortestRouteCache.class);
        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(bombayAirport, delhiAirport);
        when(ShortestRouteCache.get(cacheKey)).thenReturn(null);
        doNothing().when(ShortestRouteCache.class,"put",cacheKey,Collections.<Airport>emptyList());
        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        verifyStatic(times(1));
        ShortestRouteCache.put(cacheKey, actualRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenDestinationAirportIsDirectlyConnected() {
        foo(bombayAirport, delhiAirport);
        List<Airport> actualShortestRoute = routePlanner.plan(bombayAirport, delhiAirport);
        List<Airport> expectedShortestRoute = new ArrayList<Airport>();
        expectedShortestRoute.add(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenSourceHasMultipleOutgoingAirportsOneOfThemBeingTheDestinationAirport() {
        foo(bombayAirport, cochinAirport, delhiAirport );
        List<Airport> actualShortestRoute = routePlanner.plan(bombayAirport, delhiAirport);
        List<Airport> expectedShortestRoute = new ArrayList<Airport>();
        expectedShortestRoute.add(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereIsOneIntermediateHopsBetweenSourceAndDestination() {
        foo(bombayAirport, delhiAirport);
        foo(bangaloreAirport, bombayAirport);
        List<Airport> actualShortestRoute = routePlanner.plan(bangaloreAirport, delhiAirport);
        List<Airport> expectedShortestRoute = new LinkedList<Airport>();
        expectedShortestRoute.add(bombayAirport);
        expectedShortestRoute.add(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultipleIntermediateHopsBetweenSourceAndDestination() {
        foo(bombayAirport, delhiAirport);
        foo(bangaloreAirport, bombayAirport);
        foo(cochinAirport,bangaloreAirport);
        List<Airport> actualShortestRoute = routePlanner.plan(cochinAirport, delhiAirport);
        List<Airport> expectedShortestRoute = new LinkedList<Airport>();
        expectedShortestRoute.add(bangaloreAirport);
        expectedShortestRoute.add(bombayAirport);
        expectedShortestRoute.add(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultipleRoutesToDestinationAvailable(){
        foo(bombayAirport, delhiAirport);
        foo(bangaloreAirport, delhiAirport, bombayAirport);
        foo(cochinAirport,bangaloreAirport);
        List<Airport> actualShortestRoute = routePlanner.plan(cochinAirport, delhiAirport);
        List<Airport> expectedShortestRoute = new LinkedList<Airport>();
        expectedShortestRoute.add(bangaloreAirport);
        expectedShortestRoute.add(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnEmptyListIfThereIsNoRouteAvailable(){
        List<Airport> actualShortestRoute = routePlanner.plan(cochinAirport, delhiAirport);
        List<Airport> expectedShortestRoute = new LinkedList<Airport>();
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void foo(){
        foo(mysoreAirport, goaAirport, cochinAirport);
        foo(goaAirport, bombayAirport);
        foo(bombayAirport, delhiAirport);
        foo(cochinAirport,delhiAirport);
        foo(delhiAirport, bangaloreAirport);
        List<Airport> actualShortestRoute = routePlanner.plan(mysoreAirport, bangaloreAirport);
        List<Airport> expectedShortestRoute = new LinkedList<Airport>();
        expectedShortestRoute.add(cochinAirport);
        expectedShortestRoute.add(delhiAirport);
        expectedShortestRoute.add(bangaloreAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    private void foo(Airport airport, Airport... outGoingAirports) {
        airport.setOutgoingAirports(Arrays.asList(outGoingAirports));
    }

    @After
    public void cleanStaticCache(){
        try {
            Field field = ShortestRouteCache.class.getDeclaredField("cache");
            field.setAccessible(true);
            field.set(null,new ConcurrentHashMap<String,List<Airport>>());
        } catch (Exception e) {}
    }
}
