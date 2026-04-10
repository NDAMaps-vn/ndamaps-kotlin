package vn.ndamaps.sdk.modules

import vn.ndamaps.sdk.NDAMapsClient

class MapsModule(private val client: NDAMapsClient) {
    fun styleUrl(styleId: String = "day-v1"): String {
        return "${client.options.tilesBase}/styles/$styleId/style.json?apikey=${client.options.apiKey}"
    }
}
