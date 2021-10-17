package tech.gamedev.movietime.repository

import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.HttpException
import tech.gamedev.movietime.data.db.MovieDao
import tech.gamedev.movietime.data.remote.api.MovieApi
import tech.gamedev.movietime.data.remote.responses.Movie
import tech.gamedev.movietime.data.remote.responses.MovieResult
import tech.gamedev.movietime.utils.Resource
import tech.gamedev.movietime.utils.Status
import java.io.IOException
import javax.inject.Inject

@ActivityScoped
class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) {

    //Function for getting a list of popular movies from The MovieDB through Retrofit
    suspend fun getPopularMovies(page: Int) : Resource<MovieResult> {

        try {
            val response = movieApi.getPopularMovies(page = page)
            if (response.isSuccessful) {
                //The resource object is a custom object that can be found in the util package.
                //for save data extraction from network requests
                return Resource(Status.SUCCESS, response.body(), null)
            }
        } catch (e: IOException) {
            return Resource(Status.ERROR, null, e.message)
        } catch (e: HttpException) {
            return Resource(Status.ERROR, null, e.message)
        } catch (e: Exception) {
            return Resource(Status.ERROR, null, e.message)
        }

        return Resource(Status.ERROR, null, "Did not succeed")
    }

    //Function for getting a list of currently streaming movies from The MovieDB through Retrofit
    suspend fun getNowPlayingMovies(page: Int) : Resource<MovieResult> {

        try {
            val response = movieApi.getNowPlayingMovies(page = page)
            if (response.isSuccessful) {
                //The resource object is a custom object that can be found in the util package.
                //for save data extraction from network requests
                return Resource(Status.SUCCESS, response.body(), null)
            }
        } catch (e: IOException) {
            return Resource(Status.ERROR, null, e.message)
        } catch (e: HttpException) {
            return Resource(Status.ERROR, null, e.message)
        } catch (e: Exception) {
            return Resource(Status.ERROR, null, e.message)
        }

        return Resource(Status.ERROR, null, "Did not succeed")
    }

    //Function for getting a list of favourite movies from local database with Room
    suspend fun getAllFavouriteMovies() = movieDao.getAllMovies()

    //Function for adding a new favourite movie to local database with Room
    suspend fun insertNewFavMovie(movie: Movie) = movieDao.insertMovie(movie)
}





