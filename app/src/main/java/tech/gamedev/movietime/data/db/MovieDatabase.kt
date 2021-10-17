package tech.gamedev.movietime.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.gamedev.movietime.data.remote.responses.Movie

private const val DB_NAME = "location_database"

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        fun create(context: Context): MovieDatabase {

            return Room.databaseBuilder(
                context,
                MovieDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}