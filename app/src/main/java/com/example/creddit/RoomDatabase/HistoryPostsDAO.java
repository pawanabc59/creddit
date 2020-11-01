package com.example.creddit.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryPostsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void historyPostsInsertion(HistoryPosts historyPosts);

    @Query("Select * from HistoryPosts where r_currentUser = :currentUserId")
    List<HistoryPosts> getHistoryPosts(String currentUserId);

    @Query("Delete from HistoryPosts where r_currentUser = :currentUserId")
    void deleteHistoryPosts(String currentUserId);

//    @Delete
//    void deleteLoggedInUser(LoggedInUser loggedInUser);
}
