package hu.recommendr.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ImageService(private val apiKey: String) {
    private val client: OkHttpClient = OkHttpClient()
    private val url = "https://api.openai.com/v1/images/generations"
    suspend fun getImage(musicPiece: String): String {
        return withContext(Dispatchers.IO){
            val prompt = "Cover image for the music: $musicPiece. No text nor letters."
            val body = """
            {
                "prompt": "$prompt",
                "model": "dall-e-2",
                "size"="1024x1024",
                "quality"="standard",
                "n"=1
            }
            """.trimIndent()

            val request = Request.Builder()
                .url(url)
                .post(body.toRequestBody("application/json".toMediaType()))
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            client.newCall(request).execute().use { response -> return@use response.body?.string() ?: "Not found" }
        }
    }
}