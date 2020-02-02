package com.ger.garage.Presenter;
/*

Class which represent a contract between Presenter and View
Activity must implements view and Presenter must implements

 */
public class HomeUserContract {


    public interface Presenter {

        void detach();
        void logOut();
        void getUserDetails();

    }


    public interface View{
        void showUserDetail(String name, String email, String mobilePhone);
    }

}
