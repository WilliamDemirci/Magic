package com.williamdemirci.magic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView imageSettingProfile;
    private Uri profileImageUri = null;
    private EditText usernameSetting;
    private Button settingSaveButton;
    private ProgressBar settingSaveProgressBarSetting;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageSettingProfile = (CircleImageView) findViewById(R.id.imageAccountSetting);
        usernameSetting = (EditText) findViewById(R.id.usernameSetting);
        settingSaveButton = (Button) findViewById(R.id.settingSaveButton);
        settingSaveProgressBarSetting = (ProgressBar) findViewById(R.id.settingSaveProgressBarSetting);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        settingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameSetting.getText().toString();

                if(!TextUtils.isEmpty(username)) { // username is mandatory
                    settingSaveProgressBarSetting.setVisibility(View.VISIBLE);
                    String userId = mAuth.getCurrentUser().getUid();

                    if(profileImageUri != null) { // if there is an image, we save changes
                        StorageReference imagePath = mStorageRef.child("profileImages").child(userId);
                        imagePath.putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()) {
//                                Task<Uri> downloadUri = task.getResult().getMetadata().getReference().getDownloadUrl();
                                    Toast.makeText(SettingsActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(SettingsActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                settingSaveProgressBarSetting.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }
        });

        imageSettingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if we are running on at least Android Marshmallow (Android 6)
                // to request read storage permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(SettingsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else {
//                        Toast.makeText(SettingsActivity.this, "You already have permission", Toast.LENGTH_SHORT).show();
                        // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .setAspectRatio(1,1)
//                                .setMaxCropResultSize()
                                .start(SettingsActivity.this);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                imageSettingProfile.setImageURI(profileImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
