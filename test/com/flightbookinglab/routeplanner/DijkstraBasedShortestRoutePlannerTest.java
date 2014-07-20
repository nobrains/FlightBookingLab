package com.flightbookinglab.routeplanner;

import com.flightbookinglab.cache.DJShortestRouteCache;
import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Route;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by annarvekar on 7/10/14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DijkstraBasedShortestRoutePlanner.class, DJShortestRouteCache.class})
public class DijkstraBasedShortestRoutePlannerTest {

    private DijkstraBasedShortestRoutePlanner dijkstraBasedShortestRoutePlanner = new DijkstraBasedShortestRoutePlanner();

    private static List<Airport> bombayOutgoingAirports = new ArrayList<Airport>();
    private static List<Airport> delhiOutgoingAirports = new ArrayList<Airport>();
    private static List<Airport> goaOutgoingAirports = new ArrayList<Airport>();
    private static List<Airport> bangaloreOutgoingAirports = new ArrayList<Airport>();
    private static final String DELHI = "del";
    private static Airport delhiAirport = new Airport(DELHI);
    private static final String BOMBAY = "bom";
    private static Airport bombayAirport = new Airport(BOMBAY);
    private static final String GOA = "goi";
    private static Airport goaAirport = new Airport(GOA);
    private static final String BANGALORE = "blr";
    private static Airport bangaloreAirport = new Airport(BANGALORE);

    @BeforeClass
    public static void initialize() {
        delhiAirport.setOutgoingAirports(delhiOutgoingAirports);
        bombayAirport.setOutgoingAirports(bombayOutgoingAirports);
        bangaloreAirport.setOutgoingAirports(bangaloreOutgoingAirports);
        goaAirport.setOutgoingAirports(goaOutgoingAirports);
    }

    @Before
    public void setUp(){
        PowerMockito.mockStatic(DJShortestRouteCache.class);
    }

    @Test
    public void shouldMakeCallToCacheToSeeIfAlreadyShortestPathExists() throws Exception {
        String cacheKey = DJShortestRouteCacheUtil.getCacheKey(goaAirport, delhiAirport);
        try {
            dijkstraBasedShortestRoutePlanner.plan(goaAirport, delhiAirport);
        } catch (RouteNotFoundException e) {
        }
        PowerMockito.verifyStatic(times(1));
        DJShortestRouteCache.get(cacheKey);
    }

    @Test
    public void shouldSetShortestRouteIfNotPresentInCache() throws Exception {
        Route expectedRoute = new Route();
        expectedRoute.add(bombayAirport);
        expectedRoute.add(delhiAirport);
        String cacheKey = DJShortestRouteCacheUtil.getCacheKey(bombayAirport, delhiAirport);
        doNothing().when(DJShortestRouteCache.class,"put",cacheKey,expectedRoute);
        addRoutes(bombayOutgoingAirports, delhiAirport);
        Route actualRoute = dijkstraBasedShortestRoutePlanner.plan(bombayAirport, delhiAirport);

        verifyStatic(times(1));
        DJShortestRouteCache.put(cacheKey, actualRoute);
    }

    @Test(expected = RouteNotFoundException.class)
    public void shouldThrowExceptionIfNoPathFound() throws Exception {
        new DijkstraBasedShortestRoutePlanner().plan(delhiAirport, bombayAirport);
    }

    @Test
    public void shouldReturnShortestRoute() throws Exception {
        addRoutes(bombayOutgoingAirports, delhiAirport);
        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport(BOMBAY));
        expectedShortestRoute.add(new Airport(DELHI));
        Route actualShortestRoute = new DijkstraBasedShortestRoutePlanner().plan(bombayAirport, delhiAirport);
        assertTrue(expectedShortestRoute.equals(actualShortestRoute));
    }

    @Test
    public void shouldReturnShortestRouteWithMultiCitiesHoping() throws RouteNotFoundException {
        addRoutes(goaOutgoingAirports, bombayAirport);
        addRoutes(bombayOutgoingAirports, delhiAirport);
        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport(GOA));
        expectedShortestRoute.add(new Airport(BOMBAY));
        expectedShortestRoute.add(new Airport(DELHI));
        Route actualShortestRoute = dijkstraBasedShortestRoutePlanner.plan(goaAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultiplePathsAvailable() throws RouteNotFoundException {
        addRoutes(bombayOutgoingAirports, delhiAirport);
        addRoutes(goaOutgoingAirports, bombayAirport, bangaloreAirport);
        addRoutes(bangaloreOutgoingAirports, bombayAirport);

        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport(GOA));
        expectedShortestRoute.add(new Airport(BOMBAY));
        expectedShortestRoute.add(new Airport(DELHI));
        Route actualShortestRoute = dijkstraBasedShortestRoutePlanner.plan(goaAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    private void addRoutes(List<Airport> listToWhichAirportNeedsToBeAdded, Airport... airport) {
        Collections.addAll(listToWhichAirportNeedsToBeAdded, airport);
    }
}
