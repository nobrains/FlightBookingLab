package delete;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by annarvekar on 7/26/14.
 */
public class PlaceTest {

    Place margao;
    Place vasco ;
    Place panaji;
    private Place mapusa;
    private Place cuncolim;
    private Place ponda;

    @Before
    public void setup(){
        margao = new Place("margao");
        vasco = new Place("vasco");
        panaji = new Place("panaji");
        mapusa = new Place("mapusa");
        cuncolim = new Place("cuncolim");
        ponda = new Place("ponda");
    }

    @Test
    public void test1() {

        margao.outgoingPlaces.addAll(Arrays.asList(vasco, panaji));
        List<Place> shortest = margao.shortest(panaji);
        assertEquals(Arrays.asList(panaji),shortest);
    }

    @Test
    public void test2() {

        margao.outgoingPlaces.addAll(Arrays.asList(vasco));
        vasco.outgoingPlaces.addAll(Arrays.asList(panaji));
        List<Place> shortest = margao.shortest(panaji);
        assertEquals(Arrays.asList(vasco,panaji),shortest);
    }

    @Test
    public void test3() {

        margao.outgoingPlaces.addAll(Arrays.asList(vasco));
        vasco.outgoingPlaces.addAll(Arrays.asList(panaji));
        panaji.outgoingPlaces.addAll(Arrays.asList(mapusa));
        List<Place> shortest = margao.shortest(mapusa);
        assertEquals(Arrays.asList(vasco,panaji,mapusa),shortest);
    }

    @Test
    public void test4() {

        margao.outgoingPlaces.addAll(Arrays.asList(vasco,panaji));
        vasco.outgoingPlaces.addAll(Arrays.asList(panaji));
        List<Place> shortest = margao.shortest(panaji);
        assertEquals(Arrays.asList(panaji),shortest);
    }

    @Test
    public void test5() {

        margao.outgoingPlaces.addAll(Arrays.asList(vasco));
        vasco.outgoingPlaces.addAll(Arrays.asList(panaji,mapusa));
        panaji.outgoingPlaces.addAll(Arrays.asList(mapusa));
        List<Place> shortest = margao.shortest(mapusa);
        assertEquals(Arrays.asList(vasco,mapusa),shortest);
    }

    @Test
    public void test6() {
        cuncolim.outgoingPlaces.addAll(Arrays.asList(margao,ponda));
        margao.outgoingPlaces.addAll(Arrays.asList(vasco));
        vasco.outgoingPlaces.addAll(Arrays.asList(panaji));
        panaji.outgoingPlaces.addAll(Arrays.asList(mapusa));
        ponda.outgoingPlaces.addAll(Arrays.asList(panaji));


        List<Place> shortest = cuncolim.shortest(mapusa);
        assertEquals(Arrays.asList(ponda,panaji,mapusa),shortest);
    }

    @Test
    public void test7() {
        cuncolim.outgoingPlaces.addAll(Arrays.asList(ponda,margao));
        margao.outgoingPlaces.addAll(Arrays.asList(vasco));
        vasco.outgoingPlaces.addAll(Arrays.asList(panaji));
        panaji.outgoingPlaces.addAll(Arrays.asList(mapusa));
        ponda.outgoingPlaces.addAll(Arrays.asList(panaji));


        List<Place> shortest = cuncolim.shortest(mapusa);
        assertEquals(Arrays.asList(ponda,panaji,mapusa),shortest);
    }
}
