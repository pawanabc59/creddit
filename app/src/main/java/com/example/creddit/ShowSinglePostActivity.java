package com.example.creddit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowSinglePostActivity extends AppCompatActivity {

    SharedPref sharedPref;
    LinearLayout singlePost_text_post_layout, singlePost_normal_layout, singlePost_card_header;
    ImageView singlePost_post_share, singlePost_post_comment, singlePost_post_after_downvote, singlePost_post_downvote, singlePost_post_after_upvote,
            singlePost_post_upvote, singlePost_card_image, singlePost_card_profile_image, singlePost_joinSubreddit, singlePost_unjoinedSubreddit;
    TextView singlePost_commentCount, singlePost_downvoteCount, singlePost_upvoteCount, singlePost_text_post_description, singlePost_text_post_title,
            singlePost_card_description, singlePost_spoiler_fill_post, singlePost_nsfw_fill_post, singlePost_card_title, singlePost_posted_by, singlePost_postedTime;
    CardView singlePost_card_image_post;

    String postType, fragmentType, anotherUserId, userId, imagePath;

    FirebaseUser user;
    int nsfw, spoiler;

    DatabaseReference mRef, mRefUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_show_single_post);

        singlePost_text_post_layout = findViewById(R.id.singlePost_text_post_layout);
        singlePost_postedTime = findViewById(R.id.singlePost_postedTime);
        singlePost_posted_by = findViewById(R.id.singlePost_posted_by);
        singlePost_card_title = findViewById(R.id.singlePost_card_title);
        singlePost_nsfw_fill_post = findViewById(R.id.singlePost_nsfw_fill_post);
        singlePost_card_image_post = findViewById(R.id.singlePost_card_image_post);
        singlePost_normal_layout = findViewById(R.id.singlePost_normal_layout);
        singlePost_card_header = findViewById(R.id.singlePost_card_header);
        singlePost_post_share = findViewById(R.id.singlePost_post_share);
        singlePost_post_comment = findViewById(R.id.singlePost_post_comment);
        singlePost_post_after_downvote = findViewById(R.id.singlePost_post_after_downvote);
        singlePost_post_downvote = findViewById(R.id.singlePost_post_downvote);
        singlePost_post_after_upvote = findViewById(R.id.singlePost_post_after_upvote);
        singlePost_post_upvote = findViewById(R.id.singlePost_post_upvote);
        singlePost_card_image = findViewById(R.id.singlePost_card_image);
        singlePost_card_profile_image = findViewById(R.id.singlePost_card_profile_image);
        singlePost_joinSubreddit = findViewById(R.id.singlePost_joinSubreddit);
        singlePost_unjoinedSubreddit = findViewById(R.id.singlePost_unjoinedSubreddit);
        singlePost_commentCount = findViewById(R.id.singlePost_commentCount);
        singlePost_downvoteCount = findViewById(R.id.singlePost_downvoteCount);
        singlePost_upvoteCount = findViewById(R.id.singlePost_upvoteCount);
        singlePost_text_post_description = findViewById(R.id.singlePost_text_post_description);
        singlePost_text_post_title = findViewById(R.id.singlePost_text_post_title);
        singlePost_card_description = findViewById(R.id.singlePost_card_description);
        singlePost_spoiler_fill_post = findViewById(R.id.singlePost_spoiler_fill_post);

        Intent intent = getIntent();
        Picasso.get().load(intent.getExtras().getString("cardPostProfileImage")).into(singlePost_card_profile_image);
        Picasso.get().load(intent.getExtras().getString("imagePath")).into(singlePost_card_image);
        singlePost_card_title.setText(intent.getExtras().getString("cardTitle"));
        singlePost_posted_by.setText(intent.getExtras().getString("uploadedBy"));
        singlePost_card_description.setText(intent.getExtras().getString("cardDescription"));
        singlePost_postedTime.setText(intent.getExtras().getString("postedTime"));
        singlePost_text_post_title.setText(intent.getExtras().getString("cardDescription"));
        singlePost_text_post_description.setText(intent.getExtras().getString("imagePath"));
        anotherUserId = intent.getExtras().getString("anotherUserId");
        nsfw = intent.getExtras().getInt("NSFW");
        spoiler = intent.getExtras().getInt("spoiler");
        postType = intent.getExtras().getString("postType");
        fragmentType = intent.getExtras().getString("fragmentType");
        imagePath = intent.getExtras().getString("imagePath");

        mRef = FirebaseDatabase.getInstance().getReference("creddit");

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            mRefUser = mRef.child("users").child(userId);
        }

        if (postType.equals("text")){
            singlePost_normal_layout.setVisibility(View.GONE);
            singlePost_text_post_layout.setVisibility(View.VISIBLE);
        }

        if (nsfw == 1) {
            singlePost_nsfw_fill_post.setVisibility(View.VISIBLE);

            if (postType.equals("image")) {
                singlePost_normal_layout.setVisibility(View.VISIBLE);
            } else if (postType.equals("text")) {
                singlePost_normal_layout.setVisibility(View.GONE);
                singlePost_text_post_layout.setVisibility(View.VISIBLE);
            }
        }
        if (spoiler == 1) {
            singlePost_spoiler_fill_post.setVisibility(View.VISIBLE);
            if (postType.equals("text")) {
                singlePost_normal_layout.setVisibility(View.GONE);
                singlePost_text_post_layout.setVisibility(View.VISIBLE);
            }
        }

        if (fragmentType.equals("popularFragment")) {
            singlePost_joinSubreddit.setVisibility(View.VISIBLE);
        }

        singlePost_joinSubreddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefUser.child("followingList").child(anotherUserId).child("key").setValue(anotherUserId);
                mRefUser.child("followingList").child(anotherUserId).child("favourite").setValue(0);
                singlePost_joinSubreddit.setVisibility(View.GONE);
                singlePost_unjoinedSubreddit.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "You joined this Sub!", Toast.LENGTH_SHORT);
            }
        });

        singlePost_unjoinedSubreddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefUser.child("followingList").child(anotherUserId).child("key").removeValue();
                mRefUser.child("followingList").child(anotherUserId).child("favourite").removeValue();
                singlePost_unjoinedSubreddit.setVisibility(View.GONE);
                singlePost_joinSubreddit.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "You joined this Sub!", Toast.LENGTH_SHORT);
            }
        });

        singlePost_post_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(imagePath).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.setType("images/*");
//                        intent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION );
                        intent1.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        startActivity(Intent.createChooser(intent1, "mWallpaper"));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });

        singlePost_card_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anotherSubredditIntent();
            }
        });

        singlePost_card_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anotherSubredditIntent();
            }
        });

        singlePost_posted_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowPopUpProfileDetailsActivity.class);
                intent.putExtra("anotherUserId", anotherUserId);
                startActivity(intent);
            }
        });

        singlePost_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SingleImageShowActivity.class);
                intent.putExtra("cardImage", imagePath);
                startActivity(intent);

            }
        });

    }

    public void anotherSubredditIntent() {
        Intent intent = new Intent(this, AnotherUserActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri uri = null;
        try {
            File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "creddit_" + System.currentTimeMillis() + ".png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }
}