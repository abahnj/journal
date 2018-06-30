package com.abahnj.journalapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.abahnj.journalapp.common.Model;
import com.google.android.gms.common.internal.Objects;
import com.google.common.base.Strings;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "journal")
public final class JournalEntry extends Model {
    @PrimaryKey(autoGenerate = true)
    private int id;


    @Ignore
    public JournalEntry() {
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    private String title;

    @Nullable
    private String description;

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {

        return updatedAt;
    }

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;
    /**
     * Use this constructor to create a new active JournalEntry.
     *
     * @param title       title of the entry
     * @param description description of the entry
     */
    @Ignore
    public JournalEntry(@Nullable String title, @Nullable String description) {
        this.title = title;
        this.description = description;
        this.updatedAt = Calendar.getInstance().getTime();
    }


    /**
     *
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

//    public <T extends JournalEntry> T withId(@NonNull final String id) {
//        this.id = Integer.valueOf(id);
//        return (T) this;
//    }
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
