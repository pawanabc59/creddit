package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

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

        showNSFW = findViewById(R.id.showNSFW);
        blurNSFW = findViewById(R.id.blurNSFW);

        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mRef = firebaseDatabase.getReference("creddit").child("users").child(userId);

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

    }
}