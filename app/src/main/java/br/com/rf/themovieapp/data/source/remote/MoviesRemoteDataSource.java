package br.com.rf.themovieapp.data.source.remote;

import br.com.rf.themovieapp.data.source.MoviesDataSource;
import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRemoteDataSource implements RemoteDataSource {

    private static MoviesRemoteDataSource INSTANCE;

    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private MoviesRemoteDataSource() {
    }

    @Override
    public void getMovies(final MoviesDataSource.LoadMoviesCallback callback, String page) {

        Call<MoviesWrapper> call = MoviesRestClient.getInstance().getRestApi().getMovies(page);
        call.enqueue(new Callback<MoviesWrapper>() {
            @Override
            public void onResponse(Call<MoviesWrapper> moviesWrapperCall, Response<MoviesWrapper> response) {
                MoviesWrapper moviesWrapper = response.body();
                if (moviesWrapper != null) {
                    callback.onMoviesLoaded(moviesWrapper);
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<MoviesWrapper> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getMoreMovies(MoviesDataSource.LoadMoviesCallback callback, String page) {
        getMovies(callback, page);
    }

    @Override
    public void getMovieDetails(int movieId, final MoviesDataSource.LoadMovieDetailsCallback callback) {
        Call<MovieDetails> call = MoviesRestClient.getInstance().getRestApi().getMovieDetails(movieId);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails movieDetails = response.body();
                if (movieDetails != null) {
                    callback.onMovieDetailLoaded(movieDetails);
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }
}
