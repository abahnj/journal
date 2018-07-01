package com.abahnj.journalapp.addeditentry;/*
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



import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalRepository;
import com.abahnj.journalapp.ui.addentry.AddEntryViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for the implementation of {@link AddEntryViewModel}.
 */
public class AddEditEntryViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private JournalRepository mJournalRepository;


    private AddEntryViewModel mAddEntryViewModel;

    @Before
    public void setupAddEditTaskViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAddEntryViewModel = new AddEntryViewModel(
                mock(Application.class), mJournalRepository);
    }

    @Test
    public void saveNewTaskToRepository() {
        // When the ViewModel is asked to save a task
        mAddEntryViewModel.saveEntry(new JournalEntry("TITLE", "DESCRIPTION"));

        // Then a task is saved in the repository and the view updated
        verify(mJournalRepository).saveJournalEntry(any(JournalEntry.class)); // saved to the model
    }

    @Test
    public void populateTask_callsRepoAndUpdatesView() {
        JournalEntry journalEntry = new JournalEntry("TITLE", "DESCRIPTION", 1);



        // Get a reference to the class under test
        mAddEntryViewModel = new AddEntryViewModel(
                mock(Application.class), mJournalRepository);


        // When the ViewModel is asked to populate an existing task
        mAddEntryViewModel.start(journalEntry.getId());

        // Then the task repository is queried and the view updated
        verify(mJournalRepository).getJournalEntry(eq(journalEntry.getId()));

    }
}
