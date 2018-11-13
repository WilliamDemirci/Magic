package com.williamdemirci.magic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar dealToolbar;
    private FloatingActionButton addButton;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        // Toolbar
        dealToolbar = (Toolbar) findViewById(R.id.dealToolbar);
        setSupportActionBar(dealToolbar);

        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDealIntent();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in. If not, switch to login activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            loginIntent();
        }
        else { // ask to fill Username and profile image
            currentUserId = mAuth.getCurrentUser().getUid();
            db.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        if(!task.getResult().exists()) {
                            settingsIntent();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutMenu:
                signOut();
                return true;
            case R.id.settingsMenu:
                settingsIntent();
                return true;
            default:
                return false;
        }
    }

    private void signOut() {
        mAuth.signOut();
        loginIntent();
    }

    private void addDealIntent() { // Add a new deal intent
        Intent newDealActivity = new Intent(MainActivity.this, NewDealActivity.class);
        startActivity(newDealActivity);
        finish();
    }

    private void loginIntent() { // Login Activity Intent
        Intent intentLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }

    private void settingsIntent() { // Settings Activity Intent
        Intent intentSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intentSettingsActivity);
        finish();
    }
}
