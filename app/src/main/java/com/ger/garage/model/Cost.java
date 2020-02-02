package com.ger.garage.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

/*

Cost class: Represent cost of a booking, it is used to print invoices
It contains an array of details (items and/or tasks)

*/

public class Cost {

    private BigDecimal totalAmount;
    private Currency currency;
    private ArrayList<Detail> details;


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Cost{" +
                "totalAmount=" + totalAmount +
                ", currency=" + currency +
                ", details=" + details +
                '}';
    }
}
