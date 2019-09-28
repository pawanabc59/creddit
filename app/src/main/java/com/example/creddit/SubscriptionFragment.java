package com.example.creddit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFragment extends Fragment {

    RecyclerView sub_recyclerview;
    List<FollowingListModal> followingListModals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        sub_recyclerview = view.findViewById(R.id.sub_recyclerview);

        followingListModals = new ArrayList<>();
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Zoro Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Luffy Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Sanji Fan Club"));
        followingListModals.add(new FollowingListModal(R.drawable.zoro, "Sanji Fan Club"));

        FollowingListAdapter followingListAdapter = new FollowingListAdapter(getContext(), followingListModals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        sub_recyclerview.setLayoutManager(linearLayoutManager);
        sub_recyclerview.setAdapter(followingListAdapter);

        return view;
    }

}
