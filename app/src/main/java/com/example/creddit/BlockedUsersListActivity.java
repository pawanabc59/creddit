package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.creddit.Model.FollowingListModel;

import java.util.List;

public class BlockedUsersListActivity extends AppCompatActivity {

    SharedPref sharedPref;
    RecyclerView blockedUserRecyclerView;
    List<FollowingListModel> blockUserListModel;

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

        setContentView(R.layout.activity_blocked_users_list);

        blockedUserRecyclerView = findViewById(R.id.blockedUserRecyclerView);
    }
}