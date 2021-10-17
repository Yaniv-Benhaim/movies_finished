package tech.gamedev.movietime.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tech.gamedev.movietime.data.remote.responses.MovieResult
import tech.gamedev.movietime.other.Constants.API_KEY
import tech.gamedev.movietime.other.Constants.NOW_PLAYING
import tech.gamedev.movietime.other.Constants.POPULAR_MOVIES

interface MovieApi {

    @GET(POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("page") page: Int
    ) : Response<MovieResult>

    @GET(NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("page") page: Int
    ) : Response<MovieResult>
}