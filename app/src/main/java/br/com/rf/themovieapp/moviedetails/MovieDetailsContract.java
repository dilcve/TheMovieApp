package br.com.rf.themovieapp.moviedetails;

import br.com.rf.themovieapp.BasePresenter;
import br.com.rf.themovieapp.BaseView;
import br.com.rf.themovieapp.model.Movie;
import br.com.rf.themovieapp.model.MovieDetails;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MovieDetailsContract {

    interface View extends BaseView<Presenter> {

        void showMovieDetails(Movie movie);

        void showAllMovieDetails(MovieDetails movieDetails);

        void showAllMovieDetailsError();

        void updateFavorite(boolean favorite);
    }

    interface Presenter extends BasePresenter {

        void updateFavorite(int movieId, boolean favorite);

    }
}
