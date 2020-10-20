package com.example.creddit.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.AnotherUserActivity;
import com.example.creddit.BuildConfig;
import com.example.creddit.Model.CardModel;
import com.example.creddit.ProfileActivity;
import com.example.creddit.R;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.HistoryTab;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.example.creddit.SharedPref;
import com.example.creddit.ShowPopUpProfileDetailsActivity;
import com.example.creddit.ShowSinglePostActivity;
import com.example.creddit.SingleImageShowActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.widget.PopupMenu.OnMenuItemClickListener;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Context mContext;
    private List<CardModel> mData;
    String cardImagePath;
    int vote;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference mRef, mRefUser, mRef2, mRef3;
    Activity parentActivity;
    ValueEventListener deletePostValueEventListener, getPostCountValueEventListener,
            savedImageCountValueEventListener, savePostValueEventListener, showSavedImageValueEventListener,
            unsavePostValueEventListener, nsfwValueEventListener, showHidePostValueEventListener, hidePostValueEventListener, unHidePostValueEventListener;
    int i, numberOfPost, flag = 0;
    String TAG = "my", fragmentType;
    SharedPref sharedPref;
    int theme, savedImageCount, showNSFWValue, blurNSFWValue;
    String userId;

    public CardAdapter(Context mContext, List<CardModel> mData, Activity parentActivity, String fragmentType) {
        this.mContext = mContext;
        this.mData = mData;
        this.parentActivity = parentActivity;
        this.fragmentType = fragmentType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        sharedPref = new SharedPref(mContext);
//        if (sharedPref.loadNightModeState() == true) {
//            theme = R.style.darktheme;
////            setTheme(R.style.darktheme);
//        } else {
//            theme = R.style.AppTheme;
////            setTheme(R.style.AppTheme);
//        }

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//        if (parentActivity instanceof ProfileActivity){
//            view = inflater.inflate(R.layout.card_image_layout_delete, null);
//        }else {
//        view = layoutInflater.inflate(R.layout.card_image_layout, parent, false);

        view = layoutInflater.inflate(R.layout.card_image_layout, null);
//        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("posts").child("imagePosts");
        mRef2 = firebaseDatabase.getReference("creddit").child("posts");
        mRef3 = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        sharedPref = new SharedPref(mContext);

        final ImageView postUpvote = holder.post_upvote;
        ImageView postDownvote = holder.post_downvote;
        ImageView postComment = holder.post_comment;
        final ImageView postAfterUpvote = holder.post_after_upvote;
        ImageView postAfterDownvote = holder.post_after_downvote;
        final TextView upvoteCount = holder.upvoteCount;
        TextView downvoteCount = holder.downvoteCount;
        TextView commentCount = holder.commentCount;
        ImageView deletePost = holder.deletePost;
        ImageView cardMenu = holder.card_menu;
        final ImageView joinSubreddit = holder.joinSubreddit;
        final ImageView unjoindedSubreddit = holder.unjoinedSubreddit;
        final ImageView card_image_nsfw_spoiler = holder.card_image_nsfw_spoiler;
        TextView card_description_nsfw_spoiler = holder.card_description_nsfw_spoiler;
        final LinearLayout nsfw_spoiler_layout = holder.nsfw_spoiler_layout;
        final LinearLayout normal_layout = holder.normal_layout;
        LinearLayout text_post_layout = holder.text_post_layout;
        TextView text_post_title = holder.text_post_title;
        TextView text_post_description = holder.text_post_description;

        if (parentActivity instanceof ProfileActivity) {
            cardMenu.setVisibility(View.GONE);
            deletePost.setVisibility(View.VISIBLE);
        } else {
            cardMenu.setVisibility(View.VISIBLE);
            deletePost.setVisibility(View.GONE);
        }

        if(user == null){
            holder.card_menu.setVisibility(View.GONE);
        }
        else {
            userId = user.getUid();
            mRefUser = firebaseDatabase.getReference("creddit").child("users").child(userId);

//            nsfwValueEventListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()){
//                        showNSFWValue = dataSnapshot.child("showNSFW").getValue(Integer.class);
//                        blurNSFWValue = dataSnapshot.child("blurNSFW").getValue(Integer.class);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            };
//            mRefUser.addValueEventListener(nsfwValueEventListener);

        }
//        else {

//        try {
        showNSFWValue = sharedPref.get_showNSFW();
        blurNSFWValue = sharedPref.get_blurNSFW();

//            System.out.println("showNSFWvalue : "+showNSFWValue+" and blurNSFWvalue : "+blurNSFWValue);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (mData.get(position).getNsfw() == 1) {
            holder.nsfw.setVisibility(View.VISIBLE);

            if (mData.get(position).getPost_type().equals("image")) {
                if (blurNSFWValue == 1) {
                    normal_layout.setVisibility(View.GONE);
                    text_post_layout.setVisibility(View.GONE);
                    nsfw_spoiler_layout.setVisibility(View.VISIBLE);
                    Picasso.get().load(mData.get(position).getCard_image()).resize(7, 7).into(card_image_nsfw_spoiler);
                }
            } else if (mData.get(position).getPost_type().equals("text")) {
                normal_layout.setVisibility(View.GONE);
                nsfw_spoiler_layout.setVisibility(View.GONE);
                text_post_layout.setVisibility(View.VISIBLE);
                text_post_description.setVisibility(View.GONE);
            }
        }
        if (mData.get(position).getSpoiler() == 1) {
            holder.spoiler.setVisibility(View.VISIBLE);
            if (mData.get(position).getPost_type().equals("image")) {
                normal_layout.setVisibility(View.GONE);
                text_post_layout.setVisibility(View.GONE);
                nsfw_spoiler_layout.setVisibility(View.VISIBLE);
                Picasso.get().load(mData.get(position).getCard_image()).resize(7, 7).into(card_image_nsfw_spoiler);
            } else if (mData.get(position).getPost_type().equals("text")) {
                normal_layout.setVisibility(View.GONE);
                nsfw_spoiler_layout.setVisibility(View.GONE);
                text_post_layout.setVisibility(View.VISIBLE);
                text_post_description.setVisibility(View.GONE);
            }
        }

//        vote = Integer.parseInt(mData.get(position).getVote());
        holder.card_title.setText(mData.get(position).card_title);
        holder.posted_by.setText(mData.get(position).posted_by);
        holder.card_description.setText(mData.get(position).card_description);
        card_description_nsfw_spoiler.setText(mData.get(position).card_description);
        Picasso.get().load(mData.get(position).getCard_profile_image()).into(holder.profile_photo);
        Picasso.get().load(mData.get(position).getCard_image()).into(holder.card_image);

        text_post_title.setText(mData.get(position).card_description);
        text_post_description.setText(mData.get(position).card_image);

        holder.postedTime.setText(mData.get(position).postedTime);

        cardImagePath = mData.get(position).getCard_image();
//        }

        if (fragmentType.equals("popularFragment")) {
            joinSubreddit.setVisibility(View.VISIBLE);
        }

        joinSubreddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user==null){
                    showToast("You need to login to join subReddit");
                }else {
                    mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("key").setValue(mData.get(position).getSubId());
                    mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("favourite").setValue(0);
                    mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("type").setValue(mData.get(position).getSubType());
                    mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("profilePicture").setValue(mData.get(position).getCard_profile_image());
                    mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("name").setValue(mData.get(position).getCard_title());
                    if (mData.get(position).getSubType().equals("sub")) {
                        mRef3.child("subreddits").child(mData.get(position).getSubId()).child("members").child(userId).setValue(1);
                    }
//                mRefUser.child("followingList").child(mData.get(position).getUserId()).child("key").setValue(mData.get(position).getUserId());
//                mRefUser.child("followingList").child(mData.get(position).getUserId()).child("favourite").setValue(0);
                    joinSubreddit.setVisibility(View.GONE);
                    unjoindedSubreddit.setVisibility(View.VISIBLE);
                    showToast("You joined this Sub!");
                }
            }
        });

        unjoindedSubreddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("key").removeValue();
                mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("favourite").removeValue();
                mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("type").removeValue();
                mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("profilePicture").removeValue();
                mRef3.child("users").child(userId).child("followingList").child(mData.get(position).getSubId()).child("name").removeValue();
                if (mData.get(position).getSubType().equals("sub")){
                    mRef3.child("subreddits").child(mData.get(position).getSubId()).child("members").child(userId).removeValue();
                }
//                mRefUser.child("followingList").child(mData.get(position).getUserId()).child("key").removeValue();
//                mRefUser.child("followingList").child(mData.get(position).getUserId()).child("favourite").removeValue();
                unjoindedSubreddit.setVisibility(View.GONE);
                joinSubreddit.setVisibility(View.VISIBLE);
                showToast("You left this Sub!");
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(mContext, "delete is clicked", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onClick: delete is clicked");

                getPostCountValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        numberOfPost = dataSnapshot.child("numberOfPosts").getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                mRef2.addValueEventListener(getPostCountValueEventListener);

                new AlertDialog.Builder(parentActivity)
                        .setTitle("Delete Post?")
                        .setMessage("Do you really want to delete the post")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePostValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                if (dataSnapshot1.child("imagePath").getValue().toString().equals(mData.get(position).getCard_image())) {
                                                    mRef.child(dataSnapshot1.getKey()).child("cardPostProfileImage").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("cardTitle").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("imagePath").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("postNumber").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("postTime").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("uploadedBy").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("userId").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("vote").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("postType").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("NSFW").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("spoiler").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("subId").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("subName").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("subType").removeValue();
//                                                    mData.remove(position);
//                                                    mRef2.child("numberOfPosts").setValue(numberOfPost-1);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };

                                mRef.addListenerForSingleValueEvent(deletePostValueEventListener);
                                flag = 1;
                            }
                        }).show();
            }
        });

        postUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "post is upvoted", Toast.LENGTH_SHORT).show();

            }
        });

        postAfterUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "post is downvoted", Toast.LENGTH_SHORT).show();

            }
        });

        postDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "post is downvoted", Toast.LENGTH_SHORT).show();
            }
        });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSinglePostActivity(position, fragmentType);
            }
        });

        holder.card_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSinglePostActivity(position, fragmentType);
            }
        });

        holder.text_post_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSinglePostActivity(position, fragmentType);
            }
        });

        holder.post_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "share is clicked", Toast.LENGTH_SHORT).show();
                Picasso.get().load(mData.get(position).getCard_image()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.setType("images/*");
//                        intent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION );
                        intent1.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        mContext.startActivity(Intent.createChooser(intent1, "mWallpaper"));
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

//        holder.cardViewImagePost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "This is card header", Toast.LENGTH_SHORT);
//            }
//        });

        holder.card_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anotherSubredditIntent(position);
            }
        });

        holder.profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anotherSubredditIntent(position);
            }
        });

        card_description_nsfw_spoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSinglePostActivity(position, fragmentType);
            }
        });

        holder.posted_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShowPopUpProfileDetailsActivity.class);
                intent.putExtra("anotherUserId", mData.get(position).getUserId());
                intent.putExtra("subType", mData.get(position).getSubType());
                mContext.startActivity(intent);
            }
        });

        holder.card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleImageShowActivity.class);
//                intent.putExtra("card_title", holder.card_title.getText().toString());
//                intent.putExtra("card_description", mData.get(position).card_description);
//                intent.putExtra("posted_by", holder.posted_by.getText().toString());
                intent.putExtra("cardImage", mData.get(position).getCard_image());
//                intent.putExtra("cardProfileImage", mData.get(position).getCard_profile_image());
                mContext.startActivity(intent);

            }
        });

        card_image_nsfw_spoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleImageShowActivity.class);
                intent.putExtra("cardImage", mData.get(position).getCard_image());
                mContext.startActivity(intent);

            }
        });

        holder.card_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.card_menu, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new MyViewHolder(view));
                Menu menu = popupMenu.getMenu();
                final MenuItem saveItem = menu.findItem(R.id.card_save);
                final MenuItem unsaveItem = menu.findItem(R.id.card_unsave);
                final MenuItem hideItem = menu.findItem(R.id.card_hide_post);
                final MenuItem unHideItem = menu.findItem(R.id.card_unhide_post);
                unHideItem.setVisible(false);
//                unsaveItem.setVisible(false);

                showSavedImageValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                                if (dataSnapshot1.child("imagePath").getValue(String.class).equals(mData.get(position).getCard_image())){
                            saveItem.setVisible(false);
                            unsaveItem.setVisible(true);
//                                }
//                            }
                        } else {
                            saveItem.setVisible(true);
                            unsaveItem.setVisible(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").orderByChild("imagePath")
                        .equalTo(mData.get(position).getCard_image()).addValueEventListener(showSavedImageValueEventListener);

                showHidePostValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                                if (dataSnapshot1.child("imagePath").getValue(String.class).equals(mData.get(position).getCard_image())){
                            hideItem.setVisible(false);
                            unHideItem.setVisible(true);
//                                }
//                            }
                        } else {
                            hideItem.setVisible(true);
                            unHideItem.setVisible(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                firebaseDatabase.getReference("creddit").child("users").child(userId).child("hiddenPosts").orderByChild("imagePath")
                        .equalTo(mData.get(position).getCard_image()).addValueEventListener(showHidePostValueEventListener);

                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.card_save:
                                savedImageCountValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            savedImageCount = dataSnapshot.child("numberOfSavedImages").getValue(Integer.class);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").addListenerForSingleValueEvent(savedImageCountValueEventListener);

                                savePostValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                if (dataSnapshot1.child("imagePath").getValue(String.class).equals(mData.get(position).getCard_image())) {
                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("key").setValue(dataSnapshot1.getKey());
                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("imagePath").setValue(mData.get(position).getCard_image());
                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("postNumber").setValue(-(savedImageCount + 1));
                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child("numberOfSavedImages").setValue(savedImageCount + 1);
                                                    saveItem.setVisible(false);
                                                    unsaveItem.setVisible(true);
                                                    showToast("Post Saved! ");
//                                                    Toast.makeText(mContext, "Post Saved!", Toast.LENGTH_SHORT).show();
                                                    break;

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                firebaseDatabase.getReference("creddit").child("posts").child("imagePosts").addListenerForSingleValueEvent(savePostValueEventListener);

                                break;

                            case R.id.card_unsave:

                                unsavePostValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            try {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    if (dataSnapshot1.child("imagePath").getValue(String.class).equals(mData.get(position).getCard_image())) {
                                                        firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("key").removeValue();
                                                        firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("imagePath").removeValue();
                                                        firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("postNumber").removeValue();
                                                        unsaveItem.setVisible(false);
                                                        saveItem.setVisible(true);
                                                        showToast("Post Unsaved! ");
                                                        if (fragmentType.equals("savedPostsFragment")){

                                                            /*This line will remove the data from the recyclerView*/
                                                            mData.remove(position);
                                                            notifyItemRemoved(position);
                                                            notifyItemRangeChanged(position, mData.size());
                                                        }
//                                                        Toast.makeText(mContext, "Post Unsaved!", Toast.LENGTH_SHORT).show();
                                                        break;
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").addListenerForSingleValueEvent(unsavePostValueEventListener);
                                break;
                            case R.id.card_hide_post:
                                hidePostValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                if (dataSnapshot1.child("imagePath").getValue(String.class).equals(mData.get(position).getCard_image())) {
                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("hiddenPosts").child(dataSnapshot1.getKey()).child("key").setValue(dataSnapshot1.getKey());
                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("hiddenPosts").child(dataSnapshot1.getKey()).child("imagePath").setValue(mData.get(position).getCard_image());
//                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("postNumber").setValue(-(savedImageCount + 1));
//                                                    firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child("numberOfSavedImages").setValue(savedImageCount + 1);
                                                    hideItem.setVisible(false);
                                                    unHideItem.setVisible(true);
                                                    showToast("Post hidden ");
                                                    /*This will remove the data from the list*/
                                                    mData.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, mData.size());
                                                    break;

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                firebaseDatabase.getReference("creddit").child("posts").child("imagePosts").addListenerForSingleValueEvent(hidePostValueEventListener);

//                                showToast("hide post is clicked!");
//                                Toast.makeText(mContext, "hide post is clicked", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.card_unhide_post:
                                unHidePostValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            try {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    if (dataSnapshot1.child("imagePath").getValue(String.class).equals(mData.get(position).getCard_image())) {
                                                        firebaseDatabase.getReference("creddit").child("users").child(userId).child("hiddenPosts").child(dataSnapshot1.getKey()).child("key").removeValue();
                                                        firebaseDatabase.getReference("creddit").child("users").child(userId).child("hiddenPosts").child(dataSnapshot1.getKey()).child("imagePath").removeValue();
//                                                        firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").child(dataSnapshot1.getKey()).child("postNumber").removeValue();
                                                        unHideItem.setVisible(false);
                                                        hideItem.setVisible(true);
                                                        mData.remove(position);
                                                        notifyItemRemoved(position);
                                                        notifyItemRangeChanged(position, mData.size());
                                                        showToast("Post unhidden! ");
                                                        break;
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                firebaseDatabase.getReference("creddit").child("users").child(userId).child("hiddenPosts").addListenerForSingleValueEvent(unHidePostValueEventListener);

//                                showToast("hide post is clicked!");
//                                Toast.makeText(mContext, "hide post is clicked", Toast.LENGTH_SHORT).show();
                                break;

//                            case R.id.card_give_award:
//                                Toast.makeText(mContext, "give award is clicked", Toast.LENGTH_SHORT).show();
//                                break;
                            case R.id.card_report:
                                showToast("report is clicked");
//                                Toast.makeText(mContext, "report is clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.card_block_user:
                                mRefUser.child("blockedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mRefUser.child("blockedUsers").child(mData.get(position).getUserId()).child("key").setValue(mData.get(position).getUserId());
                                        Toast.makeText(mContext, "User is blocked now.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                break;

                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    public void anotherSubredditIntent(int position) {
        MyRoomDatabase myRoomDatabase = DatabaseClient.databaseClient(mContext);

        if (mData.get(position).getSubType().equals("sub")) {
            HistoryTab historyTab = new HistoryTab(mData.get(position).getSubId(), mData.get(position).getSubType(),
                    mData.get(position).getCard_profile_image(), mData.get(position).getCard_title(), userId);
            myRoomDatabase.dao().historyTabInsertion(historyTab);
        }

        Intent intent = new Intent(mContext, AnotherUserActivity.class);
        intent.putExtra("anotherUserId", mData.get(position).getSubId());
        intent.putExtra("subType", mData.get(position).getSubType());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void showSinglePostActivity(int position, String fragmentType) {
        Intent intent = new Intent(mContext, ShowSinglePostActivity.class);
        intent.putExtra("cardPostProfileImage", mData.get(position).getCard_profile_image());
        intent.putExtra("imagePath", mData.get(position).getCard_image());
        intent.putExtra("cardTitle", mData.get(position).getCard_title());
        intent.putExtra("uploadedBy", mData.get(position).getPosted_by());
        intent.putExtra("cardDescription", mData.get(position).getCard_description());
        intent.putExtra("postedTime", mData.get(position).getPostedTime());
        intent.putExtra("anotherUserId", mData.get(position).getUserId());
        intent.putExtra("NSFW", mData.get(position).getNsfw());
        intent.putExtra("spoiler", mData.get(position).getSpoiler());
        intent.putExtra("postType", mData.get(position).getPost_type());
        intent.putExtra("fragmentType", fragmentType);

        mContext.startActivity(intent);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (flag == 1) {
//            mRef.removeEventListener(deletePostValueEventListener);
            try {

                mRef2.removeEventListener(getPostCountValueEventListener);
//            firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").removeEventListener(savedImageCountValueEventListener);
//            firebaseDatabase.getReference("creddit").child("posts").child("imagePosts").removeEventListener(savePostValueEventListener);
                firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").removeEventListener(showSavedImageValueEventListener);
//            firebaseDatabase.getReference("creddit").child("users").child(userId).child("savedImages").removeEventListener(unsavePostValueEventListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView card_title, posted_by, card_description, postedTime, upvoteCount, downvoteCount, commentCount, nsfw, spoiler, card_description_nsfw_spoiler, text_post_title, text_post_description;
        ImageView profile_photo, card_image, post_upvote, post_downvote, post_comment, post_share, card_menu, post_after_upvote, post_after_downvote, deletePost, joinSubreddit, unjoinedSubreddit, card_image_nsfw_spoiler;
        LinearLayout cardHeader, nsfw_spoiler_layout, normal_layout, text_post_layout;
        CardView cardViewImagePost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_title = itemView.findViewById(R.id.card_title);
            posted_by = itemView.findViewById(R.id.posted_by);
            card_description = itemView.findViewById(R.id.card_description);
            profile_photo = itemView.findViewById(R.id.card_profile_image);
            card_image = itemView.findViewById(R.id.card_image);
            post_upvote = itemView.findViewById(R.id.post_upvote);
            post_downvote = itemView.findViewById(R.id.post_downvote);
            post_comment = itemView.findViewById(R.id.post_comment);
            post_share = itemView.findViewById(R.id.post_share);
            card_menu = itemView.findViewById(R.id.card_menu);
            post_after_upvote = itemView.findViewById(R.id.post_after_upvote);
            post_after_downvote = itemView.findViewById(R.id.post_after_downvote);
            postedTime = itemView.findViewById(R.id.postedTime);
            upvoteCount = itemView.findViewById(R.id.upvoteCount);
            downvoteCount = itemView.findViewById(R.id.downvoteCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            deletePost = itemView.findViewById(R.id.deletePost);
            cardHeader = itemView.findViewById(R.id.card_header);
            joinSubreddit = itemView.findViewById(R.id.joinSubreddit);
            unjoinedSubreddit = itemView.findViewById(R.id.unjoinedSubreddit);
            nsfw = itemView.findViewById(R.id.nsfw_fill_post);
            spoiler = itemView.findViewById(R.id.spoiler_fill_post);
            card_description_nsfw_spoiler = itemView.findViewById(R.id.card_description_nsfw_spoiler);
            card_image_nsfw_spoiler = itemView.findViewById(R.id.card_image_nsfw_spoiler);
            text_post_title = itemView.findViewById(R.id.text_post_title);
            text_post_description = itemView.findViewById(R.id.text_post_description);
            cardViewImagePost = itemView.findViewById(R.id.card_image_post);

            nsfw_spoiler_layout = itemView.findViewById(R.id.nsfw_spoiler_layout);
            normal_layout = itemView.findViewById(R.id.normal_layout);
            text_post_layout = itemView.findViewById(R.id.text_post_layout);

        }

    }

    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri uri = null;
        try {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "creddit_" + System.currentTimeMillis() + ".png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public void showToast(String toast_text) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(toast_text);

        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
