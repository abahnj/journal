package com.abahnj.journalapp.data.source.local;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalDataSource;
import com.abahnj.journalapp.utilities.AppExecutors;

import java.util.List;

public class JournalLocalDataSource implements JournalDataSource {
    private static volatile JournalLocalDataSource INSTANCE;

    private JournalDao mJournalDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private JournalLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull JournalDao journalDao) {
        mAppExecutors = appExecutors;
        mJournalDao = journalDao;
    }

    public static JournalLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull JournalDao journalDao) {
        if (INSTANCE == null) {
            synchronized (JournalLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JournalLocalDataSource(appExecutors, journalDao);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void clearInstance() {
        INSTANCE = null;
    }

    public LiveData<List<JournalEntry>> getJournalEntries() {
        return mJournalDao.loadAllEntries();
    }



    @Override
    public LiveData<JournalEntry> getJournalEntry(int journalEntryId) {
            return mJournalDao.loadEntryById(journalEntryId);
    }

    @Override
    public void saveJournalEntry(@NonNull JournalEntry journalEntry) {
        AppExecutors.getInstance().diskIO().execute(() -> mJournalDao.insertEntry(journalEntry));
    }

    @Override
    public void updateJournalEntry(JournalEntry journalEntry) {
        AppExecutors.getInstance().diskIO().execute(() -> mJournalDao.updateEntry(journalEntry));
    }

    @Override
    public void refreshJournalEntries() {

    }

    @Override
    public void deleteAllJournalEntries() {
        AppExecutors.getInstance().diskIO().execute(() -> mJournalDao.deleteAllEntries());

    }

    @Override
    public void deleteJournalEntry(JournalEntry journalEntry) {
        AppExecutors.getInstance().diskIO().execute(() -> mJournalDao.deleteEntry(journalEntry));
    }

    @Override
    public void deleteJournalEntry(int journalEntryId) {
        AppExecutors.getInstance().diskIO().execute(() -> mJournalDao.deleteEntry(journalEntryId));
    }
}
