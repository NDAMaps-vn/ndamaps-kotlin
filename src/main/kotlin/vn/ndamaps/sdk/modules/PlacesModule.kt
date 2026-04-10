package vn.ndamaps.sdk.modules

import com.google.gson.JsonObject
import vn.ndamaps.sdk.NDAMapsClient

class PlacesModule(private val client: NDAMapsClient) {

    fun autocomplete(
        input: String,
        location: String? = null,
        origin: String? = null,
        radius: Double? = null,
        size: Int? = null,
        sessiontoken: String? = null,
        adminV2: Boolean? = null
    ): JsonObject {
        val q = mutableMapOf("input" to input)
        location?.let { q["location"] = it }
        origin?.let { q["origin"] = it }
        radius?.let { q["radius"] = it.toString() }
        size?.let { q["size"] = it.toString() }
        adminV2?.let { q["admin_v2"] = it.toString() }

        val token = sessiontoken ?: client.sessionManager.getOrCreate()
        q["sessiontoken"] = token

        return client.get(client.options.mapsApiBase, "/autocomplete", q)
    }

    fun placeDetail(
        ids: String,
        format: String? = null,
        sessiontoken: String? = null,
        adminV2: Boolean? = null
    ): JsonObject {
        val q = mutableMapOf("ids" to ids)
        format?.let { q["format"] = it }
        adminV2?.let { q["admin_v2"] = it.toString() }

        var token = sessiontoken
        if (token == null) {
            val curr = client.sessionManager.getCurrent()
            if (curr != null) {
                token = curr
                client.sessionManager.reset()
            }
        }
        token?.let { q["sessiontoken"] = it }

        return client.get(client.options.mapsApiBase, "/place", q)
    }
}
