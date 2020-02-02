package com.ger.garage.Presenter;

import androidx.annotation.Nullable;

public class FirebaseException extends Exception {

    private String message;

    public FirebaseException(String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
