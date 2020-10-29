package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.CustomFeedActivity;
import com.example.creddit.Model.CustomFeedModel;
import com.example.creddit.R;

import java.util.List;

public class CustomFeedAdapter extends RecyclerView.Adapter<CustomFeedAdapter.ViewHolder> {

    List<CustomFeedModel> customFeedModels;
    Context mContext;

    public CustomFeedAdapter(Context mContext, List<CustomFeedModel> customFeedModels) {
        this.customFeedModels = customFeedModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CustomFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.custom_feed_layout, parent, false);

        CustomFeedAdapter.ViewHolder viewHolder = new CustomFeedAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomFeedAdapter.ViewHolder holder, final int position) {
        holder.customFeedName.setText(customFeedModels.get(position).getCustomFeedName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CustomFeedActivity.class);
                intent.putExtra("FeedName", customFeedModels.get(position).getCustomFeedName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customFeedModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customFeedName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customFeedName = itemView.findViewById(R.id.customFeedName);
        }
    }
}
