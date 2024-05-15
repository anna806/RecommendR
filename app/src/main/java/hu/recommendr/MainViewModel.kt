package hu.recommendr

import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.recommendr.service.ChatGptService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState
    private val chatGptService = ChatGptService(apiKey = ":)",
        threadId = ":))",
        assistantId = ":)))")

    fun onTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(text = text)
    }

    fun sendMessage(message: String) {
        if (message.isEmpty()) {
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(text = "")
            /*val sendResponse = chatGptService.askGPT(message = message)
            Log.d("MainViewModel", "Response: $sendResponse")
            val runResponse = chatGptService.run()
            Log.d("MainViewModel", "Response: $runResponse")*/
            val messageResponse = chatGptService.getMessages()
            Log.d("MainViewModel", "Response: $messageResponse")
            parseMessage(messageResponse, message)
        }

    }

    private fun parseMessage(response: String, genre: String) {
        val jsonResponse = JSONObject(response)
        val messages = jsonResponse.getJSONArray("data")
        val firstMessage = messages.getJSONObject(0)
        val content = firstMessage.getJSONArray("content")
        val firstContent = content.getJSONObject(0)
        val text = firstContent.getJSONObject("text")
        val value = text.getString("value")
        Log.d("MainViewModel", "Songs: $value")
        val songs = emptyList<Song>().toMutableList()
        val regex = "\\d+\\. ".toRegex()
        val list = regex.split(value).filterNot { it.isBlank() }.map { it.trim() }
        Log.d("MainViewModel", "Lines: $list")
        for (line in list) {
            if (line.contains("---")) {
                val song = line.split(" --- ")
                val artist = song[0]
                val title = song[1]
                songs += Song(title, artist, genre)
            }
        }
        Log.d("MainViewModel", "Songs: $songs")
        _uiState.value = _uiState.value.copy(response = songs)
    }

    fun run(){
        viewModelScope.launch {
            val response = chatGptService.run()
            Log.d("MainViewModel", "Response: $response")
        }
    }

    fun getMessage(){
        viewModelScope.launch {
            val response = chatGptService.getMessages()
            Log.d("MainViewModel", "firstMessage: $response")
            val jsonResponse = JSONObject(response)
            val messages = jsonResponse.getJSONArray("data")
            val firstMessage = messages.getJSONObject(0)
            val content = firstMessage.getJSONArray("content")
            val firstContent = content.getJSONObject(0)
            val text = firstContent.getJSONObject("text")
            val value = text.getString("value")
        }
    }
}


