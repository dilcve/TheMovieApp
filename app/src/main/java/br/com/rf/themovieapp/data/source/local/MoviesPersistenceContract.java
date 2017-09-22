package br.com.rf.themovieapp.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save locally.
 */
public final class MoviesPersistenceContract {

    private MoviesPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_JSON = "json";

    }

    public static abstract class MovieDetailsEntry implements BaseColumns {
        public static final String TABLE_NAME = "movieDetail";
        public static final String COLUMN_NAME_ENTRY_ID = "movieid";
        public static final String COLUMN_NAME_JSON = "json";

    }

    public static abstract class MovieFavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movieFavorite";
        public static final String COLUMN_NAME_ENTRY_ID = "movieid";
    }
}
