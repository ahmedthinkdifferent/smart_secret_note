package com.thinkdifferent.noteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * model class for note item data.
 */

public class Note implements Parcelable {
    public int id, type;
    public String title, subject, lastUpdate, backgroundColor;

    public Note(int id, String title, String subject, int type, String lastUpdate, String backgroundColor) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.lastUpdate = lastUpdate;
        this.backgroundColor = backgroundColor;
    }


    public Note(String title, String subject, int type,
                String lastUpdate, String backgroundColor) {
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.lastUpdate = lastUpdate;
        this.backgroundColor = backgroundColor;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        type = in.readInt();
        title = in.readString();
        subject = in.readString();
        lastUpdate = in.readString();
        backgroundColor = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(type);
        parcel.writeString(title);
        parcel.writeString(subject);
        parcel.writeString(lastUpdate);
        parcel.writeString(backgroundColor);
    }
}
