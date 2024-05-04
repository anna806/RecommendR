package hu.recommendr.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.io.OutputStreamWriter


class ChatGptService(private val apiKey: String, private val threadId: String) {
    private var client: OkHttpClient = OkHttpClient()

    suspend fun askGPT(message: String): String {
        val url = "https://api.openai.com/v1/threads/${threadId}/messages"
        val requestBody = """
            {
                "role": "assistant",
                "content": "$message"
            }
        """.trimIndent()

        return withContext(Dispatchers.IO){
            post(url, requestBody)
        }
    }

    private val JSON: MediaType = "application/json".toMediaType()

    @Throws(IOException::class)
    fun post(url: String, json: String): String {
        val body: RequestBody = RequestBody.create(JSON, json)

        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("OpenAI-Beta", "assistants=v1")
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() ?: "Not found" }
    }

    suspend fun getMessages(): String {
        return withContext(Dispatchers.IO) {
            val url = "https://api.openai.com/v1/threads/${threadId}/messages"

            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("OpenAI-Beta", "assistants=v1")
                .build()

            client.newCall(request).execute()
                .use { response -> return@use response.body?.string() ?: "Not found" }
        }
    }

}

