package com.ger.garage.Presenter;

import androidx.annotation.NonNull;

import com.ger.garage.model.Make;
import com.ger.garage.model.SetUpDao;
import com.ger.garage.model.User;
import com.ger.garage.model.UserDao;
import com.ger.garage.model.UserType;
import com.ger.garage.model.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;


public class PresenterRegister implements OnCompleteListener<AuthResult>, OnSuccessListener<Void>, OnFailureListener {

    // global variables
    // Userdao to bring user information from database
    private UserDao userDao;
    // view, presenter knows the view
    private View view;
    // current user
    private User user;
    // contain customization
    private SetUpDao setUpDao;
    // exp regular to validate plate number
    private final String regexPlateNumer = "^([0-9]{2})[12]?-(C|CE|CN|CW|D|DL|G|KE|KK|KY|L|LD|LH|LM|LS|MH|MN|MO|OY|RN|SO|T|W|WH|WW|WX|LK|TN|TS|WD)-([0-9]{1,6})$";

    public PresenterRegister(View view) {
        this.view = view;
        userDao = new UserDao();
        setUpDao = new SetUpDao();
    }

    public boolean isLoggedIn() {

        return userDao.existCurrentUser();

    }

    // Register a user calling a UserDao class
    public void register(String email, String password, String mobilePhoneNumber, String name, String vehiclePlate, String vehicleMake,
                         String vehicleEngineType, String vehicleType) {

        Make make = new Make(vehicleMake, "");
        Vehicle vehicle = new Vehicle(make, vehiclePlate, vehicleEngineType, vehicleType);
        user = new User(name, mobilePhoneNumber, email, password, UserType.user);
        user.addVehicle(vehicle);
        userDao.createUser(user, this);

    }

    // result from registration
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        final String registrationFailedMessage = "Registration failed: ";

        if (task.isSuccessful()) { // successfull registration

            String id = userDao.getUid();

            user.setId(id);

            userDao.saveUser(user, this, this);

        } else { // error registration
            view.showRegistrationErrorMessage(registrationFailedMessage + task.getException().getMessage());
        }
    }

    // redirect user to user/admin home
    public void getHomeActivity() {

        onSuccess(null);

    }

    // redirect user depending if this one is admin or not
    @Override
    public void onSuccess(Void aVoid) {

        if (!setUpDao.getEmailAdmin().equals(userDao.getEmail()))
            view.goToHomePage(UserType.user.toString());
        else
            view.goToHomePage(UserType.admin.toString());
    }

    @Override
    public void onFailure(@NonNull Exception e) {

        //  I could implement another presenter to create a handler to get the result from
        // delete user. in other words define a new listener
        // i can do this here because this class already implement OnCompleteListener<AuthResult>
        userDao.deleteUser();

    }

    // make vehicle from setup
    public ArrayList<String> getVehicleMakes(String vehicleType) {

        switch (vehicleType) {
            case "car":
                return setUpDao.getCarMake();
            case "motorbike":
                return setUpDao.getMotorbikeMake();
            case "small van":
                return setUpDao.getSmallVanMake();
            case "small bus":
                return setUpDao.getSmallBusMake();
            default:
                return new ArrayList<>();
        }

    }

    // type of vehicle from setup
    public ArrayList<String> getVehicleType() {

        return setUpDao.getVehicleType();

    }

    // engine type vehicle from setup
    public ArrayList<String> getVehicleEngineType() {

        return setUpDao.getVehicleEngineType();

    }

    // clean presenter before screen is destroyed
    public void detach() {

        this.view = null;
        this.userDao = null;
        this.setUpDao = null;
        this.user = null;
    }

    // check number plate using a regular expression
    public boolean isPlateNumbervalid(String plateNumer) {

        return (plateNumer.matches(regexPlateNumer));

    }

    // interface implemented by registerActivity
    public interface View{

        void showRegistrationErrorMessage(String errorMessage);
        void goToHomePage(String userType);

    }



}
