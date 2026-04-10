package vn.ndamaps.sdk.modules

import com.google.gson.JsonObject
import vn.ndamaps.sdk.NDAMapsClient

class ForcodesModule(private val client: NDAMapsClient) {

    fun encode(lat: Double, lng: Double, resolution: Int? = null): JsonObject {
        val q = mutableMapOf("lat" to lat.toString(), "lng" to lng.toString())
        resolution?.let { q["resolution"] = it.toString() }
        return client.post(client.options.mapsApiBase, "/forcodes/encode", q)
    }

    fun decode(forcodes: String): JsonObject {
        return client.post(client.options.mapsApiBase, "/forcodes/decode", mapOf("forcodes" to forcodes))
    }
}
