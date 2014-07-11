package com.flightbookinglab.routeplanner;

import com.flightbookinglab.cache.DJShortestRouteCache;
import com.flightbookinglab.exception.RouteNotFoundException;
import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Route;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Created by annarvekar on 7/10/14.
 */
@RunWith(MockitoJUnitRunner.class)
public class DijkstraBasedShortestRoutePlannerTest {

    @Mock
    private DJShortestRouteCache djShortestRouteCache;
    @InjectMocks
    private DijkstraBasedShortestRoutePlanner dijkstraBasedShortestRoutePlanner = DijkstraBasedShortestRoutePlanner.getInstance();

    private static List<Airport> bombayOutgoingAirports = new ArrayList<Airport>();
    private static List<Airport> delhiOutgoingAirports = new ArrayList<Airport>();
    private static List<Airport> goaOutgoingAirports = new ArrayList<Airport>();
    private static Airport delhiAirport = new Airport("del", delhiOutgoingAirports);
    private static Airport bombayAirport = new Airport("bom", bombayOutgoingAirports);
    private static Airport goaAirport = new Airport("goi", goaOutgoingAirports);
    private static List<Airport> bangaloreOutgoingAirports = new ArrayList<Airport>();
    private static Airport bangaloreAirport = new Airport("blr", bangaloreOutgoingAirports);

    @Test(expected = RouteNotFoundException.class)
    public void shouldThrowExceptionIfNoPathFound() throws RouteNotFoundException {
        dijkstraBasedShortestRoutePlanner.plan(delhiAirport, bombayAirport);
    }

    @Test
    public void shouldReturnShortestRoute() throws RouteNotFoundException {
        addRoutes(bombayOutgoingAirports, delhiAirport);
        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport("bom", null));
        expectedShortestRoute.add(new Airport("del", null));
        Route actualShortestRoute = dijkstraBasedShortestRoutePlanner.plan(bombayAirport, delhiAirport);

        assertTrue(expectedShortestRoute.equals(actualShortestRoute));
    }

    private void addRoutes(List<Airport> listToWhichAirportNeedsToBeAdded, Airport... airport) {
        Collections.addAll(listToWhichAirportNeedsToBeAdded, airport);
    }

    @Test
    public void shouldReturnShortestRouteWithMultiCitiesHoping() throws RouteNotFoundException {
        addRoutes(goaOutgoingAirports, bombayAirport);
        addRoutes(bombayOutgoingAirports, delhiAirport);
        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport("goi", null));
        expectedShortestRoute.add(new Airport("bom", null));
        expectedShortestRoute.add(new Airport("del", null));
        Route actualShortestRoute = dijkstraBasedShortestRoutePlanner.plan(goaAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultiplePathsAvailable() throws RouteNotFoundException {
        addRoutes(bombayOutgoingAirports, delhiAirport);
        addRoutes(goaOutgoingAirports, bombayAirport, bangaloreAirport);
        addRoutes(bangaloreOutgoingAirports, bombayAirport);

        Route expectedShortestRoute = new Route();
        expectedShortestRoute.add(new Airport("goi", null));
        expectedShortestRoute.add(new Airport("bom", null));
        expectedShortestRoute.add(new Airport("del", null));
        Route actualShortestRoute = dijkstraBasedShortestRoutePlanner.plan(goaAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldMakeCallToCacheToSeeIfAlreadyShortestPathExists() {
        try {
            dijkstraBasedShortestRoutePlanner.plan(goaAirport, delhiAirport);
        } catch (RouteNotFoundException e) {
        }
        String cacheKey = DJShortestRouteCacheUtil.getCacheKey(goaAirport, delhiAirport);
        verify(djShortestRouteCache).get(cacheKey);
    }

    @Test
    public void shouldSetShortestRouteIfNotPresentInCache() throws RouteNotFoundException {
        String cacheKey = DJShortestRouteCacheUtil.getCacheKey(bombayAirport, delhiAirport);
        Route shortestRoute = null;
        stub(djShortestRouteCache.get(cacheKey)).toReturn(null);
        addRoutes(bombayOutgoingAirports, delhiAirport);
        shortestRoute = dijkstraBasedShortestRoutePlanner.plan(bombayAirport, delhiAirport);
        verify(djShortestRouteCache).put(cacheKey, shortestRoute);
    }
}
