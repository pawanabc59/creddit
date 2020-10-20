package com.example.creddit.RoomDatabase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class DatabaseClient {

    private static MyRoomDatabase myRoomDatabase;
    public static MyRoomDatabase databaseClient(Context mContext){
        if (myRoomDatabase == null){
            myRoomDatabase = Room.databaseBuilder(mContext, MyRoomDatabase.class, "history").allowMainThreadQueries().build();
        }
        return myRoomDatabase;
    }
}
