package hu.recommendr

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.recommendr.data.ImageServiceUIState
import hu.recommendr.service.ImageService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ExpandedSongDialogViewModel: ViewModel() {
    private val imageService = ImageService(":)")

    private val _uiState = MutableStateFlow(ImageServiceUIState())
    val uiState: StateFlow<ImageServiceUIState> = _uiState


    fun getImageUrl(musicPiece: String): String {
        var returnValue = ""
        viewModelScope.launch {
            returnValue = imageService.getImage(musicPiece)
            val uri = getUriFromResponse(returnValue)
            _uiState.value = uiState.value.copy(uri = uri)
        }
        return returnValue
    }

    fun downloadImage() {
        //TODO
        Log.d("ExpandedSongDialogViewModel", "Downloading image from ${uiState.value.uri}")
    }

    fun shareImage() {
        //TODO
        Log.d("ExpandedSongDialogViewModel", "Sharing image from ${uiState.value.uri} ")
    }

    private fun getUriFromResponse(response: String): String {
        val jsonResponse = JSONObject(response)
        Log.d("ExpandedSongDialogViewModel", "Response: $response")
        val dataArray = jsonResponse.getJSONArray("data")
        val firstDataObject = dataArray.getJSONObject(0)
        val uri = firstDataObject.getString("url")
        Log.d("ExpandedSongDialogViewModel", "URI: $uri")
        return uri
    }

}