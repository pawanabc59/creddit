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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.BuildConfig;
import com.example.creddit.Model.CardModal;
import com.example.creddit.ProfileActivity;
import com.example.creddit.R;
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

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    private Context mContext;
    private List<CardModal> mData;
    String cardImagePath;
    int vote;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference mRef, mRefUser, mRef2;
    Activity parentActivity;
    ValueEventListener deletePostValueEventListener,getPostCountValueEventListener;
    int i, numberOfPost, flag=0;
    String TAG = "my";

    public CardAdapter(Context mContext, List<CardModal> mData, Activity parentActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
//        if (parentActivity instanceof ProfileActivity){
//            view = inflater.inflate(R.layout.card_image_layout_delete, null);
//        }else {
            view = inflater.inflate(R.layout.card_image_layout, null);
//        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardAdapter.MyViewHolder holder, final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("posts").child("imagePosts");
        mRef2 = firebaseDatabase.getReference("creddit").child("posts");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

//        String userId = user.getUid();
//        mRefUser = firebaseDatabase.getReference("creddit").child("users").child(userId);

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

        if (parentActivity instanceof ProfileActivity){
            cardMenu.setVisibility(View.GONE);
            deletePost.setVisibility(View.VISIBLE);
        }
        else {
            cardMenu.setVisibility(View.VISIBLE);
            deletePost.setVisibility(View.GONE);
        }

//        vote = Integer.parseInt(mData.get(position).getVote());
        holder.card_title.setText(mData.get(position).card_title);
        holder.posted_by.setText(mData.get(position).posted_by);
        holder.card_description.setText(mData.get(position).card_description);
        Picasso.get().load(mData.get(position).getCard_profile_image()).into(holder.profile_photo);
        Picasso.get().load(mData.get(position).getCard_image()).into(holder.card_image);
        holder.postedTime.setText(mData.get(position).postedTime);

        cardImagePath = mData.get(position).getCard_image();

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
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                if (dataSnapshot1.child("imagePath").getValue().toString().equals(mData.get(position).getCard_image())){
                                                    mRef.child(dataSnapshot1.getKey()).child("cardPostProfileImage").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("cardTitle").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("imagePath").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("postNumber").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("postTime").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("uploadedBy").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("userId").removeValue();
                                                    mRef.child(dataSnapshot1.getKey()).child("vote").removeValue();
//                                                    mRef2.child("numberOfPosts").setValue(numberOfPost-1);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };

                                mRef.addValueEventListener(deletePostValueEventListener);
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
                Toast.makeText(mContext, "comment is clicked", Toast.LENGTH_SHORT).show();
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

        holder.card_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.card_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new MyViewHolder(view));
                popupMenu.show();
            }
        });

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (flag == 1) {
            mRef.removeEventListener(deletePostValueEventListener);
            mRef2.removeEventListener(getPostCountValueEventListener);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMenuItemClickListener {

        TextView card_title, posted_by, card_description, postedTime, upvoteCount, downvoteCount, commentCount;
        ImageView profile_photo, card_image, post_upvote, post_downvote, post_comment, post_share, card_menu, post_after_upvote, post_after_downvote, deletePost;

        public MyViewHolder(@NonNull View itemView) {
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

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            
            switch (menuItem.getItemId()) {
                case R.id.card_save:
                    Toast.makeText(mContext, "Save is clicked", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, ProfileActivity.class);
//                    mContext.startActivity(intent);
                    break;
                case R.id.card_hide_post:
                    Toast.makeText(mContext, "hide post is clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.card_give_award:
                    Toast.makeText(mContext, "give award is clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.card_report:
                    Toast.makeText(mContext, "report is clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.card_block_user:
                    Toast.makeText(mContext, "block user is clicked", Toast.LENGTH_SHORT).show();
                    break;

            }
            return true;
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
}
