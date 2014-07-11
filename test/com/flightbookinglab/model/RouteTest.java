package com.flightbookinglab.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by annarvekar on 7/10/14.
 */
public class RouteTest {

    @Test
    public void shouldBeEqual() {
        Route route1 = new Route();
        route1.add(new Airport("goi",null));
        route1.add(new Airport("bom",null));

        Route route2 = new Route();
        route2.add(new Airport("goi",null));
        route2.add(new Airport("bom",null));
        assertTrue(route1.equals(route2));
    }

    @Test
    public void shouldNotBeEqualWhenTheRoutesHasDifferentAirports() {
        Route route1 = new Route();
        route1.add(new Airport("goi",null));

        Route route2 = new Route();
        route2.add(new Airport("goi",null));
        route2.add(new Airport("bom",null));
        assertFalse(route1.equals(route2));
    }

    @Test
    public void shouldNotBeEqualWhenTheSequenceInTwoRoutesAreDifferent() {
        Route route1 = new Route();
        route1.add(new Airport("goi",null));
        route1.add(new Airport("bom",null));

        Route route2 = new Route();
        route2.add(new Airport("bom",null));
        route2.add(new Airport("goi",null));
        assertFalse(route1.equals(route2));
    }
}
