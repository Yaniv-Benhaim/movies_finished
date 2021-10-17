package tech.gamedev.movietime.other

object Constants {

    /*
        Note!:
        Normally would create a safer way for saving the api key, but for this
        project saving it as a constant seems good enough
     */

    //Networking
    const val API_KEY = "df56cceaccf1c04bcb4086c63578e466"
    const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    const val IMG_BASE_URL = "https://image.tmdb.org/t/p/w500"

    //Categories
    const val POPULAR_MOVIES = "popular"
    const val NOW_PLAYING = "now_playing"
    const val FAVOURITES = "FAVOURITES"

    //Debug tags
    const val NETWORKING = "NETWORKING"
}