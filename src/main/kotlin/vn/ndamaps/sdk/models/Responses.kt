package vn.ndamaps.sdk.models

/**
 * Typed response models for NDAMaps API.
 */

// ── Shared ────────────────────────────────────

// LatLng is defined in LatLng.kt

data class TextValue(val text: String, val value: Double)

// ── Autocomplete ──────────────────────────────

data class AutocompleteGoogleResponse(
    val predictions: List<Prediction>,
    val status: String,
) {
    data class Prediction(
        val description: String,
        val place_id: String,
        val structured_formatting: StructuredFormatting? = null,
        val types: List<String>? = null,
        val distance_meters: Double? = null,
        val has_child: Boolean = false,
    )

    data class StructuredFormatting(
        val main_text: String,
        val secondary_text: String,
    )
}

// ── Place Detail ──────────────────────────────

data class PlaceDetailGoogleResponse(
    val result: PlaceDetailResult,
    val status: String,
) {
    data class PlaceDetailResult(
        val place_id: String,
        val name: String,
        val formatted_address: String,
        val geometry: PlaceGeometry? = null,
        val address_components: List<AddressComponent>? = null,
        val types: List<String>? = null,
    )

    data class PlaceGeometry(val location: LatLng)
    data class AddressComponent(val long_name: String, val short_name: String)
}

// ── GeoJSON FeatureCollection ─────────────────

data class FeatureCollection(
    val type: String, // "FeatureCollection"
    val features: List<Feature>,
) {
    data class Feature(
        val type: String, // "Feature"
        val geometry: GeoJsonPoint? = null,
        val properties: PlaceProperties? = null,
    )

    data class GeoJsonPoint(
        val type: String, // "Point"
        val coordinates: List<Double>, // [lng, lat]
    )

    data class PlaceProperties(
        val id: String? = null,
        val name: String? = null,
        val label: String? = null,
        val street: String? = null,
        val country: String? = null,
        val region: String? = null,
        val county: String? = null,
        val locality: String? = null,
        val category: List<String>? = null,
        val has_child: Boolean = false,
        val distance: Double? = null,
    )
}

// ── Directions ────────────────────────────────

data class DirectionsResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>? = null,
    val routes: List<Route>,
) {
    data class GeocodedWaypoint(val geocoder_status: String, val place_id: String)

    data class Route(
        val legs: List<RouteLeg>,
        val overview_polyline: Polyline? = null,
        val summary: String? = null,
    )

    data class RouteLeg(
        val distance: TextValue,
        val duration: TextValue,
        val start_address: String? = null,
        val end_address: String? = null,
        val start_location: LatLng? = null,
        val end_location: LatLng? = null,
        val steps: List<RouteStep>? = null,
    )

    data class RouteStep(
        val distance: TextValue,
        val duration: TextValue,
        val html_instructions: String? = null,
        val maneuver: String? = null,
        val polyline: Polyline? = null,
    )

    data class Polyline(val points: String)
}

// ── Geocode ───────────────────────────────────

data class GeocodeGoogleResponse(
    val results: List<GeocodeResult>,
    val status: String,
) {
    data class GeocodeResult(
        val formatted_address: String,
        val place_id: String? = null,
        val name: String? = null,
        val geometry: PlaceDetailGoogleResponse.PlaceGeometry? = null,
    )
}

// ── Forcodes ──────────────────────────────────

data class ForcodeEncodeResponse(
    val forcodes: String,
    val lat: Double,
    val lng: Double,
    val resolution: Int,
    val admin_code: String,
    val status: String,
)

data class ForcodeDecodeResponse(
    val forcodes: String,
    val lat: Double?,
    val lng: Double?,
    val resolution: Int?,
    val status: String, // "OK" or "INVALID_FORCODES"
)
