package hu.recommendr

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.recommendr.data.ImageServiceUIState
import hu.recommendr.service.ImageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

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
            _uiState.value = uiState.value.copy(musicPiece = musicPiece)
        }
        return returnValue
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun downloadImage(context: Context) {
        Log.d("ExpandedSongDialogViewModel", "Downloading image from ${uiState.value.uri}")
        viewModelScope.launch(Dispatchers.IO) {
            val imageBytes = getBytesFromUrl(uiState.value.uri)
            if (imageBytes != null) {
                saveImageToGallery(imageBytes, context, "${uiState.value.musicPiece}.jpg")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image saved to galery", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun shareImage(context: Context) {
        val imageUri = Uri.parse(uiState.value.uri)
        shareImageWithOtherApps(imageUri, context)
        Log.d("ExpandedSongDialogViewModel", "Sharing image from ${uiState.value.uri} ")
    }

    private fun shareImageWithOtherApps(uri: Uri, context: Context) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "Share Image")
        context.startActivity(chooser)
    }

    private fun getBytesFromUrl(urlString: String): ByteArray? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connect()
            connection.getInputStream().use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageToGallery(imageBytes: ByteArray, context: Context, displayName: String) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val item = context.contentResolver.insert(collection, values)

        item?.let { uri ->
            try {
                context.contentResolver.openFileDescriptor(uri, "w", null).use { pfd ->
                    FileOutputStream(pfd?.fileDescriptor).use { outputStream ->
                        outputStream.write(imageBytes)
                    }
                }
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, values, null, null)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}