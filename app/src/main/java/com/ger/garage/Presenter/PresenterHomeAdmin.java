package com.ger.garage.Presenter;

import com.ger.garage.model.UserDao;

public class PresenterHomeAdmin implements HomeAdminContract.Presenter {


    private UserDao userDao;
    private HomeAdminContract.View view;

    public PresenterHomeAdmin(HomeAdminContract.View view) {
        this.view = view;
        this.userDao = new UserDao();
    }




    @Override
    public void detach() {

        this.userDao = null;
        this.view = null;

    }

    @Override
    public void logOut() {

        userDao.logOut();

    }

}
