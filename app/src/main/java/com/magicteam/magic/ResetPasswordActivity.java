package com.magicteam.magic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "Reset password activity";
    private Toolbar resetPasswordToolbar;
    private FirebaseAuth mAuth;
    private EditText email;
    private Button resetButton;
    private TextView connectionLink;
    private TextView createAccountLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetButton = (Button) findViewById(R.id.resetButtonResetPassword);
        connectionLink = (TextView) findViewById(R.id.connectionLink);
        createAccountLink = (TextView) findViewById(R.id.createAccountLink);
        resetPasswordToolbar = (Toolbar) findViewById(R.id.resetPasswordToolbar);

        // customize toolbar
        setSupportActionBar(resetPasswordToolbar);
        getSupportActionBar().setTitle("Reset your password");

        // reset password
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                email = (EditText) findViewById(R.id.emailResetPassword);
                final String emailAddress = email.getText().toString();

                if(!emailAddress.equals("")) {
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(getApplicationContext(), "Email sent to " + emailAddress, Toast.LENGTH_SHORT).show();
                                        loginIntent();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // connection link
        connectionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent();
            }
        });

        // create account link
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerIntent();
            }
        });
    }

    private void mainIntent() { // MainActivity Intent
        Intent intentMainActivity = new Intent(ResetPasswordActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }

    private void registerIntent() { // Register Activity Intent
        Intent intentRegisterActivity = new Intent(ResetPasswordActivity.this, RegisterActivity.class);
        startActivity(intentRegisterActivity);
        finish();
    }

    private void loginIntent() { // Login Activity Intent
        Intent intentLoginActivity = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }
}
