package com.flightbooking;

import com.flightbooking.planner.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by annarvekar on 7/24/14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Airport.class, RoutePlanningStrategyProvider.class, ShortestRouteCache.class})
public class AirportTest {

    @Mock
    private RoutePlanner mockedStrategy;
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
        PowerMockito.mockStatic(RoutePlanningStrategyProvider.class);
        when(RoutePlanningStrategyProvider.getShortestRouteStrategy()).thenReturn(new ShortestRoutePlanner());
    }

    @Test
    public void testShortestRouteWithMockedStrategy() {
        List<Airport> expectedRoute = Collections.emptyList();
        when(mockedStrategy.plan(bombayAirport, delhiAirport)).thenReturn(expectedRoute);
        when(RoutePlanningStrategyProvider.getShortestRouteStrategy()).thenReturn(mockedStrategy);
        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        planningStrategyProviderShouldBeCalledToGetAStrategy();
        shouldReturnListOfAirportIndicatingTheShortestRouteAsReturnedByTheStrategy(expectedRoute, actualRoute);
    }

    @Test
    public void shouldCheckIfRouteExistsInCache() {
        PowerMockito.mockStatic(ShortestRouteCache.class);
        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(bombayAirport, delhiAirport);
        when(ShortestRouteCache.get(cacheKey)).thenReturn(null);

        bombayAirport.getShortestRouteTo(delhiAirport);

        verifyStatic(times(1));
        ShortestRouteCache.get(cacheKey);
    }

    @Test
    public void shouldReturnValueInCacheIfExists() {
        PowerMockito.mockStatic(ShortestRouteCache.class);
        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(bombayAirport, delhiAirport);
        List<Airport> expectedRoute = Collections.emptyList();
        when(ShortestRouteCache.get(cacheKey)).thenReturn(expectedRoute);

        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        assertEquals(expectedRoute, actualRoute);
    }

    @Test
    public void shouldInsertIntoCacheIfNotPresentAlready() throws Exception {
        PowerMockito.mockStatic(ShortestRouteCache.class);
        String cacheKey = ShortestRouteCacheKeyUtil.getCacheKey(bombayAirport, delhiAirport);
        when(ShortestRouteCache.get(cacheKey)).thenReturn(null);
        doNothing().when(ShortestRouteCache.class, "put", cacheKey, Collections.<Airport>emptyList());
        List<Airport> actualRoute = bombayAirport.getShortestRouteTo(delhiAirport);

        verifyStatic(times(1));
        ShortestRouteCache.put(cacheKey, actualRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenDestinationAirportIsDirectlyConnected() {
        setOutgoingAirport(bombayAirport, delhiAirport);
        List<Airport> actualShortestRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = createListWith(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenSourceHasMultipleOutgoingAirportsOneOfThemBeingTheDestinationAirport() {
        setOutgoingAirport(bombayAirport, cochinAirport, delhiAirport);
        List<Airport> actualShortestRoute = bombayAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = createListWith(delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereIsOneIntermediateHopsBetweenSourceAndDestination() {
        setOutgoingAirport(bombayAirport, delhiAirport);
        setOutgoingAirport(bangaloreAirport, bombayAirport);
        List<Airport> actualShortestRoute = bangaloreAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = createListWith(bombayAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultipleIntermediateHopsBetweenSourceAndDestination() {
        setOutgoingAirport(bombayAirport, delhiAirport);
        setOutgoingAirport(bangaloreAirport, bombayAirport);
        setOutgoingAirport(cochinAirport, bangaloreAirport);
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = createListWith(bangaloreAirport, bombayAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRouteWhenThereAreMultipleRoutesToDestinationAvailable() {
        setOutgoingAirport(bombayAirport, delhiAirport);
        setOutgoingAirport(bangaloreAirport, delhiAirport, bombayAirport);
        setOutgoingAirport(cochinAirport, bangaloreAirport);
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = createListWith(bangaloreAirport, delhiAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnEmptyListIfThereIsNoRouteAvailable() {
        List<Airport> actualShortestRoute = cochinAirport.getShortestRouteTo(delhiAirport);
        List<Airport> expectedShortestRoute = Collections.emptyList();
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    @Test
    public void shouldReturnShortestRoute() {
        setOutgoingAirport(mysoreAirport, goaAirport, cochinAirport);
        setOutgoingAirport(goaAirport, bombayAirport);
        setOutgoingAirport(bombayAirport, delhiAirport);
        setOutgoingAirport(cochinAirport, delhiAirport);
        setOutgoingAirport(delhiAirport, bangaloreAirport);
        List<Airport> actualShortestRoute = mysoreAirport.getShortestRouteTo(bangaloreAirport);
        List<Airport> expectedShortestRoute = createListWith(cochinAirport, delhiAirport, bangaloreAirport);
        assertEquals(expectedShortestRoute, actualShortestRoute);
    }

    private void setOutgoingAirport(Airport airport, Airport... outGoingAirports) {
        airport.setOutgoingAirports(Arrays.asList(outGoingAirports));
    }

    @Test
    public void testEquals() {
        shouldBeEqualWhenAirportNamesAreSame();
        shouldNotBeEqualWhenAirportNamesAreDifferent();
    }

    private void shouldNotBeEqualWhenAirportNamesAreDifferent() {
        assertFalse(new Airport("bom").equals(new Airport("del")));
    }

    private void shouldBeEqualWhenAirportNamesAreSame() {
        assertTrue(new Airport("del").equals(new Airport("del")));
    }

    private void planningStrategyProviderShouldBeCalledToGetAStrategy() {
        verifyStatic(times(1));
        RoutePlanningStrategyProvider.getShortestRouteStrategy();
    }

    private void shouldReturnListOfAirportIndicatingTheShortestRouteAsReturnedByTheStrategy(List<Airport> expectedRoute, List<Airport> actualRoute) {
        assertEquals(expectedRoute, actualRoute);
    }

    private List<Airport> createListWith(Airport... airport) {
        return Arrays.asList(airport);
    }

    @After
    public void cleanStaticCache() {
        try {
            Field field = ShortestRouteCache.class.getDeclaredField("cache");
            field.setAccessible(true);
            field.set(null, new ConcurrentHashMap<String, List<Airport>>());
        } catch (Exception e) {
        }
    }

}
