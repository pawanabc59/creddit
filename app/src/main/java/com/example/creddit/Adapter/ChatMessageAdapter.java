package com.example.creddit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Model.ChatModel;
import com.example.creddit.Model.UsersModel;
import com.example.creddit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    Context mContext;
    List<ChatModel> mData;

    public ChatMessageAdapter(Context mContext, List<ChatModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.chat_layout, parent, false);
//        view = layoutInflater.inflate(R.layout.chat_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(R.drawable.reddit_logo).into(holder.userImage);
        holder.chatMessage.setText(mData.get(position).getChatMessage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView chatMessage, chatUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            chatMessage = itemView.findViewById(R.id.chatMessage);
            chatUserName = itemView.findViewById(R.id.chatUserName);
        }
    }
}
