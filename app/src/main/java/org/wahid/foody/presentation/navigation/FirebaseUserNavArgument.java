package org.wahid.foody.presentation.navigation;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FirebaseUserNavArgument implements Parcelable {
    String username;
    String email;
    String imageUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FirebaseUserNavArgument(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(imageUrl);
    }

    public static final Parcelable.Creator<FirebaseUserNavArgument> CREATOR
            = new Parcelable.Creator<FirebaseUserNavArgument>() {
        public FirebaseUserNavArgument createFromParcel(Parcel in) {
            return new FirebaseUserNavArgument(in);
        }

        public FirebaseUserNavArgument[] newArray(int size) {
            return new FirebaseUserNavArgument[size];
        }
    };


    private FirebaseUserNavArgument(Parcel in) {
        username = in.readString();
        email = in.readString();
        imageUrl = in.readString();
    }
}
