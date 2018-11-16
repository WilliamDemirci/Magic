package com.williamdemirci.magic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbarSettings;
    private CircleImageView imageSettingProfile;
    private boolean imageHasChanged = false;
    private Uri profileImageUri = null;
    private EditText usernameSetting;
    private ProgressBar settingSaveProgressBarSetting;
    private String userId;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private Bitmap compressedImage;
    private String actualUsername;
    private String actualImage;
    private String newUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        componentsDeclaration();
        customizeToolbar();

        getUsernameAndProfileImageFromDatabase();
        setProfileImage();
    }

    private void setProfileImage() {
        imageSettingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });
    }

    private void imagePicker() {
        // check if we are running on at least Android Marshmallow (Android 6)
        // to request read storage permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(SettingsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
//                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL) // There is an uncorrected bug on Android 9 for CropSjape.OVAL (16/11/2018)
                .setAspectRatio(1,1)
//                                .setMaxCropResultSize()
                .start(SettingsActivity.this);
    }

    private void saveSettings() {
        if(!TextUtils.isEmpty(newUsername) && profileImageUri != null) { // username and image are mandatory
//                    if(true) {
            settingSaveProgressBarSetting.setVisibility(View.VISIBLE);
            if(imageHasChanged) {
                File newImageFile = new File(profileImageUri.getPath());
                try {
                    compressedImage = new Compressor(SettingsActivity.this)
                            .setMaxWidth(125)
                            .setMaxHeight(125)
                            .setQuality(75)
                            .compressToBitmap(newImageFile);
//                                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                                    .compressToFile(actualImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                userId = mAuth.getCurrentUser().getUid(); // to delete
//                Toast.makeText(SettingsActivity.this, "userId = " + userId, Toast.LENGTH_SHORT).show();
                StorageReference image_path = mStorageRef.child("profileImages").child(userId + ".jpg");
                image_path.putBytes(thumbData).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            storeFirestore(task, newUsername);
                        }
                        else {
                            Toast.makeText(SettingsActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        settingSaveProgressBarSetting.setVisibility(View.INVISIBLE);
                    }
                });
            } // if(imagesHasChanged)
            else {
                settingSaveProgressBarSetting.setVisibility(View.INVISIBLE);
                storeFirestore(null, newUsername);

            }
        }
    }

    private void getUsernameAndProfileImageFromDatabase() {
        // get username and image from database
        db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) { // if data exist
                        actualUsername = task.getResult().getString("username");
                        actualImage = task.getResult().getString("image");

                        usernameSetting.setText(actualUsername);
                        profileImageUri = Uri.parse(actualImage);

                        // placeholder image
                        RequestOptions defaultImageRequest = new RequestOptions();
                        defaultImageRequest.placeholder(R.mipmap.default_profile);

                        // glide (caching images)
                        Glide.with(SettingsActivity.this)
                                .setDefaultRequestOptions(defaultImageRequest)
                                .load(actualImage).into(imageSettingProfile);
                    }
                }
                else {
                    Toast.makeText(SettingsActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                settingSaveProgressBarSetting.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void componentsDeclaration() {
        imageSettingProfile = (CircleImageView) findViewById(R.id.imageAccountSetting);
        usernameSetting = (EditText) findViewById(R.id.usernameSetting);
        settingSaveProgressBarSetting = (ProgressBar) findViewById(R.id.settingSaveProgressBarSetting);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        toolbarSettings = (Toolbar) findViewById(R.id.settingsToolbar);
    }

    private void customizeToolbar() {
        // customize toolbar
        setSupportActionBar(toolbarSettings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String username) {
        Uri downloadUri;

        if(task != null) { // if image has changed
            downloadUri = task.getResult().getDownloadUrl(); // problème : l'url stockée sur Firestore n'est pas valide
//            Toast.makeText(SettingsActivity.this, "downloadUri : " + downloadUri, Toast.LENGTH_SHORT).show();
        }
        else { // if image hasn't changed
            downloadUri = profileImageUri;
        }
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("image", downloadUri.toString());
        db.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
                    mainIntent();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
//                                Task<Uri> downloadUri = task.getResult().getMetadata().getReference().getDownloadUrl();
//        Toast.makeText(SettingsActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                imageSettingProfile.setImageURI(profileImageUri);
                imageHasChanged = true;
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(SettingsActivity.this, "Error : " + result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void mainIntent() { // MainActivity Intent
        Intent intentMainActivity = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }

    @Override
    public void onBackPressed() { // on back button pressed (not on toolbar)
        doBackup();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // back button
                doBackup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doBackup() { // make save only if there is a modification
        newUsername = usernameSetting.getText().toString();
        if(!actualUsername.equals(newUsername) || imageHasChanged) {
            saveSettings();
        }
        else {
            mainIntent();
        }
    }
}