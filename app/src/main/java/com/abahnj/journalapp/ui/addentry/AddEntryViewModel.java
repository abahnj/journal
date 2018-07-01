package com.abahnj.journalapp.ui.addentry;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.abahnj.journalapp.common.SingleLiveEvent;
import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalRepository;

public class AddEntryViewModel extends AndroidViewModel {
    private final SingleLiveEvent<Void> mEntryUpdated = new SingleLiveEvent<>();
    private final JournalRepository mJournalRepository;
    public Boolean dataLoading = false;
    private LiveData<JournalEntry> journalEntry;
    private int mJournalId;
    public boolean mIsNewEntry = true;
    private boolean mIsDataLoaded = false;

    public AddEntryViewModel(Application context, JournalRepository journalRepository) {
        super(context);
        mJournalRepository = journalRepository;
    }

    public LiveData<JournalEntry> getJournalEntry() {
        return journalEntry;
    }

    public void start(int journalId) {
        if (dataLoading) {
            // Already loading, ignore.
            return;
        }
        mJournalId = journalId;
        if (journalId == -1) {
            // No need to populate, it's a new task
            mIsNewEntry = true;
            return;
        }
        if (mIsDataLoaded) {
            // No need to populate, already have data.
            return;
        }

       journalEntry = mJournalRepository.getJournalEntry(journalId);
    }

    // Called when clicking on fab.
    public void saveEntry(JournalEntry entry) {
        if (entry.isEmpty()) {
            return;
        }
        if (isNewEntry() ) {
            createEntry(entry);
        } else {
            updateEntry(entry);
        }
    }

    private boolean isNewEntry() {
        return mIsNewEntry;
    }

    SingleLiveEvent<Void> getEntryUpdatedEvent() {
        return mEntryUpdated;
    }

    private void createEntry(JournalEntry newEntry) {
        mJournalRepository.saveJournalEntry(newEntry);
        mEntryUpdated.call();
    }

    private void updateEntry(JournalEntry entry) {
        if (isNewEntry()) {
            throw new RuntimeException("updateEntry() was called but task is new.");
        }
        mJournalRepository.updateJournalEntry(entry);
        mEntryUpdated.call();
    }
}
