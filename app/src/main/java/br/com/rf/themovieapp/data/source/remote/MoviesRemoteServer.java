package br.com.rf.themovieapp.data.source.remote;

import br.com.rf.themovieapp.model.MovieDetails;
import br.com.rf.themovieapp.model.MoviesWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesRemoteServer {

    String BASE_API = "https://api.themoviedb.org";

    @GET("/3/discover/movie?api_key=e764a27cb17b01f54152a69437559e46&sort_by=popularity.desc")
    Call<MoviesWrapper> getMovies(@Query("page") String page);

    @GET("/3/movie/{movieId}?api_key=e764a27cb17b01f54152a69437559e46")
    Call<MovieDetails> getMovieDetails(@Path("movieId") int movieId);
}
