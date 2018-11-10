package com.williamdemirci.magic;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewDealActivity extends AppCompatActivity {
    // components
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
    private TextView notAGoodDeal;

    // TextView labels
    private TextView labelTitleNewDeal;
    private TextView labelLinkNewDeal;
    private TextView labelPriceNewDeal;
    private TextView labelNormalPriceNewDeal;
    private TextView labelShippingCostNewDeal;
    private TextView labelDiscountCodeNewDeal;
    private TextView labelStartingDateNewDeal;
    private TextView labelEndingDateNewDeal;
    private TextView labelDescriptionNewDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);

        componentsDeclaration();
        customizeToolbar();
        displayLabel();
        customizeCategoryDropDownList();
    }

    private void customizeCategoryDropDownList() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryArrays, R.layout.spinner_category_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryNewDeal.getBackground().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP); // change arrow color
        spinnerCategoryNewDeal.setAdapter(adapter);
    }

    private void customizeToolbar() {
        setSupportActionBar(newDealToolbar);
        getSupportActionBar().setTitle("Post a new deal");
    }

    private void displayLabel() {
        // display label above the field if the field is not empty + easter egg if price > 999999 Kc
        // titleNewDeal
        titleNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(titleNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelTitleNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelTitleNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // linkNewDeal
        linkNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(linkNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelLinkNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelLinkNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // priceNewDeal
        priceNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(priceNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelPriceNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelPriceNewDeal.setVisibility(View.VISIBLE);
                    if(Float.valueOf(priceNewDeal.getText().toString()) > 999999.0f) { // if price > 999999, display an easter egg
                        notAGoodDeal.setVisibility(View.VISIBLE);
                    }
                    else {
                        notAGoodDeal.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // normalPriceNewDeal
        normalPriceNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(normalPriceNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelNormalPriceNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelNormalPriceNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // shippingCostNewDeal
        shippingCostNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(shippingCostNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelShippingCostNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelShippingCostNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // discountCodeNewDeal
        discountCodeNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(discountCodeNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelDiscountCodeNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelDiscountCodeNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // startingDateNewDeal
        startingDateNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(startingDateNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelStartingDateNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelStartingDateNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // endingDateNewDeal
        endingDateNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(endingDateNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelEndingDateNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelEndingDateNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // descriptionNewDeal
        descriptionNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(descriptionNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelDescriptionNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    labelDescriptionNewDeal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void componentsDeclaration() {
        // components
        newDealToolbar = (Toolbar) findViewById(R.id.newDealToolbar);
        titleNewDeal = (EditText) findViewById(R.id.titleNewDeal);
        linkNewDeal = (EditText) findViewById(R.id.linkNewDeal);
        imageNewDeal = (ImageView) findViewById(R.id.imageAccountSetting);
        priceNewDeal = (EditText) findViewById(R.id.priceNewDeal);
        notAGoodDeal = (TextView) findViewById(R.id.notAGoodNewDeal);
        normalPriceNewDeal = (EditText) findViewById(R.id.normalPriceNewDeal);
        shippingCostNewDeal = (EditText) findViewById(R.id.shippingCostNewDeal);
        discountCodeNewDeal = (EditText) findViewById(R.id.discountCodeNewDeal);
        spinnerCategoryNewDeal = (Spinner) findViewById(R.id.categoryNewDeal);
        startingDateNewDeal = (EditText) findViewById(R.id.startingDateNewDeal);
        endingDateNewDeal = (EditText) findViewById(R.id.endingDateNewDeal);
        descriptionNewDeal = (EditText) findViewById(R.id.descriptionNewDeal);

        // labels
        labelTitleNewDeal= (TextView) findViewById(R.id.labelTitleNewDeal);
        labelLinkNewDeal= (TextView) findViewById(R.id.labellinkNewDeal);
        labelPriceNewDeal = (TextView) findViewById(R.id.labelPriceNewDeal);
        labelNormalPriceNewDeal= (TextView) findViewById(R.id.labelNormalPriceNewDeal);
        labelShippingCostNewDeal= (TextView) findViewById(R.id.labelShippingCostNewDeal);
        labelDiscountCodeNewDeal= (TextView) findViewById(R.id.labelDiscountCodeNewDeal);
        labelStartingDateNewDeal= (TextView) findViewById(R.id.labelStartingDateNewDeal);
        labelEndingDateNewDeal= (TextView) findViewById(R.id.labelEndingDateNewDeal);
        labelDescriptionNewDeal= (TextView) findViewById(R.id.labelDescriptionNewDeal);
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
