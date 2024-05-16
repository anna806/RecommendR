package hu.recommendr.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class ImageService(private val apiKey: String) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val url = "https://api.openai.com/v1/images/generations"
    suspend fun getImage(musicPiece: String): String {

        return withContext(Dispatchers.IO){
            val prompt = "Abstract painting for the music: $musicPiece. No text nor letters."
            val json = """
            {
                "prompt": "$prompt",
                "model": "dall-e-2",
                "size":"1024x1024",
                "quality":"standard",
                "n":1
            }
            """.trimIndent()

            val body = json
                .toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            client.newCall(request).execute()
                .use { response -> return@use response.body?.string() ?: "Not found" }
        }
    /*
        //imitate a network call and return a placeholder image
        return withContext(Dispatchers.IO){
            //wait for 2 seconds
            Thread.sleep(2000)
            //create a json response
            val json = """
            {
                "data": [
                    {
                    "url":  "https://covers.openlibrary.org/b/olid/OL7440033M-L.jpg"
                    }
                ]
            }
            """.trimIndent()

            json
        }

     */
    }
}