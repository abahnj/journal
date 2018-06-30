package com.abahnj.journalapp.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.abahnj.journalapp.data.source.JournalRepository;
import com.abahnj.journalapp.ui.addentry.AddEntryViewModel;
import com.abahnj.journalapp.utilities.Injection;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    @SuppressLint("StaticFieldLeak")
    private static volatile MainViewModelFactory INSTANCE;

    private final Application mApplication;

    private final JournalRepository mJournalRepository;

    public static MainViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (MainViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainViewModelFactory(application,
                            Injection.provideJournalRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }


    private MainViewModelFactory(Application application, JournalRepository repository) {
        mApplication = application;
        mJournalRepository = repository;
    }

    // Note: This can be reused with minor modifications
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mApplication, mJournalRepository);
    }
}
