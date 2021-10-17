package tech.gamedev.movietime.data.db

import androidx.room.*
import tech.gamedev.movietime.data.remote.responses.Movie

@Dao
interface MovieDao {

    @Update
    suspend fun updateMovie(movie: Movie) {
        insertMovie(movie)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: Movie)

    @Query("DELETE FROM movies")
    suspend fun deleteLocations()

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>
}