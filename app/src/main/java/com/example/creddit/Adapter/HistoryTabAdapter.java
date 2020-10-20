package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.AnotherUserActivity;
import com.example.creddit.Model.HistoryTabModel;
import com.example.creddit.R;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.HistoryTab;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryTabAdapter extends RecyclerView.Adapter<HistoryTabAdapter.ViewHolder> {
    Context mContext;
    List<HistoryTabModel> historyTabModels;
    MyRoomDatabase myRoomDatabase;
    String userId;

    public HistoryTabAdapter(Context mContext, List<HistoryTabModel> historyTabModels) {
        this.mContext = mContext;
        this.historyTabModels = historyTabModels;
    }

    @NonNull
    @Override
    public HistoryTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.history_tab_layout, parent, false);
        HistoryTabAdapter.ViewHolder viewHolder = new HistoryTabAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTabAdapter.ViewHolder holder, final int position) {

        myRoomDatabase = DatabaseClient.databaseClient(mContext);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Picasso.get().load(historyTabModels.get(position).getProfilePicture()).into(holder.cardImage);
        holder.cardName.setText(historyTabModels.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAnotherActivity(position);
            }
        });
        holder.cancelHistoryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HistoryTab historyTab = new HistoryTab(historyTabModels.get(position).getKey(), historyTabModels.get(position).getType(),
                        historyTabModels.get(position).getProfilePicture(), historyTabModels.get(position).getName(), userId);

                myRoomDatabase.dao().deleteHistoryTab(historyTab);

                historyTabModels.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, historyTabModels.size());
            }
        });
    }

    private void sendToAnotherActivity(int position) {
        Intent intent = new Intent(mContext, AnotherUserActivity.class);
        intent.putExtra("anotherUserId", historyTabModels.get(position).getKey());
        intent.putExtra("subType", historyTabModels.get(position).getType());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return historyTabModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage, cancelHistoryTab;
        TextView cardName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardImage = itemView.findViewById(R.id.cardImage);
            cardName = itemView.findViewById(R.id.cardName);
            cancelHistoryTab = itemView.findViewById(R.id.cancelHistoryTab);
        }
    }
}
