package com.abahnj.journalapp.data.source.local;/*
 * Copyright 2017, The Android Open Source Project
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


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.abahnj.journalapp.TestUtils;
import com.abahnj.journalapp.data.JournalEntry;
import com.airbnb.lottie.L;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class JournalDaoTest {

    private static final JournalEntry JOURNAL_ENTRY = new JournalEntry("title", "description");

    private AppDatabase mDatabase;

    @Before
    public void initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void insertEntryReplacesOnConflict() {
        //Given that a journalEntry is inserted
        mDatabase.journalDao().insertEntry(JOURNAL_ENTRY);

        // When a journalEntry with the same id is inserted
        JournalEntry newEntry = new JournalEntry("title2", "description2", 1);
        mDatabase.journalDao().insertEntry(newEntry);
        // When getting the journalEntry by id from the database
        JournalEntry loaded = mDatabase.journalDao().loadEntryById(JOURNAL_ENTRY.getId()).getValue();

        // The loaded data contains the expected values
        assertEntry(loaded, "title2", "description2");
    }

    @Test
    public void insertEntryAndGetEntries() {
        Observer<List<JournalEntry>> observer = mock(Observer.class);

        // When inserting a journalEntry
        mDatabase.journalDao().insertEntry(JOURNAL_ENTRY);

        // When getting the entries from the database
        LiveData<List<JournalEntry>> entries = mDatabase.journalDao().loadAllEntries();

        entries.observe(TestUtils.TEST_OBSERVER, observer);

        List<JournalEntry> entryList= new ArrayList<>();
        entryList.add(JOURNAL_ENTRY);

        verify(observer).onChanged(anyList());

        // There is only 1 journalEntry in the database
        //assertThat(entries.size(), is(1));
        // The loaded data contains the expected values
        //assertEntry(entries.get(0), "title", "description");
    }

    @Test
    public void updateEntryAndGetById() {
        // When inserting a journalEntry
        mDatabase.journalDao().insertEntry(JOURNAL_ENTRY);

        // When the journalEntry is updated
        JournalEntry updatedEntry = new JournalEntry("title2", "description2", 1);
        mDatabase.journalDao().updateEntry(updatedEntry);

        // When getting the journalEntry by id from the database
        JournalEntry loaded = mDatabase.journalDao().loadEntryById(1).getValue();

        // The loaded data contains the expected values
        assertEntry(loaded, "title2", "description2");
    }

    
    @Test
    public void deleteEntryByIdAndGettingEntries() {
        //Given a journalEntry inserted
        mDatabase.journalDao().insertEntry(JOURNAL_ENTRY);

        //When deleting a journalEntry by id
        mDatabase.journalDao().deleteEntry(JOURNAL_ENTRY.getId());

        //When getting the entries
        List<JournalEntry> entries = mDatabase.journalDao().loadAllEntries().getValue();
        // The list is empty
        assertThat(entries.size(), is(0));
    }

    @Test
    public void deleteEntriesAndGettingEntries() {
        //Given a journalEntry inserted
        mDatabase.journalDao().insertEntry(JOURNAL_ENTRY);

        //When deleting all entries
        mDatabase.journalDao().deleteAllEntries();

        //When getting the entries
        List<JournalEntry> entries = mDatabase.journalDao().loadAllEntries().getValue();
        // The list is empty
        assertThat(entries.size(), is(0));
    }



    private void assertEntry(JournalEntry entry, String title,
                             String description) {
        assertThat(entry, notNullValue());
        assertThat(entry.getTitle(), is(title));
        assertThat(entry.getDescription(), is(description));
    }
}
