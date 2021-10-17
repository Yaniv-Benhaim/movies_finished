package tech.gamedev.movietime.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tech.gamedev.movietime.R
import tech.gamedev.movietime.data.remote.responses.Movie
import tech.gamedev.movietime.other.Constants.IMG_BASE_URL


class MoviePagingAdapter(private val glide: RequestManager, private val list: List<Movie>) : RecyclerView.Adapter<MoviePagingAdapter.MovieViewHolder>() {

    //Just a listener to get the movie item that is clicked and go to the MovieDetailFragment.kt
    private var listener: MovieClicked? = null

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)

        //Initializing click listener for every movie item
        fun initialize(movie: Movie) {
            thumbnail.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    listener?.movieClickedListener(movie)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = list[position]
        holder.apply {
            glide.load(IMG_BASE_URL + movie.backdrop_path).into(thumbnail)
            holder.initialize(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item,
                parent,
                false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MovieClicked {
        fun movieClickedListener(movie: Movie)
    }

    fun setOnMovieClickedListener(listener: MovieClicked) {
        this.listener = listener
    }
}