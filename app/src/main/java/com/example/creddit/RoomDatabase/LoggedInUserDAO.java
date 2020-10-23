package com.example.creddit.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoggedInUserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void loggedInUserInsertion(LoggedInUser loggedInUser);

    @Query("Select * from LoggedInUser")
    List<LoggedInUser> getLoggedInUser();

//    @Query("Delete from HistoryTab where subId = :subId")
//    void deleteHistoryTab(String subId);

    @Delete
    void deleteLoggedInUser(LoggedInUser loggedInUser);
}
