package br.com.rf.themovieapp.moviedetails;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import br.com.rf.themovieapp.R;
import br.com.rf.themovieapp.data.source.MoviesRepository;
import br.com.rf.themovieapp.model.Movie;
import br.com.rf.themovieapp.model.MovieDetails;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View {

    MovieDetailsContract.Presenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mTollbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.appbar_img)
    ImageView mAppbarImg;
    @BindView(R.id.appbar_poster)
    ImageView mAppbarPoster;
    @BindView(R.id.movie_details_title)
    TextView mTextTitle;
    @BindView(R.id.movie_details_release_date)
    TextView mTextReleaseDate;
    @BindView(R.id.movie_details_overview)
    TextView mTextOverview;
    @BindView(R.id.movie_details_tag_line)
    TextView mTextTagLine;
    @BindView(R.id.movie_details_rate)
    TextView mTextRate;
    @BindView(R.id.movie_details_time)
    TextView mTextTime;
    @BindView(R.id.movie_details_homepage)
    TextView mTextHomepage;
    @BindView(R.id.movie_details_companies)
    LinearLayout mLayoutCompanies;
    @BindView(R.id.movie_details_genres)
    LinearLayout mLayoutGenres;
    @BindView(R.id.movie_details_status)
    TextView mTextStatus;
    @BindView(R.id.movie_details_fab)
    FloatingActionButton mFabFav;

    Movie mMovie;

    boolean mFavorite = false;

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        setupToolbar(mMovie.title);
        // Create the presenter
        new MovieDetailsPresenter(mMovie, this, MoviesRepository.provideMoviesRepository(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public void setupToolbar(String title) {
        setSupportActionBar(mTollbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setPresenter(MovieDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showMovieDetails(Movie movie) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTextRate.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_yellow, getTheme())));
        } else {
            ViewCompat.setBackgroundTintList(mTextRate, ColorStateList.valueOf(getResources().getColor(R.color.color_yellow)));
        }
        mTextTitle.setText(movie.title);
        mTextReleaseDate.setText(getString(R.string.lbl_release_date, movie.getReleaseDate()));
        mTextOverview.setText(movie.overview);
        mTextRate.setText(movie.getRate());

        if (!TextUtils.isEmpty(movie.getBackdropUrl())) {
            Glide.with(this)
                    .load(movie.getBackdropUrl())
                    .into(mAppbarImg);
        }
        if (!TextUtils.isEmpty(movie.getPosterUrl())) {
            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .into(mAppbarPoster);
        }
    }

    @Override
    public void showAllMovieDetails(MovieDetails movieDetails) {
        mTextTagLine.setText(movieDetails.getTagline());
        mTextTime.setText(movieDetails.getRuntime());
        mTextHomepage.setText(movieDetails.getHomepage());

        if (movieDetails.getStatus().length() > 0) {
            mTextStatus.setText(movieDetails.getStatus());
            mTextStatus.setVisibility(View.VISIBLE);
        }

        if (movieDetails.production_companies.size() > 0) {
            mLayoutCompanies.setVisibility(View.VISIBLE);

            for (MovieDetails.Values companies : movieDetails.production_companies) {
                TextView textView = new TextView(this);
                textView.setText(companies.name);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_normal), 0, getResources().getDimensionPixelSize(R.dimen.padding_normal), 0);
                mLayoutCompanies.addView(textView);
            }
        }

        if (movieDetails.genres.size() > 0) {
            mLayoutGenres.setVisibility(View.VISIBLE);

            for (MovieDetails.Values genres : movieDetails.genres) {
                TextView textView = new TextView(this);
                textView.setText(genres.name);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_normal), 0, getResources().getDimensionPixelSize(R.dimen.padding_normal), 0);
                mLayoutGenres.addView(textView);
            }
        }

    }

    @Override
    public void showAllMovieDetailsError() {
        Toast.makeText(this, R.string.lbl_load_full_details, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFavorite(boolean favorite) {
        mFavorite = favorite;
        if (favorite) {
            mFabFav.setImageResource(R.drawable.ic_favorited_white_24_dp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFabFav.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark, getTheme())));
            } else {
                mFabFav.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            }
        } else {
            mFabFav.setImageResource(R.drawable.ic_favorite_white_24_dp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFabFav.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent, getTheme())));
            } else {
                mFabFav.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.movie_details_fab)
    public void onFabFavClicked() {
        mPresenter.updateFavorite(mMovie.id, !mFavorite);
    }
}
