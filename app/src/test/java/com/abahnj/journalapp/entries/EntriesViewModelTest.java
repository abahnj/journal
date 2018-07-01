package com.abahnj.journalapp.entries;/*
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
import android.content.res.Resources;

import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.JournalRepository;
import com.abahnj.journalapp.ui.main.MainViewModel;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link MainViewModel}
 */
public class EntriesViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private static List<JournalEntry> journalEntries;

    @Mock
    private JournalRepository mJournalRepository;

    @Mock
    private Application mContext;


    private MainViewModel viewModel;

    @Before
    public void setupTasksViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        setupContext();

        // Get a reference to the class under test
        viewModel = new MainViewModel(mContext, mJournalRepository);

        // We initialise the tasks to 3, with one active and two completed
        journalEntries = Lists.newArrayList(new JournalEntry("Title1", "Description1"),
                new JournalEntry("Title2", "Description2"), new JournalEntry("Title3", "Description3"));

    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);

        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void loadAllTasksFromRepository_dataLoaded() {
        // Given an initialized TasksViewModel with initialized tasks
        // When loading of Tasks is requested
        viewModel.getEntries();
        // And data loaded
        assertFalse(viewModel.getEntries().getValue().isEmpty());
        assertEquals(3, viewModel.getEntries().getValue().size());
    }


}
