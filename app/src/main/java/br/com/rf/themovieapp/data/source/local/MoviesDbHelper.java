package br.com.rf.themovieapp.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Movies.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String SQL_CREATE_MOVIES =
            "CREATE TABLE " + MoviesPersistenceContract.MoviesEntry.TABLE_NAME + " (" +
                    MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_JSON + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_MOVIE_DETAILS =
            "CREATE TABLE " + MoviesPersistenceContract.MovieDetailsEntry.TABLE_NAME + " (" +
                    MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_JSON + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_MOVIE_FAVORITES =
            "CREATE TABLE " + MoviesPersistenceContract.MovieFavoritesEntry.TABLE_NAME + " (" +
                    MoviesPersistenceContract.MovieFavoritesEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY" +
                    " )";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIES);
        db.execSQL(SQL_CREATE_MOVIE_DETAILS);
        db.execSQL(SQL_CREATE_MOVIE_FAVORITES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
