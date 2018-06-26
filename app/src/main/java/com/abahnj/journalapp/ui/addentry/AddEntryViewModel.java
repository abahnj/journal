package com.abahnj.journalapp.ui.addentry;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.local.AppDatabase;

// TODO (5) Make this class extend ViewModel
public class AddEntryViewModel extends ViewModel {
    private LiveData<JournalEntry> journalEntry;
    public AddEntryViewModel(AppDatabase mDb, int mTaskId) {
        journalEntry = mDb.journalDao().loadEntryById(mTaskId);
    }

    public LiveData<JournalEntry> getJournalEntry() {
        return journalEntry;
    }
// TODO (6) Add a task member variable for the TaskEntry object wrapped in a LiveData

    // TODO (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId

    // TODO (7) Create a getter for the task variable
}
