package com.example.creddit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.creddit.Fragments.BottomSheetDialogFragment;
import com.example.creddit.Fragments.ChatFragment;
import com.example.creddit.Fragments.DashboardFragment;
import com.example.creddit.Fragments.MailFragment;
import com.example.creddit.Fragments.RedditHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    SharedPref sharedPref;
    private SwitchCompat switchCompat;
    TextView setting_tab, nav_username, nav_age;
    ImageView night_mode, nav_profile_image;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef,mRef2;

    String userId;
    String currentDate;
    SimpleDateFormat sdf;
    LinearLayout setting_layout;

    Toolbar toolbar;

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

        setContentView(R.layout.activity_main);

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("users");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setting_tab = findViewById(R.id.setting_tab);
        night_mode = findViewById(R.id.night_mode);
        setting_layout = findViewById(R.id.setting);

//        setting_tab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), " setting is clicked ", Toast.LENGTH_SHORT).show();
//            }
//        });
        setting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        night_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                switchCompat = findViewById(R.id.drawer_night_switch);
//                switchCompat.setEnabled(false);
                if (sharedPref.loadNightModeState() == true) {
                    sharedPref.setNightModeState(false);
                    Picasso.get().load(R.drawable.ic_night).into(night_mode);
//                    switchCompat.setChecked(false);
                    recreate();
                } else {
                    sharedPref.setNightModeState(true);
//                    switchCompat.setChecked(true);
//                    night_mode.setImageIcon(R.drawable.ic_day);
                    Picasso.get().load(R.drawable.ic_day).into(night_mode);
                    recreate();
                }
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (user == null) {
            navigationView.getMenu().clear();
            View nav_header_login = LayoutInflater.from(this).inflate(R.layout.nav_header_login, null);
            navigationView.addHeaderView(nav_header_login);
            navigationView.inflateMenu(R.menu.drawer_menu_login);

        } else {
            navigationView.getMenu().clear();
            View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
            navigationView.addHeaderView(nav_header);
            nav_profile_image = nav_header.findViewById(R.id.nav_profile_image);
            nav_username = nav_header.findViewById(R.id.nav_username);
            nav_age = nav_header.findViewById(R.id.nav_age);

            userId = user.getUid();
            mRef2 = mRef.child(userId);

            navigationValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                    String signInDate = dataSnapshot.child("createdAt").getValue().toString();

//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

//                    LocalDate todayDate = LocalDate.parse(currentDate, sdf);
                    try {
                        Date todayDate = sdf.parse(currentDate);
                        Date signedInDate = sdf.parse(signInDate);

//                        Calendar endCalendar = new GregorianCalendar();
//                        endCalendar.setTime(todayDate);
//
//                        Calendar startCalendar = new GregorianCalendar();
//                        startCalendar.setTime(signedInDate);
//
//                        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
//                        int diffMonth = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
//                        int diffDays = endCalendar.get(Calendar.DAY)

//                        long daysBetween = ChronoUnit.DAYS.between(todayDate,signedInDate);
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
                            nav_age.setText(year+"y "+month+"m "+leftDays+"d");
                        }
                        else if ((days/30)>0){
                            int month = days/30;
                            int leftDays = days%30;
                            nav_age.setText(month+"m "+leftDays+"d");
                        }
                        else {
                            nav_age.setText(days+"d");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (profileImage.equals("null")){
                        Picasso.get().load(R.drawable.reddit_logo_hd).into(nav_profile_image);
                    }else {
                        Picasso.get().load(profileImage).error(R.drawable.reddit_logo_hd).into(nav_profile_image);
                    }
                    nav_username.setText(dataSnapshot.child("optionalName").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mRef2.addValueEventListener(navigationValueEventListener);

            navigationView.inflateMenu(R.menu.drawer_menu);
        }

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        toggle.setDrawerIndicatorEnabled(false);
//        toolbar.setNavigationIcon(R.drawable.reddit_logo_hd);

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reddit_logo_hd);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);

        Fragment selectedFragment = new RedditHomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        recreate();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new RedditHomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_dashboard:
                            selectedFragment = new DashboardFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_post:
//                            you have to delete the post fragment and profile fragment
//                            selectedFragment = new PostFragment();
                            if (user == null) {
                                Intent intent = new Intent(getApplicationContext(), FirstPageActivity.class);
                                startActivity(intent);
                            } else {
                                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogFragment();
                                bottomSheetDialogFragment.show(getSupportFragmentManager(), "ExampleBottomSheet");
                            }
                            break;
                        case R.id.nav_chat:
                            if (user == null) {
                                Intent intent = new Intent(getApplicationContext(), FirstPageActivity.class);
                                startActivity(intent);
                            } else {
                                selectedFragment = new ChatFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }
                            break;
                        case R.id.nav_mail:
                            if (user == null) {
                                Intent intent = new Intent(getApplicationContext(), FirstPageActivity.class);
                                startActivity(intent);
                            } else {
                                selectedFragment = new MailFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }
                            break;

                    }

                    return true;
                }
            };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.drawer_login:
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.saved_posts:
                Intent intent3 = new Intent(getApplicationContext(), SavedActivity.class);
                startActivity(intent3);
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

//    public void restartApp(){
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//        finish();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mRef2.removeEventListener(navigationValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
