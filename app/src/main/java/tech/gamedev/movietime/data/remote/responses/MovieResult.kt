package tech.gamedev.movietime.data.remote.responses

data class MovieResult(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)