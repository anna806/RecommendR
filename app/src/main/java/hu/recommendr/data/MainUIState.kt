package hu.recommendr.data

import hu.recommendr.data.Song

data class MainUIState (
    val text: String = "",
    val response: List<Song> = listOf(
        Song("The Girl from Ipanema", " Stan Getz & João Gilberto", "Blue nossa"),
                ),
    val loading: Boolean = false,
    val selectedSong: Song? = null
)