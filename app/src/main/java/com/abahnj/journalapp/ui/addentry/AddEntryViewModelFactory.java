package com.abahnj.journalapp.ui.addentry;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.abahnj.journalapp.data.source.local.AppDatabase;

// TODO (1) Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class AddEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDb;

    private final int mTaskId;

    public AddEntryViewModelFactory(AppDatabase mDb, int taskId) {
        this.mDb = mDb;
        this.mTaskId = taskId;
    }

// TODO (2) Add two member variables. One for the database and one for the taskId

    // TODO (3) Initialize the member variables in the constructor with the parameters received

    // TODO (4) Uncomment the following method
    // Note: This can be reused with minor modifications
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddEntryViewModel(mDb, mTaskId);
    }
}
