package com.abahnj.journalapp.data;/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.abahnj.journalapp.data.source.JournalDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class FakeJournalRemoteDataSource implements JournalDataSource {

    MutableLiveData mutableLiveData = new MutableLiveData();


    private static FakeJournalRemoteDataSource INSTANCE;

    private static final Map<Integer, JournalEntry> JOURNAL_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeJournalRemoteDataSource() {}

    public static FakeJournalRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeJournalRemoteDataSource();
        }
        return INSTANCE;
    }


    @VisibleForTesting
    public void addEntries(JournalEntry... journalEntries) {
        if (journalEntries != null) {
            for (JournalEntry journalEntry : journalEntries) {
                JOURNAL_SERVICE_DATA.put(journalEntry.getId(), journalEntry);
            }
        }
    }

    @Override
    public <T extends LiveData> T getJournalEntries() {
         mutableLiveData.setValue(Lists.newArrayList(JOURNAL_SERVICE_DATA.values()));

         return (T) mutableLiveData;
    }

    @Override
    public LiveData<JournalEntry> getJournalEntry(int journalEntryId) {
        JournalEntry journalEntry = JOURNAL_SERVICE_DATA.get(journalEntryId);
        mutableLiveData.setValue(journalEntry);

        return mutableLiveData;
    }

    @Override
    public void saveJournalEntry(@NonNull JournalEntry journalEntry) {
        JOURNAL_SERVICE_DATA.put(journalEntry.getId(), journalEntry);
    }

    @Override
    public void refreshJournalEntries() {

    }

    @Override
    public void deleteAllJournalEntries() {
        JOURNAL_SERVICE_DATA.clear();
    }

    @Override
    public void deleteJournalEntry(JournalEntry journalEntry) {
        JOURNAL_SERVICE_DATA.remove(journalEntry.getId());
    }

}
