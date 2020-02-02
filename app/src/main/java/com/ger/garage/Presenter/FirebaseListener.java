package com.ger.garage.Presenter;

import com.ger.garage.model.Booking;
import com.ger.garage.model.User;

import java.util.ArrayList;
import java.util.Map;

public interface FirebaseListener {

    void onSuccess(User user);
    void onSuccess(Map<Integer, Integer> quantityOfBookingsByShift);
    void onSuccess(Integer idBooking);
    void onFailure(FirebaseException e);
    void onSuccess(ArrayList<Booking> bookings);

}
