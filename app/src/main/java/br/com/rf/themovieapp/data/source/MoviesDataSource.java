package br.com.rf.themovieapp.data.source;

import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;

public interface MoviesDataSource {

    interface RemoteDataSource {
        void getMovies(LoadMoviesCallback callback, String page);

        void getMoreMovies(LoadMoviesCallback callback, String page);

        void getMovieDetails(int movieId, LoadMovieDetailsCallback callback);
    }

    interface LocalDataSource {

        void getMovies(LoadMoviesCallback callback, String page);

        void getMovieDetails(int movieId, LoadMovieDetailsCallback callback);

        void saveMovies(MoviesWrapper moviesWrapper);

        void saveMovieDetails(MovieDetails movieDetails);

        void updateFavorite(int movieId, boolean add);

        void checkFavorite(int movieId, FavoritesCallback favoritesCallback);
    }

    interface LoadMoviesCallback {

        void onMoviesLoaded(MoviesWrapper moviesWrapper);

        void onDataNotAvailable();
    }

    interface LoadMovieDetailsCallback {

        void onMovieDetailLoaded(MovieDetails movieDetails);

        void onDataNotAvailable();
    }

    interface FavoritesCallback {
        void onCheckFavoirite(boolean favorite);
    }


}
