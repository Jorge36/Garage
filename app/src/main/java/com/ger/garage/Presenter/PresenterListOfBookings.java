package com.ger.garage.Presenter;

import com.ger.garage.model.Booking;
import com.ger.garage.model.BookingDao;
import com.ger.garage.model.BookingIdComparator;
import com.ger.garage.model.SetUpDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PresenterListOfBookings implements ListOfBookingsContract.Presenter, FirebaseListener2 {

    private BookingDao bookingDao;
    private ListOfBookingsContract.View view;
    private SetUpDao setUpDao;
    private HashMap<Integer, Booking> bookingHashMap;

    public PresenterListOfBookings(ListOfBookingsContract.View view) {

        this.view = view;
        bookingDao = new BookingDao();
        bookingHashMap = new HashMap<>();
        setUpDao = new SetUpDao();
    }

    @Override
    public void detach() {

        this.view = null;
        this.setUpDao = null;
        this.bookingHashMap.clear();
        this.bookingHashMap = null;
        this.bookingDao.removeListenerBookings();
        this.bookingDao = null;

    }

    @Override
    public void getBookings(LocalDate fDate, LocalDate sDate) {

        bookingDao.getBookingsByDate(fDate, sDate, this);

    }

    public void removeListener() {


        bookingDao.removeListenerBookings();

    }

    @Override
    public void changeStatus(String booking, String newStatus) {

        Booking IdBooking = Booking.valueOfFullInformation(booking);

        Booking b = bookingHashMap.get(IdBooking.getId());

        if (!b.getStatus().equals(newStatus))
            bookingDao.changeStatus(b, newStatus, this);
        else
            view.showStatusAlreadyUpdatedMessage();

    }

    public String[] getStatus(String booking) {

        Booking b = Booking.valueOfFullInformation(booking);

        String status = bookingHashMap.get(b.getId()).getStatus();

        ArrayList<String> listOfStatus = setUpDao.getListOfStatus(status);

        return listOfStatus.toArray(new String[listOfStatus.size()]);

    }

    @Override
    public void onSuccess(ArrayList<Booking> bookings) {

        BookingIdComparator bookingIdComparator = new BookingIdComparator();

        Collections.sort(bookings, bookingIdComparator);

        ArrayList<String> bookingsResult = new ArrayList<>();

        for (Booking booking: bookings) {

            bookingsResult.add(booking.toStringWithFullInformation());
            bookingHashMap.put(booking.getId(), booking);

        }

        view.showBookings(bookingsResult);
    }

    @Override
    public void onSuccess(String newStatus) {
        view.showSuccessUpdateStatusMessage();
    }

    @Override
    public void onFailure(FirebaseException e) {

        view.showErrorMessage(e.getMessage());

    }

    @Override
    public void onSuccessUpdateMechanic(ArrayList<Booking> bookings) {
        // Not implemented
    }
}
