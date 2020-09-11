package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.storage.UploadTask;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post_Text_Activity extends AppCompatActivity {

    SharedPref sharedPref;
    int numberOfPosts, spoiler_number, nsfw_number;
    TextView spoiler, spoilerFill, nsfw, nsfwFill, post_text_post, post_text_title, post_text_txt;
    String postTitle, postDescription, userId, pushId, currentDate, cardPostProfile,subName, subId, subType;
    ProgressBar postProgressBar;
    SimpleDateFormat simpleDateFormat;
    Date date;
    DatabaseReference mRef, mRef_post, mRef_user;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    SearchableSpinner searchableSpinner;
    List<String> subNameList, subIdList, cardPostProfileList;
    ArrayAdapter<String> adapter;

    ValueEventListener numberOfPostValueEventListener, cardPostProfileValueEventListener, followingCommunityValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_post__text_);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        date = new Date();
        currentDate = simpleDateFormat.format(date);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        subId = userId;
        subType = "user";

        mRef_post = firebaseDatabase.getReference("creddit").child("posts");
        mRef_user = mRef.child("users").child(userId);

        post_text_post = findViewById(R.id.post_text_post);
        post_text_title = findViewById(R.id.post_text_title);
        post_text_txt = findViewById(R.id.post_text_txt);
        postProgressBar = findViewById(R.id.postProgressBar);
        searchableSpinner = findViewById(R.id.subSearchText);

        nsfw = findViewById(R.id.nsfw);
        nsfwFill = findViewById(R.id.nsfw_fill);
        spoiler = findViewById(R.id.spoiler);
        spoilerFill = findViewById(R.id.spoiler_fill);

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

        nsfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nsfw.setVisibility(View.GONE);
                nsfwFill.setVisibility(View.VISIBLE);
                nsfw_number = 1;
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

        post_text_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_text_post.setVisibility(View.GONE);
                postProgressBar.setVisibility(View.VISIBLE);

                postTitle = post_text_title.getText().toString();
                postDescription = post_text_txt.getText().toString();
                if (postDescription.equals("")){
                    postDescription = "null";
                }

                if (postTitle.equals("")) {
                    post_text_post.setVisibility(View.VISIBLE);
                    postProgressBar.setVisibility(View.GONE);
                    post_text_title.setError("Please add a title to post");
                } else {
                    numberOfPostValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            if (dataSnapshot2.exists()) {
                                numberOfPosts = ((Long) dataSnapshot2.getValue()).intValue();

                                cardPostProfileValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                        if (dataSnapshot1.exists()) {
//                                            cardPostProfile = dataSnapshot1.child("profileImage").getValue().toString();

                                            pushId = mRef.push().getKey();
                                            mRef_post = mRef.child("posts").child("imagePosts").child(pushId);

//                                            mRef_user.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (subType.equals("user")){
                                                        subName = dataSnapshot1.child("optionalName").getValue(String.class);
                                                        cardPostProfile = dataSnapshot1.child("profileImage").getValue(String.class);
                                                    }
                                                    mRef_post.child("postNumber").setValue((-1) * (numberOfPosts + 1));
                                                    mRef_post.child("uploadedBy").setValue(dataSnapshot1.child("optionalName").getValue());
                                                    mRef_post.child("imagePath").setValue(postDescription);
                                                    mRef_post.child("userId").setValue(userId);
                                                    mRef_post.child("cardTitle").setValue(postTitle);
//                                            mRef_post.child("upvote").setValue("0");
//                                            mRef_post.child("downvote").setValue("0");
                                                    mRef_post.child("vote").setValue("0");
                                                    mRef_post.child("postTime").setValue(currentDate);
                                                    mRef_post.child("cardPostProfileImage").setValue(cardPostProfile);
                                                    mRef_post.child("NSFW").setValue(nsfw_number);
                                                    mRef_post.child("spoiler").setValue(spoiler_number);
                                                    mRef_post.child("postType").setValue("text");
                                                    mRef_post.child("subType").setValue(subType);
                                                    mRef_post.child("subName").setValue(subName);
                                                    mRef_post.child("subId").setValue(subId);
                                                    mRef.child("posts").child("numberOfPosts").setValue(numberOfPosts + 1);

                                                    post_text_post.setVisibility(View.VISIBLE);
                                                    postProgressBar.setVisibility(View.GONE);

                                                    Toast.makeText(getApplicationContext(), "Post Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Post_Text_Activity.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                    post_text_post.setVisibility(View.VISIBLE);
//                                                    postProgressBar.setVisibility(View.GONE);
//                                                    Toast.makeText(getApplicationContext(), "Post Not Uploaded", Toast.LENGTH_SHORT);
//                                                }
//                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };

                                mRef_user.addListenerForSingleValueEvent(cardPostProfileValueEventListener);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    mRef.child("posts").child("numberOfPosts").addListenerForSingleValueEvent(numberOfPostValueEventListener);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            mRef_user.child("followingList").removeEventListener(followingCommunityValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
