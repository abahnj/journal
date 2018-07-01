package com.abahnj.journalapp.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.abahnj.journalapp.data.JournalEntry;

import java.util.List;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal")
    LiveData<List<JournalEntry>> loadAllEntries();

    @Insert
    void insertEntry(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(JournalEntry journalEntry);

    @Delete
    void deleteEntry(JournalEntry journalEntry);

    @Query("DELETE FROM journal WHERE id = :id")
    void deleteEntry(int id);

    @Query("SELECT * FROM journal WHERE id = :id")
    LiveData<JournalEntry> loadEntryById(int id);

    @Query("DELETE FROM journal")
    void deleteAllEntries();
}
