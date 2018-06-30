/*
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

package com.abahnj.journalapp.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.abahnj.journalapp.data.JournalEntry;

import java.util.List;


/**
 * Main entry point for accessing data.
 */
public interface JournalDataSource {


    <T extends LiveData> T getJournalEntries();

    LiveData<JournalEntry> getJournalEntry(int journalEntryId);

    void saveJournalEntry(@NonNull JournalEntry journalEntry);

    void refreshJournalEntries();

    void deleteAllJournalEntries();

    void deleteJournalEntry(int journalEntryId);
}
