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

import java.util.ArrayList;
import java.util.List;

public class PopularFragment extends Fragment {

    RecyclerView recycler_popular_posts;
    List<CardModal> popular_posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        recycler_popular_posts = view.findViewById(R.id.recycler_popular_posts);

        popular_posts = new ArrayList<>();

        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
        popular_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));

        CardAdapter cardAdapter = new CardAdapter(getContext(), popular_posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_popular_posts.setLayoutManager(linearLayoutManager);
        recycler_popular_posts.setAdapter(cardAdapter);

        return view;
    }

}
