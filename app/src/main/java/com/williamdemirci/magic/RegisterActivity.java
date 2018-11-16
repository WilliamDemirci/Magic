package com.williamdemirci.magic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailRegistration;
    private EditText passwordRegistration;
    private EditText passwordRegistrationVerification;
    private Toolbar toolbarRegistration;
    private ProgressBar registerProgressBar;
    private Button registrationButton;
    private TextView loginLink;
    private TextView resetPasswordLink;
    private FirebaseAuth mAuth;
    private static final String TAG = "SIGN UP (Registration Activity)";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailRegistration = (EditText) findViewById(R.id.emailRegistration);
        passwordRegistration = (EditText) findViewById(R.id.passwordRegistration);
        passwordRegistrationVerification = (EditText) findViewById(R.id.passwordRegistrationVerification);
        registrationButton = (Button) findViewById(R.id.signUpButtonRegistration);
        loginLink = (TextView) findViewById(R.id.connectionLink);
        resetPasswordLink = (TextView) findViewById(R.id.resetPasswordLink);
        toolbarRegistration = (Toolbar) findViewById(R.id.registerToolbar);
        registerProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);

        // customize toolbar
        setSupportActionBar(toolbarRegistration);
        getSupportActionBar().setTitle("Registration");


        // registration button
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProgressBar.setVisibility(View.VISIBLE);
                // get email & password values
                String registrationEmailText = emailRegistration.getText().toString();
                String registrationPasswordText = passwordRegistration.getText().toString();
                String registrationPasswordVerificationText = passwordRegistrationVerification.getText().toString();

                if(registrationPasswordText.equals(registrationPasswordVerificationText)) {
                    // create an account and automatically login
                    mAuth.createUserWithEmailAndPassword(registrationEmailText, registrationPasswordText)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        settingsIntent();
                                    } else {
                                        // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    registerProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Passwords don't march", Toast.LENGTH_SHORT).show();
                    registerProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        // login link
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent();
            }
        });

        // reset password link
        resetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordIntent();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            mainIntent();
        }
    }

    private void mainIntent() { // MainActivity Intent
        Intent intentMainActivity = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }

    private void loginIntent() { // Login Activity Intent
        Intent intentLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }

    private void resetPasswordIntent() { // Reset Password Activity Intent
        Intent intentResetPasswordActivity = new Intent(RegisterActivity.this, ResetPasswordActivity.class);
        startActivity(intentResetPasswordActivity);
        finish();
    }

    private void settingsIntent() { // Account setting Activity Intent
        Intent intentSettingsActivity = new Intent(RegisterActivity.this, SettingsActivity.class);
        startActivity(intentSettingsActivity);
        finish();
    }
}
