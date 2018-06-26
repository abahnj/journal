package com.abahnj.journalapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.local.AppDatabase;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<JournalEntry>> tasks;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.journalDao().loadAllEntries();
    }

    public LiveData<List<JournalEntry>> getEntries() {
        return tasks;
    }
}
