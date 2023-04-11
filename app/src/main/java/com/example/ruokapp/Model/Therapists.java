package com.example.ruokapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Therapists implements Parcelable {

    private String name, username, password, therapistId;
    private Long rating;

    public Therapists() {
    }

    protected Therapists(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        therapistId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<Therapists> CREATOR = new Creator<Therapists>() {
        @Override
        public Therapists createFromParcel(Parcel in) {
            return new Therapists(in);
        }

        @Override
        public Therapists[] newArray(int size) {
            return new Therapists[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(therapistId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
    }
}
