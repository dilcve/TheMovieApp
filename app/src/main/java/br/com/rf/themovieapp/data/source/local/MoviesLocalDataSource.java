package br.com.rf.themovieapp.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import br.com.rf.themovieapp.data.source.MoviesDataSource;
import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;


public class MoviesLocalDataSource implements MoviesDataSource.LocalDataSource {

    private static MoviesLocalDataSource INSTANCE;

    private MoviesDbHelper mDbHelper;

    // Prevent direct instantiation.
    private MoviesLocalDataSource(Context context) {
        mDbHelper = new MoviesDbHelper(context);
    }

    public static MoviesLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(MoviesDataSource.LoadMoviesCallback callback, String page) {
        MoviesWrapper moviesWrapper = new MoviesWrapper();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_ENTRY_ID,
                MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_JSON,

        };

        Cursor c = db.query(
                MoviesPersistenceContract.MoviesEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String json = c.getString(c.getColumnIndexOrThrow(MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_JSON));

                moviesWrapper = new Gson().fromJson(json, MoviesWrapper.class);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (moviesWrapper.results.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onMoviesLoaded(moviesWrapper);
        }

    }

    @Override
    public void getMovieDetails(int movieId, MoviesDataSource.LoadMovieDetailsCallback callback) {
        MovieDetails movieDetails = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_ENTRY_ID,
                MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_JSON,

        };

        String where = MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_ENTRY_ID + "=?";
        String[] whereArgs = {String.valueOf(movieId)};

        Cursor c = db.query(
                MoviesPersistenceContract.MovieDetailsEntry.TABLE_NAME, projection, where, whereArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String json = c.getString(c.getColumnIndexOrThrow(MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_JSON));

                movieDetails = new Gson().fromJson(json, MovieDetails.class);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (movieDetails == null) {
            // This will be called if there is no data.
            callback.onDataNotAvailable();
        } else {
            callback.onMovieDetailLoaded(movieDetails);
        }
    }

    private MoviesWrapper getMovies() {
        MoviesWrapper moviesWrapper = new MoviesWrapper();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_ENTRY_ID,
                MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_JSON,

        };

        Cursor c = db.query(
                MoviesPersistenceContract.MoviesEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_ENTRY_ID));
                String json = c.getString(c.getColumnIndexOrThrow(MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_JSON));

                moviesWrapper = new Gson().fromJson(json, MoviesWrapper.class);
                moviesWrapper.page = Integer.parseInt(itemId);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return moviesWrapper;
    }

    @Override
    public void saveMovies(MoviesWrapper moviesWrapper) {
        MoviesWrapper localMovies = getMovies();
        deleteAllMovies();

        if (localMovies != null && localMovies.results != null && localMovies.results.size() > 0) {
            localMovies.results.addAll(moviesWrapper.results);
        } else {
            localMovies = new MoviesWrapper();
            localMovies.page = moviesWrapper.page;
            localMovies.results.addAll(moviesWrapper.results);

        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_ENTRY_ID, String.valueOf(localMovies.page));
        values.put(MoviesPersistenceContract.MoviesEntry.COLUMN_NAME_JSON, new Gson().toJson(localMovies));

        db.insert(MoviesPersistenceContract.MoviesEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void saveMovieDetails(MovieDetails movieDetails) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_ENTRY_ID, String.valueOf(movieDetails.id));
        values.put(MoviesPersistenceContract.MovieDetailsEntry.COLUMN_NAME_JSON, new Gson().toJson(movieDetails));

        db.insert(MoviesPersistenceContract.MovieDetailsEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void updateFavorite(int movieId, boolean add) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MovieFavoritesEntry.COLUMN_NAME_ENTRY_ID, String.valueOf(movieId));

        if (add) {
            db.insert(MoviesPersistenceContract.MovieFavoritesEntry.TABLE_NAME, null, values);
        } else {
            String where = MoviesPersistenceContract.MovieFavoritesEntry.COLUMN_NAME_ENTRY_ID + "=?";
            String[] whereArgs = {String.valueOf(movieId)};
            db.delete(MoviesPersistenceContract.MovieFavoritesEntry.TABLE_NAME, where, whereArgs);
        }

        db.close();

    }

    @Override
    public void checkFavorite(int movieId, MoviesDataSource.FavoritesCallback favoritesCallback) {
        boolean favorite = false;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MoviesPersistenceContract.MovieFavoritesEntry.COLUMN_NAME_ENTRY_ID
        };

        String where = MoviesPersistenceContract.MovieFavoritesEntry.COLUMN_NAME_ENTRY_ID + "=?";
        String[] whereArgs = {String.valueOf(movieId)};

        Cursor c = db.query(
                MoviesPersistenceContract.MovieFavoritesEntry.TABLE_NAME, projection, where, whereArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            favorite = true;
        }
        if (c != null) {
            c.close();
        }

        db.close();

        favoritesCallback.onCheckFavoirite(favorite);
    }

    public void deleteAllMovies() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(MoviesPersistenceContract.MoviesEntry.TABLE_NAME, null, null);

        db.close();
    }
}
