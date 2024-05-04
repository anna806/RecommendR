package hu.recommendr.service

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class ChatGptService(private val apiKey: String, private val threadId: String) {

    private val client = OkHttpClient()

    init {
        // Initialization block to set up the ChatGPT API thread
        setupThread()
    }

    private fun setupThread() {
        // Function to set up the ChatGPT API thread
        val json = """
            {
                "model": "text-davinci-002",
                "messages": [
                    {
                        "role": "system",
                        "content": "Hello, I'm a bot here to help you with your questions."
                    }
                ]
            }
        """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/start")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute()
        } catch (e: IOException) {
            // Handle exception
        }
    }

    fun sendMessage(message: String): String? {
        // Function to send a message to the ChatGPT API thread
        val json = """
            {
                "model": "gpt-4-0125-preview",
                "messages": [
                    {
                        "thread_id": "$threadId",
                        "role": "user",
                        "content": "$message"
                    }
                ]
            }
        """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            handleResponse(response)
        } catch (e: IOException) {
            null
        }
    }

    private fun handleResponse(response: Response): String? {
        // Function to handle the response from the ChatGPT API
        return if (response.isSuccessful) {
            val responseBody = response.body?.string()
            // Parse and extract relevant information from the response body
            responseBody
        } else {
            // Handle non-successful response
            null
        }
    }
}

