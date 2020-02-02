package com.ger.garage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ger.garage.Presenter.AllocateMechanicContract;
import com.ger.garage.Presenter.PresenterAllocateMechanic;
import com.ger.garage.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AllocateMechanicActivity extends AppCompatActivity implements AllocateMechanicContract.View, ListViewAdapter.CheckBoxCheckedListener {

    private LocalDate date;
    private Spinner spinnerMechanic;
    private ListViewAdapter adapter;
    private ListView bookingsListView;
    private ArrayList<String> bookings;
    private HashMap<Integer, String> positionsChecked;
    //private HashMap<Integer, String> mechanicsSaved;
    private HashMap<String, String> mechanicsToAllocate;
    private ArrayList<Boolean> checkBoxes;

    //private Button btnAllocate;
    private String mechanic;
    //private Button btnSave;
    private AllocateMechanicContract.Presenter presenter;
    private ProgressBar progressBar;
    private final String NoBookingsSelected = "No bookings were found";
    private final String chooseMechanic = "Choose a mechanic";
    private final String updateMechanicSuccessfull = "The mechanics/s were updated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_mechanic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String dateAux = intent.getStringExtra("date");
        date = LocalDate.parse(dateAux);
        positionsChecked = new HashMap<>();
        //mechanicsSaved = new HashMap<>();
        mechanicsToAllocate = new HashMap<>();
        spinnerMechanic = findViewById(R.id.spinnerMechanic);
        bookingsListView = findViewById(R.id.listViewBookings);
        progressBar = findViewById(R.id.progressBarListOfBookings);
        progressBar.setVisibility(View.VISIBLE);
        //btnAllocate = findViewById(R.id.allocate);
        //btnSave = findViewById(R.id.save);
        presenter = new PresenterAllocateMechanic(AllocateMechanicActivity.this);
        loadMechanics();
        allocateMechanic();
    }

    private void allocateMechanic() {

        /*
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mechanic == "")
                    return;

                Iterator it = positionsChecked.entrySet().iterator();

                while (it.hasNext()) {

                    Map.Entry pair = ((Map.Entry)it.next());

                    mechanicsSaved.put((Integer) pair.getKey(), mechanic);

                }

            }
        });

        btnAllocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Iterator it = mechanicsSaved.entrySet().iterator();

                while (it.hasNext()) {

                    Map.Entry pair = ((Map.Entry)it.next());

                    String booking = bookings.get((Integer)pair.getKey());

                    mechanicsToAllocate.put(booking, (String) pair.getValue());


                }

                presenter.allocateMechanic(mechanicsToAllocate);

            }
        });

        */

    }

    private void loadMechanics() {

        final ArrayList<String> mechanics = presenter.getMechanics();
        mechanics.add(0, chooseMechanic);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, mechanics);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMechanic.setAdapter(dataAdapter);
        spinnerMechanic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0)
                    mechanic = parent.getItemAtPosition(position).toString();
                else
                    mechanic = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.allocate_mechanic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                // No implemented
                break;
            case R.id.allocateMechanic:
                if (mechanic == "")
                    break;

                Iterator it = positionsChecked.entrySet().iterator();

                String boooking;

                while (it.hasNext()) {

                    Map.Entry pair = ((Map.Entry)it.next());

                    boooking = bookings.get((Integer)pair.getKey());

                    mechanicsToAllocate.put(boooking, mechanic);

                }

                if (mechanicsToAllocate.isEmpty())
                    break;
                else
                    presenter.allocateMechanic(mechanicsToAllocate);

            default:
                // No implemented
        }

        return true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        getBookings();
    }

    private void getBookings() {

        presenter.getBookings(date);

    }

    @Override
    public void showBookings(ArrayList<String> bookings) {

        this.bookings = bookings;
        if (checkBoxes == null) {
            checkBoxes = new ArrayList<>();
            for (int i = 0; i < this.bookings.size(); i++)
                checkBoxes.add(false);
        }

        adapter = new ListViewAdapter(this.bookings,checkBoxes,AllocateMechanicActivity.this);
        bookingsListView.setAdapter(adapter);
        adapter.setCheckedListener(AllocateMechanicActivity.this);
        if (this.bookings.isEmpty())
            Toast.makeText(this, NoBookingsSelected, Toast.LENGTH_SHORT ).show();
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showBookingsUpdate(ArrayList<String> bookings) {

        if (checkBoxes == null) {
            checkBoxes = new ArrayList<>();
            for (int i = 0; i < this.bookings.size(); i++)
                checkBoxes.add(false);
        }
        adapter = (ListViewAdapter) bookingsListView.getAdapter();
        adapter.clear();
        adapter.addAll(bookings);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, updateMechanicSuccessfull, Toast.LENGTH_SHORT ).show();

    }

    @Override
    public void showErrorMessage(String errorMessage) {

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT ).show();
        if (progressBar != null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getCheckBoxCheckedListener(int position, Boolean isChecked) {

        if (isChecked) {
            positionsChecked.put(position, bookings.get(position));
            checkBoxes.set(position, true);
        }
        else {
            positionsChecked.remove(position);
            checkBoxes.set(position, false);
        }
    }


}
