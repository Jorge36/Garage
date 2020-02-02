package com.ger.garage.model;

/*

Class make: represent make and model of a car: Ford (Make) Fiesta (Model)

*/

public class Make {

    private String name;
    private String model;

    public Make(String name, String model) {
        this.name = name;
        this.model = model;
    }

    public Make() { }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {

        if (this.model != "")
            return this.name + " " + this.model;
        else
            return this.name;
    }
}
