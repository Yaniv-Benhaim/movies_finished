package tech.gamedev.movietime.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.gamedev.movietime.R
import tech.gamedev.movietime.databinding.FragmentHomeBinding
import tech.gamedev.movietime.databinding.FragmentSplashBinding
import tech.gamedev.movietime.viewmodels.MovieViewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {

    /*
        Note!:
        This splash fragment gives the app a nice start for preloading the movies.
        Would be good to do an internet connection here also.
     */

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preloadMovies()
    }

    private fun preloadMovies() {
        lifecycleScope.launch {
            movieViewModel.getMovies()
            delay(2000)
            findNavController().navigate(R.id.action_splashFragment_to_navigation_home)
        }
    }
}

