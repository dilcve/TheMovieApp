package br.com.rf.themovieapp.data.source;

import br.com.rf.themovieapp.data.source.local.LocalDataSource;
import br.com.rf.themovieapp.data.source.remote.RemoteDataSource;
import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;

public interface MoviesDataSource extends RemoteDataSource, LocalDataSource {

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
