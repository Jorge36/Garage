package com.ger.garage.Presenter;

import com.ger.garage.model.Booking;
import com.ger.garage.model.User;
import com.ger.garage.model.UserDao;

import java.util.ArrayList;
import java.util.Map;

public class PresenterHomeUser implements HomeUserContract.Presenter, FirebaseListener {

    private UserDao userDao;
    private HomeUserContract.View view;

    public PresenterHomeUser(HomeUserContract.View view) {
        this.view = view;
        userDao = new UserDao();
    }

    @Override
    public void logOut() {

        userDao.logOut();

    }

    // get user details from database
    @Override
    public void getUserDetails() {

        userDao.getUser(this);

    }

    @Override
    public void detach() {

        this.view = null;
        this.userDao = null;

    }

    @Override
    public void onSuccess(User user) {
        view.showUserDetail(user.getName(), user.getEmail(), user.getMobilePhoneNumber());

    }

    @Override
    public void onSuccess(Map<Integer, Integer> quantityOfBookingsByShift) {
        // We don't need implementation on this Presenter
    }


    @Override
    public void onSuccess(Integer idBooking) {
        // We don't need implementation on this Presenter
    }

    @Override
    public void onFailure(FirebaseException e) {
        // We don't need implementation on this Presenter
    }

    @Override
    public void onSuccess(ArrayList<Booking> bookings) {

    }
}
