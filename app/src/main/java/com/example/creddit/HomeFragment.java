package com.example.creddit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recycler_home_posts;
    List<CardModal> home_posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycler_home_posts = view.findViewById(R.id.recycler_home_posts);

        home_posts = new ArrayList<>();
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));
        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));

        CardAdapter cardAdapter = new CardAdapter(getContext(), home_posts);
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext());
        recycler_home_posts.setLayoutManager(cardManager);
        recycler_home_posts.setAdapter(cardAdapter);

        return view;
    }

}
