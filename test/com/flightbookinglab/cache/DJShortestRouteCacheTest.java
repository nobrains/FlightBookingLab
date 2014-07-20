package com.flightbookinglab.cache;

import com.flightbookinglab.model.Airport;
import com.flightbookinglab.model.Route;
import org.junit.After;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by annarvekar on 7/20/14.
 */
public class DJShortestRouteCacheTest {

    @Test
    public void shouldReturnValueWhenInsertedToCache() {
        Route expectedRoute = new Route();
        expectedRoute.add(new Airport("goi"));
        DJShortestRouteCache.put("SomeKey",expectedRoute);
        assertEquals(expectedRoute,DJShortestRouteCache.get("SomeKey"));
    }

    @Test
    public void shouldReturnNullWhenKeyNotPresentInCache() {
        assertNull(DJShortestRouteCache.get("SomeRandomKey"));
    }
}
