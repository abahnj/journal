package com.abahnj.journalapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.abahnj.journalapp.R;
import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.login.LoginActivity;
import com.abahnj.journalapp.ui.addentry.AddEntryActivity;
import com.abahnj.journalapp.utilities.AppUtils;
import com.abahnj.journalapp.utilities.AppUtils.AppStart;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


public class MainActivity extends AppCompatActivity implements EntriesFragment.OnListFragmentInteractionListener{

    private GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppStart appStart = AppUtils.checkAppStart(this,
                PreferenceManager.getDefaultSharedPreferences(this));

        googleSignInAccount = getIntent().getParcelableExtra("default");

       checkAppStart(appStart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab =  findViewById(R.id.fab_add_entry);
        fab.setOnClickListener(view -> {
            Intent addEntryIntent = new Intent(MainActivity.this, AddEntryActivity.class);
            startActivity(addEntryIntent);
        });
    }

    private void checkAppStart(AppStart appStart) {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        switch (appStart) {
            case NORMAL:
                if (googleSignInAccount == null) {
                    startActivity(loginActivity);
                }
                break;
            case FIRST_TIME:
                startActivity(loginActivity);
                break;
            case FIRST_TIME_VERSION:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(JournalEntry item) {
// Launch AddTaskActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
        intent.putExtra(AddEntryActivity.EXTRA_ENTRY_ID, item.getId());
        startActivity(intent);
    }
}
