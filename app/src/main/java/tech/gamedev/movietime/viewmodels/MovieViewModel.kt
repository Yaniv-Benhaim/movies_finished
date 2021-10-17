package tech.gamedev.movietime.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tech.gamedev.movietime.data.remote.responses.Movie
import tech.gamedev.movietime.other.Constants.FAVOURITES
import tech.gamedev.movietime.other.Constants.NETWORKING
import tech.gamedev.movietime.other.Constants.NOW_PLAYING
import tech.gamedev.movietime.other.Constants.POPULAR_MOVIES
import tech.gamedev.movietime.repository.MovieRepository
import tech.gamedev.movietime.utils.Status
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private val query = MutableLiveData("")
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    private val _favMovies = MutableLiveData<List<Movie>>()

    private val _currentPage = MutableLiveData(1)
    private val _totalPages = MutableLiveData(0)
    private val _currentCategory = MutableLiveData("")
    val currentCategory: LiveData<String> = _currentCategory

    init {
        getPopularMovies()
    }

    //Unused function for if i want to implement search functionality
    fun setQuery(s: String) {
        query.postValue(s)
    }

    //Function that receives a category and chooses which movies to load accordingly
    fun getMovies(category: String = POPULAR_MOVIES) = when(category) {
        POPULAR_MOVIES -> getPopularMovies()
        NOW_PLAYING -> getNowPlayingMovies()
        else -> getAllFavMovies()
    }

    //Function for getting the list of popular movies according to the _currentPage variable
    private fun getPopularMovies(page: Int = 1) = viewModelScope.launch {
        try {
            //Setting initial values if there are none yet
            if (_currentPage.value == null || _currentCategory.value != POPULAR_MOVIES) {
                _currentPage.value = 1
                _currentCategory.value = POPULAR_MOVIES
            }

            //Getting the response wrapped in my custom Resource object
            val response = movieRepository.getPopularMovies(page = page)
            when (response.status) {
                Status.SUCCESS -> {
                    _movies.postValue(response.data!!.results)
                    _totalPages.value = response.data.total_pages
                    _currentPage.value = response.data.page
                }
                Status.ERROR -> Log.d(NETWORKING, response.message.toString())
                else -> Log.d(NETWORKING, "Request Failed from view model line 52")
            }
        } catch (e: Exception) {
            Log.d(NETWORKING, "Failed to get request: $e")
        }
    }

    //Function for getting the list of currently playing movies according to the _currentPage variable
    private fun getNowPlayingMovies(page: Int = 1) = viewModelScope.launch {
        try {
            //Setting initial values if there are none yet
            if (_currentPage.value == null || _currentCategory.value != NOW_PLAYING) {
                _currentPage.value = 1
                _currentCategory.value = NOW_PLAYING
            }

            //Getting the response wrapped in my custom Resource object
            val response = movieRepository.getNowPlayingMovies(page = page)
            when (response.status) {
                Status.SUCCESS -> {
                    _movies.postValue(response.data!!.results)
                    _totalPages.value = response.data.total_pages
                    _currentPage.value = response.data.page
                }
                Status.ERROR -> Log.d(NETWORKING, response.message.toString())
                else -> Log.d(NETWORKING, "Request Failed from view model line 52")
            }
        } catch (e: Exception) {
            Log.d(NETWORKING, "Failed to get request: $e")
        }
    }

    //Function for loading the next page
    fun getNextPage() {
        if (_currentPage.value!! < _totalPages.value!!) {
            _currentPage.postValue(_currentPage.value!! + 1)
            getPopularMovies(page = _currentPage.value!! + 1)
        }
    }

    //Function for loading the previous page
    fun getPreviousPage() {
        if (_currentPage.value!! > 1) {
            _currentPage.value!!.minus(1)
            getPopularMovies()
        }
    }

    //Function for saving a movie to our local Room Database
    fun insertNewFavMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.insertNewFavMovie(movie)
    }

    //Function for getting all movies currently listed in our local database
    private fun getAllFavMovies() = viewModelScope.launch {
        if (_favMovies.value == null) {
            _favMovies.value = ArrayList()
            _currentCategory.value = FAVOURITES
        }

        val favMovies = movieRepository.getAllFavouriteMovies()
        _movies.value = favMovies
    }
}