package com.example.creddit.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creddit.Adapter.CardAdapter;
import com.example.creddit.Model.CardModel;
import com.example.creddit.R;

import java.util.ArrayList;
import java.util.List;

public class PopularFragment extends Fragment {

    RecyclerView recycler_popular_posts;
    List<CardModel> popular_posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        recycler_popular_posts = view.findViewById(R.id.recycler_popular_posts);

        popular_posts = new ArrayList<>();

//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));
//        popular_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Posted by pawan", "Check my awesome zoro wallpaper" ));

        CardAdapter cardAdapter = new CardAdapter(getContext(), popular_posts, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_popular_posts.setLayoutManager(linearLayoutManager);
        recycler_popular_posts.setAdapter(cardAdapter);

        return view;
    }

}
