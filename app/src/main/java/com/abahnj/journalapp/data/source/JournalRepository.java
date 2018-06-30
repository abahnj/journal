package com.abahnj.journalapp.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.abahnj.journalapp.data.JournalEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class JournalRepository implements JournalDataSource {

    private volatile static JournalRepository INSTANCE = null;

    private final JournalDataSource mJournalRemoteDataSource;

    private final JournalDataSource mJournalLocalDataSource;




    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    //Map<String, JournalEntry> mCachedEntries;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private JournalRepository(@NonNull JournalDataSource journalRemoteDataSource,
                            @NonNull JournalDataSource journalLocalDataSource) {
        mJournalRemoteDataSource = checkNotNull(journalRemoteDataSource);
        mJournalLocalDataSource = checkNotNull(journalLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param journalRemoteDataSource the backend data source
     * @param journalLocalDataSource  the device storage data source
     * @return the {@link JournalRepository} instance
     */
    public static JournalRepository getInstance(JournalDataSource journalRemoteDataSource,
                                              JournalDataSource journalLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (JournalRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JournalRepository(journalRemoteDataSource, journalLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(JournalDataSource, JournalDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public LiveData getJournalEntries() {

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            return mJournalRemoteDataSource.getJournalEntries();
        } else {
            // Query the local storage if available. If not, query the network.
            return mJournalLocalDataSource.getJournalEntries();
        }
    }

    @Override
    public LiveData<JournalEntry> getJournalEntry(int journalEntryId) {
        if (mCacheIsDirty) {
            return mJournalRemoteDataSource.getJournalEntry(journalEntryId);
        } else {
            return mJournalLocalDataSource.getJournalEntry(journalEntryId);
        }

    }

    @Override
    public void saveJournalEntry(@NonNull JournalEntry journalEntry) {
        checkNotNull(journalEntry);
        mJournalLocalDataSource.saveJournalEntry(journalEntry);
        mJournalRemoteDataSource.saveJournalEntry(journalEntry);

    }

    @Override
    public void refreshJournalEntries() {

    }

    @Override
    public void deleteAllJournalEntries() {

    }

    @Override
    public void deleteJournalEntry(int journalEntryId) {
        mJournalLocalDataSource.deleteJournalEntry(journalEntryId);
    }
}
