package br.com.rf.themovieapp.data.source.remote;

import br.com.rf.themovieapp.data.source.MoviesDataSource;

/**
 * Created by rodrigoferreira on 31/01/2018.
 */

public interface RemoteDataSource {

    void getMovies(MoviesDataSource.LoadMoviesCallback callback, String page);

    void getMoreMovies(MoviesDataSource.LoadMoviesCallback callback, String page);

    void getMovieDetails(int movieId, MoviesDataSource.LoadMovieDetailsCallback callback);

}
