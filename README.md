<p align="center">
  <img src="https://ndamaps.vn/logo.png" width="200" alt="NDAMaps Logo" />
</p>

# NDAMaps Kotlin SDK

Official Kotlin SDK for **NDAMaps** — Vietnam's national digital map platform API.

Uses **Java 11** `java.net.http` and **GSON** (`JsonObject` responses). Idiomatic Kotlin defaults on optional parameters.

## Features

- **Places** — Autocomplete and place detail with **session token** handling
- **Geocoding** — Forward and reverse (**Google**-style)
- **Navigation** — Directions, distance matrix, optimized multi-stop route
- **Maps** — MapLibre **style.json** URL (`styleUrl`)
- **NDAView** — Static thumbnail URL builder and **search**
- **Forcodes** — Encode and decode
- **Retries** — Exponential backoff on **429** and **5xx**

> JVM package surface is **smaller** than TypeScript/Python (no OSM autocomplete helpers, children/nearby, or static raster map builder in this repo).

## Requirements

- **Kotlin** targeting **Java 11+**

## Install

Add to `build.gradle.kts`:

```kotlin
dependencies {
    implementation("vn.ndamaps:sdk-kotlin:0.1.0")
}
```

## Quick start

```kotlin
import vn.ndamaps.sdk.ClientOptions
import vn.ndamaps.sdk.NDAMapsClient

fun main() {
    val client = NDAMapsClient(ClientOptions(apiKey = "YOUR_API_KEY"))

    val autocomplete = client.places.autocomplete("Landmark 81")
    println(autocomplete)

    val detail = client.places.placeDetail("PLACE_ID_FROM_UI")
    println(detail)
}
```

## API overview

### Places & geocoding

```kotlin
client.places.autocomplete(input = "Hồ Hoàn Kiếm", size = 8)
client.places.placeDetail(ids = "...", format = "google")

client.geocoding.forwardGoogle("12 Ngõ 1 Dịch Vọng Hậu")
client.geocoding.reverseGoogle("21.076,105.813")
```

### Navigation

```kotlin
client.navigation.directions(
    origin = "21.03,105.79",
    destination = "21.05,105.80",
    vehicle = "car",
)

val src = listOf(mapOf("lat" to "21.03", "lon" to "105.79"))
val dst = listOf(mapOf("lat" to "21.05", "lon" to "105.79"))
client.navigation.distanceMatrix(src, dst)

val loc1 = mapOf("lat" to 21.03624, "lon" to 105.77142)
val loc2 = mapOf("lat" to 21.03326, "lon" to 105.78743)
client.navigation.optimizedRoute(listOf(loc1, loc2, loc1))
```

### Maps, NDAView, Forcodes

```kotlin
client.maps.styleUrl("day-v1")

client.ndaview.staticThumbnailUrl(id = null, lon = 105.788, lat = 21.033, yaw = 276.0, pitch = 20.0)
client.ndaview.search(bbox = null, limit = 10)

client.forcodes.encode(20.990396, 105.868825, 13)
client.forcodes.decode("HNVTDXJEB2UBBO")
```

## Session tokens (billing)

`autocomplete` assigns a session UUID when `sessiontoken` is omitted. The next `placeDetail` without its own token consumes that session so billing can group the pair. Tokens expire after **5 minutes**.

## Error handling

```kotlin
import vn.ndamaps.sdk.errors.NDAMapsError

try {
    client.forcodes.decode("INVALID")
} catch (e: NDAMapsError) {
    println(e.code)
    println(e.statusCode)
}
```

## Configuration

```kotlin
ClientOptions(
    apiKey = "YOUR_KEY",
    mapsApiBase = "https://mapapis.ndamaps.vn/v1",
    tilesBase = "https://maptiles.ndamaps.vn",
    ndaViewBase = "https://api-view.ndamaps.vn/v1",
    maxRetries = 3,
    baseDelayMs = 500,
)
```

## Links

- [NDAMaps documentation](https://docs.ndamaps.vn)
- [NDAMaps platform](https://ndamaps.vn)

## License

MIT
