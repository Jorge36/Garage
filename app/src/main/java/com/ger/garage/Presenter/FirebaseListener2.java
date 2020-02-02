package com.ger.garage.Presenter;

import com.ger.garage.model.Booking;
import com.ger.garage.model.Mechanic;

import java.util.ArrayList;
import java.util.HashMap;

public interface FirebaseListener2 {

    void onSuccess(ArrayList<Booking> bookings);
    void onSuccess(String newStatus);
    void onFailure(FirebaseException e);
    void onSuccessUpdateMechanic(ArrayList<Booking> bookings);

}