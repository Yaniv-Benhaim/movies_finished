package tech.gamedev.movietime.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import tech.gamedev.movietime.R
import tech.gamedev.movietime.data.remote.responses.Movie
import tech.gamedev.movietime.databinding.FragmentHomeBinding
import tech.gamedev.movietime.other.Constants
import tech.gamedev.movietime.other.Constants.POPULAR_MOVIES
import tech.gamedev.movietime.ui.adapters.MoviePagingAdapter
import tech.gamedev.movietime.utils.getCategory
import tech.gamedev.movietime.viewmodels.MovieViewModel
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), MoviePagingAdapter.MovieClicked {

    /*
        Note!:
        Improvements:
        Best practice for refreshing the recyclerview would of course be diffUtil with a paging adapter.
        But since the scope of this project is not to big and essentially every page of movies is loaded
        separately the performance can actually be increased by not using diffUtil.

        The tab layout also would be better together with a viewpager2 but since there are only
        3 categories (Popular, favourites, Now Streaming) there is no need in the scope of this project.
     */

    @Inject
    lateinit var glide: RequestManager
    private val movieViewModel by activityViewModels<MovieViewModel>()
    private lateinit var movieAdapter : MoviePagingAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        movieViewModel.getMovies(POPULAR_MOVIES)
        setupCategoryTabs()
        setupPrevAndNext()
        setupSearch()
        swipeToRefresh()
    }

    private fun setupSearch()  = binding.etSearchText.apply {
        doOnTextChanged { query, _, _, _ ->
            query?.let {
                movieViewModel.setQuery(it.toString())
            }
        }
    }

    private fun setupRecyclerView(list: List<Movie>) {
        movieAdapter = MoviePagingAdapter(glide, list)
        movieAdapter.setOnMovieClickedListener(this@HomeFragment)
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeToObservers() {
        movieViewModel.movies.observe(viewLifecycleOwner) {
            //Log.d("ROOMDB", "list ${it.size}")
            setupRecyclerView(list = it)
            movieAdapter.notifyDataSetChanged()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun swipeToRefresh() = binding.swipeToRefresh.setOnRefreshListener {
        movieViewModel.getMovies(movieViewModel.currentCategory.value ?: POPULAR_MOVIES)
    }

    private fun setupPrevAndNext() {
        binding.btnNext.setOnClickListener { movieViewModel.getNextPage() }
        binding.btnPrevious.setOnClickListener { movieViewModel.getPreviousPage() }

        binding.btnNext2.setOnClickListener { movieViewModel.getNextPage() }
        binding.btnPrevious2.setOnClickListener { movieViewModel.getPreviousPage() }
    }

    private fun setupCategoryTabs() {
        binding.catergoriesTabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text.let {
                    movieViewModel.getMovies(it.toString().getCategory(it.toString()
                        .uppercase(Locale.getDefault())))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {/*NO-OP*/}

            override fun onTabUnselected(tab: TabLayout.Tab?) {/*NO-OP*/}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun movieClickedListener(movie: Movie) {
        val action = HomeFragmentDirections.actionNavigationHomeToMovieDetailFragment(movie)
        findNavController().navigate(action)
    }
}