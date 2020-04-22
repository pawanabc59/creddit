package com.example.creddit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Model.ChatModel;
import com.example.creddit.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    Context mContext;
    List<ChatModel> mData;
    String userId;
    ValueEventListener userValueEventListener;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        userValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        FirebaseDatabase.getInstance().getReference("cerddit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(userValueEventListener);

//        try {
//            if (userId.equals(mData.get(position).getSenderId())) {
//                Picasso.get().load(mData.get(position).getSenderProfilePicture()).into(holder.userImage);
//                holder.chatMessage.setText(mData.get(position).getChatMessage());
//                holder.chatUserName.setText(mData.get(position).getSenderUserName());
//            } else {

        if (userId.equals(mData.get(position).getUserId())) {
            holder.chatMessage.setBackgroundResource(R.drawable.sender_chat_layout);
        } else {
            holder.chatMessage.setBackgroundResource(R.drawable.receiver_chat_layout);
        }

//        to set layout margin programmatically.
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.chatLayout.getLayoutParams();

        if (position == 0) {
            Picasso.get().load(mData.get(position).getUserImage()).into(holder.userImage);
            holder.chatMessage.setText(mData.get(position).getChatMessage());
            holder.chatUserName.setText(mData.get(position).getChatUserName());
            params.setMargins(40, 50, 40, 0);
            holder.chatLayout.setLayoutParams(params);
        }else {
            try {
                if (mData.get(position).getUserId().equals(mData.get(position - 1).getUserId())) {
                    holder.userImage.setVisibility(View.INVISIBLE);
                    holder.chatUserName.setVisibility(View.GONE);
                    holder.chatMessage.setText(mData.get(position).getChatMessage());
                    params.setMargins(40, 0, 40, 0);
                    holder.chatLayout.setLayoutParams(params);
                } else {
                    Picasso.get().load(mData.get(position).getUserImage()).into(holder.userImage);
                    holder.chatMessage.setText(mData.get(position).getChatMessage());
                    holder.chatUserName.setText(mData.get(position).getChatUserName());
                    params.setMargins(40, 50, 40, 0);
                    holder.chatLayout.setLayoutParams(params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (userId.equals(mData.get(position).getUserId())) {
            holder.chatMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {

                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.getMenuInflater().inflate(R.menu.delete_message_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if (menuItem.getItemId() == R.id.deleteMessage) {

                                FirebaseDatabase.getInstance().getReference("creddit").child("chats").child(mData.get(position).getChatId()).child("chatMessage").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                            if (dataSnapshot1.child("message").getValue(String.class).equals(mData.get(position).getChatMessage()) &&
                                            dataSnapshot1.child("senderId").getValue(String.class).equals(userId)){
                                                FirebaseDatabase.getInstance().getReference("creddit").child("chats").child(mData.get(position).getChatId()).child("chatMessage").child(dataSnapshot1.getKey()).child("chatNumber").removeValue();
                                                FirebaseDatabase.getInstance().getReference("creddit").child("chats").child(mData.get(position).getChatId()).child("chatMessage").child(dataSnapshot1.getKey()).child("chatTime").removeValue();
                                                FirebaseDatabase.getInstance().getReference("creddit").child("chats").child(mData.get(position).getChatId()).child("chatMessage").child(dataSnapshot1.getKey()).child("message").removeValue();
                                                FirebaseDatabase.getInstance().getReference("creddit").child("chats").child(mData.get(position).getChatId()).child("chatMessage").child(dataSnapshot1.getKey()).child("receiverId").removeValue();
                                                FirebaseDatabase.getInstance().getReference("creddit").child("chats").child(mData.get(position).getChatId()).child("chatMessage").child(dataSnapshot1.getKey()).child("senderId").removeValue();

                                                Snackbar.make(view, "deleted", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            return true;
                        }

                    });

                    popupMenu.show();

                    return true;
                }
            });
        }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView chatMessage, chatUserName;
        LinearLayout chatLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            chatMessage = itemView.findViewById(R.id.chatMessage);
            chatUserName = itemView.findViewById(R.id.chatUserName);
            chatLayout = itemView.findViewById(R.id.chatLayourLinearLayout);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        FirebaseDatabase.getInstance().getReference("cerddit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeEventListener(userValueEventListener);
    }
}
