package com.flightbookinglab.model;

import com.flightbookinglab.exception.AirportNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by annarvekar on 7/10/14.
 */
public class AirportsTest {

    @Test(expected = AirportNotFoundException.class)
    public void shouldThrowAirportNotFoundExceptionWhenInvalidAirportIsPassed() throws AirportNotFoundException {
        Airports.getAirport("bom");
    }

    @Test
    public void shouldReturnProperAirport() throws AirportNotFoundException {
        Airports.add(new Airport("bom"));
        assertEquals("bom", Airports.getAirport("bom").getAirportName());
    }

    @Test(expected = AirportNotFoundException.class)
    public void airportNameShouldBeCaseSensitive() throws AirportNotFoundException {
        Airports.add(new Airport("BOM"));
        Airports.getAirport("bom");
    }

    @After
    public void cleanUp() throws NoSuchFieldException, IllegalAccessException {
        Field field = Airports.class.getDeclaredField("locallyCachedAirports");
        field.setAccessible(true);
        field.set(null,new ArrayList<Airport>());
    }
}
