package hu.recommendr.service

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.*
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.json.*
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.message.Message
import com.aallam.openai.api.message.MessageRequest
import com.aallam.openai.api.thread.ThreadId
import com.aallam.openai.client.OpenAIConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.time.Duration.Companion.seconds

class ChatGptService(private val apiKey: String, private val threadId: String) {

    //private val client = OkHttpClient()
    private lateinit var openAI: OpenAI

    init {
        // Initialization block to set up the ChatGPT API thread
        //setupThread()

        val config = OpenAIConfig(
            token = apiKey,
            timeout = Timeout(socket = 60.seconds),
            // additional configurations...
        )

        val openAI = OpenAI(config)
    }

    @OptIn(BetaOpenAI::class)
    suspend fun askGPT(threadId: String, message: String): Message {
        return coroutineScope {
            val thread = async { openAI.thread(id = ThreadId(threadId)) }
            val response = async {
                openAI.message(
                    threadId = ThreadId(threadId),
                    request = MessageRequest(
                        role = Role.User,
                        content = message
                    )
                )
            }
            // Await both async operations
            val threadResult = thread.await()
            val messageResult = response.await()

            // Return the message
            messageResult
        }
    }

}

