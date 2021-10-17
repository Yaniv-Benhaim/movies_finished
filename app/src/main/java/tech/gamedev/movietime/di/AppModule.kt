package tech.gamedev.movietime.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.gamedev.movietime.R
import tech.gamedev.movietime.data.db.MovieDao
import tech.gamedev.movietime.data.db.MovieDatabase
import tech.gamedev.movietime.data.remote.api.MovieApi
import tech.gamedev.movietime.other.Constants.BASE_URL
import tech.gamedev.movietime.repository.MovieRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_movie)
            .error(R.drawable.ic_movie)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(40, java.util.concurrent.TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, java.util.concurrent.TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, java.util.concurrent.TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, java.util.concurrent.TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideDirectionsApi(okHttpClient: OkHttpClient): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(movieApi: MovieApi, movieDao: MovieDao) = MovieRepository(movieApi, movieDao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase =
        MovieDatabase.create(context)

    @Provides
    fun provideDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }



}