package com.flightbookinglab.model;

import com.flightbookinglab.exception.AirportNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by annarvekar on 7/10/14.
 */
public class AirportsTest {

    private Airports airports;

    @Before
    public void setUp(){
        airports=new Airports();
    }

    @Test(expected = AirportNotFoundException.class)
    public void shouldThrowAirportNotFoundExceptionWhenInvalidAirportIsPassed() throws AirportNotFoundException {
        airports.getAirport("bom");
    }

    @Test
    public void shouldReturnProperAirport() throws AirportNotFoundException {
        airports.add(new Airport("bom",null));
        assertEquals("bom", airports.getAirport("bom").getAirportName());
    }

    @Test(expected = AirportNotFoundException.class)
    public void airportNameShouldBeCaseSensitive() throws AirportNotFoundException {
        airports.add(new Airport("BOM",null));
        airports.getAirport("bom");
    }
}
