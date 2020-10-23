package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.MainActivity;
import com.example.creddit.Model.LoggedInUsersModel;
import com.example.creddit.R;
import com.example.creddit.RoomDatabase.DatabaseClient;
import com.example.creddit.RoomDatabase.LoggedInUser;
import com.example.creddit.RoomDatabase.MyRoomDatabase;
import com.example.creddit.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoggedInUsersAdapter extends RecyclerView.Adapter<LoggedInUsersAdapter.ViewHolder> {
    Context mContext;
    List<LoggedInUsersModel> loggedInUsersModels;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mRef, mRef2;
    SharedPref sessionManager;
    MyRoomDatabase myRoomDatabase;

    public LoggedInUsersAdapter(Context mContext, List<LoggedInUsersModel> loggedInUsersModels) {
        this.mContext = mContext;
        this.loggedInUsersModels = loggedInUsersModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.logged_in_users_layout, null);

        LoggedInUsersAdapter.ViewHolder viewHolder = new LoggedInUsersAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Picasso.get().load(loggedInUsersModels.get(position).getUserPhoto()).into(holder.loggedInUserPhoto);
        holder.loggedInUserName.setText(loggedInUsersModels.get(position).getUserName());

        sessionManager = new SharedPref(mContext);
        mRef = FirebaseDatabase.getInstance().getReference("creddit").child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        myRoomDatabase = DatabaseClient.databaseClient(mContext);

        holder.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                LoggedInUser loggedInUser = new LoggedInUser(loggedInUsersModels.get(position).getUserIds(), loggedInUsersModels.get(position).getUserEmail(),
                        loggedInUsersModels.get(position).getUsersPassword(), loggedInUsersModels.get(position).getUserPhoto(),
                        loggedInUsersModels.get(position).getUserName());

                myRoomDatabase.loggedInUserDAO().deleteLoggedInUser(loggedInUser);
                loggedInUsersModels.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, loggedInUsersModels.size());

                List<LoggedInUser> loggedInUsersData = myRoomDatabase.loggedInUserDAO().getLoggedInUser();
                if (loggedInUsersData.size() == 0){
                    Intent intent2 = new Intent(mContext, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent2);
                }else {
                    loginIntoApp(0);
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser != null){
                    loginIntoApp(position);
                }
            }
        });
    }

    private void loginIntoApp(final int position) {
        if (firebaseUser != null) {
            firebaseAuth.signOut();
        }
        firebaseAuth.signInWithEmailAndPassword(loggedInUsersModels.get(position).getUserEmail(), loggedInUsersModels.get(position).getUsersPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser.isEmailVerified()) {

                            String uid = firebaseUser.getUid();
                            mRef2 = mRef.child(uid);
                            mRef2.child("email").setValue(loggedInUsersModels.get(position).getUserEmail());
                            if (task.isSuccessful()) {

//                            this is to add the profile of the user if user has uploaded the profile image earlier then that url will be loaded otherwise the null value will be set.
                                mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        sessionManager.put_showNSFW(dataSnapshot.child("showNSFW").getValue(Integer.class));
                                        sessionManager.put_blurNSFW(dataSnapshot.child("blurNSFW").getValue(Integer.class));
                                        try {
                                            String profileImagePath = dataSnapshot.child("profileImage").getValue().toString();
                                            mRef2.child("profileImage").setValue(profileImagePath);
                                            mRef2.child("optionalName").setValue(dataSnapshot.child("optionalName").getValue().toString());
                                            mRef2.child("optionalAbout").setValue(dataSnapshot.child("optionalAbout").getValue().toString());
                                            mRef2.child("profileBannerImage").setValue(dataSnapshot.child("profileBannerImage").getValue().toString());
                                            mRef2.child("savedImages").child("numberOfSavedImages").setValue(dataSnapshot.child("savedImages").child("numberOfSavedImages").getValue(Integer.class));
                                            mRef2.child("showNSFW").setValue(dataSnapshot.child("showNSFW").getValue(Integer.class));
                                            mRef2.child("blurNSFW").setValue(dataSnapshot.child("blurNSFW").getValue(Integer.class));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            mRef2.child("profileImage").setValue("null");
                                            mRef2.child("optionalName").setValue(loggedInUsersModels.get(position).getUserEmail());
                                            mRef2.child("optionalAbout").setValue("none");
                                            mRef2.child("profileBannerImage").setValue("null");
                                            mRef2.child("savedImages").child("numberOfSavedImages").setValue(0);
                                            mRef2.child("showNSFW").setValue(0);
                                            mRef2.child("blurNSFW").setValue(0);
                                            sessionManager.put_showNSFW(0);
                                            sessionManager.put_blurNSFW(0);
                                        }
                                        Toast.makeText(mContext, "Welcome User", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(mContext, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


//                          this was earlier code :  mRef2.child("profileImage").setValue("null");

                            } else {
                                Toast.makeText(mContext, "Sorry! Login failed", Toast.LENGTH_SHORT);
                            }

                        } else {
                            Toast.makeText(mContext, "Please verify your email address first!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Either Username or Password is wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return loggedInUsersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView loggedInUserPhoto, logout;
        TextView loggedInUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            loggedInUserName = itemView.findViewById(R.id.loggedInUserName);
            loggedInUserPhoto = itemView.findViewById(R.id.loggedInUserPhoto);
            logout = itemView.findViewById(R.id.logout);
        }
    }
}
