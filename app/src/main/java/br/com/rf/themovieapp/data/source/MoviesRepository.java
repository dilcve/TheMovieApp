package br.com.rf.themovieapp.data.source;

import android.content.Context;

import br.com.rf.themovieapp.data.source.local.MoviesLocalDataSource;
import br.com.rf.themovieapp.data.source.remote.MoviesRemoteDataSource;
import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;

public class MoviesRepository implements MoviesDataSource.RemoteDataSource, MoviesDataSource.LocalDataSource {

    private static MoviesRepository INSTANCE = null;

    private final MoviesDataSource.RemoteDataSource mMoviesRemoteDataSource;
    private final MoviesDataSource.LocalDataSource mMoviesLocalDataSource;

    // Prevent direct instantiation.
    private MoviesRepository(MoviesDataSource.RemoteDataSource moviesRemoteDataSource, MoviesDataSource.LocalDataSource moviesLocalDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesLocalDataSource = moviesLocalDataSource;
    }

    public static MoviesRepository getInstance(MoviesDataSource.RemoteDataSource moviesRemoteDataSource, MoviesDataSource.LocalDataSource moviesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesRemoteDataSource, moviesLocalDataSource);
        }
        return INSTANCE;
    }

    public static MoviesRepository provideMoviesRepository(Context context) {
        return MoviesRepository.getInstance(MoviesRemoteDataSource.getInstance(),
                MoviesLocalDataSource.getInstance(context));
    }

    @Override
    public void getMovies(final MoviesDataSource.LoadMoviesCallback callback, String page) {
        getMoviesFromLocalDataSource(callback, page);
    }

    @Override
    public void getMoreMovies(MoviesDataSource.LoadMoviesCallback callback, String page) {
        getMoviesFromRemoteDataSource(callback, page);
    }

    @Override
    public void getMovieDetails(int movieId, MoviesDataSource.LoadMovieDetailsCallback callback) {
        getMovieDetailsFromLocalDataSource(movieId, callback);
    }

    @Override
    public void saveMovieDetails(MovieDetails movieDetails) {
        mMoviesLocalDataSource.saveMovieDetails(movieDetails);
    }

    @Override
    public void updateFavorite(int movieId, boolean add) {
        mMoviesLocalDataSource.updateFavorite(movieId, add);
    }

    @Override
    public void checkFavorite(int movieId, MoviesDataSource.FavoritesCallback favoritesCallback) {
        mMoviesLocalDataSource.checkFavorite(movieId, favoritesCallback);
    }

    @Override
    public void saveMovies(MoviesWrapper moviesWrapper) {
        mMoviesLocalDataSource.saveMovies(moviesWrapper);
    }

    private void getMoviesFromRemoteDataSource(final MoviesDataSource.LoadMoviesCallback callback, String page) {
        mMoviesRemoteDataSource.getMovies(new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(MoviesWrapper moviesWrapper) {
                saveMovies(moviesWrapper);
                callback.onMoviesLoaded(moviesWrapper);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        }, page);
    }

    private void getMoviesFromLocalDataSource(final MoviesDataSource.LoadMoviesCallback callback, final String page) {
        mMoviesLocalDataSource.getMovies(new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(MoviesWrapper moviesWrapper) {
                callback.onMoviesLoaded(moviesWrapper);
            }

            @Override
            public void onDataNotAvailable() {
                getMoviesFromRemoteDataSource(callback, page);
            }
        }, page);
    }

    private void getMovieDetailsFromRemoteDataSource(int movieId, final MoviesDataSource.LoadMovieDetailsCallback callback) {
        mMoviesRemoteDataSource.getMovieDetails(movieId, new MoviesDataSource.LoadMovieDetailsCallback() {
            @Override
            public void onMovieDetailLoaded(MovieDetails movieDetails) {
                saveMovieDetails(movieDetails);
                callback.onMovieDetailLoaded(movieDetails);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();

            }
        });
    }

    private void getMovieDetailsFromLocalDataSource(final int movieId, final MoviesDataSource.LoadMovieDetailsCallback callback) {
        mMoviesLocalDataSource.getMovieDetails(movieId, new MoviesDataSource.LoadMovieDetailsCallback() {
            @Override
            public void onMovieDetailLoaded(MovieDetails movieDetails) {
                callback.onMovieDetailLoaded(movieDetails);
            }

            @Override
            public void onDataNotAvailable() {
                getMovieDetailsFromRemoteDataSource(movieId, callback);
            }
        });
    }
}
