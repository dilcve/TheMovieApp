package br.com.rf.themovieapp.movies;

import android.view.View;

import br.com.rf.themovieapp.data.source.MoviesDataSource;
import br.com.rf.themovieapp.model.Movie;
import br.com.rf.themovieapp.model.MoviesWrapper;


public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesDataSource mMoviesRepository;

    private final MoviesContract.View mMoviesView;

    public MoviesPresenter(MoviesDataSource moviesRepository, MoviesContract.View moviesView) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies();
    }

    @Override
    public void loadMovies() {
        mMoviesView.setLoadingIndicator(true);

        mMoviesRepository.getMovies(new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(MoviesWrapper moviesWrapper) {

                mMoviesView.setLoadingIndicator(false);

                mMoviesView.showMovies(moviesWrapper.results);
            }

            @Override
            public void onDataNotAvailable() {

                mMoviesView.showLoadingMoviesError();
            }
        }, "1");
    }

    @Override
    public void loadMoreMovies(String page) {
        mMoviesRepository.getMoreMovies(new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(MoviesWrapper moviesWrapper) {

                mMoviesView.showMoreMovies(moviesWrapper.results);
            }

            @Override
            public void onDataNotAvailable() {

                mMoviesView.showLoadingMoreMoviesError();
            }
        }, page);
    }

    @Override
    public void openMovieDetails(Movie movie, View transitionView) {
        mMoviesView.showMovieDetailsUi(movie, transitionView);
    }

}
