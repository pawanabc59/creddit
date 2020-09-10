package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNewSubredditActivity extends AppCompatActivity {

    SharedPref sharedPref;
    Toolbar createSubToolbar;
    EditText subName, subDescription;
    RadioGroup types, content;
    RadioButton publicSub, privateSub, restrictedSub, any, textOnly, linksOnly;
    Button btnCreate;
    ProgressBar createSubProgressBar;
    String strtype="public", strcontent="any", userId, pushId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_create_new_subreddit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user!=null){
            userId = user.getUid();
        }

        createSubToolbar = findViewById(R.id.createSubToolbar);
        subName = findViewById(R.id.subName);
        subDescription = findViewById(R.id.subDescription);
        types = findViewById(R.id.types);
        content = findViewById(R.id.content);
        publicSub = findViewById(R.id.publicSub);
        privateSub = findViewById(R.id.privateSub);
        restrictedSub = findViewById(R.id.restrictedSub);
        any = findViewById(R.id.any);
        textOnly = findViewById(R.id.textOnly);
        linksOnly = findViewById(R.id.linksOnly);
        btnCreate = findViewById(R.id.btnCreate);
        createSubProgressBar = findViewById(R.id.createSubProgressBar);

        setSupportActionBar(createSubToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createSubToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        types.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        pushId = mRef.push().getKey();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subRedditName = subName.getText().toString().trim();
                String subRedditDescription = subDescription.getText().toString().trim();
                btnCreate.setVisibility(View.GONE);
                createSubProgressBar.setVisibility(View.VISIBLE);

                if (subRedditName.equals("")){
                    subName.setError("Enter sub name");
                }
                else if (subRedditDescription.equals("")){
                    subDescription.setError("Enter sub description");
                }
                else {
                    mRef.child("subreddits").child(pushId).child("key").setValue(pushId);
                    mRef.child("subreddits").child(pushId).child("subName").setValue(subRedditName);
                    mRef.child("subreddits").child(pushId).child("description").setValue(subRedditDescription);
                    mRef.child("subreddits").child(pushId).child("type").setValue(strtype);
                    mRef.child("subreddits").child(pushId).child("content").setValue(strcontent);
                    mRef.child("subreddits").child(pushId).child("moderators").child(userId).child("key").setValue(userId);
                    mRef.child("users").child(userId).child("createdSubreddits").child(pushId).child("key").setValue(pushId);
                    mRef.child("subreddits").child(pushId).child("subPicture").setValue("null");
                    mRef.child("subreddits").child(pushId).child("subPictureBanner").setValue("null");

                    mRef.child("search").child(pushId).child("name").setValue(subRedditName);
                    mRef.child("search").child(pushId).child("profilePicture").setValue("null");
                    mRef.child("search").child(pushId).child("type").setValue("sub");

                    createSubProgressBar.setVisibility(View.GONE);
                    btnCreate.setVisibility(View.VISIBLE);
                    subName.setText("");
                    subDescription.setText("");
                    publicSub.setChecked(true);
                    any.setChecked(true);
                    Toast.makeText(getApplicationContext(),"Subreddit created!!",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}