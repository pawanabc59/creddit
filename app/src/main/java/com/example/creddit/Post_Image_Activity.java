package com.example.creddit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post_Image_Activity extends AppCompatActivity {

    SharedPref sharedPref;
    CardView open_camera, open_gallary;
    ImageView gallery_image;
    LinearLayout linear_cam_gallery;
    private Bitmap bitmap;
    byte[] image_byte_data;
    Uri filepath, uri;
    TextView postImagePost, postImageTitle, spoiler, spoilerFill, nsfw, nsfwFill;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference mRef, mRef_post, mRef_user;
    String userId, pushId, postTitle, currentDate, cardPostProfile, subName, subId, subType;
    SimpleDateFormat simpleDateFormat;
    Date date;
    int numberOfPosts, spoiler_number, nsfw_number, imageUploaded=0;
    ProgressBar postProgressBar;
    ValueEventListener numberOfPostValueEventListener, cardPostProfileValueEventListener, followingCommunityValueEventListener;
    SearchableSpinner searchableSpinner;
    List<String> subNameList, subIdList, cardPostProfileList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_post__image_);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        date = new Date();
        currentDate = simpleDateFormat.format(date);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
        subId = userId;
        subType = "user";

        mRef = firebaseDatabase.getReference("creddit");
        mRef_user = mRef.child("users").child(userId);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("creddit");

        open_camera = findViewById(R.id.post_image_open_camera);
        open_gallary = findViewById(R.id.post_image_open_gallery);

        gallery_image = findViewById(R.id.gallery_image);
        linear_cam_gallery = findViewById(R.id.linear_cam_gallery);
        postImagePost = findViewById(R.id.post_image_post);
        postImageTitle = findViewById(R.id.post_image_title);
        postProgressBar = findViewById(R.id.postProgressBar);

        searchableSpinner = findViewById(R.id.subSearch);

        nsfw = findViewById(R.id.nsfw);
        nsfwFill = findViewById(R.id.nsfw_fill);
        spoiler = findViewById(R.id.spoiler);
        spoilerFill = findViewById(R.id.spoiler_fill);

        nsfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nsfw.setVisibility(View.GONE);
                nsfwFill.setVisibility(View.VISIBLE);
                nsfw_number = 1;
            }
        });

        subNameList = new ArrayList<>();
        subIdList = new ArrayList<>();
        cardPostProfileList = new ArrayList<>();
        subNameList.add("My profile");
        subIdList.add(userId);
        cardPostProfileList.add("my profile picture");
        followedCommunity(subNameList, subIdList, cardPostProfileList);

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subName = subNameList.get(i);
                subId = subIdList.get(i);
                if (!subIdList.get(i).equals(userId)){
                    subType="sub";
                }
                cardPostProfile = cardPostProfileList.get(i);
//                Toast.makeText(getApplicationContext(), "You clicked "+ subNameList.get(i)+" where subid is "+subIdList.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nsfwFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nsfwFill.setVisibility(View.GONE);
                nsfw.setVisibility(View.VISIBLE);
                nsfw_number = 0;
            }
        });

        spoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spoiler.setVisibility(View.GONE);
                spoilerFill.setVisibility(View.VISIBLE);
                spoiler_number = 1;
            }
        });

        spoilerFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spoilerFill.setVisibility(View.GONE);
                spoiler.setVisibility(View.VISIBLE);
                spoiler_number = 0;
            }
        });

        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "This will open the camera", Toast.LENGTH_SHORT).show();
            }
        });

        open_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(Post_Image_Activity.this);
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 1);
            }
        });

        postImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postImagePost.setVisibility(View.GONE);
                postProgressBar.setVisibility(View.VISIBLE);

                postTitle = postImageTitle.getText().toString();

                if (postTitle.equals("")) {
                    postImagePost.setVisibility(View.VISIBLE);
                    postProgressBar.setVisibility(View.GONE);
                    postImageTitle.setError("Please add a title to post");
                } else if (imageUploaded == 1){

                        numberOfPostValueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    numberOfPosts = ((Long) dataSnapshot.getValue()).intValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };

                    mRef.child("posts").child("numberOfPosts").addListenerForSingleValueEvent(numberOfPostValueEventListener);

//                    cardPostProfileValueEventListener = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                cardPostProfile = dataSnapshot.child("profileImage").getValue().toString();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    };
//
//                    mRef_user.addListenerForSingleValueEvent(cardPostProfileValueEventListener);

                    pushId = mRef.push().getKey();
                    mRef_post = mRef.child("posts").child("imagePosts").child(pushId);

                    UploadTask uploadTask = storageReference.child("posts").child(userId).child(filepath.getLastPathSegment()).putBytes(image_byte_data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("posts").child(userId).child(filepath.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull final Task<Uri> task) {

                                    mRef_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (subType.equals("user")){
                                                subName = dataSnapshot.child("optionalName").getValue(String.class);
                                                cardPostProfile = dataSnapshot.child("profileImage").getValue(String.class);
                                            }
                                            mRef_post.child("postNumber").setValue((-1) * (numberOfPosts + 1));
                                            mRef_post.child("uploadedBy").setValue(dataSnapshot.child("optionalName").getValue(String.class));
                                            mRef_post.child("imagePath").setValue(task.getResult().toString());
                                            mRef_post.child("userId").setValue(userId);
                                            mRef_post.child("cardTitle").setValue(postTitle);
//                                            mRef_post.child("upvote").setValue("0");
//                                            mRef_post.child("downvote").setValue("0");
                                            mRef_post.child("vote").setValue("0");
                                            mRef_post.child("postTime").setValue(currentDate);
                                            mRef_post.child("cardPostProfileImage").setValue(cardPostProfile);
                                            mRef_post.child("NSFW").setValue(nsfw_number);
                                            mRef_post.child("spoiler").setValue(spoiler_number);
                                            mRef_post.child("postType").setValue("image");
                                            mRef_post.child("subType").setValue(subType);
                                            mRef_post.child("subName").setValue(subName);
                                            mRef_post.child("subId").setValue(subId);
                                            mRef.child("posts").child("numberOfPosts").setValue(numberOfPosts + 1);

                                            postImagePost.setVisibility(View.VISIBLE);
                                            postProgressBar.setVisibility(View.GONE);

                                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Post_Image_Activity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            postImagePost.setVisibility(View.VISIBLE);
                                            postProgressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Image Not Uploaded", Toast.LENGTH_SHORT);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please select image to upload.", Toast.LENGTH_SHORT);
                    postImagePost.setVisibility(View.VISIBLE);
                    postProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void followedCommunity(final List<String> subNameList, final List<String> subIdList, final List<String> cardPostProfileList) {
        followingCommunityValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        if (!userId.equals(dataSnapshot1.getKey())){
                            subNameList.add(dataSnapshot1.child("name").getValue(String.class));
                            subIdList.add(dataSnapshot1.child("key").getValue(String.class));
                            cardPostProfileList.add(dataSnapshot1.child("profilePicture").getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef_user.child("followingList").orderByChild("type").equalTo("sub").addValueEventListener(followingCommunityValueEventListener);

        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, subNameList);
        searchableSpinner.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)){
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
            else {
                startCrop(imageuri);
            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                filepath = result.getUri();
                try {
                    linear_cam_gallery.setVisibility(View.GONE);
                    gallery_image.setVisibility(View.VISIBLE);
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filepath);
//                gallery_image.setImageBitmap(bitmap);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                    image_byte_data = byteArrayOutputStream.toByteArray();

                    imageUploaded = 1;
                    Picasso.get().load(filepath).into(gallery_image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

//        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
//
//            filepath = data.getData();
//            try {
//                linear_cam_gallery.setVisibility(View.GONE);
//                gallery_image.setVisibility(View.VISIBLE);
//                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filepath);
////                gallery_image.setImageBitmap(bitmap);
//
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
//                image_byte_data = byteArrayOutputStream.toByteArray();
//
//                Picasso.get().load(filepath).into(gallery_image);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    private void startCrop(Uri imageuri){
        CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {

            mRef.child("posts").child("numberOfPosts").removeEventListener(numberOfPostValueEventListener);
            mRef_user.child("followingList").removeEventListener(followingCommunityValueEventListener);

            mRef_user.removeEventListener(cardPostProfileValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
