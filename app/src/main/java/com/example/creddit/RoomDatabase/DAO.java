package com.example.creddit.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void historyTabInsertion(HistoryTab historyTab);

    @Query("Select * from HistoryTab where userId = :userId")
    List<HistoryTab> getHistory(String userId);

//    @Query("Delete from HistoryTab where subId = :subId")
//    void deleteHistoryTab(String subId);

    @Delete
    void deleteHistoryTab(HistoryTab historyTab);
}
