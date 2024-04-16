package com.assistant.tearex.api
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://api.openai.com/v1/"

interface ChatGPTService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")  // Ensure this path is correct
    fun sendMessage(@Body requestBody: ChatGPTRequest): Call<ChatGPTResponse>
}

// You would also define any necessary data models here or in separate Kotlin files within the same package
data class ChatGPTResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val text: String
)
data class ChatGPTRequest(
    val model: String = "text-davinci-003",  // Change according to the API docs
    val prompt: String,
    val max_tokens: Int,
    val temperature: Double = 0.7  // Example additional parameter
)