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


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.abahnj.journalapp.R;
import com.abahnj.journalapp.TestUtils;
import com.abahnj.journalapp.ui.main.MainActivity;
import com.abahnj.journalapp.ui.main.MainViewModelFactory;
import com.abahnj.journalapp.utilities.Injection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.abahnj.journalapp.TestUtils.getCurrentActivity;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests for the entries screen, the main screen which contains a list of all entries.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EntriesScreenTest {

    private final static String TITLE1 = "TITLE1";

    private final static String DESCRIPTION = "DESCR";

    private final static String TITLE2 = "TITLE2";

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mEntriesActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void resetState() {
        MainViewModelFactory.destroyInstance();
        Injection.provideJournalRepository(InstrumentationRegistry.getTargetContext())
                .deleteAllJournalEntries();
    }

    /**
     * A custom {@link Matcher} which matches an item in a {@link ListView} by its text.
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link ListView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Test
    public void clickAddEntryButton_opensAddEntryUi() {
        // Click on the add entry button
        onView(withId(R.id.fab_add_entry)).perform(click());

        // Check if the add entry screen is displayed
        onView(withId(R.id.editTextEntryDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void editEntry() throws Exception {
        // First add a entry
        createEntry(TITLE1, DESCRIPTION);

        // Click on the entry on the list
        onView(withText(TITLE1)).perform(click());


        String editEntryTitle = TITLE2;
        String editEntryDescription = "New Description";

        // Edit entry title and description
        onView(withId(R.id.editTextEntryTitle))
                .perform(replaceText(editEntryTitle), closeSoftKeyboard()); // Type new entry title
        onView(withId(R.id.editTextEntryDescription)).perform(replaceText(editEntryDescription),
                closeSoftKeyboard()); // Type new entry description and close the keyboard

        // Save the entry
        onView(withId(R.id.saveButton)).perform(click());

        // Verify entry is displayed on screen in the entry list.
        onView(withItemText(editEntryTitle)).check(matches(isDisplayed()));

        // Verify previous entry is not displayed
        onView(withItemText(TITLE1)).check(doesNotExist());
    }

    @Test
    public void addEntryToEntriesList() throws Exception {
        createEntry(TITLE1, DESCRIPTION);

        // Verify entry is displayed on screen
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
    }




    @Test
    public void showAllEntries() {
        // Add 2 active entries
        createEntry(TITLE1, DESCRIPTION);
        createEntry(TITLE2, DESCRIPTION);

        
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        onView(withItemText(TITLE2)).check(matches(isDisplayed()));
    }




    @Test
    public void createOneEntry_deleteEntry() {
        // Add active entry
        createEntry(TITLE1, DESCRIPTION);

        // Click delete entry in menu
        onView(withId(R.id.journal_entry_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        
        onView(withText(TITLE1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void createTwoEntries_deleteOneEntry() {
        // Add 2 active entries
        createEntry(TITLE1, DESCRIPTION);
        createEntry(TITLE2, DESCRIPTION);

        // Swipe to delete entry
        onView(withId(R.id.journal_entry_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, swipeLeft()));

        // Verify only one entry was deleted
        onView(withText(TITLE1)).check(matches(isDisplayed()));
        onView(withText(TITLE2)).check(doesNotExist());
    }
    

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    public void orientationChange_DuringEdit_ChangePersists() throws Throwable {
        // Add a completed entry
        createEntry(TITLE1, DESCRIPTION);

        // Open the entry in details view
        onView(withText(TITLE1)).perform(click());

        // Change entry title (but don't save)
        onView(withId(R.id.editTextEntryTitle))
                .perform(replaceText(TITLE2), closeSoftKeyboard()); // Type new entry title

        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        // Verify entry title is restored
        onView(withId(R.id.editTextEntryTitle)).check(matches(withText(TITLE2)));
    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    public void orientationChange_DuringEdit_NoDuplicate() throws IllegalStateException {
        // Add a completed entry
        createEntry(TITLE1, DESCRIPTION);

        // Open the entry in details view
        onView(withText(TITLE1)).perform(click());

        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        // Edit entry title and description
        onView(withId(R.id.editTextEntryTitle))
                .perform(replaceText(TITLE2), closeSoftKeyboard()); // Type new entry title
        onView(withId(R.id.editTextEntryDescription)).perform(replaceText(DESCRIPTION),
                closeSoftKeyboard()); // Type new entry description and close the keyboard

        // Save the entry
        onView(withId(R.id.saveButton)).perform(click());

        // Verify entry is displayed on screen in the entry list.
        onView(withItemText(TITLE2)).check(matches(isDisplayed()));

        // Verify previous entry is not displayed
        onView(withItemText(TITLE1)).check(doesNotExist());
    }
    
    @Test
    public void noEntries_EmptyViewVisible() {
        // Given an empty list of entries, make sure "All entries" filter is on

        // Add entry View should be displayed
        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
    }
    

    private void createEntry(String title, String description) {
        // Click on the add entry button
        onView(withId(R.id.fab_add_entry)).perform(click());

        // Add entry title and description
        onView(withId(R.id.editTextEntryTitle)).perform(typeText(title),
                closeSoftKeyboard()); // Type new entry title
        onView(withId(R.id.editTextEntryDescription)).perform(typeText(description),
                closeSoftKeyboard()); // Type new entry description and close the keyboard

        // Save the entry
        onView(withId(R.id.saveButton)).perform(click());
    }


    private String getText(int stringId) {
        return mEntriesActivityTestRule.getActivity().getResources().getString(stringId);
    }

    private String getToolbarNavigationContentDescription() {
        return TestUtils.getToolbarNavigationContentDescription(
                mEntriesActivityTestRule.getActivity(), R.id.toolbar);
    }
}
