package br.com.rf.themovieapp.movies;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.rf.themovieapp.R;
import br.com.rf.themovieapp.model.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private MovieItemListener mItemListener;

    public MoviesAdapter(List<Movie> movies, MovieItemListener itemListener) {
        this.mMovies = new ArrayList<>(movies);
        this.mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        final Movie movie = mMovies.get(position);
        holder.mTextMovieTitle.setText(movie.title);
        holder.mTextReleaseDate.setText(context.getString(R.string.lbl_release_date, movie.getReleaseDate()));
        holder.mTextOverview.setText(movie.overview);
        holder.mTextRate.setText(movie.getRate());

        if (!TextUtils.isEmpty(movie.getBackdropUrl())) {
            Glide.with(context)
                    .load(movie.getBackdropUrl())
                    .into(holder.mImgMovie);
        }

        holder.mImgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onMovieClicked(movie, holder.mImgMovie);
            }
        });

        holder.itemView.setClickable(true);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_movie_img)
        ImageView mImgMovie;
        @BindView(R.id.item_movie_title)
        TextView mTextMovieTitle;
        @BindView(R.id.item_movie_release_date)
        TextView mTextReleaseDate;
        @BindView(R.id.item_movie_overview)
        TextView mTextOverview;
        @BindView(R.id.item_movie_rate)
        TextView mTextRate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTextRate.setCompoundDrawableTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.color_yellow, itemView.getContext().getTheme())));
            } else {
                ViewCompat.setBackgroundTintList(mTextRate, ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.color_yellow)));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_movie;
    }

    public void appendItems(List<Movie> items) {
        int count = getItemCount();
        mMovies.addAll(items);
        notifyItemRangeInserted(count, items.size());
    }

    public interface MovieItemListener {

        void onMovieClicked(Movie movie, View transtionView);

    }
}
