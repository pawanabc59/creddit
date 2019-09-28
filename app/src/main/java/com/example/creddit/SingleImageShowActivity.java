package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SingleImageShowActivity extends AppCompatActivity {

    SharedPref sharedPref;
    ImageView single_card_profile_image, single_card_image;
    TextView single_card_title, single_card_posted_by;
    ImageView single_post_upvote, single_post_downvote, single_post_comment, single_post_share;

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

        setContentView(R.layout.activity_single_image_show);

        single_card_profile_image = findViewById(R.id.single_card_profile_image);
        single_card_image = findViewById(R.id.single_card_image);
        single_card_title = findViewById(R.id.single_card_title);
        single_card_posted_by = findViewById(R.id.single_posted_by);
        single_post_upvote = findViewById(R.id.single_post_upvote);
        single_post_downvote = findViewById(R.id.single_post_downvote);
        single_post_comment = findViewById(R.id.single_post_comment);
        single_post_share = findViewById(R.id.single_post_share);

        Intent intent = getIntent();
        String card_title = intent.getExtras().getString("card_title");
        String card_description = intent.getExtras().getString("card_description");
        String posted_by = intent.getExtras().getString("posted_by");

        single_card_title.setText(card_title);
        single_card_posted_by.setText(posted_by);

        Picasso.get().load(R.drawable.zoro).into(single_card_profile_image);
        Picasso.get().load(R.drawable.zoro).into(single_card_image);

        single_post_upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "post is upvoted", Toast.LENGTH_SHORT).show();
            }
        });

        single_post_downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "post is downvoted", Toast.LENGTH_SHORT).show();
            }
        });

        single_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "comment is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        single_post_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "share is clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
