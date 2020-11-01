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
import com.example.creddit.Model.JoinedCommunityModel;
import com.example.creddit.R;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.HistoryTab;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JoinedCommunityAdapter extends RecyclerView.Adapter<JoinedCommunityAdapter.MyViewHolder> {
    private Context mContext;
    private List<JoinedCommunityModel> mData;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String userId;
    DatabaseReference mRef;

    public JoinedCommunityAdapter(Context mContext, List<JoinedCommunityModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.joined_community_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();

        holder.sub_name.setText(mData.get(position).sub_name);
        Picasso.get().load(mData.get(position).sub_image).into(holder.sub_image);
        holder.membersCount.setText(mData.get(position).getMembers()+" members");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AnotherUserActivity.class);
                intent.putExtra("anotherUserId", mData.get(position).getAnotherUserId());
                intent.putExtra("subType", mData.get(position).getSubType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        userId = user.getUid();

        holder.removeJoinedCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("users").child(userId).child("customFeed").child(mData.get(position).getFeedName()).child(mData.get(position).getAnotherUserId()).removeValue();

                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mData.size());

                Snackbar.make(view, "Removed Community", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sub_name, membersCount;
        ImageView sub_image, removeJoinedCommunity;
        LinearLayout followingListLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_image = itemView.findViewById(R.id.sub_image);
            sub_name = itemView.findViewById(R.id.sub_name);
            followingListLayout = itemView.findViewById(R.id.followingListLayout);
            removeJoinedCommunity = itemView.findViewById(R.id.removeJoinedCommunity);
            membersCount = itemView.findViewById(R.id.membersCount);
        }
    }
}
