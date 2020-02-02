package com.ger.garage.Presenter;

import com.ger.garage.model.Booking;
import com.ger.garage.model.BookingDao;
import com.ger.garage.model.BookingShiftComparator;
import com.ger.garage.model.Mechanic;
import com.ger.garage.model.SetUpDao;
import com.ger.garage.model.Shift;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PresenterAllocateMechanic implements AllocateMechanicContract.Presenter, FirebaseListener2 {

    private BookingDao bookingDao;
    private AllocateMechanicContract.View view;
    private SetUpDao setUpDao;
    private HashMap<Integer, Booking> bookingHashMap;
    private HashMap<Booking, Mechanic> mechanicToBooking;

    public PresenterAllocateMechanic(AllocateMechanicContract.View view) {
        this.view = view;
        this.bookingDao = new BookingDao();
        this.setUpDao = new SetUpDao();
        this.bookingHashMap = new HashMap<>();
    }

    @Override
    public void detach() {

    }

    @Override
    public void getBookings(LocalDate date) {

        bookingDao.getBookingsByDate(date, null, this);

    }

    @Override
    public ArrayList<String> getMechanics() {

        ArrayList<Mechanic> mechanics = setUpDao.getMechanics();

        ArrayList<String> mechanicsResult = new ArrayList<>();

        for (Mechanic mechanic: mechanics) {

            mechanicsResult.add(mechanic.toString());


        }

        return mechanicsResult;
    }

    @Override
    public void allocateMechanic(HashMap<String, String> mechanicToAllocate) {

        mechanicToBooking = new HashMap<>();
        HashMap<Integer, Booking> bookingHashMapAux = new HashMap<Integer, Booking>();
        ArrayList<Shift> shifts = setUpDao.getShifts();
        HashMap<Integer, ArrayList<Integer>> shiftMechanic = new HashMap<>();

        for (Shift s: shifts) {

            shiftMechanic.put(s.getId(), new ArrayList<Integer>());

        }

        Booking booking;
        Map.Entry pair;
        Iterator it = bookingHashMap.entrySet().iterator();

        while (it.hasNext()) {

            // get the pair booking mechanic
            pair = ((Map.Entry) it.next());

            booking = (Booking) pair.getValue();

            if (booking.getMechanic() == null)
                    continue;

            booking = new Booking((Integer) pair.getKey(), new Mechanic(booking.getMechanic().getId(), booking.getMechanic().getName()), booking.getShifts());

            bookingHashMapAux.put(booking.getId(), booking);

        }

        it = mechanicToAllocate.entrySet().iterator();
        Booking bookingAux;
        Integer idBooking;
        Mechanic mechanic = null;
        Integer idMechanic;

        // Create a deep copy of the hash map
        while (it.hasNext()) {

            // get the pair booking mechanic
            pair = ((Map.Entry) it.next());

            // get Booking
            booking = Booking.valueOfToAllocateMechanic((String) pair.getKey());

            // get Id booking
            idBooking = booking.getId();

            // get Mechanic
            mechanic = Mechanic.valueOf((String) pair.getValue());

            // Get id mechanic
            idMechanic = mechanic.getId();

            // get full mechanic
            mechanic = setUpDao.getMechanic(idMechanic);

            // HashMap wit all the bookings, I get the booking which
            booking = bookingHashMap.get(idBooking);

            // Create a new booking
            bookingAux = new Booking(booking.getId(), mechanic, booking.getShifts());

            bookingHashMapAux.put(booking.getId(), bookingAux);

            // fill the hashmap with the pair booking mechanic to save in the database
            mechanicToBooking.put(booking, mechanic);

        }

        ArrayList<Integer> idMechanicsByShift;
        Shift shiftFoundError = null;

        it = bookingHashMapAux.entrySet().iterator();

        // Check every shift has 4 mechanics and they are different
        while (it.hasNext()) {

            // get the pair booking mechanic
            pair = ((Map.Entry) it.next());

            // get Booking
            booking = (Booking) pair.getValue();

            if (booking.getMechanic() == null)
                continue;
            // get mechanic working for this booking
            idMechanic = booking.getMechanic().getId();

            // the mechanic working in thoese shift doesn't have to be twice
            for (Shift shift: booking.getShifts()) {

                idMechanicsByShift = shiftMechanic.get(shift.getId());

                // if id mechanic is not in the list for a particular shift
                if (!idMechanicsByShift.contains(idMechanic))
                    idMechanicsByShift.add(idMechanic);
                else { //
                    shiftFoundError = shift;
                    mechanic = booking.getMechanic();
                    break;
                }

            }

            if (shiftFoundError != null)
                break;

        }

        if (shiftFoundError != null)
            view.showErrorMessage("Shift " + shiftFoundError.toString() + "contain the mechanic" + mechanic.toString() + "twice");
        else {
            bookingDao.setMechanicToAbooking(mechanicToBooking, this);
        }

    }

    @Override
    public void onSuccess(ArrayList<Booking> bookings) {

        BookingShiftComparator bookingShiftComparator = new BookingShiftComparator();

        Collections.sort(bookings, bookingShiftComparator);

        ArrayList<String> bookingsResult = new ArrayList<>();

        for (Booking booking: bookings) {

            if ((booking.getStatus().equals("in service")) || (booking.getStatus().equals("booked"))) {

                bookingsResult.add(booking.toStringToAllocateMechanic());
                bookingHashMap.put(booking.getId(), booking);

            }

        }

        view.showBookings(bookingsResult);
    }

    @Override
    public void onSuccess(String newStatus) {
        // Not implemented
    }

    @Override
    public void onFailure(FirebaseException e) {

        view.showErrorMessage(e.getMessage());

    }

    @Override
    public void onSuccessUpdateMechanic(ArrayList<Booking> bookings) {

        BookingShiftComparator bookingShiftComparator = new BookingShiftComparator();

        Collections.sort(bookings, bookingShiftComparator);

        ArrayList<String> bookingsResult = new ArrayList<>();

        for (Booking booking: bookings) {

            if (bookingHashMap.containsKey(booking.getId()))
                bookingHashMap.put(booking.getId(), booking);

        }

        Iterator it = bookingHashMap.entrySet().iterator();
        Map.Entry pair;

        while(it.hasNext()) {

            pair = ((Map.Entry) it.next());

            bookingsResult.add(((Booking)pair.getValue()).toStringToAllocateMechanic());

        }

        view.showBookingsUpdate(bookingsResult);

    }

}
