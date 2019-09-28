package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Post_Text_Activity extends AppCompatActivity {

    SharedPref sharedPref;

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
    }
}
