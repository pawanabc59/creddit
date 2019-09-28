package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    SharedPref sharedPref;
    private SwitchCompat switchCompat;
    TextView setting_tab;
    ImageView night_mode;

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

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setting_tab = findViewById(R.id.setting_tab);
        night_mode = findViewById(R.id.night_mode);

        setting_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), " setting is clicked ", Toast.LENGTH_SHORT).show();
            }
        });

        night_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                switchCompat = findViewById(R.id.drawer_night_switch);
//                switchCompat.setEnabled(false);
                if (sharedPref.loadNightModeState()==true){
                    sharedPref.setNightModeState(false);
                    Picasso.get().load(R.drawable.ic_night).into(night_mode);
//                    switchCompat.setChecked(false);
                    recreate();
                }
                else {
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

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
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

                    switch (menuItem.getItemId()){
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
                                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogFragment();
                                bottomSheetDialogFragment.show(getSupportFragmentManager(),"ExampleBottomSheet");
                            break;
                        case R.id.nav_chat:
                            selectedFragment = new ChatFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_mail:
                            selectedFragment = new MailFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                    }

                    return true;
                }
            };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.history:
                break;
            case R.id.profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
//            case  R.id.night:
////                sharedPref.setNightModeState(true);
//                switchCompat = findViewById(R.id.drawer_night_switch);
//                switchCompat.setEnabled(false);
//                if (sharedPref.loadNightModeState()==true){
//                    sharedPref.setNightModeState(false);
//                    switchCompat.setChecked(false);
//                    recreate();
//                }
//                else {
//                    sharedPref.setNightModeState(true);
//                    switchCompat.setChecked(true);
//                    recreate();
//                }
//
//                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        if (b){
//                            sharedPref.setNightModeState(true);
//                            recreate();
//                        }
//                        else {
//                            sharedPref.setNightModeState(false);
//                            recreate();
//                        }
//                    }
//                });
//                recreate();
//                restartApp();
//                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

//    public void restartApp(){
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
