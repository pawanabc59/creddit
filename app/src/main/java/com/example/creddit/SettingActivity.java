package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {

    SharedPref sharedPref;
    Switch showNSFW, blurNSFW;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userId;
    ValueEventListener settingValueEventListener;
    int NSFWvalue = 0, blurNSFWValue = 0;
    TextView manageBlockedUsers, resetPassword, createSubreddit, manageSubReddit, hiddenPosts;
//    TextView verifyMail;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.settingToolbar);
        showNSFW = findViewById(R.id.showNSFW);
        blurNSFW = findViewById(R.id.blurNSFW);
        manageBlockedUsers = findViewById(R.id.manageBlockedUsers);
        resetPassword = findViewById(R.id.resetPassword);
        createSubreddit = findViewById(R.id.createSubreddit);
        manageSubReddit = findViewById(R.id.manageSubReddit);
        hiddenPosts = findViewById(R.id.hiddenPosts);
//        verifyMail = findViewById(R.id.verify_mail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mRef = firebaseDatabase.getReference("creddit").child("users").child(userId);

//        verifyMail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "Mail sent", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });

        settingValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    NSFWvalue = dataSnapshot.child("showNSFW").getValue(Integer.class);
                    blurNSFWValue = dataSnapshot.child("blurNSFW").getValue(Integer.class);

                    if (NSFWvalue==1){
                        showNSFW.setChecked(true);
                        mRef.child("showNSFW").setValue(1);
                    }
                    else {
                        showNSFW.setChecked(false);
                        mRef.child("showNSFW").setValue(0);
                    }

                    if (blurNSFWValue == 1){
                        blurNSFW.setChecked(true);
                        mRef.child("blurNSFW").setValue(1);
                    }
                    else {
                        blurNSFW.setChecked(false);
                        mRef.child("blurNSFW").setValue(0);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addValueEventListener(settingValueEventListener);

        showNSFW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    NSFWvalue = 1;
                    mRef.child("showNSFW").setValue(1);
                    sharedPref.put_showNSFW(1);
                }
                else{
                    NSFWvalue = 0;
                    mRef.child("showNSFW").setValue(0);
                    sharedPref.put_showNSFW(0);
                }
            }
        });

        blurNSFW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    blurNSFWValue = 1;
                    mRef.child("blurNSFW").setValue(1);
                    sharedPref.put_blurNSFW(1);
                }
                else {
                    blurNSFWValue = 0;
                    mRef.child("blurNSFW").setValue(0);
                    sharedPref.put_blurNSFW(0);
                }
            }
        });

        manageBlockedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BlockedUsersListActivity.class);
                startActivity(intent);
            }
        });

        createSubreddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, CreateNewSubredditActivity.class);
                startActivity(intent);
            }
        });

        manageSubReddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MySubRedditListActivity.class);
                startActivity(intent);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetPassword1 = new EditText(view.getContext());
                resetPassword1.setTextColor(R.attr.textcolor);
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter email to receive reset link");
                passwordResetDialog.setView(resetPassword1);

                passwordResetDialog.setPositiveButton("Email me", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetPassword1.getText().toString().trim();
                        if (mail.equals("")) {
                            resetPassword1.setError("Please provide email first");
                            Toast.makeText(getApplicationContext(),"Please provide email first", Toast.LENGTH_SHORT).show();
                        } else {

                            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error in sending email", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        dialogInterface.dismiss();
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                passwordResetDialog.create().show();
            }
        });

        hiddenPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, HiddenPostsActivity.class);
                startActivity(intent);
            }
        });

    }
}