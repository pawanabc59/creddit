package com.example.creddit.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.creddit.Adapter.LoggedInUsersAdapter;
import com.example.creddit.LoginActivity;
import com.example.creddit.Model.LoggedInUsersModel;
import com.example.creddit.R;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.LoggedInUser;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetUsersDialogFragment extends BottomSheetDialogFragment {

    RecyclerView showLoggedInUsersRecyclerView;
    LinearLayout addAccountLinearLayout;
    List<LoggedInUsersModel> loggedInUsersModels;
    LoggedInUsersAdapter loggedInUsersAdapter;
    MyRoomDatabase myRoomDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_users_dialog, container, false);

        loggedInUsersModels = new ArrayList<>();

        loggedInUsersAdapter = new LoggedInUsersAdapter(getContext(), loggedInUsersModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        showLoggedInUsersRecyclerView = view.findViewById(R.id.showLoggedInUsersRecyclerView);
        addAccountLinearLayout = view.findViewById(R.id.addAccountLinearLayout);

        myRoomDatabase = DatabaseClient.databaseClient(getContext());
        List<LoggedInUser> loggedInUserData = myRoomDatabase.loggedInUserDAO().getLoggedInUser();

        if (loggedInUserData.size() == 0){
            Toast.makeText(getContext(), "No user data to show", Toast.LENGTH_SHORT).show();
        }
        else{
            loggedInUsersModels.clear();
            for (int i = 0; i<loggedInUserData.size(); i++){
                loggedInUsersModels.add(new LoggedInUsersModel(loggedInUserData.get(i).getUserName(), loggedInUserData.get(i).getUserPhoto(),
                        loggedInUserData.get(i).getUserIds(), loggedInUserData.get(i).getUserPassword(), loggedInUserData.get(i).getUserEmail()));

                loggedInUsersAdapter.notifyDataSetChanged();
            }
        }

        addAccountLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        showLoggedInUsersRecyclerView.setLayoutManager(linearLayoutManager);
        showLoggedInUsersRecyclerView.setAdapter(loggedInUsersAdapter);

        return view;
    }
}