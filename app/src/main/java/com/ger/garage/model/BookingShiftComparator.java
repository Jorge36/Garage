package com.ger.garage.model;

import java.util.Comparator;

public class BookingShiftComparator implements Comparator<Booking> {


        @Override
        public int compare(Booking o1, Booking o2) {

            if (o1.getShifts().get(0).getId() < o2.getShifts().get(0).getId())
                return -1;
            if (o1.getShifts().get(0).getId() > o2.getShifts().get(0).getId())
                return 1;
            else return 0;

        }
}


