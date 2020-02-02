package com.ger.garage.model;

import java.util.Comparator;

public class BookingIdComparator implements Comparator<Booking> {


    @Override
    public int compare(Booking o1, Booking o2) {

        if (o1.getId() < o2.getId())
            return -1;
        if (o1.getId() > o2.getId())
            return 1;
        else return 0;

    }
}
