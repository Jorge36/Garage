package com.ger.garage.model;

import java.math.BigDecimal;
import java.util.Currency;

/*

 Class details: represent a line of the invoice.
 It was created to design an invoice with lines. Every line can be an item (parts of a car) or task
 (performed by a mechanic)

 */

public abstract class Detail {

    private Integer id;
    private BigDecimal ampunt;
    private Currency currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmpunt() {
        return ampunt;
    }

    public void setAmpunt(BigDecimal ampunt) {
        this.ampunt = ampunt;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Detail(Integer id, BigDecimal ampunt, Currency currency) {
        this.id = id;
        this.ampunt = ampunt;
        this.currency = currency;
    }
}
