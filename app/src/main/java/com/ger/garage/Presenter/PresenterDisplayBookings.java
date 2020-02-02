package com.ger.garage.Presenter;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import com.ger.garage.model.Booking;
import com.ger.garage.model.BookingDao;
import com.ger.garage.model.User;
import com.ger.garage.model.UserDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PresenterDisplayBookings implements DisplayBookingsContract.Presenter, FirebaseListener {

    private BookingDao bookingDao;
    private UserDao userDao;
    private DisplayBookingsContract.View view;

    public PresenterDisplayBookings(DisplayBookingsContract.View view) {

        this.view = view;
        this.bookingDao = new BookingDao();
        this.userDao = new UserDao();
    }


    @Override
    public void detach() {

        this.view = null;
        this.bookingDao.removeListenerBookingsByRef();
        this.bookingDao = null;
        this.userDao = null;

    }

    @Override
    public void getBookings() {

        bookingDao.getBookingsByUser(userDao.getUid(), this);

    }

    @Override
    public void onSuccess(User user) {
        // We don't need implementation here

    }

    @Override
    public void onSuccess(Map<Integer, Integer> quantityOfBookingsByShift) {
        // We don't need implementation here

    }

    @Override
    public void onSuccess(Integer idBooking) {
        // We don't need implementation here

    }

    @Override
    public void onFailure(FirebaseException e) {
        // We don't need implementation here

    }

    @Override
    public void onSuccess(ArrayList<Booking> bookings) {

        HashMap<Integer,String> bookingsInfo = new HashMap<>();
        HashMap<Integer,String> status = new HashMap<>();

        for (Booking booking: bookings) {

            bookingsInfo.put(booking.getId(), booking.toStringWithoutStatus());
            status.put(booking.getId(), booking.getStatus());

        }

        view.showBookings(bookingsInfo, status);
    }



}
