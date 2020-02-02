package com.ger.garage.Presenter;

import java.util.HashMap;

public class DisplayBookingsContract {

    public interface Presenter {

        void detach();
        void getBookings();

    }


    public interface View{

        void showBookings(HashMap<Integer, String> bookings, HashMap<Integer,String> status);

    }


}
