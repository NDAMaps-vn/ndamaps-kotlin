package vn.ndamaps.sdk.modules

import com.google.gson.JsonObject
import vn.ndamaps.sdk.NDAMapsClient

class NDAViewModule(private val client: NDAMapsClient) {

    fun staticThumbnailUrl(id: String?, lon: Double?, lat: Double?, yaw: Double?, pitch: Double?): String {
        val q = mutableListOf("apikey=${client.options.apiKey}")
        id?.let { q.add("id=$it") }
        if (lon != null && lat != null) q.add("place_position=$lon,$lat")
        yaw?.let { q.add("yaw=$it") }
        pitch?.let { q.add("pitch=$it") }
        
        return "${client.options.ndaViewBase}/items/static/thumbnail.jpeg?${q.joinToString("&")}"
    }
    
    fun search(bbox: String? = null, limit: Int? = null): JsonObject {
        val q = mutableMapOf<String, String>()
        bbox?.let { q["bbox"] = it }
        limit?.let { q["limit"] = it.toString() }
        return client.get(client.options.ndaViewBase, "/search", q)
    }
}
