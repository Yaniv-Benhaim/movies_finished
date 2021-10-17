package tech.gamedev.movietime.ui.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import tech.gamedev.movietime.R
import tech.gamedev.movietime.data.remote.responses.Movie
import tech.gamedev.movietime.databinding.FragmentFavouriteBinding
import tech.gamedev.movietime.other.Constants.FAVOURITES
import tech.gamedev.movietime.ui.adapters.MoviePagingAdapter
import tech.gamedev.movietime.viewmodels.MovieViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment : Fragment(R.layout.fragment_favourite), MoviePagingAdapter.MovieClicked {

    /*
        Note!:
        This is just a simple fragment that gets a list of movies saved by the user in the local database.
     */

    @Inject
    lateinit var glide: RequestManager
    private val movieViewModel by activityViewModels<MovieViewModel>()
    private lateinit var movieAdapter : MoviePagingAdapter
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        movieViewModel.getMovies(FAVOURITES)
    }

    private fun setupRecyclerView(list: List<Movie>) {
        movieAdapter = MoviePagingAdapter(glide, list)
        movieAdapter.setOnMovieClickedListener(this@FavouriteFragment)
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeToObservers() {
        movieViewModel.movies.observe(viewLifecycleOwner) {
            setupRecyclerView(list = it)
            movieAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun movieClickedListener(movie: Movie) {
        val action = FavouriteFragmentDirections.actionGlobalToMovieDetailFragment(movie)
        findNavController().navigate(action)
    }
}