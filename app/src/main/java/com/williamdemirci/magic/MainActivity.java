package com.williamdemirci.magic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar dealToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Toolbar
        dealToolbar = (Toolbar) findViewById(R.id.dealToolbar);
        setSupportActionBar(dealToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in. If not, switch to login activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null) {
//            loginIntent();
//        }
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
