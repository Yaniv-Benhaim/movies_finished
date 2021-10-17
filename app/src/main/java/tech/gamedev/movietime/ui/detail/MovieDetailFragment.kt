package tech.gamedev.movietime.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import tech.gamedev.movietime.R
import tech.gamedev.movietime.data.remote.responses.Movie
import tech.gamedev.movietime.databinding.FragmentMovieDetailBinding
import tech.gamedev.movietime.other.Constants
import tech.gamedev.movietime.ui.adapters.MoviePagingAdapter
import tech.gamedev.movietime.viewmodels.MovieViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail), MoviePagingAdapter.MovieClicked {

    /*
        Note!:
        The movie object is passed as a parameter through Safe Arguments from Navigation components.
        Just as can be seen at the bottom of this fragment in the interface implementation.
     */

    @Inject
    lateinit var glide: RequestManager
    private val args: MovieDetailFragmentArgs by navArgs()
    private val movieViewModel by viewModels<MovieViewModel>()
    private lateinit var movieAdapter : MoviePagingAdapter
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupMovieDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun setupMovieDetails() {
        val movie = args.movie
        val year = args.movie.release_date.substring(0,4)
        glide.load(Constants.IMG_BASE_URL + movie.backdrop_path).into(binding.ivThumbnail)
        binding.tvTitle.text = movie.title

        //The text (Action fantasy etc. is just an example.
        //normally i would use an extension function to get correct category from genre_id)
        binding.tvSummary.text = "$year • Action fantasy • 1h 45m"
        binding.tvPlotSummaryText.text = movie.overview
        binding.ratingBar.rating = (movie.vote_average/2).toFloat()
        binding.btnSaveToFavourites.setOnClickListener { movieViewModel.insertNewFavMovie(movie) }
        binding.btnAddtoFacourites.setOnClickListener {
            binding.btnAddtoFacourites.setImageResource(R.drawable.heart)
            movieViewModel.insertNewFavMovie(movie)
        }
        binding.btnBack.setOnClickListener { activity?.onBackPressed() }
    }

    private fun setupRecyclerView(list: List<Movie>) {
        movieAdapter = MoviePagingAdapter(glide, list)
        movieAdapter.setOnMovieClickedListener(this@MovieDetailFragment)
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeToObservers() {
        movieViewModel.movies.observe(viewLifecycleOwner) {
            setupRecyclerView(list = it)
            movieAdapter.notifyDataSetChanged()
        }
    }

    override fun movieClickedListener(movie: Movie) {
        val action = MovieDetailFragmentDirections.actionGlobalToMovieDetailFragment(movie)
        findNavController().navigate(action)
    }
}