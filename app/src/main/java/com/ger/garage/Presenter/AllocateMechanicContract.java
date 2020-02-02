package com.ger.garage.Presenter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class AllocateMechanicContract  {

    public interface Presenter {

        void detach();
        void getBookings(LocalDate date);
        ArrayList<String> getMechanics();
        void allocateMechanic(HashMap<String, String> mechanicToAllocate);
    }


    public interface View{

        void showBookings(ArrayList<String> bookings);
        void showErrorMessage(String errorMessage);
        void showBookingsUpdate(ArrayList<String> bookings);

    }


}
