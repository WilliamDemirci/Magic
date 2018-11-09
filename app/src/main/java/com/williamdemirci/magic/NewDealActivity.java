package com.williamdemirci.magic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class NewDealActivity extends AppCompatActivity {
    private Toolbar newDealToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);

        newDealToolbar = (Toolbar) findViewById(R.id.newDealToolbar);

        // customize toolbar
        setSupportActionBar(newDealToolbar);
        getSupportActionBar().setTitle("Post a new deal");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.validatePost) {
            Toast.makeText(NewDealActivity.this, "ok button", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return false;
        }
    }
}
