package com.ger.garage.Presenter;

import java.time.LocalDate;
import java.util.ArrayList;

public class ListOfBookingsContract {

    public interface Presenter {

        void detach();
        void getBookings(LocalDate fDate, LocalDate sDate);
        void changeStatus(String bookings, String newStatus);
        String[] getStatus(String booking);

    }


    public interface View{

        void showBookings(ArrayList<String> bookings);
        void showErrorMessage(String errorMessage);
        void showSuccessUpdateStatusMessage();
        void showStatusAlreadyUpdatedMessage();

    }


}
