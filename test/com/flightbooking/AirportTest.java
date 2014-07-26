package com.flightbooking;

import com.flightbooking.planner.RoutePlanner;
import com.flightbooking.planner.RoutePlanningStrategyProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by annarvekar on 7/24/14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Airport.class,RoutePlanningStrategyProvider.class})
public class AirportTest {

    @Mock
    private RoutePlanner mockedStrategy;
    private Airport bomAirport;
    private Airport delAirport;

    @Before
    public void setUp(){
        bomAirport = new Airport("bom");
        delAirport = new Airport("del");
        PowerMockito.mockStatic(RoutePlanningStrategyProvider.class);
    }

    @Test
    public void testShortestRoute(){
        List<Airport> expectedRoute = Collections.emptyList();
        when(mockedStrategy.plan(bomAirport, delAirport)).thenReturn(expectedRoute);
        when(RoutePlanningStrategyProvider.getShortestRouteStrategy()).thenReturn(mockedStrategy);
        List<Airport> actualRoute = bomAirport.getShortestRouteTo(delAirport);

        planningStrategyProviderShouldBeCalledToGetAStrategy();
        shouldReturnListOfAirportIndicatingTheShortestRouteAsReturnedByTheStrategy(expectedRoute, actualRoute);
    }

    @Test
    public void testEquals(){
        shouldBeEqualWhenAirportNamesAreSame();
        shouldNotBeEqualWhenAirportNamesAreDifferent();
    }

    private void shouldNotBeEqualWhenAirportNamesAreDifferent() {
        assertFalse(new Airport("bom").equals(new Airport("del")));
    }

    private void shouldBeEqualWhenAirportNamesAreSame() {
        assertTrue(new Airport("del").equals(new Airport("del")));
    }

    private void planningStrategyProviderShouldBeCalledToGetAStrategy(){
        verifyStatic(times(1));
        RoutePlanningStrategyProvider.getShortestRouteStrategy();
    }

    private void shouldReturnListOfAirportIndicatingTheShortestRouteAsReturnedByTheStrategy(List<Airport> expectedRoute, List<Airport> actualRoute) {
        assertEquals(expectedRoute,actualRoute);
    }


}
