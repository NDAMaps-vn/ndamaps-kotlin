package vn.ndamaps.sdk.modules

import com.google.gson.JsonObject
import vn.ndamaps.sdk.NDAMapsClient

class NavigationModule(private val client: NDAMapsClient) {

    fun directions(
        origin: String,
        destination: String,
        vehicle: String? = null,
        alternatives: Boolean? = null
    ): JsonObject {
        val q = mutableMapOf("origin" to origin, "destination" to destination)
        vehicle?.let { q["vehicle"] = it }
        alternatives?.let { q["alternatives"] = it.toString() }
        return client.get(client.options.mapsApiBase, "/direction", q)
    }

    fun distanceMatrix(sources: List<Map<String, Any>>, targets: List<Map<String, Any>>): JsonObject {
        val bodyMap = mapOf("sources" to sources, "targets" to targets)
        val jsonStr = client.gson.toJson(bodyMap)
        return client.get(client.options.mapsApiBase, "/distancematrix", mapOf("json" to jsonStr))
    }

    fun optimizedRoute(locations: List<Map<String, Any>>, costing: String? = null): JsonObject {
        val bodyMap = mapOf(
            "locations" to locations,
            "costing" to (costing ?: "auto")
        )
        val jsonStr = client.gson.toJson(bodyMap)
        return client.get(client.options.mapsApiBase, "/optimized-route", mapOf("json" to jsonStr))
    }
}
