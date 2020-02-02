package com.ger.garage.Presenter;

import com.ger.garage.model.BookingDao;

public class PresenterAdminDisplayBookings implements AdminDisplayBookingsContract.Presenter {

    private BookingDao bookingDao;
    private AdminDisplayBookingsContract.View view;

    public PresenterAdminDisplayBookings(AdminDisplayBookingsContract.View view) {
        this.view = view;
        this.bookingDao = new BookingDao();
    }


    @Override
    public void detach() {

        this.view = null;
        this.bookingDao = null;

    }

}
