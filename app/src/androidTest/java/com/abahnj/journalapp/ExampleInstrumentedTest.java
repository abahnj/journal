package com.abahnj.journalapp;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.abahnj.journalapp.data.source.local.AppDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.abahnj.journalapp", appContext.getPackageName());
    }

    @Test
    public void createDatabase(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppDatabase appDatabase = AppDatabase.getInstance(appContext);

        assertNotNull(appDatabase);
    }
}
