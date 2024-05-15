package hu.recommendr.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ChatGptService(private val apiKey: String, private val threadId: String, private val assistantId: String) {
    private var client: OkHttpClient = OkHttpClient()

    suspend fun askGPT(message: String): String {
        val url = "https://api.openai.com/v1/threads/${threadId}/messages"
        val requestBody = """
            {
                "role": "user",
                "content": "$message"
            }
        """.trimIndent()

        return withContext(Dispatchers.IO){
            post(url, requestBody)
        }
    }

    private val json: MediaType = "application/json".toMediaType()

    @Throws(IOException::class)
    fun post(url: String, json: String): String {
        val body: RequestBody = json.toRequestBody("application/json".toMediaType())

        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() ?: "Not found" }
    }

    suspend fun run(): String {
        return withContext(Dispatchers.IO) {
            val body = """
            {
                "assistant_id": "$assistantId",
                "max_completion_tokens": 100
            }
        """.trimIndent()
            val url = "https://api.openai.com/v1/threads/${threadId}/runs"

            val request = Request.Builder()
                .url(url)
                .post(body.toRequestBody("application/json".toMediaType()))
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("OpenAI-Beta", "assistants=v2")
                .build()
            client.newCall(request).execute()
                .use { response -> return@use response.body?.string() ?: "Not found" }
        }
    }

    suspend fun getMessages(): String {
        try {
            return withContext(Dispatchers.IO) {
                val url = "https://api.openai.com/v1/threads/${threadId}/messages"

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("OpenAI-Beta", "assistants=v2")
                    .build()

                client.newCall(request).execute()
                    .use { response -> return@use response.body?.string() ?: "Not found" }
            }
        } catch (e: Exception) {
            return "Not yet :)"
        }
    }

}

