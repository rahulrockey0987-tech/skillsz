package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    // Model per system guidelines: basic text tasks -> gemini-2.5-flash
    private const val MODEL_NAME = "gemini-2.5-flash"

    suspend fun generateContent(prompt: String, student: CurrentUser?): String? = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "No valid GEMINI_API_KEY configured in environment, using dynamic on-device AI generator")
            return@withContext null
        }

        try {
            val systemContext = if (student != null) {
                "You are SKILLSZ AI, a career and engineering mentor for technical students. The user is ${student.name}, a ${student.year} ${student.branch} student targeting ${student.careerGoal} with CGPA ${student.gpa}. Be concise, practical, technical, and use markdown formatting."
            } else {
                "You are SKILLSZ AI, a career and technical education mentor. Be concise, actionable, and use markdown formatting."
            }

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemContext\n\nUser request: $prompt"))
                        })
                    })
                }
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 800)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.w(TAG, "Gemini API HTTP Error: ${response.code} ${response.message}")
                    return@withContext null
                }

                val responseString = response.body?.string() ?: return@withContext null
                val rootJson = JSONObject(responseString)
                val candidates = rootJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error invoking Gemini API: ${e.message}", e)
        }
        return@withContext null
    }
}
