package com.ger.garage.model;

import java.math.BigDecimal;
import java.util.Currency;

/*

Class detailItem: represent an item or part of a car
 */

public class DetailItem extends Detail {

    private String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public DetailItem(Integer id, BigDecimal ampunt, Currency currency, String item) {
        super(id, ampunt, currency);
        this.item = item;
    }
}
