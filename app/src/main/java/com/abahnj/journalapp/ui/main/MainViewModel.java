package com.abahnj.journalapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import com.abahnj.journalapp.common.SnackbarMessage;
import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalRepository;
import com.abahnj.journalapp.utilities.AppExecutors;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private final LiveData<List<JournalEntry>> entries;
    private final MediatorLiveData<List<JournalEntry>> entryMediatorLiveData = new MediatorLiveData<>();

    private JournalRepository mRepository;

    public MainViewModel(Application application, JournalRepository repository) {
        super(application);
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        mRepository = repository;
        entries = mRepository.getJournalEntries();
    }

    public void deleteEntry(JournalEntry journalEntry) {
        mRepository.deleteJournalEntry(journalEntry);
    }
    public void deleteAllEntries() {
        mRepository.deleteAllJournalEntries();
    }
    public void addEntry (JournalEntry journalEntry) {
        mRepository.saveJournalEntry(journalEntry);
    }
    public LiveData<List<JournalEntry>> getEntries() {
        return entries;
    }
    public LiveData<List<JournalEntry>> getT() {
        return entryMediatorLiveData;
    }

}
