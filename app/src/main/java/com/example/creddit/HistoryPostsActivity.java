package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creddit.Adapter.CardAdapter;
import com.example.creddit.Model.CardModel;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.HistoryPosts;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryPostsActivity extends AppCompatActivity {
    RecyclerView historyRecyclerView;
    List<CardModel> historyModel;
    SimpleDateFormat sdf;
    String currentDate, postTime, cardPostTime, userId;
    Date todayDate, postedDate;
    CardAdapter cardAdapter;
    TextView deleteHistory;
    MyRoomDatabase myRoomDatabase;

    ValueEventListener historyPostsValueEventListener, nsfwValueEventListener;
    ArrayList<String> historyList = new ArrayList<>();

    DatabaseReference mRef, postRef;
    int showNSFWvalue = 0, blurNSFWvalue = 0;

    SharedPref sharedPref;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_history_posts);

        toolbar = findViewById(R.id.historyToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        historyRecyclerView = findViewById(R.id.recycler_history);
        deleteHistory = findViewById(R.id.deleteHistory);

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        mRef = FirebaseDatabase.getInstance().getReference("creddit");
        postRef = FirebaseDatabase.getInstance().getReference("creddit").child("posts").child("imagePosts");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        getHiddenPostsLists();
//        Toast.makeText(getApplicationContext(), "This is history tab", Toast.LENGTH_SHORT).show();

        nsfwValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showNSFWvalue = dataSnapshot.child("showNSFW").getValue(Integer.class);
                    blurNSFWvalue = dataSnapshot.child("blurNSFW").getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference("creddit").child("users").child(userId).addValueEventListener(nsfwValueEventListener);


        historyModel = new ArrayList<>();

        cardAdapter = new CardAdapter(this, historyModel, HistoryPostsActivity.this, "uploadedImageFragment");
        LinearLayoutManager cardManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        cardManager.setReverseLayout(true);
//        cardManager.setStackFromEnd(true);

        myRoomDatabase = DatabaseClient.databaseClient(getApplicationContext());
        List<HistoryPosts> historyPostsList = myRoomDatabase.historyPostsDAO().getHistoryPosts(userId);

        deleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRoomDatabase.historyPostsDAO().deleteHistoryPosts(userId);
                Toast.makeText(getApplicationContext(), "All history cleared", Toast.LENGTH_SHORT).show();
                historyModel.clear();
                cardAdapter.notifyDataSetChanged();
            }
        });

//        Toast.makeText(getApplicationContext(), "historyPosts list size "+historyPostsList.size(), Toast.LENGTH_SHORT).show();

        if (historyPostsList.size() == 0){
            Toast.makeText(getApplicationContext(), "No data to show", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(getApplicationContext(), "There is data to show", Toast.LENGTH_SHORT).show();
            for (int i = historyPostsList.size()-1; i>=0; i--) {
//                    postTime = historyPostsList.get(i).getR_cardPostTime();

//                    todayDate = sdf.parse(currentDate);
//                    postedDate = sdf.parse(postTime);
//
//                    long diff = todayDate.getTime() - postedDate.getTime();
//                    long seconds = diff / 1000;
//                    long minutes = seconds / 60;
//                    long hours = minutes / 60;
//                    int days = (int) (hours / 24);
//
//                    if ((days / 365) > 0) {
//                        int year = days / 365;
//                        int leftMonths = days % 365;
//                        int month = leftMonths / 30;
//                        int leftDays = month % 30;
//                        cardPostTime = (year + "y " + month + "m ago");
//                    } else if ((days / 30) > 0) {
//                        int month = days / 30;
//                        int leftDays = days % 30;
//                        cardPostTime = (month + "m ago");
//                    } else if ((hours / 24) > 0) {
//                        cardPostTime = (days + "d ago");
//                    } else if ((minutes / 60) > 0) {
//                        cardPostTime = (hours + "h ago");
//                    } else {
//                        cardPostTime = (minutes + "min ago");
//                    }
                if (showNSFWvalue == 0) {
                    if (historyPostsList.get(i).getR_NSFW() == 0) {
//                        Log.d("roomDBHistory", '\n'+historyPostsList.get(i).getR_cardPostProfileImage()+'\n'+ historyPostsList.get(i).getR_imagePath()+'\n'+
//                                historyPostsList.get(i).getR_subName()+'\n'+ "Posted by " + historyPostsList.get(i).getR_uploadedBy()+'\n'+
//                                historyPostsList.get(i).getR_cardTitle()+'\n'+ historyPostsList.get(i).getR_cardPostTime()+'\n'+
//                                historyPostsList.get(i).getR_userId()+'\n'+ historyPostsList.get(i).getR_NSFW()+'\n'+
//                                historyPostsList.get(i).getR_spoiler()+'\n'+ historyPostsList.get(i).getR_postType()+'\n'+ historyPostsList.get(i).getR_subId()+'\n'+ userId);


                        historyModel.add(new CardModel(historyPostsList.get(i).getR_cardPostProfileImage(),
                                historyPostsList.get(i).getR_imagePath(),
                                historyPostsList.get(i).getR_subName(), "Posted by " + historyPostsList.get(i).getR_uploadedBy(),
                                historyPostsList.get(i).getR_cardTitle(), historyPostsList.get(i).getR_cardPostTime(),
                                historyPostsList.get(i).getR_userId(), historyPostsList.get(i).getR_NSFW(),
                                historyPostsList.get(i).getR_spoiler(), historyPostsList.get(i).getR_postType(), historyPostsList.get(i).getR_subId(),
                                historyPostsList.get(i).getR_subType(), historyPostsList.get(i).getR_id()));
                    }
                } else {
//                    Log.d("roomDBHistory", historyPostsList.get(i).getR_cardPostProfileImage()+'\n'+ historyPostsList.get(i).getR_imagePath()+'\n'+
//                            historyPostsList.get(i).getR_subName()+'\n'+ "Posted by " + historyPostsList.get(i).getR_uploadedBy()+'\n'+
//                            historyPostsList.get(i).getR_cardTitle()+'\n'+ historyPostsList.get(i).getR_cardPostTime()+'\n'+
//                            historyPostsList.get(i).getR_userId()+'\n'+ historyPostsList.get(i).getR_NSFW()+'\n'+
//                            historyPostsList.get(i).getR_spoiler()+'\n'+ historyPostsList.get(i).getR_postType()+'\n'+ historyPostsList.get(i).getR_subId()+'\n'+ userId);

                    historyModel.add(new CardModel(historyPostsList.get(i).getR_cardPostProfileImage(),
                            historyPostsList.get(i).getR_imagePath(),
                            historyPostsList.get(i).getR_subName(), "Posted by " + historyPostsList.get(i).getR_uploadedBy(),
                            historyPostsList.get(i).getR_cardTitle(), historyPostsList.get(i).getR_cardPostTime(),
                            historyPostsList.get(i).getR_userId(), historyPostsList.get(i).getR_NSFW(),
                            historyPostsList.get(i).getR_spoiler(), historyPostsList.get(i).getR_postType(), historyPostsList.get(i).getR_subId(),
                            historyPostsList.get(i).getR_subType(), historyPostsList.get(i).getR_id()));
                }
                cardAdapter.notifyDataSetChanged();
            }
        }

        historyRecyclerView.setLayoutManager(cardManager);
        historyRecyclerView.setAdapter(cardAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("creddit").child("users").child(userId).removeEventListener(nsfwValueEventListener);
    }
}