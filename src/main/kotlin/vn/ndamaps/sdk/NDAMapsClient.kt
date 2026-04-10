package vn.ndamaps.sdk

import com.google.gson.Gson
import com.google.gson.JsonObject
import vn.ndamaps.sdk.errors.NDAMapsError
import vn.ndamaps.sdk.modules.*
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

data class ClientOptions(
    val apiKey: String,
    val mapsApiBase: String = "https://mapapis.ndamaps.vn/v1",
    val tilesBase: String = "https://maptiles.openmap.vn",
    val ndaViewBase: String = "https://api-view.ndamaps.vn/v1",
    val maxRetries: Int = 3,
    val baseDelayMs: Long = 500
)

class NDAMapsClient(val options: ClientOptions) {
    private val httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build()
    val gson = Gson()
    val sessionManager = SessionManager()

    val places = PlacesModule(this)
    val geocoding = GeocodingModule(this)
    val navigation = NavigationModule(this)
    val maps = MapsModule(this)
    val ndaview = NDAViewModule(this)
    val forcodes = ForcodesModule(this)

    fun get(base: String, path: String, query: Map<String, String>): JsonObject {
        return doReq("GET", base, path, query, null)
    }

    fun post(base: String, path: String, query: Map<String, String>, body: String? = null): JsonObject {
        return doReq("POST", base, path, query, body)
    }

    private fun doReq(method: String, base: String, path: String, query: Map<String, String>, body: String?): JsonObject {
        val queryString = query.entries.joinToString("&") {
            "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value, "UTF-8")}"
        }
        val fullQueryString = if (queryString.isEmpty()) "apikey=${options.apiKey}" else "$queryString&apikey=${options.apiKey}"
        val url = "$base$path?$fullQueryString"

        val reqBuilder = HttpRequest.newBuilder().uri(URI.create(url))
        if (method.equals("POST", ignoreCase = true)) {
            reqBuilder.header("Content-Type", "application/json")
            reqBuilder.POST(if (body != null) HttpRequest.BodyPublishers.ofString(body) else HttpRequest.BodyPublishers.noBody())
        } else {
            reqBuilder.GET()
        }

        var lastErr: Exception? = null

        for (attempt in 0..options.maxRetries) {
            if (attempt > 0) {
                Thread.sleep(options.baseDelayMs * (1L shl (attempt - 1)))
            }

            try {
                val resp = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString())
                val status = resp.statusCode()
                val bodyStr = resp.body()

                if (status >= 400) {
                    if (status == 429 || status >= 500) {
                        lastErr = NDAMapsError(mapHTTPStatus(status), "HTTP $status", status)
                        continue
                    }
                    throw NDAMapsError(mapHTTPStatus(status), bodyStr, status)
                }

                val jsonObj = gson.fromJson(bodyStr, JsonObject::class.java)
                if (jsonObj?.has("status") == true) {
                    val apiStatus = jsonObj.get("status").asString
                    val errCode = mapResponseStatus(apiStatus)
                    if (errCode != null) throw NDAMapsError(errCode, apiStatus, status)
                }
                return jsonObj
            } catch (e: java.io.IOException) {
                lastErr = NDAMapsError("NETWORK_ERROR", e.message ?: "", 0)
            }
        }
        throw NDAMapsError("RETRIES_EXHAUSTED", "Max retries reached: ${lastErr?.message}", 0)
    }

    private fun mapHTTPStatus(status: Int): String {
        return when (status) {
            400 -> "INVALID_PARAMS"
            401, 403 -> "INVALID_API_KEY"
            404 -> "PLACE_NOT_FOUND"
            429 -> "RATE_LIMIT_EXCEEDED"
            else -> if (status >= 500) "NETWORK_ERROR" else "UNKNOWN"
        }
    }

    private fun mapResponseStatus(status: String): String? {
        return when (status) {
            "OK" -> null
            "INVALID_FORCODES" -> "INVALID_FORCODE"
            "NOT_FOUND" -> "PLACE_NOT_FOUND"
            "ZERO_RESULTS" -> "ZERO_RESULTS"
            "INVALID_REQUEST" -> "INVALID_PARAMS"
            "REQUEST_DENIED" -> "INVALID_API_KEY"
            "OVER_QUERY_LIMIT" -> "RATE_LIMIT_EXCEEDED"
            else -> null
        }
    }
}
