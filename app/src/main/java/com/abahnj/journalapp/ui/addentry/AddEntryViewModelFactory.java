package com.abahnj.journalapp.ui.addentry;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.abahnj.journalapp.data.source.JournalRepository;
import com.abahnj.journalapp.utilities.Injection;

public class AddEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    @SuppressLint("StaticFieldLeak")
    private static volatile AddEntryViewModelFactory INSTANCE;

    private final Application mApplication;

    private final JournalRepository mJournalRepository;

    public static AddEntryViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (AddEntryViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AddEntryViewModelFactory(application,
                            Injection.provideJournalRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }


    AddEntryViewModelFactory(Application application, JournalRepository repository) {
        mApplication = application;
        mJournalRepository = repository;
    }

    // Note: This can be reused with minor modifications
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddEntryViewModel(mApplication, mJournalRepository);
    }
}
