package com.williamdemirci.magic;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

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
    private EditText startingDateNewDeal;
    private EditText endingDateNewDeal;
    private EditText descriptionNewDeal;
    private EditText categoryNewDeal;
    private TextView notAGoodDeal;
    private ProgressBar progressBarNewDeal;
    private ImageView deleteImage;
    private ImageView deleteStartingDate;
    private ImageView deleteEndingDate;

    // TextView labels
    private TextView labelTitleNewDeal;
    private TextView labelLinkNewDeal;
    private TextView labelPriceNewDeal;
    private TextView labelNormalPriceNewDeal;
    private TextView labelShippingCostNewDeal;
    private TextView labelDiscountCodeNewDeal;
    private TextView labelCategoryNewDeal;
    private TextView labelStartingDateNewDeal;
    private TextView labelEndingDateNewDeal;
    private TextView labelDescriptionNewDeal;

    // for categories
    private String[] listCategories;
    private boolean[] checkedCategories;
    private ArrayList<Integer> selectedCategoriesList;

    // for image
    private Uri imageUri = null;
    private String downloadUri = "";
    private String downloadThumbUri = "";

    // for Firebase
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private Map<String, Object> dealMap;
    private Bitmap compressedImageFile;
    private Boolean imageSuccessfullyUploaded = false;

    // for dates
    private DatePickerDialog.OnDateSetListener startingDate;
    private DatePickerDialog.OnDateSetListener endingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deal);
        
        componentsDeclaration();
        customizeToolbar();
        displayLabel();

        setImage();
        setCategories();
        setDates();
    }

    private void setDates() {
        // set starting and ending dates
        // set starting date
        startingDateNewDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewDealActivity.this,
                        android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        startingDate,
                        year,month,day);
                dialog.show();
            }
        });

        startingDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startingDateNewDeal.setText(day + "/" + (month+1) + "/" + year);
                deleteStartingDate.setVisibility(View.VISIBLE);
            }
        };

        // set ending date
        endingDateNewDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewDealActivity.this,
                        android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        endingDate,
                        year,month,day);
                dialog.show();
            }
        });

        endingDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endingDateNewDeal.setText(day + "/" + (month+1) + "/" + year);
                deleteEndingDate.setVisibility(View.VISIBLE);
            }
        };

        // if deleteStartingDate button is visible (if startingDateNewDeal is not empty)
        deleteStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingDateNewDeal.setText("");
                deleteStartingDate.setVisibility(View.INVISIBLE);
            }
        });

        // if deleteEndingDate button is visible (if endingDateNewDeal is not empty)
        deleteEndingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endingDateNewDeal.setText("");
                deleteEndingDate.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setImage() { // set an image for the new deal
        imageNewDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        // if delete button is visible
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show an alert dialog to confirm deletion
                AlertDialog.Builder alert = new AlertDialog.Builder(NewDealActivity.this);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        imageNewDeal.setImageResource(0);
                        deleteImage.setVisibility(View.INVISIBLE);
                        imageNewDeal.setImageResource(R.drawable.ic_add_a_photo_white_48dp); // restore default image
                        deleteImagesFromFirebase(); // delete images from database cause we download it before submit
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    private void imagePicker() {
        // check if we are running on at least Android Marshmallow (Android 6)
        // to request read storage permission
        // user may have removed this permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(NewDealActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(SettingsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(NewDealActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else {
//                        Toast.makeText(SettingsActivity.this, "You already have permission", Toast.LENGTH_SHORT).show();
                runImagePicker();
            }
        }
        // if we are running on an older version than Marshmallow (Android 6), we don't have to ask permission
        else {
            runImagePicker();
        }
    }

    private void runImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(16,9)
//                                .setMaxCropResultSize()
                .start(NewDealActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                imageNewDeal.setImageURI(imageUri);
                deleteImage.setVisibility(View.VISIBLE);
                firebaseImagePublishing(); // save directly file on Firebase to save time and to fix a bug
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(NewDealActivity.this, "Error : " + result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setCategories() { // set categories using an AlertDialog
        listCategories = getResources().getStringArray(R.array.categoriesArrays); // get checkbox StringArray
        checkedCategories = new boolean[listCategories.length];

        categoryNewDeal.setOnClickListener(new View.OnClickListener() { // on clic of category TextView
            @Override
            public void onClick(View v) {
                // create an AlertDialog (pop-up)
                AlertDialog.Builder categoriesBuilder = new AlertDialog.Builder(NewDealActivity.this);
                categoriesBuilder.setTitle("Select categories");
                categoriesBuilder.setMultiChoiceItems(listCategories, checkedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        // add or remove position of the checkbox to an IntegerArray
                        if(isChecked) {
                            selectedCategoriesList.add(position);
                        }
                        else {
                            selectedCategoriesList.remove(Integer.valueOf(position));
                        }
                    }
                });
                categoriesBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // OK button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        // create a string with all selected values separated by commas
                        for(int i = 0; i < selectedCategoriesList.size(); i++) {
                            item += listCategories[selectedCategoriesList.get(i)];
                            if(i != selectedCategoriesList.size() - 1) {
                                item += ", ";
                            }
                        }
                        categoryNewDeal.setText(item);
                    }
                });

                categoriesBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() { // Dismiss button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                categoriesBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() { // Clear button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < checkedCategories.length; i++) {
                            checkedCategories[i] = false;
                        }
                        selectedCategoriesList.clear();
                        categoryNewDeal.setText("");
                    }
                });

                AlertDialog categoriesDialog = categoriesBuilder.create();
                categoriesDialog.show();
            }
        });
    }

    private void customizeToolbar() {
        setSupportActionBar(newDealToolbar);
        getSupportActionBar().setTitle("Post a new deal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        // categoryNewDeal
        categoryNewDeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(categoryNewDeal.getText().toString().equals("")) { // if field is not empty, display field label
                    labelCategoryNewDeal.setVisibility(View.INVISIBLE);
                }
                else {
                    if(categoryNewDeal.getText().toString().contains(",")) {
                        labelCategoryNewDeal.setText("Categories");
                    }
                    else {
                        labelCategoryNewDeal.setText("Category");
                    }
                    labelCategoryNewDeal.setVisibility(View.VISIBLE);
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
        // for Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        dealMap = new HashMap<>();

        // for categories
        selectedCategoriesList = new ArrayList<>();

        // components
        newDealToolbar = (Toolbar) findViewById(R.id.newDealToolbar);
        titleNewDeal = (EditText) findViewById(R.id.titleNewDeal);
        linkNewDeal = (EditText) findViewById(R.id.linkNewDeal);
        imageNewDeal = (ImageView) findViewById(R.id.imageNewDeal);
        priceNewDeal = (EditText) findViewById(R.id.priceNewDeal);
        notAGoodDeal = (TextView) findViewById(R.id.notAGoodNewDeal);
        normalPriceNewDeal = (EditText) findViewById(R.id.normalPriceNewDeal);
        shippingCostNewDeal = (EditText) findViewById(R.id.shippingCostNewDeal);
        discountCodeNewDeal = (EditText) findViewById(R.id.discountCodeNewDeal);
        categoryNewDeal = (EditText) findViewById(R.id.categoryNewDeal); // use EditText instead of TextView because I can't add the underline bar under the field ><
        startingDateNewDeal = (EditText) findViewById(R.id.startingDateNewDeal); // use EditText instead of TextView because I can't add the underline bar under the field ><
        endingDateNewDeal = (EditText) findViewById(R.id.endingDateNewDeal); // use EditText instead of TextView because I can't add the underline bar under the field ><
        descriptionNewDeal = (EditText) findViewById(R.id.descriptionNewDeal);
        progressBarNewDeal = (ProgressBar) findViewById(R.id.progressBarNewDeal);
        deleteImage = (ImageView) findViewById(R.id.deleteImage);
        deleteStartingDate = (ImageView) findViewById(R.id.deleteStartingDate);
        deleteEndingDate = (ImageView) findViewById(R.id.deleteEndingDate);

        // labels
        labelTitleNewDeal= (TextView) findViewById(R.id.labelTitleNewDeal);
        labelLinkNewDeal= (TextView) findViewById(R.id.labelLinkNewDeal);
        labelPriceNewDeal = (TextView) findViewById(R.id.labelPriceNewDeal);
        labelNormalPriceNewDeal= (TextView) findViewById(R.id.labelNormalPriceNewDeal);
        labelShippingCostNewDeal= (TextView) findViewById(R.id.labelShippingCostNewDeal);
        labelDiscountCodeNewDeal= (TextView) findViewById(R.id.labelDiscountCodeNewDeal);
        labelStartingDateNewDeal= (TextView) findViewById(R.id.labelStartingDateNewDeal);
        labelEndingDateNewDeal= (TextView) findViewById(R.id.labelEndingDateNewDeal);
        labelDescriptionNewDeal= (TextView) findViewById(R.id.labelDescriptionNewDeal);
        labelCategoryNewDeal= (TextView) findViewById(R.id.labelCategoryNewDeal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void mainIntent() { // MainActivity Intent
        Intent intentMainActivity = new Intent(NewDealActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // back button
                cancelCreation();
                return true;
            case R.id.validatePost: // save post (check button)
                publishDeal();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void publishDeal() {
        // Mandatory fields without image
        final String title = titleNewDeal.getText().toString();
        final String price = priceNewDeal.getText().toString();
        final String categories = categoryNewDeal.getText().toString();
        final String description = descriptionNewDeal.getText().toString();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(categories) && !TextUtils.isEmpty(description) && imageSuccessfullyUploaded) { // mandatory fields must be filled in
            progressBarNewDeal.setVisibility(View.VISIBLE);

            // get all non mandatory data
            final String link = linkNewDeal.getText().toString();
            final String normalPrice = normalPriceNewDeal.getText().toString();
            final String shippingCost = shippingCostNewDeal.getText().toString();
            final String discountCode = discountCodeNewDeal.getText().toString();
            final String startingDate = startingDateNewDeal.getText().toString();
            final String endingDate = endingDateNewDeal.getText().toString();

            // set deal HashMap
            // technical data
            dealMap.put("userId", currentUserId);
            dealMap.put("timestamp", FieldValue.serverTimestamp());
            // mandatory fields
            dealMap.put("title", title);
            dealMap.put("price", price);
            dealMap.put("categories", categories);
            dealMap.put("description", description);
            dealMap.put("image", downloadUri);
            dealMap.put("thumb", downloadThumbUri);
            // other fields
            dealMap.put("link", link);
            dealMap.put("normalPrice", normalPrice);
            dealMap.put("shippingCost", shippingCost);
            dealMap.put("discountCode", discountCode);
            dealMap.put("startingDate", startingDate);
            dealMap.put("endingDate", endingDate);

            firebaseDealPublishing();
        }
    }

    private void firebaseImagePublishing() {
        // store image with a unique ID name
        final String fileName = UUID.randomUUID().toString();
        StorageReference filePath = mStorageRef.child("dealImages").child(fileName + ".jpg");
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    File newImageFile = new File(imageUri.getPath());
                    try {
                        compressedImageFile = new Compressor(NewDealActivity.this)
                                .setMaxHeight(480)
                                .setMaxWidth(640)
                                .setQuality(10) // same results for 50 and 10 :/
//                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToBitmap(newImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    downloadUri = task.getResult().getDownloadUrl().toString();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] thumbData = baos.toByteArray();

                    UploadTask uploadTask = mStorageRef.child("thumbsImages").child(fileName + ".jpg").putBytes(thumbData);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadThumbUri = taskSnapshot.getDownloadUrl().toString();
                            imageSuccessfullyUploaded = true;
                        }});
                }
                else {
                    Toast.makeText(NewDealActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void firebaseDealPublishing() {
        db.collection("Deals").add(dealMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(NewDealActivity.this, "Thanks! New deal published", Toast.LENGTH_LONG).show();
                    mainIntent();
                }
                else {
                    Toast.makeText(NewDealActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                progressBarNewDeal.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // on back button pressed (not on toolbar)
        // TODO if at least one field is not empty, display an alert dialog 'do you really want to abandon the creation of the deal?' or something like that
        // TODO do the two elements above for onOptionsItemSelected -> Home button
        cancelCreation();
//        super.onBackPressed();
    }

    private void deleteImagesFromFirebase() {
        mStorageRef.getStorage().getReferenceFromUrl(downloadThumbUri).delete();
        mStorageRef.getStorage().getReferenceFromUrl(downloadUri).delete();
        imageSuccessfullyUploaded = false;
    }

    private void cancelCreation() {
        if (!TextUtils.isEmpty(titleNewDeal.getText()) || !TextUtils.isEmpty(linkNewDeal.getText()) || !TextUtils.isEmpty(descriptionNewDeal.getText()) || !TextUtils.isEmpty(discountCodeNewDeal.getText()) || imageSuccessfullyUploaded) { // check if some big fields are filled in
            // show an alert dialog to confirm data deletion
            AlertDialog.Builder alert = new AlertDialog.Builder(NewDealActivity.this);
            alert.setTitle("Cancel creation");
            alert.setMessage("Are you sure you want to leave this page?\n" +
                    "All your data will be deleted!");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (imageSuccessfullyUploaded) {
                        deleteImagesFromFirebase();
                    }
                    mainIntent();
                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();
        }
        else {
            mainIntent();
        }
    }
}
