package br.com.rf.themovieapp.movies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.rf.themovieapp.R;
import br.com.rf.themovieapp.data.source.MoviesRepository;
import br.com.rf.themovieapp.model.Movie;
import br.com.rf.themovieapp.moviedetails.MovieDetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {

    MoviesContract.Presenter mPresenter;

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    MoviesAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.loading)
    View mLoadingView;
    @BindView(R.id.view_no_data)
    View mNoDataView;
    @BindView(R.id.no_data_msg)
    TextView mNoDataMsg;

    @BindView(R.id.toolbar)
    Toolbar mTollbar;

    EndlessRecyclerViewAdapter mEndlessRecyclerViewAdapter;

    private List<Movie> mMovies = new ArrayList<>();

    private static final int PAGE_SIZE = 20;
    private int mNextPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        setSupportActionBar(mTollbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // Create the presenter
        new MoviesPresenter(MoviesRepository.provideMoviesRepository(this), this);

        mPresenter.start();
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mNoDataView.setVisibility(View.GONE);
        if (active) {
            mLoadingView.setVisibility(View.VISIBLE);
        } else {
            mLoadingView.setVisibility(View.GONE);
        }

    }

    @Override
    public void showMovies(final List<Movie> movies) {
        mMovies = movies;
        mNextPage = (mMovies.size() / PAGE_SIZE) + 1;
        mAdapter = new MoviesAdapter(mMovies, mItemListener);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mEndlessRecyclerViewAdapter = new EndlessRecyclerViewAdapter(mAdapter, new EndlessRecyclerViewAdapter.RequestToLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreMovies(String.valueOf(mNextPage));
            }
        });

        mRecyclerView.setAdapter(mEndlessRecyclerViewAdapter);
    }

    @Override
    public void showMoreMovies(List<Movie> movies) {
        mNextPage++;
        mEndlessRecyclerViewAdapter.onDataReady(true);
        mAdapter.appendItems(movies);
    }

    @Override
    public void showMovieDetailsUi(Movie movie, View transtionView) {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transtionView, getString(R.string.transition_img)).toBundle();
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent, bundle);
    }

    @Override
    public void showLoadingMoreMoviesError() {
        mAdapter.notifyDataSetChanged();
        mEndlessRecyclerViewAdapter.onDataReady(false);
        Toast.makeText(this, R.string.lbl_load_more_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingMoviesError() {
        mNoDataMsg.setText(R.string.lbl_no_data_available);
        mNoDataView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.view_no_data)
    public void onNoDataClick() {
        mPresenter.loadMovies();
    }

    /**
     * Listener for clicks in the RecyclerView.
     */
    MoviesAdapter.MovieItemListener mItemListener = new MoviesAdapter.MovieItemListener() {

        @Override
        public void onMovieClicked(Movie movie, View transitionView) {
            mPresenter.openMovieDetails(movie, transitionView);
        }

    };

}
