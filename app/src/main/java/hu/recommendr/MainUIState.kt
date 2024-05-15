package hu.recommendr

import androidx.lifecycle.MutableLiveData

data class MainUIState (
    val text: String = "",
    val response: List<Song> = emptyList()
)