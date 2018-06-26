package com.abahnj.journalapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.internal.Objects;
import com.google.common.base.Strings;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "journal")
public final class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Nullable
    private final String title;

    @Nullable
    private final String description;

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {

        return updatedAt;
    }

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;
    /**
     * Use this constructor to create a new active Task.
     *
     * @param title       title of the task
     * @param description description of the task
     */
    @Ignore
    public JournalEntry(@Nullable String title, @Nullable String description) {
        this.title = title;
        this.description = description;
        this.updatedAt = Calendar.getInstance().getTime();
    }


    /**
     * Use this constructor to specify a completed Task if the Task already has an id (copy of
     * another Task).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     */
    public JournalEntry(@Nullable String title, @Nullable String description,
                 int id) {
        this(title, description);
        this.id = id;
        this.updatedAt = Calendar.getInstance().getTime();
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(title)) {
            return title;
        } else {
            return description;
        }
    }

    @Nullable
    public String getDescription() {
        return description;
    }


    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntry entry = (JournalEntry) o;
        return Objects.equal(id, entry.id) &&
                Objects.equal(title, entry.title) &&
                Objects.equal(description, entry.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, description);
    }

    @Override
    public String toString() {
        return "Entry with title " + title;
    }
}
