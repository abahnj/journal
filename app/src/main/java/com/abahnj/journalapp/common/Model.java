package com.abahnj.journalapp.common;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * A Base Model to be extended by other models to add ids.
 */

@IgnoreExtraProperties
public class Model {
    @Exclude
    public String mId;

    public <T extends Model> T withId(@NonNull final String id) {
        this.mId = id;
        return (T) this;
    }
}
