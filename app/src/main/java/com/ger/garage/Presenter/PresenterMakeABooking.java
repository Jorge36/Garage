package com.ger.garage.Presenter;

import com.ger.garage.model.Booking;
import com.ger.garage.model.BookingDao;
import com.ger.garage.model.SetUpDao;
import com.ger.garage.model.Shift;
import com.ger.garage.model.User;
import com.ger.garage.model.UserDao;
import com.ger.garage.model.Vehicle;
import java.time.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

public class PresenterMakeABooking implements MakeABookingContract.Presenter, FirebaseListener{

    // global variables
    private UserDao userDao;
    private MakeABookingContract.View view;
    private BookingDao bookingDao;
    private SetUpDao setUpDao;
    private String typeOfBooking;
    private LocalDate dateOfBooking;
    private User user;

    public PresenterMakeABooking(MakeABookingContract.View view) {
        this.view = view;
        this.userDao = new UserDao();
        this.setUpDao = new SetUpDao();
        this.bookingDao = new BookingDao();
    }

    @Override
    public void logOut() {

        userDao.logOut();

    }

    @Override
    public void getVehicles() {

        userDao.getUser(this);

    }

    @Override
    public void getShifts(String typeOfBooking, LocalDate date) {

        this.typeOfBooking = typeOfBooking;
        this.dateOfBooking = date;
        bookingDao.getQuantityOfBookingByShift(date, setUpDao.getShifts(), this);

    }

    @Override
    public void getTypeOfBooking() {

        view.showTypeOfBooking(setUpDao.getTypeOfBooking());

    }

    @Override
    public boolean isWorkingDay(DayOfWeek day) {

        return setUpDao.getWorkingDays().contains(day);

    }

    // I create the object booking which i want to create
    @Override
    public void book(String vehiclePlateNumber, String typeOfBooking, LocalDate date, String shift) {

        Vehicle vehicle = null;

        vehicle = Vehicle.valueOf(vehiclePlateNumber);

        for (Vehicle v: user.getVehicles()) {

            if (v.getNumberPlate().equals(vehicle.getNumberPlate())) {

                vehicle = v;
                break;
            }
        }

        Booking booking = new Booking(date, typeOfBooking, "", vehicle, setUpDao.getFirstStatus(), user);
        Shift shiftAux = Shift.valueOf(shift);

        ArrayList<Shift> shifts = setUpDao.getShifts();
        ArrayList<Shift> shiftsAux = new ArrayList<>();
        Integer quantityOfShiftsByTypeOfBooking = setUpDao.getQuantityOfShiftsByTypeOfBooking(typeOfBooking);

        // Get the shifts to register
        // quantityOfShiftsByTypeOfBooking = Quantity of shifts by type of Booking --> Mayor repair count double
        switch (quantityOfShiftsByTypeOfBooking) {

            case 1:

                for (Shift s: shifts) {

                    if (s.getId() == shiftAux.getId()) {

                        shiftAux.setDescription(s.getDescription());
                        shiftAux.setTimeEnd(s.getTimeEnd());
                        shiftAux.setTimeStart(s.getTimeStart());
                        shiftsAux.add(shiftAux);
                        break;

                    }

                }
                break;


            case 2:

                for (int i = 0; i < shifts.size() - 1; i++) {

                    if (shifts.get(i).getId() == shiftAux.getId()) {

                        shiftAux.setDescription(shifts.get(i).getDescription());
                        shiftAux.setTimeEnd(shifts.get(i).getTimeEnd());
                        shiftAux.setTimeStart(shifts.get(i).getTimeStart());

                        shiftsAux.add(shiftAux);
                        shiftsAux.add(shifts.get(i+1));
                        break;

                    }

                }


        }

        booking.setShifts(shiftsAux);
        // we should check here if the shift/s is/are available
        // but we already checked when the user choose a shift
        // and the transaction check but this one doesn't have a function
        // to stop
        bookingDao.createBooking(booking, setUpDao.getQuantityBookingsByShift(), this);

    }

    @Override
    public void detach() {

        this.view = null;
        this.userDao = null;

    }


    // I got vehicles, i show it in the spinner
    @Override
    public void onSuccess(User user) {

        this.user = user;
        this.user.setId(userDao.getUid());

        ArrayList<String> vehiclesResult = new ArrayList<>();
        ArrayList<Vehicle> vehicles;

        vehicles = user.getVehicles();

        for (Vehicle vehicle: vehicles) {

            vehiclesResult.add(vehicle.toString());
        }

        view.showVehicles(vehiclesResult);

    }

    // get shifts to show in the spinner shift
    @Override
    public void onSuccess(Map<Integer, Integer> quantityOfBookingsByShift) {

        Integer quantityBookingByShift = setUpDao.getQuantityBookingsByShift();
        ArrayList<Shift> shifts = setUpDao.getShifts();
        ArrayList<String> shiftsResult = new ArrayList<>();
        Integer quantityOfShiftsByTypeOfBooking = setUpDao.getQuantityOfShiftsByTypeOfBooking(typeOfBooking);
        // Quantity of shifts by type of Booking --> Mayor repair count double
        switch (quantityOfShiftsByTypeOfBooking) {

            case 1: // if user select a type of booking which occupy one shift, don't group shifts

                for (Shift shift: shifts) {

                    if (dateOfBooking.isEqual(LocalDate.now()) && shift.getTimeStart().isBefore(LocalTime.now()))
                        continue;

                    if (quantityOfBookingsByShift.get(shift.getId()) < quantityBookingByShift)
                        shiftsResult.add(shift.toString());
                }

                view.showShiftsAvailable(shiftsResult);
                break;

            case 2: // if user select a mayor repar, this one occuppy 2 shifts, so it must be group bookings
                   // could be group the first one with the second one, second one with third one and so on...

                for (int i = 0; i < (shifts.size() - 1); i++) {

                    if (dateOfBooking.isEqual(LocalDate.now()) && shifts.get(i).getTimeStart().isBefore(LocalTime.now()))
                        continue;

                    if (quantityOfBookingsByShift.get(shifts.get(i).getId()) == quantityBookingByShift)
                        continue;
                    if (quantityOfBookingsByShift.get(shifts.get(i + 1).getId()) < quantityBookingByShift) {

                        shiftsResult.add(shifts.get(i).toString());

                    }

                }

                view.showShiftsAvailable(shiftsResult);
                break;

             // case X: We could do a generic algorithm for quantity greater than 2 (include / exclude)
        }
        this.typeOfBooking = null;
        this.dateOfBooking = null;

    }

    @Override
    public void onSuccess(Integer idBooking) {

        view.showSuccessMessage(idBooking.toString());
    }

    @Override
    public void onFailure(FirebaseException e) {

        view.showErrorMessage(e.getMessage());
    }

    @Override
    public void onSuccess(ArrayList<Booking> bookings) {
        // We don't need implementation here
    }
}
