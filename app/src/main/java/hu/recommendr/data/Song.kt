package hu.recommendr.data

data class Song(
    val title: String,
    val artist: String,
    val genre: String,
    val imageUrl: String = ""
)
