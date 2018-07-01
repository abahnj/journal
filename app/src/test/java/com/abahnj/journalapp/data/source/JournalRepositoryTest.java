package com.abahnj.journalapp.data.source;/*
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


import android.arch.lifecycle.Observer;

import com.abahnj.journalapp.TestUtils;
import com.abahnj.journalapp.data.JournalEntry;
import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
@RunWith(MockitoJUnitRunner.class)
public class JournalRepositoryTest {

    private final static String ENTRY_TITLE = "title";

    private final static String ENTRY_TITLE2 = "title2";

    private final static String ENTRY_TITLE3 = "title3";

    private static List<JournalEntry> ENTRIES = Lists.newArrayList(new JournalEntry("Title1", "Description1"),
            new JournalEntry("Title2", "Description2"));

    private JournalRepository mEntriesRepository;
    

    @Mock
    private JournalDataSource mEntriesRemoteDataSource;

    @Mock
    private JournalDataSource mEntriesLocalDataSource;
    
    @Before
    public void setupEntriesRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mEntriesRepository = JournalRepository.getInstance(
                mEntriesRemoteDataSource, mEntriesLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        JournalRepository.destroyInstance();
    }

    @Test
    public void getEntries_repositoryCachesAfterFirstApiCall() {
        Observer<List<JournalEntry>> observer = mock(Observer.class);
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the entries repository
        twoEntriesLoadCallsToRepository();

        // Then entries were only requested once from Service API
        mEntriesRemoteDataSource.getJournalEntries().observe(TestUtils.TEST_OBSERVER, observer);
    }

   
    @Test
    public void getEntry_requestsSingleEntryFromLocalDataSource() {
        // When a entry is requested from the entries repository
        mEntriesRepository.getJournalEntry(1);

        // Then the entry is loaded from the database
        verify(mEntriesLocalDataSource).getJournalEntry(eq(1));
    }

  

    @Test
    public void deleteAllEntries_deleteEntriesToServiceAPIUpdatesCache() {
        // Given 2 stub completed entries and 1 stub active entries in the repository
        JournalEntry newEntry = new JournalEntry(ENTRY_TITLE, "Some Entry Description");
        mEntriesRepository.saveJournalEntry(newEntry);
        JournalEntry newEntry2 = new JournalEntry(ENTRY_TITLE2, "Some Entry Description");
        mEntriesRepository.saveJournalEntry(newEntry2);
        JournalEntry newEntry3 = new JournalEntry(ENTRY_TITLE3, "Some Entry Description");
        mEntriesRepository.saveJournalEntry(newEntry3);

        // When all entries are deleted to the entries repository
        mEntriesRepository.deleteAllJournalEntries();

        // Verify the data sources were called
        verify(mEntriesRemoteDataSource).deleteAllJournalEntries();
        verify(mEntriesLocalDataSource).deleteAllJournalEntries();

    }

   
    /**
     * Convenience method that issues two calls to the entries repository
     */
    private void twoEntriesLoadCallsToRepository() {
        // When entries are requested from repository
        mEntriesRepository.getJournalEntries(); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mEntriesLocalDataSource).getJournalEntries();
        

        // Verify the remote data source is queried
        verify(mEntriesRemoteDataSource).getJournalEntries();
        
    }
    
}
