package com.ger.garage.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ger.garage.Presenter.AdminDisplayBookingsContract;
import com.ger.garage.Presenter.PresenterAdminDisplayBookings;
import com.ger.garage.R;
import com.ger.garage.model.Cache;
import com.ger.garage.model.CacheImplementation;
import com.ger.garage.model.QueryType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class AdminDisplayBookingsActivity extends AppCompatActivity implements AdminDisplayBookingsContract.View, SlyCalendarDialog.Callback {

    private Button btnToday;
    private Button btnTomorrow;
    private Button btnThisWeek;
    private Button btnNextWeek;
    private Button btnRangeOfDates;
    private Button btnDate;
    private Button btnIdBooking;
    private Button btnCustomerEmail;
    private PresenterAdminDisplayBookings presenterAdminDisplayBookings;
    private final String perioderrorMessage = "The period must be in the same Montb";
    private Cache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_bookings);

        presenterAdminDisplayBookings = new PresenterAdminDisplayBookings(this);
        initialize();
        cache = new CacheImplementation(AdminDisplayBookingsActivity.this);
    }

    private void initialize() {


        btnToday = findViewById(R.id.btnToday);
        btnTomorrow = findViewById(R.id.btnTomorrow);
        btnThisWeek = findViewById(R.id.btnThisWeek);
        btnNextWeek = findViewById(R.id.btnNextWeek);
        btnRangeOfDates = findViewById(R.id.btnRangeOfDates);
        btnDate = findViewById(R.id.btnDate);
        btnIdBooking = findViewById(R.id.btnIdBooking);
        btnCustomerEmail = findViewById(R.id.btnCustomerEmail);

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cache.setQuery(QueryType.date);
                cache.setDate(LocalDate.now());
                Intent intent =  new Intent(AdminDisplayBookingsActivity.this, ListOfBookingsActivity.class);
                startActivity(intent);

            }
        });

        btnTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cache.setQuery(QueryType.date);
                cache.setDate(LocalDate.now().plusDays(1));
                Intent intent =  new Intent(AdminDisplayBookingsActivity.this, ListOfBookingsActivity.class);
                startActivity(intent);
            }
        });

        btnThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate today = LocalDate.now();
                LocalDate endOfThisWeek;
                if (today.getDayOfWeek() == DayOfWeek.SUNDAY)
                    endOfThisWeek = today;
                else
                    endOfThisWeek = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
                cache.setQuery(QueryType.period);
                cache.setPeriod(new LocalDate[]{today, endOfThisWeek});

                Intent intent =  new Intent(AdminDisplayBookingsActivity.this, ListOfBookingsActivity.class);
                startActivity(intent);


            }
        });

        btnNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalDate today = LocalDate.now();
                LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                LocalDate endOfNextWeek = nextMonday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

                cache.setQuery(QueryType.period);
                cache.setPeriod(new LocalDate[]{nextMonday, endOfNextWeek});

                Intent intent =  new Intent(AdminDisplayBookingsActivity.this, ListOfBookingsActivity.class);
                startActivity(intent);

            }
        });

        btnRangeOfDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(AdminDisplayBookingsActivity.this)
                        .show(getSupportFragmentManager(), "Bookings");

            }

        });


        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No Implemented
            }
        });

        btnIdBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No Implemented
            }
        });

        btnCustomerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No Implemented
            }
        });

    }

    @Override
    public void showBookings() {

    }

    @Override
    public void onCancelled() {
        // Nothing
    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {

        if ((firstDate != null) && (secondDate != null)) {

            if (firstDate.get(Calendar.MONTH) != secondDate.get(Calendar.MONTH))
                Toast.makeText(AdminDisplayBookingsActivity.this, perioderrorMessage, Toast.LENGTH_LONG).show();

            else {

                int day1 = firstDate.get(Calendar.DAY_OF_MONTH);
                int day2 = secondDate.get(Calendar.DAY_OF_MONTH);
                int month1 = firstDate.get(Calendar.MONTH) + 1;
                int month2 = secondDate.get(Calendar.MONTH) + 1;
                int year1 = firstDate.get(Calendar.YEAR);
                int year2 = secondDate.get(Calendar.YEAR);


                LocalDate fDate = LocalDate.of(year1, month1, day1);
                LocalDate sDate = LocalDate.of(year2, month2, day2);
                cache.setQuery(QueryType.period);
                cache.setPeriod(new LocalDate[]{fDate, sDate});

                Intent intent =  new Intent(AdminDisplayBookingsActivity.this, ListOfBookingsActivity.class);
                startActivity(intent);


            }

        }



    }

    @Override
    protected void onDestroy() {
        presenterAdminDisplayBookings.detach();
        btnToday.setOnClickListener(null);
        btnTomorrow.setOnClickListener(null);
        btnThisWeek.setOnClickListener(null);
        btnNextWeek.setOnClickListener(null);
        btnRangeOfDates.setOnClickListener(null);
        btnDate.setOnClickListener(null);
        btnIdBooking.setOnClickListener(null);
        btnCustomerEmail.setOnClickListener(null);
        cache = null;
        super.onDestroy();
    }
}


