package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowPopUpProfileDetailsActivity extends AppCompatActivity {

    SharedPref sharedPref;
    LinearLayout popupViewProfile, popupBlockPerson;
    TextView popupAge, popupUsername;
    ImageView popupProfileImage;

    String anotherUserId, currentDate;
    SimpleDateFormat sdf;
    DatabaseReference mRef;

    ValueEventListener navigationValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_show_pop_up_profile_details);

//        to show the popup according to the device size below 5 lines is used
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.4));

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());

        popupProfileImage = findViewById(R.id.popup_profile_image);
        popupAge = findViewById(R.id.popup_age);
        popupUsername = findViewById(R.id.popup_username);
        popupViewProfile = findViewById(R.id.popup_viewProfile);
        popupBlockPerson = findViewById(R.id.popup_blockPerson);

        final Intent intent = getIntent();
        anotherUserId = intent.getExtras().getString("anotherUserId");

        mRef = FirebaseDatabase.getInstance().getReference("creddit").child("users").child(anotherUserId);

        navigationValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                String signInDate = dataSnapshot.child("createdAt").getValue().toString();

                try {
                    Date todayDate = sdf.parse(currentDate);
                    Date signedInDate = sdf.parse(signInDate);

                    long diff = todayDate.getTime() - signedInDate.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    int days = (int) (hours / 24);

                    if ((days/365)>0){
                        int year = days/365;
                        int leftMonths = days%365;
                        int month = leftMonths/30;
                        int leftDays = month%30;
                        popupAge.setText(year+"y "+month+"m "+leftDays+"d");
                    }
                    else if ((days/30)>0){
                        int month = days/30;
                        int leftDays = days%30;
                        popupAge.setText(month+"m "+leftDays+"d");
                    }
                    else {
                        popupAge.setText(days+"d");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (profileImage.equals("null")){
                    Picasso.get().load(R.drawable.zoro).into(popupProfileImage);
                }else {
                    Picasso.get().load(profileImage).error(R.drawable.zoro).into(popupProfileImage);
                }
                popupUsername.setText(dataSnapshot.child("optionalName").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.addListenerForSingleValueEvent(navigationValueEventListener);

        popupViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), AnotherUserActivity.class);
                intent1.putExtra("anotherUserId", anotherUserId);
                startActivity(intent1);
            }
        });

    }
}