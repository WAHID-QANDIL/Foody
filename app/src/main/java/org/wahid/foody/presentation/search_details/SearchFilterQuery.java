package org.wahid.foody.presentation.search_details;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
public class SearchFilterQuery implements Parcelable {

    @Nullable public String category;
    @Nullable public String country;
    @Nullable public String ingredient;

    public SearchFilterQuery(String category, String country, String ingredient) {
        this.category = category;
        this.country = country;
        this.ingredient = ingredient;
    }

    protected SearchFilterQuery(Parcel in) {
        category = in.readString();
        country = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<SearchFilterQuery> CREATOR = new Creator<SearchFilterQuery>() {
        @Override
        public SearchFilterQuery createFromParcel(Parcel in) {
            return new SearchFilterQuery(in);
        }

        @Override
        public SearchFilterQuery[] newArray(int size) {
            return new SearchFilterQuery[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(country);
        dest.writeString(ingredient);
    }
}