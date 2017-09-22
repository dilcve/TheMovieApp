package br.com.rf.themovieapp.model;

import android.os.Parcel;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends Movie {

    public List<Values> genres = new ArrayList<>();
    public String homepage;
    public List<Values> production_companies = new ArrayList<>();
    public int runtime;
    public String tagline;
    public int vote_count;
    public String status;

    public class Values {
        public String id;
        public String name;
    }

    public MovieDetails() {
    }

    public String getTagline() {
        if (TextUtils.isEmpty(tagline)) {
            return "";
        }
        return tagline;
    }

    public String getHomepage() {
        if (TextUtils.isEmpty(homepage)) {
            return "";
        }
        return homepage;
    }

    public String getStatus() {
        if (TextUtils.isEmpty(status)) {
            return "";
        }
        return status;
    }

    public String getRuntime() {
        return runtime > 0 ? "(" + runtime + " min)" : "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.genres);
        dest.writeString(this.homepage);
        dest.writeList(this.production_companies);
        dest.writeInt(this.runtime);
        dest.writeString(this.tagline);
        dest.writeInt(this.vote_count);
        dest.writeString(this.status);
    }

    protected MovieDetails(Parcel in) {
        super(in);
        this.genres = new ArrayList<>();
        in.readList(this.genres, Values.class.getClassLoader());
        this.homepage = in.readString();
        this.production_companies = new ArrayList<>();
        in.readList(this.production_companies, Values.class.getClassLoader());
        this.runtime = in.readInt();
        this.tagline = in.readString();
        this.vote_count = in.readInt();
        this.status = in.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel source) {
            return new MovieDetails(source);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
}
