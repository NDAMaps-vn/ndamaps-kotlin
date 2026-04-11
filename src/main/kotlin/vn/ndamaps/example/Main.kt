package vn.ndamaps.example

import vn.ndamaps.sdk.ClientOptions
import vn.ndamaps.sdk.NDAMapsClient

fun main() {
    val apiKey = System.getenv("NDAMAPS_API_KEY")
    if (apiKey == null) {
        System.err.println("Please set NDAMAPS_API_KEY environment variable")
        kotlin.system.exitProcess(1)
    }

    val client = NDAMapsClient(ClientOptions(apiKey = apiKey))

    println("--- 1. Forward Geocoding ---")
    try {
        val geoRes = client.geocoding.forwardGoogle("Hồ Hoàn Kiếm, Hà Nội")
        if (geoRes.has("results") && geoRes.getAsJsonArray("results").size() > 0) {
            println("Result: ${geoRes.getAsJsonArray("results").get(0).asJsonObject.getAsJsonObject("geometry").getAsJsonObject("location")}")
        }

        println("\n--- 2. Optimized Route ---")
        val l1 = mapOf("lat" to 21.03624, "lon" to 105.77142)
        val l2 = mapOf("lat" to 21.03326, "lon" to 105.78743)
        val l3 = mapOf("lat" to 21.00329, "lon" to 105.81834)
        
        val routeRes = client.navigation.optimizedRoute(listOf(l1, l2, l3, l1))
        
        if (routeRes.has("trip")) {
            val trip = routeRes.getAsJsonObject("trip")
            if (trip.has("summary")) {
                println("Total Distance: ${trip.getAsJsonObject("summary").get("length").asDouble} meters")
                println("Total ETA: ${trip.getAsJsonObject("summary").get("time").asDouble} seconds")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
