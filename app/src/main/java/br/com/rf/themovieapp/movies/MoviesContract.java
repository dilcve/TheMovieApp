package br.com.rf.themovieapp.movies;

import java.util.List;

import br.com.rf.themovieapp.BasePresenter;
import br.com.rf.themovieapp.BaseView;
import br.com.rf.themovieapp.model.Movie;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MoviesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showMoreMovies(List<Movie> movies);

        void showMovieDetailsUi(Movie movie, android.view.View transitionView);

        void showLoadingMoviesError();

        void showLoadingMoreMoviesError();

    }

    interface Presenter extends BasePresenter {

        void loadMovies();

        void loadMoreMovies(String page);

        void openMovieDetails(Movie movie, android.view.View transitionView);

    }
}
