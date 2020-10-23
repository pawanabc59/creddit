package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.AnotherUserActivity;
import com.example.creddit.EditSubRedditActivity;
import com.example.creddit.Model.FollowingListModel;
import com.example.creddit.R;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.HistoryTab;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.MyViewHolder> {
    private Context mContext;
    private List<FollowingListModel> mData;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String userId;
    DatabaseReference mRef;

    public FollowingListAdapter(Context mContext, List<FollowingListModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.following_list_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();

        holder.sub_name.setText(mData.get(position).sub_name);
        Picasso.get().load(mData.get(position).sub_image).into(holder.sub_image);

        userId = user.getUid();

        if (mData.get(position).getType().equals("subscription")){
            holder.markFavouriteLayout.setVisibility(View.VISIBLE);
            holder.blockLayout.setVisibility(View.GONE);
        }
        else if (mData.get(position).getType().equals("blocked")){
            holder.blockLayout.setVisibility(View.VISIBLE);
            holder.markFavouriteLayout.setVisibility(View.GONE);
        }

        holder.unblockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mRef.child("users").child(userId).child("blockedUsers").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()){
//                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                if (dataSnapshot1.getKey().equals(mData.get(position).getAnotherUserId())){
                                    mRef.child("users").child(userId).child("blockedUsers").child(mData.get(position).getAnotherUserId()).child("key").removeValue();
                                    holder.unblockUser.setVisibility(View.GONE);
                                    holder.blockUser.setVisibility(View.VISIBLE);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
            }
        });

        holder.blockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("users").child(userId).child("blockedUsers").child(mData.get(position).getAnotherUserId()).child("key").setValue(mData.get(position).getAnotherUserId());
                holder.blockUser.setVisibility(View.GONE);
                holder.unblockUser.setVisibility(View.VISIBLE);
            }
        });

        mRef.child("users").child(userId).child("followingList").orderByChild("key").equalTo(mData.get(position).getAnotherUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        try {
                            if (dataSnapshot1.child("favourite").getValue(Integer.class) == 1) {
                                holder.addFavourite.setVisibility(View.GONE);
                                holder.removeFavourite.setVisibility(View.VISIBLE);
                            } else {
                                holder.removeFavourite.setVisibility(View.GONE);
                                holder.addFavourite.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (mData.get(position).getType().equals("mySubRedditList")){
            holder.followingListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.markFavouriteLayout.setVisibility(View.GONE);
                    holder.blockLayout.setVisibility(View.GONE);
                    Intent intent = new Intent(mContext, EditSubRedditActivity.class);
                    intent.putExtra("subId", mData.get(position).getAnotherUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

        }else {
            holder.followingListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyRoomDatabase myRoomDatabase = DatabaseClient.databaseClient(mContext);

                    if (mData.get(position).getSubType().equals("sub")) {
                        HistoryTab historyTab = new HistoryTab(mData.get(position).getAnotherUserId(), mData.get(position).getSubType(),
                                mData.get(position).getSub_image(), mData.get(position).getSub_name(), userId);
                        myRoomDatabase.dao().historyTabInsertion(historyTab);
                    }

                    Intent intent = new Intent(mContext, AnotherUserActivity.class);
                    intent.putExtra("anotherUserId", mData.get(position).getAnotherUserId());
                    intent.putExtra("subType", mData.get(position).getSubType());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }

        holder.addFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("users").child(userId).child("followingList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.getKey().equals(mData.get(position).anotherUserId)) {
                                    mRef.child("users").child(userId).child("followingList").child(dataSnapshot1.getKey()).child("favourite").setValue(1);
                                    holder.addFavourite.setVisibility(View.GONE);
                                    holder.removeFavourite.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.removeFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("users").child(userId).child("followingList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.getKey().equals(mData.get(position).anotherUserId)) {
                                    mRef.child("users").child(userId).child("followingList").child(dataSnapshot1.getKey()).child("favourite").setValue(0);
                                    holder.removeFavourite.setVisibility(View.GONE);
                                    holder.addFavourite.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sub_name, blockUser, unblockUser;
        ImageView sub_image, addFavourite, removeFavourite;
        LinearLayout followingListLayout;
        RelativeLayout markFavouriteLayout, blockLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_image = itemView.findViewById(R.id.sub_image);
            sub_name = itemView.findViewById(R.id.sub_name);
            followingListLayout = itemView.findViewById(R.id.followingListLayout);
            addFavourite = itemView.findViewById(R.id.addFavourite);
            removeFavourite = itemView.findViewById(R.id.removeFavourite);
            markFavouriteLayout = itemView.findViewById(R.id.markFavourite);
            blockLayout = itemView.findViewById(R.id.blockLayout);
            blockUser = itemView.findViewById(R.id.blockUser);
            unblockUser = itemView.findViewById(R.id.unblockUser);
        }
    }
}
