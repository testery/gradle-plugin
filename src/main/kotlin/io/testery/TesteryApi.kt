package io.testery

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.StringBuilder

object TesteryApi {
    private val client = OkHttpClient()
    private const val prodUrl = "https://api.testery.io/api"
    private const val devUrl = "https://api.dev.testery.io/api"
    var useDevUrl = false

    fun getUploadUrl(apiKey: String, projectKey: String, buildId: String, fileName: String, commit: String? = null, branch: String? = null): String? {
        var url = "$baseUrl/projects/$projectKey/upload-url?build=${buildId}&file=$fileName"

        if(commit != null) url += "&commit=$commit"
        if(branch != null) url += "&branch=$branch"

        val request = Request.Builder()
            .url(url)
            .method("GET", null)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return client.newCall(request).execute().body?.string()
    }

    fun uploadJarFile(jarFile: File, url: String): Boolean {
        val request = Request.Builder()
            .url(url)
            .put(jarFile.asRequestBody("application/java-archive".toMediaType()))
            .build()

        return client.newCall(request).execute().isSuccessful
    }

    fun createDeploy(apiKey: String, projectKey: String, environmentKey: String, buildId: String? = null, commit: String? = null, branch: String? = null): Boolean {
        val json = mapOf(
            "project" to projectKey,
            "environment" to environmentKey,
            "commit" to commit,
            "buildId" to buildId,
            "branch" to branch
        ).toJson()

        val request = Request.Builder()
            .url("$baseUrl/deploys")
            .method("POST", json.toRequestBody("application/json".toMediaType()))
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return client.newCall(request).execute().isSuccessful
    }

    private val baseUrl: String
        get() = if(useDevUrl) devUrl else prodUrl

    private fun Map<String, Any?>.toJson(): String {
        val string = StringBuilder()

        mapValues { (key, value) ->
            string.append("\"$key\": ")
            when (value) {
                is String -> string.append("\"${value.replace("\"", "\\\"")}\"")
                is Char -> string.append("\"$value\"")
                else -> string.append(value)
            }
            string.append(",")
        }

        return "{ ${string.removeSuffix(",")} }"
    }
}