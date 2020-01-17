package com.example.creddit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Model.FollowingListModal;
import com.example.creddit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.MyViewHolder> {
    private Context mContext;
    private List<FollowingListModal> mData;

    public FollowingListAdapter(Context mContext, List<FollowingListModal> mData) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.sub_name.setText(mData.get(position).sub_name);
        Picasso.get().load(mData.get(position).sub_image).into(holder.sub_image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sub_name;
        ImageView sub_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_image = itemView.findViewById(R.id.sub_image);
            sub_name = itemView.findViewById(R.id.sub_name);
        }
    }
}
