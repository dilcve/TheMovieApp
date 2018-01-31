package br.com.rf.themovieapp.moviedetails;

import br.com.rf.themovieapp.data.source.MoviesDataSource;
import br.com.rf.themovieapp.model.Movie;
import br.com.rf.themovieapp.model.MovieDetails;


public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

    private final MovieDetailsContract.View mMovieDetailsView;
    private final MoviesDataSource mMoviesRepository;

    private Movie mMovie;

    public MovieDetailsPresenter(Movie movie, MovieDetailsContract.View movieDetailsView, MoviesDataSource moviesRepository) {
        mMoviesRepository = moviesRepository;
        mMovieDetailsView = movieDetailsView;
        mMovieDetailsView.setPresenter(this);
        mMovie = movie;
    }

    @Override
    public void start() {
        showMovieDetail(mMovie);
        getFullMovieDetails(mMovie.id);
        checkFavorite(mMovie.id);
    }

    private void showMovieDetail(Movie movie) {
        mMovieDetailsView.showMovieDetails(movie);
    }

    private void getFullMovieDetails(int movieId) {
        mMoviesRepository.getMovieDetails(movieId, new MoviesDataSource.LoadMovieDetailsCallback() {
            @Override
            public void onMovieDetailLoaded(MovieDetails movieDetails) {
                mMovieDetailsView.showAllMovieDetails(movieDetails);
            }

            @Override
            public void onDataNotAvailable() {
                mMovieDetailsView.showAllMovieDetailsError();
            }
        });
    }

    private void checkFavorite(int movieId) {
        mMoviesRepository.checkFavorite(movieId, new MoviesDataSource.FavoritesCallback() {
            @Override
            public void onCheckFavoirite(boolean favorite) {
                mMovieDetailsView.updateFavorite(favorite);
            }
        });
    }

    @Override
    public void updateFavorite(int movieId, boolean favorite) {
        mMoviesRepository.updateFavorite(movieId, favorite);
        mMovieDetailsView.updateFavorite(favorite);
    }
}
