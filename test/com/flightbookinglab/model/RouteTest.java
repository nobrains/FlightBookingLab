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
        route1.add(new Airport("goi"));
        route1.add(new Airport("bom"));

        Route route2 = new Route();
        route2.add(new Airport("goi"));
        route2.add(new Airport("bom"));
        assertTrue(route1.equals(route2));
    }

    @Test
    public void shouldNotBeEqualWhenTheRoutesHasDifferentAirports() {
        Route route1 = new Route();
        route1.add(new Airport("goi"));

        Route route2 = new Route();
        route2.add(new Airport("goi"));
        route2.add(new Airport("bom"));
        assertFalse(route1.equals(route2));
    }

    @Test
    public void shouldNotBeEqualWhenTheSequenceInTwoRoutesAreDifferent() {
        Route route1 = new Route();
        route1.add(new Airport("goi"));
        route1.add(new Airport("bom"));

        Route route2 = new Route();
        route2.add(new Airport("bom"));
        route2.add(new Airport("goi"));
        assertFalse(route1.equals(route2));
    }
}
