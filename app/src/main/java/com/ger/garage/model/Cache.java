package com.ger.garage.model;

import java.time.LocalDate;
import java.time.Period;


// level of cache to pass information between classes
public interface Cache {

    LocalDate getDate();
    void setDate(LocalDate date);
    LocalDate[] getPeriod();
    void setPeriod(LocalDate[] period);
    Integer getIdBooking();
    void setIdBooking(Integer idBooking);
    String getEmail();
    void setEmail(String email);
    void setQuery(QueryType query);
    QueryType getQuery();

}
