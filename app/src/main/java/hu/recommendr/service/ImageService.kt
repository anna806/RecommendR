package hu.recommendr.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient


class ImageService(private val apiKey: String) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val url = "https://api.openai.com/v1/images/generations"
    suspend fun getImage(musicPiece: String): String {
        /*
        return withContext(Dispatchers.IO){
            val prompt = "Cover image for the music: $musicPiece. No text nor letters."
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

         */

        //imitate a network call and return a placeholder image
        return withContext(Dispatchers.IO){
            //wait for 2 seconds
            Thread.sleep(2000)
            //create a json response
            val json = """
            {
                "data": [
                    {
                    "url":  "https://oaidalleapiprodscus.blob.core.windows.net/private/org-YCbo3zAamBSp9sMn1QgzfX2x/user-S8dHxBMimYzXYgAW8EvOr2eB/img-acLjwGlMMKCwuXBLsF7wJ1fg.png?st=2024-05-15T18%3A58%3A30Z&se=2024-05-15T20%3A58%3A30Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2024-05-15T12%3A07%3A51Z&ske=2024-05-16T12%3A07%3A51Z&sks=b&skv=2021-08-06&sig=Tc%2BOoedr2m3EtuzagBg/QXFMQivLbpQYUHP8CWcEjiM%3D"

                    }
                ]
            }
            """.trimIndent()

            json
        }
    }
}