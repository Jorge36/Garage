package com.ger.garage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ger.garage.Presenter.MakeABookingContract;
import com.ger.garage.Presenter.PresenterMakeABooking;
import com.ger.garage.R;


import java.time.LocalDate;
import java.util.ArrayList;

/*

    This class allow to make a booking

 */

public class MakeABookingActivity extends AppCompatActivity implements MakeABookingContract.View {

    // presenter
    private MakeABookingContract.Presenter presenter;

    // elements on the screen
    private Spinner vehiclesSpinner;
    private CalendarView calendar;
    private Spinner shiftSpinner;
    private Spinner typeOfBookingSpinner;
    private Button btnBook;
    private ProgressBar progressBar;

    // variables globales save current data
    private String vehicleString = "";
    private String shiftString = "";
    private String typeOfBookingString = "";
    private LocalDate calendarDate = LocalDate.now();

    // message to print on the screen
    private final String chooseVehicle = "Choose a vehicle";
    private final String chooseshift = "Book available time";
    private final String chooseTypeOfBooking = "Choose a type of booking";
    private final String selectVehicle = "Please select the vehicle";
    private final String selectTypeOfBooking = "Please select the type of booking";
    private final String selectShfit = "Please select a shift";
    private final String dateInPast = "The date selected is in the past";
    private final String notWorkingDay = "The garage is not open on ";
    private final String bookingCreatedSuccessfully1 = "The booking ";
    private final String bookingCreatedSuccessfully2 = " was created successfully";
    private final String noBookingSelected = "First, the type of booking must be selected";
    private final String noShiftsAvailable = "No shifts available for this date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_booking);

        presenter = new PresenterMakeABooking(MakeABookingActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        addCalendar();
        addShifts();
        addVehicles();
        book();

        presenter.getVehicles();
        presenter.getTypeOfBooking();

    }

    // add data to the spinner vehicle. it works with a ArrauAdapter<T>
    private void addVehicles() {

        vehiclesSpinner = findViewById(R.id.vehicles);
        ArrayList<String> vehicles = new ArrayList<>();
        vehicles.add(chooseVehicle);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, vehicles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehiclesSpinner.setAdapter(dataAdapter);
        vehiclesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override // select a item handler
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    vehicleString = parent.getItemAtPosition(position).toString();

              }
                else
                    vehicleString = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void addCalendar() {

        calendar = findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override // user select a date
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarDate = LocalDate.of(year, month + 1, dayOfMonth);
                resetShifts(new ArrayList<String>());
            }
        });

    }


    // add Shifts to the spinner
    private void addShifts() {

        shiftSpinner = findViewById(R.id.shift);
        final ArrayList<String> shifts = new ArrayList<>();
        shifts.add(0, chooseshift);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, shifts);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shiftSpinner.setAdapter(dataAdapter);

        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    shiftString = parent.getItemAtPosition(position).toString();
                }
                else
                    shiftString = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        shiftSpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (typeOfBookingString == "") { // if want to select a shift but the type of booking is empty, error

                        resetShifts(new ArrayList<String>());

                        Toast.makeText(MakeABookingActivity.this, noBookingSelected, Toast.LENGTH_LONG).show();

                    } else if (calendarDate.isBefore(LocalDate.now())) { // if the date is before today

                        resetShifts(new ArrayList<String>());

                        Toast.makeText(MakeABookingActivity.this, dateInPast, Toast.LENGTH_LONG).show();

                    } else if (!presenter.isWorkingDay(calendarDate.getDayOfWeek())) { // if it is not a working day

                        resetShifts(new ArrayList<String>());

                        String day = calendarDate.getDayOfWeek().toString();

                        day = day.substring(0,1).toUpperCase() + day.substring(1).toLowerCase();

                        Toast.makeText(MakeABookingActivity.this, notWorkingDay + day, Toast.LENGTH_LONG).show();

                    }
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        presenter.getShifts(typeOfBookingString, calendarDate);

                    }

                }

                return false;
            }

        });

    }

    private void book() {

        btnBook = findViewById(R.id.book);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vehicleString.isEmpty())
                    Toast.makeText(MakeABookingActivity.this, selectVehicle, Toast.LENGTH_LONG).show();
                else if (typeOfBookingString.isEmpty())
                    Toast.makeText(MakeABookingActivity.this, selectTypeOfBooking , Toast.LENGTH_LONG).show();
                else if (calendarDate.isBefore(LocalDate.now()))
                    Toast.makeText(MakeABookingActivity.this, dateInPast , Toast.LENGTH_LONG).show();
                else if (!presenter.isWorkingDay(calendarDate.getDayOfWeek())) {

                    String day = calendarDate.getDayOfWeek().toString();
                    day = day.substring(0,1).toUpperCase() + day.substring(1).toLowerCase();

                    Toast.makeText(MakeABookingActivity.this, day , Toast.LENGTH_LONG).show();
                }
                else if (shiftString.isEmpty())
                    Toast.makeText(MakeABookingActivity.this, selectShfit, Toast.LENGTH_LONG).show();
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.book(vehicleString, typeOfBookingString, calendarDate, shiftString);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.make_a_booking_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                presenter.logOut();
                Intent intentRegister = new Intent(MakeABookingActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            default:
                break;
        }

        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
        btnBook.setOnClickListener(null);
        shiftSpinner.setOnItemSelectedListener(null);
        shiftSpinner.setOnTouchListener(null);
        vehiclesSpinner.setOnItemSelectedListener(null);
        typeOfBookingSpinner.setOnItemSelectedListener(null);
        vehiclesSpinner.setAdapter(null);
        shiftSpinner.setAdapter(null);
        typeOfBookingSpinner.setAdapter(null);

        // Detach handlers
    }

    // update spinner
    @Override
    public void showVehicles(ArrayList<String> vehicles) {

        vehicles.add(0, chooseVehicle);
        ArrayAdapter<String> dataAdapter = (ArrayAdapter<String>) vehiclesSpinner.getAdapter();
        dataAdapter.clear();
        dataAdapter.addAll(vehicles);
        dataAdapter.notifyDataSetChanged();
        vehiclesSpinner.setSelection(0);

    }

    // update type of booking
    @Override
    public void showTypeOfBooking(ArrayList<String> typeOfBookings) {

        typeOfBookingSpinner = findViewById(R.id.typeOfBooking);
        typeOfBookings.add(0, chooseTypeOfBooking);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, typeOfBookings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfBookingSpinner.setAdapter(dataAdapter);
        typeOfBookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    resetShifts(new ArrayList<String>());
                    typeOfBookingString = parent.getItemAtPosition(position).toString();
                }
                else
                    typeOfBookingString = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    // show shifts available
    @Override
    public void showShiftsAvailable(ArrayList<String> shiftAvailable) {

        if (shiftAvailable.isEmpty())
            Toast.makeText(MakeABookingActivity.this, noShiftsAvailable, Toast.LENGTH_LONG).show();

        shiftAvailable.add(0, chooseshift);
        ArrayAdapter<String> dataAdapter = (ArrayAdapter<String>) shiftSpinner.getAdapter();
        dataAdapter.clear();
        dataAdapter.addAll(shiftAvailable);
        dataAdapter.notifyDataSetChanged();
        shiftSpinner.setSelection(0);
        progressBar.setVisibility(View.GONE);

    }

    // blank spinner shift
    private void resetShifts(ArrayList<String> shiftAvailable) {

        shiftAvailable.add(0, chooseshift);
        ArrayAdapter<String> dataAdapter = (ArrayAdapter<String>) shiftSpinner.getAdapter();
        dataAdapter.clear();
        dataAdapter.addAll(shiftAvailable);
        dataAdapter.notifyDataSetChanged();
        shiftSpinner.setSelection(0);

    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(MakeABookingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        if (progressBar != null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showSuccessMessage(String bookingId) {
        Toast.makeText(MakeABookingActivity.this, bookingCreatedSuccessfully1 + bookingId + bookingCreatedSuccessfully2, Toast.LENGTH_LONG).show();
        resetShifts(new ArrayList<String>());
        if (progressBar != null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }

}
