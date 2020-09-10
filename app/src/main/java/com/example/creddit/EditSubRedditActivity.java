package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.io.IOException;

public class EditSubRedditActivity extends AppCompatActivity {

    ImageView editBanner, editImage;
    Button saveEditDetailButton;
    ProgressBar editProgressbar, progressBarBanner, progressBarImage;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef, mRef_post;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Bitmap bitmap, bitmap2;
    EditText subDescriptionSubReddit;
    RadioGroup typesSubReddit,contentSubReddit;
    RadioButton publicSubSubReddit, privateSubSubReddit, restrictedSubSubReddit,anySubReddit,textOnlySubReddit,linksOnlySubReddit;

    String userId, strtype, strcontent,subId;

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

        setContentView(R.layout.activity_edit_sub_reddit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("subreddits");
        mRef_post = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("creddit");

        Intent intent = getIntent();
        subId = intent.getExtras().getString("subId");

        userId = firebaseUser.getUid();

        editBanner = findViewById(R.id.editBannerSubReddit);
        editImage = findViewById(R.id.editImageSubReddit);
        saveEditDetailButton = findViewById(R.id.saveEditDetailButtonSubReddit);
        editProgressbar = findViewById(R.id.editProgressBarSubReddit);
        progressBarBanner = findViewById(R.id.progressBarBannerSubReddit);
        progressBarImage = findViewById(R.id.progressBarimageSubReddit);
        subDescriptionSubReddit = findViewById(R.id.subDescriptionSubReddit);
        typesSubReddit = findViewById(R.id.typesSubReddit);
        contentSubReddit = findViewById(R.id.contentSubReddit);
        publicSubSubReddit = findViewById(R.id.publicSubSubReddit);
        privateSubSubReddit = findViewById(R.id.privateSubSubReddit);
        restrictedSubSubReddit = findViewById(R.id.restrictedSubSubReddit);
        anySubReddit = findViewById(R.id.anySubReddit);
        linksOnlySubReddit = findViewById(R.id.linksOnlySubReddit);
        textOnlySubReddit = findViewById(R.id.textOnlySubReddit);

        loadPreviousSubRedditValues();

        typesSubReddit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.publicSub:
                        strtype = "public";
                        break;
                    case R.id.privateSub:
                        strtype = "private";
                        break;
                    case R.id.restrictedSub:
                        strtype = "restricted";
                        break;
                }
            }
        });

        contentSubReddit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.any:
                        strcontent = "any";
                        break;
                    case R.id.textOnly:
                        strcontent = "text";
                        break;
                    case R.id.linksOnly:
                        strcontent = "link";
                        break;
                }
            }
        });

//        editProfileValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String profileImagePath = dataSnapshot.child("profileImage").getValue(String.class);
//                String profileBannerImagePath = dataSnapshot.child("profileBannerImage").getValue(String.class);
//                if (profileImagePath.equals("null")){
//                    Picasso.get().load(R.drawable.reddit_logo_hd).into(editImage);
//                }else {
//                    Picasso.get().load(profileImagePath).error(R.drawable.reddit_logo_hd).into(editImage);
//                }
//
//                if (profileBannerImagePath.equals("null")){
//                    Picasso.get().load(R.drawable.reddit_logo_hd).into(editBanner);
//                }else {
//                    Picasso.get().load(profileBannerImagePath).error(R.drawable.reddit_logo_hd).into(editBanner);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//
//        mRef.child(userId).addValueEventListener(editProfileValueEventListener);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
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

            }
        });

    }

    private void loadPreviousSubRedditValues() {

        mRef.child(subId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subDescriptionSubReddit.setText(dataSnapshot.child("description").getValue(String.class));

                String subProfileImagePath = dataSnapshot.child("subPicture").getValue(String.class);
                String subProfileBannerImagePath = dataSnapshot.child("subPictureBanner").getValue(String.class);
                if (subProfileImagePath.equals("null")){
                    Picasso.get().load(R.drawable.reddit_logo_hd).into(editImage);
                }else {
                    Picasso.get().load(subProfileImagePath).error(R.drawable.reddit_logo_hd).into(editImage);
                }

                if (subProfileBannerImagePath.equals("null")){
                    Picasso.get().load(R.drawable.reddit_logo_hd).into(editBanner);
                }else {
                    Picasso.get().load(subProfileBannerImagePath).error(R.drawable.reddit_logo_hd).into(editBanner);
                }

                String type = dataSnapshot.child("type").getValue(String.class);
                String content = dataSnapshot.child("content").getValue(String.class);

                if (type.equals("public")){
                    strtype="public";
                    publicSubSubReddit.setChecked(true);
                }
                else if (type.equals("private")){
                    strtype="private";
                    privateSubSubReddit.setChecked(true);
                }
                else if (type.equals("restricted")){
                    strtype="restricted";
                    restrictedSubSubReddit.setChecked(true);
                }

                if (content.equals("any")){
                    strcontent="any";
                    anySubReddit.setChecked(true);
                } else if (content.equals("text")){
                    strcontent="text";
                    textOnlySubReddit.setChecked(true);
                } else if (content.equals("link")){
                    strcontent="link";
                    linksOnlySubReddit.setChecked(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                image_byte_data = byteArrayOutputStream.toByteArray();

                final StorageReference storageReference1 = storageReference.child("subRedditProfileImage").child(subId).child(filepath.getLastPathSegment());

                UploadTask uploadTask = storageReference1.putBytes(image_byte_data);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull final Task<Uri> task) {

                                mRef.child(subId).child("subPicture").setValue(task.getResult().toString());

                                mRef_post.child("posts").child("imagePosts").orderByChild("subId").equalTo(subId).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                mRef_post.child("search").child(subId).child("profilePicture").setValue(task.getResult().toString());

                                progressBarImage.setVisibility(View.GONE);
                                editImage.setVisibility(View.VISIBLE);
                                editImage.setImageURI(filepath);
                                showToast("Profile image updated successfully!");

                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            editBanner.setVisibility(View.GONE);
            progressBarBanner.setVisibility(View.VISIBLE);

            byte[] image_byte_data;
            Uri filepath = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                image_byte_data = byteArrayOutputStream.toByteArray();

                final StorageReference storageReference1 = storageReference.child("subRedditBannerImages").child(subId).child(filepath.getLastPathSegment());

                UploadTask uploadTask = storageReference1.putBytes(image_byte_data);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
                        storageReference1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
//
                                mRef.child(subId).child("subPictureBanner").setValue(task.getResult().toString());
//                                Toast.makeText(getApplicationContext(), "Banner Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                showToast("Banner image updated successfully!");
                                progressBarBanner.setVisibility(View.GONE);
                                editBanner.setVisibility(View.VISIBLE);
                                editBanner.setImageBitmap(bitmap2);
                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showToast(String toast_text){
        LayoutInflater inflater = LayoutInflater.from(EditSubRedditActivity.this);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(toast_text);

        Toast toast = new Toast(EditSubRedditActivity.this);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        mRef.child(userId).removeEventListener(editProfileValueEventListener);
    }
}
