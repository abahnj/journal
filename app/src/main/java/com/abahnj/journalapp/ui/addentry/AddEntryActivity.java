/*
* Copyright (C) 2016 The Android Open Source Project
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

package com.abahnj.journalapp.ui.addentry;


import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.arch.lifecycle.ViewModelProviders;
import com.abahnj.journalapp.R;
import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.local.AppDatabase;


public class AddEntryActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_ENTRY_ID = "extraEntryId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_ENTRY_ID = "instanceTaskId";

    private JournalEntry currentEntry;

    AddEntryViewModel viewModel = null;


    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_ENTRY_ID = -1;
    // Constant for logging
    private static final String TAG = AddEntryActivity.class.getSimpleName();
    // Fields for views
    EditText mTitleEditText;
    EditText mDescriptionEditText;
    Button mButton;

    private int mEntryId = DEFAULT_ENTRY_ID;

    // Member variable for the Database
    private AppDatabase mDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        initViews();
        obtainViewModel();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ENTRY_ID)) {
            mEntryId = savedInstanceState.getInt(INSTANCE_ENTRY_ID, DEFAULT_ENTRY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ENTRY_ID)) {
            viewModel.mIsNewEntry = false;
            mButton.setText(R.string.update_button);
            if (mEntryId == DEFAULT_ENTRY_ID) {
                // populate the UI
                mEntryId = intent.getIntExtra(EXTRA_ENTRY_ID, DEFAULT_ENTRY_ID);

                viewModel.start(mEntryId);

                // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getJournalEntry().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getJournalEntry().removeObserver(this);
                        currentEntry = journalEntry;
                        populateUI(journalEntry);
                        viewModel.dataLoading = false;
                    }
                });
            }
        } else {
            viewModel.mIsNewEntry = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void obtainViewModel() {
        // COMPLETED (9) Remove the logging and the call to loadTaskById, this is done in the ViewModel now
        // COMPLETED (10) Declare a AddTaskViewModelFactory using mDb and mTaskId
        AddEntryViewModelFactory factory = AddEntryViewModelFactory.getInstance(this.getApplication());
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        viewModel = ViewModelProviders.of(this, factory).get(AddEntryViewModel.class);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_ENTRY_ID, mEntryId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mTitleEditText = findViewById(R.id.editTextEntryTitle);
        //mTitleEditText.setEnabled(false);
        //mTitleEditText.setFocusable(false);
        mDescriptionEditText = findViewById(R.id.editTextEntryDescription);

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(view -> onSaveButtonClicked());
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param journalEntry the taskEntry to populate the UI
     */
    private void populateUI(JournalEntry journalEntry) {
        if (journalEntry == null) {
            return;
        }
        mTitleEditText.setText(journalEntry.getTitle());
        mDescriptionEditText.setText(journalEntry.getDescription());
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        if (currentEntry != null) {
            viewModel.saveEntry(currentEntry);
        } else {
        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        final JournalEntry journalEntry = new JournalEntry(title, description);
        viewModel.saveEntry(journalEntry);
        }
        finish();

/*
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (mEntryId == DEFAULT_ENTRY_ID) {
                // insert new journalEntry
                mDb.journalDao().insertEntry(journalEntry);
            } else {
                //update journalEntry
                journalEntry.setId(mEntryId);
                mDb.journalDao().updateEntry(journalEntry);
            }
            finish();
        });
*/
    }

}
