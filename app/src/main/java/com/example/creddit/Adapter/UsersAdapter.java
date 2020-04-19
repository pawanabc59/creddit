package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Model.UsersModel;
import com.example.creddit.R;
import com.example.creddit.SingleChatActicity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    Context mContext;
    List<UsersModel> mData;

    public UsersAdapter(Context mContext, List<UsersModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.user_card_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.UserName.setText(mData.get(position).getUserName());
        Picasso.get().load(mData.get(position).getUserProfileImage()).into(holder.UserProfileImage);

        holder.UserCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleChatActicity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("receiverId", mData.get(position).getReceiverId());
                intent.putExtra("receiverProfilePicture", mData.get(position).getUserProfileImage());
                intent.putExtra("receiverUsername", mData.get(position).getUserName());
                intent.putExtra("receiverNumber", mData.get(position).getReceiverNumber());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView UserName;
        ImageView UserProfileImage;
        LinearLayout UserCardLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            UserName = itemView.findViewById(R.id.UserName);
            UserCardLayout = itemView.findViewById(R.id.UserCardLayout);
            UserProfileImage = itemView.findViewById(R.id.UsersProfileImage);
        }
    }
}
