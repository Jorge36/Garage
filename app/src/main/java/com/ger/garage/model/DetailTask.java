package com.ger.garage.model;

import java.math.BigDecimal;
import java.util.Currency;
/*

Details task: Represent a task performed by a mechanic

 */


public class DetailTask extends Detail {

    private String task;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public DetailTask(Integer id, BigDecimal ampunt, Currency currency, String task) {
        super(id, ampunt, currency);
        this.task = task;
    }
}
