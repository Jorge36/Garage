package com.ger.garage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ger.garage.R;
import com.ger.garage.model.SetUpDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*

this class allow a user to log in the system

 */
public class LoginActivity extends AppCompatActivity {

    // global variables
    private EditText emailId, password;
    private Button btnLogin;
    private TextView haveAccount;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SetUpDao setUpDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpDao = new SetUpDao();
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        haveAccount = findViewById(R.id.haveAccount);

        // firebase authentication listener tell login screen
        // if the user is logged in
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // get current user
                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                if (currentUser != null) { // if it is logged in, it is != null

                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent intentHome;
                    if (!mFirebaseAuth.getCurrentUser().getEmail().equals(setUpDao.getEmailAdmin()))
                        intentHome =  new Intent(LoginActivity.this, UserHomeActivity.class);
                    else
                        intentHome = new Intent(LoginActivity.this, AdminHomeActivity.class);
                    startActivity(intentHome);
                } else { // show toast message
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();

                }

            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                if (pass.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();

                }
                else if(email.isEmpty() && pass.isEmpty()) {

                    Toast.makeText(LoginActivity.this, "The fields are empty!", Toast.LENGTH_SHORT).show();

                } else if(!(email.isEmpty() && pass.isEmpty())) {

                    mFirebaseAuth.signInWithEmailAndPassword(email, pass) // sign in with an email and password
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) { // handler

                                    final String authenticationFailedMessage = "Authentication failed: ";

                                    if (task.isSuccessful()) { // result

                                        Intent intentHome;
                                        // redirect user to home
                                        if (!mFirebaseAuth.getCurrentUser().getEmail().equals(setUpDao.getEmailAdmin()))
                                            intentHome =  new Intent(LoginActivity.this, UserHomeActivity.class);
                                        else
                                            intentHome = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                        startActivity(intentHome);

                                    } else {
                                        // shows error message
                                        Toast.makeText(LoginActivity.this, authenticationFailedMessage + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }


                                }
                            });

                } else {
                    Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnLogin.setOnClickListener(null);
    }

}
