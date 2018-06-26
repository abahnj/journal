package com.abahnj.journalapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abahnj.journalapp.R;
import com.abahnj.journalapp.data.JournalEntry;
import com.abahnj.journalapp.data.source.local.AppDatabase;
import com.abahnj.journalapp.utilities.AppExecutors;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EntriesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = EntriesFragment.class.getSimpleName();
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView mRecyclerView;
    JournalEntriesAdapter mAdapter;
    private View mEmptyView;
    private ItemTouchHelper touchHelper;
    private OnListFragmentInteractionListener mListener;
    private AppDatabase mDb;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EntriesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EntriesFragment newInstance(int columnCount) {
        EntriesFragment fragment = new EntriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = rootView.findViewById(R.id.journal_entry_list);
        mEmptyView = rootView.findViewById(R.id.main_activity_empty_view);
        mDb = AppDatabase.getInstance(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Set the adapter
        mAdapter = new JournalEntriesAdapter(mListener);
        mRecyclerView.setAdapter(mAdapter);

        touchHelper = setupTouchHelper();
        touchHelper.attachToRecyclerView(mRecyclerView);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), HORIZONTAL);
        mRecyclerView.addItemDecoration(decoration);

        setupViewModel();
        return rootView;
    }

    private ItemTouchHelper setupTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {

                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        JournalEntry journalEntry = mAdapter.getItem(position);
                        mDb.journalDao().deleteEntry(journalEntry);
                    }
                });
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(JournalEntry item);
    }

   Observer<List<JournalEntry>> observer = new Observer<List<JournalEntry>>() {
        @Override
        public void onChanged(@Nullable List<JournalEntry> journalEntries) {
            if (journalEntries.size() > 0){
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mEmptyView.setVisibility(View.GONE);
                mAdapter.submitList(journalEntries);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }
    };
    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.getEntries().observe(this, observer);
    }
}
