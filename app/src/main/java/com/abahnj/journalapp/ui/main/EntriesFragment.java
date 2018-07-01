package com.abahnj.journalapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.abahnj.journalapp.data.source.JournalRepository;
import com.abahnj.journalapp.data.source.local.AppDatabase;
import com.abahnj.journalapp.utilities.AppExecutors;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EntriesFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = EntriesFragment.class.getSimpleName();
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView mRecyclerView;
    JournalEntriesAdapter mAdapter;
    private View mEmptyView;
    private OnListFragmentInteractionListener mListener;
    private JournalRepository repository;
    private MainViewModel viewModel;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = rootView.findViewById(R.id.journal_entry_list);
        mEmptyView = rootView.findViewById(R.id.main_activity_empty_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Set the adapter
        mAdapter = new JournalEntriesAdapter(mListener);
        mRecyclerView.setAdapter(mAdapter);

        setupTouchHelper();

        setupViewModel();
        return rootView;
    }

    private void setupTouchHelper() {
           /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);


    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof JournalEntriesAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mAdapter.getItem(viewHolder.getAdapterPosition()).getTitleForList();

            // backup of removed item for undo purpose
            final JournalEntry deletedItem = mAdapter.getItem(viewHolder.getAdapterPosition());
            viewModel.deleteEntry(deletedItem);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(getView(), name + " removed!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {
                // undo is selected, restore the deleted item
                viewModel.addEntry(deletedItem);
            });
            snackbar.setActionTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

        }
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
        void onListFragmentInteraction(JournalEntry item);
    }

    private void setupViewModel() {
        MainViewModelFactory factory = MainViewModelFactory.getInstance(getActivity().getApplication());

        viewModel = ViewModelProviders.of(getActivity(), factory).get(MainViewModel.class);
        viewModel.getEntries().observe(this, journalEntries -> {
            if (checkNotNull(journalEntries).size() > 0){
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mEmptyView.setVisibility(View.GONE);
                mAdapter.submitList(journalEntries);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}
