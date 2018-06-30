package com.abahnj.journalapp.data.source.remote;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.abahnj.journalapp.common.QueryLiveData;
import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalDataSource;
import com.abahnj.journalapp.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class JournalRemoteDataSource implements JournalDataSource {
    private FirebaseFirestore firestore;

    private static JournalRemoteDataSource INSTANCE;

    private JournalRemoteDataSource(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public static JournalRemoteDataSource getInstance(FirebaseFirestore firestore) {
        if (INSTANCE == null) {
            INSTANCE = new JournalRemoteDataSource(firestore);
        }
        return INSTANCE;
    }

    private QueryLiveData<JournalEntry> entries() {
        return new QueryLiveData<>(toQuery(), JournalEntry.class);
    }

    private Query toQuery() {
        // Construct query basic query

        /* query could be limited like: query.limit(5) */
        return firestore.collection(Constants.FIRESTORE_JOURNAL);
    }


    public QueryLiveData<JournalEntry> getEntries() {
        return entries();
    }

    @Override
    public QueryLiveData<JournalEntry> getJournalEntries() {
        return getEntries();
    }

    @Override
    public LiveData<JournalEntry> getJournalEntry(int journalEntryId) {
        return null;
    }

    @Override
    public void saveJournalEntry(@NonNull JournalEntry journalEntry) {
        firestore.collection("journalEntries")
                .document(String.valueOf(journalEntry.hashCode()))
                .set(journalEntry);

    }

    @Override
    public void refreshJournalEntries() {

    }

    @Override
    public void deleteAllJournalEntries() {

    }

    @Override
    public void deleteJournalEntry(int journalEntryId) {

    }
}
