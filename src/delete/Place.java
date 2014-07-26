package delete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by annarvekar on 7/26/14.
 */
public class Place {
    List<Place> outgoingPlaces = new ArrayList<Place>();
    private String placeName;

    public Place(String goa) {

        placeName = goa;
    }

    public List<Place> shortest(Place destination) {
        List<Place> foo = foo(this.outgoingPlaces, destination);
        Collections.reverse(foo);
        return foo;
    }

    private List<Place> foo(List<Place> outgoingPlaces, Place destination) {
        List<Place> l = new ArrayList<Place>();
        for (Place outgoingPlace : outgoingPlaces) {
            if (destination.equals(outgoingPlace)) {
                List<Place> p = new ArrayList<Place>();
                p.add(outgoingPlace);
                return p;
            }
            List<Place> foo = foo(outgoingPlace.outgoingPlaces, destination);
            foo.add(outgoingPlace);
            if (l.isEmpty() || foo.size() < l.size()) {
                l = foo;
            }
        }
        return l;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Place) obj).placeName.equals(placeName);
    }
}
