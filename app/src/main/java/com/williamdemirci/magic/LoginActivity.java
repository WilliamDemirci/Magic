package com.williamdemirci.magic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button connectionButton;
    private TextView createAccountLink;
    private TextView resetPasswordLink;
    private FirebaseAuth mAuth;
    private static final String TAG = "CONNECTION (Login Activity)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailLogin = (EditText) findViewById(R.id.emailLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        connectionButton = (Button) findViewById(R.id.connectionButtonLogin);
        createAccountLink = (TextView) findViewById(R.id.createAccountLink);
        resetPasswordLink = (TextView) findViewById(R.id.resetPasswordLink);

        // connection button
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get email & password values
                String loginEmailText = emailLogin.getText().toString();
                String loginPasswordText = passwordLogin.getText().toString();

                // connection process
                mAuth.signInWithEmailAndPassword(loginEmailText, loginPasswordText)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    mainIntent();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // create account link
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerIntent();
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
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // check if user is already connected, switch to MainActivity
        if(currentUser != null) {
            mainIntent();
        }
    }

    private void mainIntent() { // MainActivity Intent
        Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }

    private void registerIntent() { // MainActivity Intent
        Intent intentRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intentRegisterActivity);
        finish();
    }

    private void resetPasswordIntent() { // MainActivity Intent
        Intent intentResetPasswordActivity = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intentResetPasswordActivity);
        finish();
    }
}
