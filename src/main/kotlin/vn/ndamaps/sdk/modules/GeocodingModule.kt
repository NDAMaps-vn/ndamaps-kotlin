package vn.ndamaps.sdk.modules

import com.google.gson.JsonObject
import vn.ndamaps.sdk.NDAMapsClient

class GeocodingModule(private val client: NDAMapsClient) {

    fun forwardGoogle(address: String, adminV2: Boolean? = null): JsonObject {
        val q = mutableMapOf("address" to address)
        adminV2?.let { q["admin_v2"] = it.toString() }
        return client.get(client.options.mapsApiBase, "/geocode/forward", q)
    }

    fun reverseGoogle(latlng: String, adminV2: Boolean? = null): JsonObject {
        val q = mutableMapOf("latlng" to latlng)
        adminV2?.let { q["admin_v2"] = it.toString() }
        return client.get(client.options.mapsApiBase, "/geocode/reverse", q)
    }
}
