package com.abahnj.journalapp.ui.main;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abahnj.journalapp.R;
import com.abahnj.journalapp.ui.main.EntriesFragment.OnListFragmentInteractionListener;
import com.abahnj.journalapp.data.JournalEntry;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link JournalEntry} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class JournalEntriesAdapter extends ListAdapter<JournalEntry, JournalEntriesAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;


    JournalEntriesAdapter(OnListFragmentInteractionListener listener) {
        super(DIFF_CALLBACK);
        mListener =listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_entries, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        JournalEntry journalEntry = getItem(position);
        holder.mItem = getItem(position);
        holder.mTitleView.setText(String.valueOf(journalEntry.getTitle()));
        holder.mDescriptionView.setText(journalEntry.getDescription());
        String dateFormat = "EEE, MMM d";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String date = sdf.format(journalEntry.getUpdatedAt()); // formats to 09/23/2009 13:53:28.238
        holder.mDateView.setText(date);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitleView;
        final TextView mDescriptionView;
        final TextView mDateView;
        JournalEntry mItem;
        final RelativeLayout viewBackground;
        final MaterialCardView viewForeground;


        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView =  view.findViewById(R.id.tv_overline);
            mDescriptionView =  view.findViewById(R.id.tv_description);
            mDateView = view.findViewById(R.id.tv_date_view);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }

    private static final DiffUtil.ItemCallback<JournalEntry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<JournalEntry>() {
                @Override
                public boolean areItemsTheSame(@NonNull JournalEntry journalEntry, @NonNull JournalEntry t1) {
                    return journalEntry.getId() == t1.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull JournalEntry journalEntry, @NonNull JournalEntry t1) {
                    return journalEntry.equals(t1);
                }
            };

    @Override
    protected JournalEntry getItem(int position) {
        return super.getItem(position);
    }

    public void removeItem(int position) {
        //cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(JournalEntry item, int position) {
        //cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
