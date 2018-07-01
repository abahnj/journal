package com.abahnj.journalapp.data.source.local;/*
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


import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalDataSource;
import com.abahnj.journalapp.util.SingleExecutors;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for the {@link JournalDataSource}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class JournalLocalDataSourceTest {

    private final static String TITLE = "title";

    private final static String TITLE2 = "title2";

    private final static String TITLE3 = "title3";

    private JournalLocalDataSource mLocalDataSource;

    private AppDatabase mDatabase;

    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class)
                .build();
        JournalDao entriesDao = mDatabase.journalDao();

        // Make sure that we're not keeping a reference to the wrong instance.
        JournalLocalDataSource.clearInstance();
        mLocalDataSource = JournalLocalDataSource.getInstance(new SingleExecutors(), entriesDao);
    }

    @After
    public void cleanUp() {
        mDatabase.close();
        JournalLocalDataSource.clearInstance();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveEntry_retrievesEntry() {
        // Given a new entry
        final JournalEntry newEntry = new JournalEntry(TITLE, "", 1);

        // When saved into the persistent repository
        mLocalDataSource.saveJournalEntry(newEntry);

        // Then the entry can be retrieved from the persistent repository
        JournalEntry journalEntry = mLocalDataSource.getJournalEntry(newEntry.getId()).getValue();
        assertThat(journalEntry, is(newEntry));
    }
  
    @Test
    public void deleteAllEntries_emptyListOfRetrievedEntry() {
        // Given a new entry in the persistent repository and a mocked callback
        JournalEntry newEntry = new JournalEntry(TITLE, "");
        
        mLocalDataSource.saveJournalEntry(newEntry);

        // When all entries are deleted
        mLocalDataSource.deleteAllJournalEntries();

        // Then the retrieved entries is an empty list
        List<JournalEntry> journalEntries = mLocalDataSource.getJournalEntries().getValue();

        assert journalEntries != null;
        assertEquals(0, journalEntries.size());
    }

    @Test
    public void getEntries_retrieveSavedEntries() {
        // Given 2 new entries in the persistent repository
        final JournalEntry newEntry1 = new JournalEntry(TITLE, "", 1);
        mLocalDataSource.saveJournalEntry(newEntry1);
        final JournalEntry newEntry2 = new JournalEntry(TITLE, "", 2);
        mLocalDataSource.saveJournalEntry(newEntry2);

        // Then the entries can be retrieved from the persistent repository
        List<JournalEntry> journalEntries = mLocalDataSource.getJournalEntries().getValue();
        assertNotNull(journalEntries);
        assertTrue(journalEntries.size() >= 2);

        boolean newEntry1IdFound = false;
        boolean newEntry2IdFound = false;
        for (JournalEntry entry : journalEntries) {
            if (entry.getId() == newEntry1.getId()) {
                newEntry1IdFound = true;
            }
            if (entry.getId() == (newEntry2.getId())) {
                newEntry2IdFound = true;
            }
        }
        assertTrue(newEntry1IdFound);
        assertTrue(newEntry2IdFound);
    }
}
