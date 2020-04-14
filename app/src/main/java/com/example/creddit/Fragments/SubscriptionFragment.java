package com.example.creddit.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creddit.Adapter.FollowingListAdapter;
import com.example.creddit.Model.FollowingListModel;
import com.example.creddit.R;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFragment extends Fragment {

    RecyclerView sub_recyclerview;
    List<FollowingListModel> followingListModals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        sub_recyclerview = view.findViewById(R.id.sub_recyclerview);

        followingListModals = new ArrayList<>();
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Sanji Fan Club"));

        FollowingListAdapter followingListAdapter = new FollowingListAdapter(getContext(), followingListModals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        sub_recyclerview.setLayoutManager(linearLayoutManager);
        sub_recyclerview.setAdapter(followingListAdapter);

        return view;
    }

}
