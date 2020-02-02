package com.ger.garage.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/*

Class user which represents a user in the system.
Contains getters and setters, different constructors and
toString method

*/

public class User {

    private String id;
    private String name;
    private String mobilePhoneNumber;
    private String email;
    private UserType userType;
    private ArrayList<Vehicle> vehicles;
    private String password;
    private ArrayList<Booking> bookings;

    //@Exclude
    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    //@Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {

        vehicles.add(vehicle);
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public User(String name, String mobilePhone, String email, String password,UserType userType) {
        this.name = name;
        this.mobilePhoneNumber = mobilePhone;
        this.email = email;
        this.password = password;
        this.userType = userType;
        vehicles = new ArrayList<>();
    }

    public User(String id, String name, String mobilePhone, String email, String password,UserType userType) {
        this.id = id;
        this.name = name;
        this.mobilePhoneNumber = mobilePhone;
        this.email = email;
        this.password = password;
        this.userType = userType;
        vehicles = new ArrayList<>();
    }

    public User(String id) {

        this.id = id;

    }

    public User() {
    }

    @Override
    public String toString() {
        return "name: " + name + System.lineSeparator()
               + "mobilePhoneNumber: " + mobilePhoneNumber + System.lineSeparator()
               + "email: " + email;
    }
}
