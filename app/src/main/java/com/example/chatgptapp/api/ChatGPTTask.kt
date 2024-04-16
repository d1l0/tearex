package com.assistant.tearex.api

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject


fun extractContent(jsonString: String): String? {
    try {
        // Parse the entire JSON response
        val jsonObject = JSONObject(jsonString)

        // Access the 'choices' array which is a part of the top-level object
        val choicesArray = jsonObject.getJSONArray("choices")

        // For simplicity, assuming there's at least one element in the array
        if (choicesArray.length() > 0) {
            // Get the first element of the array
            val firstChoice = choicesArray.getJSONObject(0)

            // Access the 'message' object within this choice
            val messageObject = firstChoice.getJSONObject("message")

            // Finally, get the 'content' string from the message object
            return messageObject.getString("content")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

class ChatGPTTask(private val apiKey: String, private val onResponse: (String?) -> Unit) : AsyncTask<String, Void, String>() {
    companion object {
        private const val TAG = "chatgpttag"
    }
    override fun doInBackground(vararg params: String?): String? {
        val textInput = params[0] ?: return null
        val urlString = "https://api.openai.com/v1/chat/completions" // Ensure this is the correct API endpoint
        val model = "gpt-3.5-turbo"
        val prompt = "[{\"role\": \"user\", \"content\": \"$textInput\"}]"
        val maxTokens = 50
        val postData = "{\"model\": \"" + model + "\", \"messages\": " + prompt + ", \"max_tokens\": " + maxTokens + "}";

        var response: String? = null
        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.setRequestProperty("Authorization", "Bearer $apiKey")
            urlConnection.doOutput = true

            OutputStreamWriter(urlConnection.outputStream).use { writer ->
                writer.write(postData)
            }

            BufferedReader(InputStreamReader(urlConnection.inputStream)).use { reader ->
                response = reader.readText()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val content = extractContent("$response")
        Log.d(TAG,"String value: $content")

        return content
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        onResponse(result)
    }
}
