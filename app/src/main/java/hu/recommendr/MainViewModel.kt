package hu.recommendr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.recommendr.service.ChatGptService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState
    val chatGptService = ChatGptService()

    fun onTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(text = text)
    }

    fun sendMessage(message: String) {
        if (message.isEmpty()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(text = "")
            val response = chatGptService.askGPT(message = message)
            Log.d("MainViewModel", "Response: $response")
        }
        /*val song1 = Song("title", "artist", "genre")
        val song2 = Song("title", "artist", "genre")
        val responseList = listOf(song1, song2)
        val response = MutableLiveData(responseList)
        response.value = responseList
        _uiState.value = _uiState.value.copy(response = response)*/

    }
}