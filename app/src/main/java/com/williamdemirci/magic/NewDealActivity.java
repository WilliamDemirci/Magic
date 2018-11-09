package com.williamdemirci.magic;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class NewDealActivity extends AppCompatActivity {
    private Toolbar newDealToolbar;
    private EditText titleNewDeal;
    private EditText linkNewDeal;
    private ImageView imageNewDeal;
    private EditText priceNewDeal;
    private EditText normalPriceNewDeal;
    private EditText shippingCostNewDeal;
    private EditText discountCodeNewDeal;
    private Spinner spinnerCategoryNewDeal;
    private EditText startingDateNewDeal;
    private EditText endingDateNewDeal;
    private EditText descriptionNewDeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);

        newDealToolbar = (Toolbar) findViewById(R.id.newDealToolbar);

        // customize toolbar
        setSupportActionBar(newDealToolbar);
        getSupportActionBar().setTitle("Post a new deal");

        // drop down list
        spinnerCategoryNewDeal = (Spinner) findViewById(R.id.categoryNewDeal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryArrays, R.layout.spinner_category_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryNewDeal.setAdapter(adapter);
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
            return true;
        }
        else{
            return false;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
