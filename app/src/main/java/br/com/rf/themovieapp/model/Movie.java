package br.com.rf.themovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Movie implements Parcelable {

    public int id;
    public double vote_average;
    public String title;
    public String poster_path;
    public String backdrop_path;
    public String overview;
    public String release_date;

    static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    static final String IMG_BACKDROP_SIZE_MEDIUM = "w780";
    static final String IMG_POSTER_SIZE_MEDIUM = "w342";

    public Movie() {
    }

    public String getBackdropUrl() {
        if (TextUtils.isEmpty(backdrop_path)) {
            return "";
        }
        return BASE_IMAGE_URL + IMG_BACKDROP_SIZE_MEDIUM + "/" + backdrop_path;
    }

    public String getPosterUrl() {
        if (TextUtils.isEmpty(backdrop_path)) {
            return "";
        }
        return BASE_IMAGE_URL + IMG_POSTER_SIZE_MEDIUM + "/" + poster_path;
    }

    public String getRate() {
        if (vote_average == 0) {
            return "-";
        }
        return String.valueOf(vote_average);
    }

    public String getReleaseDate() {
        return release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.vote_average = in.readDouble();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
