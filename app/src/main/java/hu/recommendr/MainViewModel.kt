package hu.recommendr

import android.util.Log
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


