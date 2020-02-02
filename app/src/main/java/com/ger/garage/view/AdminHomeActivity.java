package com.ger.garage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.ger.garage.Presenter.HomeAdminContract;
import com.ger.garage.Presenter.PresenterHomeAdmin;
import com.ger.garage.R;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Calendar;

public class AdminHomeActivity extends AppCompatActivity implements HomeAdminContract.View, DatePickerDialog.OnDateSetListener {

    private Button btnMakeAAdminBooking;
    private Button btnDisplayAdminBooking;
    private Button btnAllocateMechanic;
    private Button btnAllocateCost;
    private HomeAdminContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        presenter = new PresenterHomeAdmin(this);

        btnMakeAAdminBooking = findViewById(R.id.btnMakeAAdminBooking);
        btnDisplayAdminBooking = findViewById(R.id.btndisplayAdminBooking);
        btnAllocateMechanic = findViewById(R.id.btnAllocateMechanic);
        btnAllocateCost = findViewById(R.id.btnAllocateCost);

        setListeners();

    }

    private void setListeners() {

        btnMakeAAdminBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(AdminHomeActivity.this, );
                // startActivity(intent);

            }
        });

        btnDisplayAdminBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminHomeActivity.this, AdminDisplayBookingsActivity.class);
                startActivity(intent);

            }
        });

        btnAllocateMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AdminHomeActivity.this,
                         AdminHomeActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();

            }
        });

        btnAllocateCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(AdminHomeActivity.this, AllocateCostActivity.class);
                startActivity(intent);
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
                Intent intentRegister = new Intent(AdminHomeActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            default:
                break;
        }

        return true;

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnAllocateCost.setOnClickListener(null);
        btnAllocateMechanic.setOnClickListener(null);
        btnDisplayAdminBooking.setOnClickListener(null);
        btnMakeAAdminBooking.setOnClickListener(null);
        presenter.detach();
        this.presenter = null;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Intent intent = new Intent(AdminHomeActivity.this, AllocateMechanicActivity.class);
        intent.putExtra("date", LocalDate.of(year, month + 1, dayOfMonth).toString());
        startActivity(intent);

    }
}
