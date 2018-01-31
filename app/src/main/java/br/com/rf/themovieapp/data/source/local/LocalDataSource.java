package br.com.rf.themovieapp.data.source.local;

import br.com.rf.themovieapp.data.source.MoviesDataSource;
import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;

/**
 * Created by rodrigoferreira on 31/01/2018.
 */

public interface LocalDataSource {

    void getMovies(MoviesDataSource.LoadMoviesCallback callback, String page);

    void getMovieDetails(int movieId, MoviesDataSource.LoadMovieDetailsCallback callback);

    void saveMovies(MoviesWrapper moviesWrapper);

    void saveMovieDetails(MovieDetails movieDetails);

    void updateFavorite(int movieId, boolean add);

    void checkFavorite(int movieId, MoviesDataSource.FavoritesCallback favoritesCallback);

}
