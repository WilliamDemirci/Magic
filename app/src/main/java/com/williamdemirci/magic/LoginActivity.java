package com.williamdemirci.magic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button connectionButtonLogin;
    private Button createAccountButtonLogin;
    private Button forgotPasswordButtonLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "CONNECTION (Login Activity)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailLogin = (EditText) findViewById(R.id.emailLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        connectionButtonLogin = (Button) findViewById(R.id.connectionButtonLogin);
        createAccountButtonLogin = (Button) findViewById(R.id.createAccountButtonLogin);
        forgotPasswordButtonLogin = (Button) findViewById(R.id.forgotPasswordButtonLogin);

        // connection button
        connectionButtonLogin.setOnClickListener(new View.OnClickListener() {
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
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // check if user is already connected, switch to MainActivity
        if(currentUser != null) {
            Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentMainActivity);
            finish();
        }
    }
}
