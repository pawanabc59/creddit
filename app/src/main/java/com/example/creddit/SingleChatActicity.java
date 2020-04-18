package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.creddit.Adapter.ChatMessageAdapter;
import com.example.creddit.Adapter.UsersAdapter;
import com.example.creddit.Model.ChatModel;
import com.example.creddit.Model.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleChatActicity extends AppCompatActivity {

    SharedPref sharedPref;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    String senderUserId, receiverUserId, receiverProfilePicture, receiverUsername, chatId, editMessage, currentDate ;
    Date date;
    SimpleDateFormat simpleDateFormat;
    DatabaseReference mRef;
    Toolbar chatToolbarTitle;
    RecyclerView chatRecyclerView;
    EditText sendMessageEdit;
    ImageButton sendMessageButton;
    List<ChatModel> chatModels;
    ChatMessageAdapter chatMessageAdapter;
    int sender, receiver;
    int chatNumber;
    ValueEventListener showChatMessageValueEventListener, chatNumberValueEventListener, sendMessageValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_single_chat_acticity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        senderUserId = firebaseUser.getUid();

        sendMessageEdit = findViewById(R.id.sendMessageEdit);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatToolbarTitle = findViewById(R.id.chatToolbarTitle);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        Intent intent = getIntent();
        receiverUserId = intent.getExtras().getString("receiverId");
        receiverProfilePicture = intent.getExtras().getString("receiverProfilePicture");
        receiverUsername = intent.getExtras().getString("receiverUsername");

//        sender = Integer.parseInt(senderUserId.substring(0,5).toLowerCase());
//        receiver = Integer.parseInt(receiverUserId.substring(0,5).toLowerCase());
//
//        if (sender > receiver){
//            chatId = senderUserId+receiverUserId;
//        }else{
//            chatId = receiverUserId+senderUserId;
//        }

        mRef.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        if (dataSnapshot1.getKey().equals((senderUserId+receiverUserId))){
                            chatId = senderUserId+receiverUserId;
                            break;
                        }
                        else {
                            chatId = receiverUserId+senderUserId;
                        }
                    }
//                    chatId = senderUserId+receiverUserId;
                }
                else {
                    chatId = receiverUserId+senderUserId;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatModels = new ArrayList<>();

        chatMessageAdapter = new ChatMessageAdapter(SingleChatActicity.this, chatModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mRef.child("users").child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatToolbarTitle.setTitle(dataSnapshot.child("optionalName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        try{
            chatNumberValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        chatNumber = dataSnapshot.getValue(Integer.class);
                    }
                    else{
                        chatNumber = 0;
                        mRef.child("chats").child(chatId).child("numberOfChats").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            try {
                mRef.child("chats").child(chatId).child("numberOfChats").addValueEventListener(chatNumberValueEventListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMessage = sendMessageEdit.getText().toString().trim();
                if (editMessage.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    final String key = mRef.push().getKey();
//                    sendMessageValueEventListener = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            date = new Date();
                            currentDate = simpleDateFormat.format(date);

                            mRef.child("chats").child(chatId).child("chatMessage").child(key).child("senderId").setValue(senderUserId);
                            mRef.child("chats").child(chatId).child("chatMessage").child(key).child("receiverId").setValue(receiverUserId);
                            mRef.child("chats").child(chatId).child("chatMessage").child(key).child("message").setValue(editMessage);
                            mRef.child("chats").child(chatId).child("chatMessage").child(key).child("chatNumber").setValue(-(chatNumber+1));
                            mRef.child("chats").child(chatId).child("chatMessage").child(key).child("chatTime").setValue(currentDate);
                            mRef.child("chats").child(chatId).child("numberOfChats").setValue((chatNumber+1));
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    };
//                    mRef.child("chats").child(chatId).child("chatMessage").child(key).addValueEventListener(sendMessageValueEventListener);
                }
                sendMessageEdit.setText("");
            }
        });

//        try{
            showChatMessageValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();
                    if (dataSnapshot.exists()){
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            chatModels.add(new ChatModel("UserImage", "UserName", dataSnapshot1.child("message").getValue(String.class)));
                            chatMessageAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            try {
                mRef.child("chats").child(receiverUserId+senderUserId).child("chatMessage").addValueEventListener(showChatMessageValueEventListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(chatMessageAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRef.child("chats").child(chatId).child("numberOfChats").removeEventListener(chatNumberValueEventListener);
        mRef.child("chats").child(chatId).child("chatMessage").removeEventListener(showChatMessageValueEventListener);

    }
}
