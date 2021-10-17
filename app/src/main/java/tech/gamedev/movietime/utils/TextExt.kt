package tech.gamedev.movietime.utils

import tech.gamedev.movietime.other.Constants.NOW_PLAYING
import tech.gamedev.movietime.other.Constants.POPULAR_MOVIES

fun String.getCategory(category: String) : String = when(category) {

    "POPULAR" -> POPULAR_MOVIES
    "STREAMING" -> NOW_PLAYING
    "FAVOURITES" -> "FAVOURITES"
    else -> POPULAR_MOVIES
}