package com.assistant.tearex.api
import okhttp3.logging.HttpLoggingInterceptor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.openai.com/v1/"
    private const val API_KEY = "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"  // Replace with your actual API key

    private val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        // Create OkHttpClient to add Authorization header
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .build()
                chain.proceed(request)
            }
            .build()

        // Build Retrofit instance
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ChatGPTService by lazy {
        retrofit.create(ChatGPTService::class.java)
    }

}
