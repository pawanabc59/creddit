package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    ImageView editBanner, editImage;
    EditText editOptionalName, editAbout;
    Button saveEditDetailButton;
    ProgressBar editProgressbar, progressBarBanner, progressBarImage;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef, mRef_post;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Bitmap bitmap, bitmap2;
    AlertDialog alertDialog;

    String userId, optionalName, optionalAbout;

    SharedPref sessionManager;

    ValueEventListener editProfileValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SharedPref(this);
        if (sessionManager.loadNightModeState() == true) {

            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_edit_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("users");
        mRef_post = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("creddit");

        userId = firebaseUser.getUid();

        editBanner = findViewById(R.id.editBanner);
        editImage = findViewById(R.id.editImage);
        editOptionalName = findViewById(R.id.editOptionalName);
        editAbout = findViewById(R.id.editAbout);
        saveEditDetailButton = findViewById(R.id.saveEditDetailButton);
        editProgressbar = findViewById(R.id.editProgressBar);
        progressBarBanner = findViewById(R.id.progressBarBanner);
        progressBarImage = findViewById(R.id.progressBarimage);

        editProfileValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileImagePath = dataSnapshot.child("profileImage").getValue(String.class);
                String profileBannerImagePath = dataSnapshot.child("profileBannerImage").getValue(String.class);
                if (profileImagePath.equals("null")){
                    Picasso.get().load(R.drawable.reddit_logo_hd).into(editImage);
                }else {
                    Picasso.get().load(profileImagePath).error(R.drawable.reddit_logo_hd).into(editImage);
                }

                if (profileBannerImagePath.equals("null")){
                    Picasso.get().load(R.drawable.reddit_logo_hd).into(editBanner);
                }else {
                    Picasso.get().load(profileBannerImagePath).error(R.drawable.reddit_logo_hd).into(editBanner);
                }

                editOptionalName.setText(dataSnapshot.child("optionalName").getValue(String.class));
                editAbout.setText(dataSnapshot.child("optionalAbout").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child(userId).addValueEventListener(editProfileValueEventListener);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
//                alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
//                alertDialog.setTitle("Uploading image...");
//                alertDialog.setMessage("Please wait while image is being uploaded.\n This will disappear automatically");
//                alertDialog.show();
            }
        });

        editBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        saveEditDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveEditDetailButton.setVisibility(View.GONE);
                editProgressbar.setVisibility(View.VISIBLE);

                optionalName = editOptionalName.getText().toString();
                optionalAbout = editAbout.getText().toString();
                if (TextUtils.isEmpty(optionalName)&&TextUtils.isEmpty(optionalAbout)){
                    editOptionalName.setError("This field can not be left empty");
                    editAbout.setError("This field can not be left empty");

                    saveEditDetailButton.setVisibility(View.VISIBLE);
                    editProgressbar.setVisibility(View.GONE);
                }
                else if (TextUtils.isEmpty(optionalName) && !TextUtils.isEmpty(optionalAbout)){
                    mRef.child(userId).child("optionalAbout").setValue(optionalAbout);

                    saveEditDetailButton.setVisibility(View.VISIBLE);
                    editProgressbar.setVisibility(View.GONE);
                }
                else if (!TextUtils.isEmpty(optionalName) && TextUtils.isEmpty(optionalAbout)){
                    mRef.child(userId).child("optionalName").setValue(optionalName);

                    saveEditDetailButton.setVisibility(View.VISIBLE);
                    editProgressbar.setVisibility(View.GONE);
                }
                else if (!TextUtils.isEmpty(optionalName) && !TextUtils.isEmpty(optionalAbout)){
                    mRef.child(userId).child("optionalName").setValue(optionalName);
                    mRef.child(userId).child("optionalAbout").setValue(optionalAbout);

                    mRef_post.child("posts").child("imagePosts").orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    mRef_post.child("posts").child("imagePosts").child(dataSnapshot1.getKey()).child("uploadedBy").setValue(optionalName);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    saveEditDetailButton.setVisibility(View.VISIBLE);
                    editProgressbar.setVisibility(View.GONE);
                    showToast("Profile updated successfully!");
                }
//                Toast.makeText(getApplicationContext(),"Record updated successfully", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){

            editImage.setVisibility(View.GONE);
            progressBarImage.setVisibility(View.VISIBLE);

            byte[] image_byte_data;
            final Uri filepath = data.getData();
//            File file_path = new File(data.getData().getPath());
//            final ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "",
//                    "Loading. Please wait...", true);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
//                bitmap = new Compressor(this)
//                        .setMaxHeight(200)
//                        .setMaxWidth(200)
//                        .setQuality(75)
//                        .compressToBitmap(file_path);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                image_byte_data = byteArrayOutputStream.toByteArray();

                final StorageReference storageReference1 = storageReference.child("profileImages").child(userId).child(filepath.getLastPathSegment());

                UploadTask uploadTask = storageReference1.putBytes(image_byte_data);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull final Task<Uri> task) {

                                mRef.child(userId).child("profileImage").setValue(task.getResult().toString());
//                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();


                                mRef_post.child("posts").child("imagePosts").orderByChild("subId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                mRef_post.child("posts").child("imagePosts").child(dataSnapshot1.getKey()).child("cardPostProfileImage").setValue(task.getResult().toString());

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                mRef_post.child("search").child(userId).child("profilePicture").setValue(task.getResult().toString());

                                progressBarImage.setVisibility(View.GONE);
                                editImage.setVisibility(View.VISIBLE);
                                editImage.setImageURI(filepath);
                                showToast("Profile image updated successfully!");
//                                alertDialog.dismiss();
//                                dialog.dismiss();

                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
//                alertDialog.dismiss();
            }
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            editBanner.setVisibility(View.GONE);
            progressBarBanner.setVisibility(View.VISIBLE);

            byte[] image_byte_data;
            Uri filepath = data.getData();
//            File file_path = new File(filepath.getPath());
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
//                bitmap2 = new Compressor(this)
//                        .setMaxHeight(200)
//                        .setMaxWidth(200)
//                        .setQuality(75)
//                        .compressToBitmap(file_path);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                image_byte_data = byteArrayOutputStream.toByteArray();

                final StorageReference storageReference1 = storageReference.child("profileBannerImages").child(userId).child(filepath.getLastPathSegment());

                UploadTask uploadTask = storageReference1.putBytes(image_byte_data);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
                        storageReference1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
//
                                mRef.child(userId).child("profileBannerImage").setValue(task.getResult().toString());
//                                Toast.makeText(getApplicationContext(), "Banner Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                showToast("Banner image updated successfully!");
                                progressBarBanner.setVisibility(View.GONE);
                                editBanner.setVisibility(View.VISIBLE);
                                editBanner.setImageBitmap(bitmap2);

//                                alertDialog.dismiss();
                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
//                alertDialog.dismiss();
            }
        }
    }

    public void showToast(String toast_text){
        LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(toast_text);

        Toast toast = new Toast(EditProfileActivity.this);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRef.child(userId).removeEventListener(editProfileValueEventListener);
    }
}
