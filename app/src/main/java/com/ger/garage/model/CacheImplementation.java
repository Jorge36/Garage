package com.ger.garage.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

// implementation of interface cache to pass information between AdminDisplayBookingsActivity
// and ListOfBookingsActivity
// i use sharedPreferences to save information
public class CacheImplementation implements Cache {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private QueryType query;
    private LocalDate date;
    private LocalDate[] period;
    private Integer idBooking;
    private String email;
    public final String sharedPref = "SharedPref";

    public CacheImplementation(Context context) {

        this.sharedPreferences = context.getSharedPreferences(sharedPref,Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setDate(LocalDate date) {
        editor.putString("date", date.toString());
        editor.apply();
        this.date = date;
    }

    @Override
    public LocalDate[] getPeriod() {

        if (period != null)
            return period;
        else {
            period = new LocalDate[2];
            LocalDate startDate = LocalDate.parse(sharedPreferences.getString("startDate", ""));
            LocalDate endDate  = LocalDate.parse(sharedPreferences.getString("endDate", ""));
            this.period[0] = startDate;
            this.period[1] = endDate;
            return period;
        }
    }


    public void setPeriod(LocalDate[] period) {
        editor.putString("startDate", period[0].toString());
        editor.putString("endDate", period[1].toString());
        editor.apply();
        this.period = period;
    }

    public void setIdBooking(Integer idBooking) {
        editor.putInt("idBooking", idBooking);
        editor.apply();
        this.idBooking = idBooking;
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.apply();
        this.email = email;
    }

    @Override
    public LocalDate getDate() {

        if (date != null)
            return date;
        else {
            this.date = LocalDate.parse(sharedPreferences.getString("date", ""));
            return date;

        }
    }

    @Override
    public Integer getIdBooking() {

        if (idBooking != null)
            return idBooking;
        else {
            this.idBooking = sharedPreferences.getInt("idBooking", -1);
            return idBooking;

        }
    }

    @Override
    public String getEmail() {

        if (email != null)
            return email;
        else {

            this.email = sharedPreferences.getString("email","");
            return this.email;

        }
    }


    @Override
    public void setQuery(QueryType query)
    {
        editor.putString("query", query.toString());
        editor.apply();
        this.query = query;
    }

    @Override
    public QueryType getQuery() {

        if (query != null)
            return query;
        else {


            switch (sharedPreferences.getString("query", "")) {

                case "date":
                    this.query = QueryType.date;
                    break;
                case "period":
                    this.query = QueryType.period;
                    break;
                case "idBooking":
                    this.query = QueryType.idBooking;
                    break;
                case "emailCustomer":
                    this.query = QueryType.emailCustomer;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + QueryType.NoExist);

            }
            return this.query;
        }

    }

}
