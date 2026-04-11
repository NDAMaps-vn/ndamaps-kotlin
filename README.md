<p align="center">
  <img src="https://ndamaps.vn/logo.png" width="200" alt="NDAMaps Logo" />
</p>

# NDAMaps Kotlin SDK 💜

Official, idiomatic Kotlin SDK for [NDAMaps REST APIs](https://ndamaps.vn). No heavy JVM threading models, built with Java 11 `java.net.http` mapping natively to simple API surfaces.

## 📦 Installation

Add to your `build.gradle.kts`:
```kotlin
dependencies {
    implementation("vn.ndamaps:sdk-kotlin:0.1.0")
}
```

## 🚀 Quick Start

Initialize and go!

```kotlin
import vn.ndamaps.sdk.ClientOptions
import vn.ndamaps.sdk.NDAMapsClient

fun main() {
    val client = NDAMapsClient(ClientOptions(apiKey = "YOUR_API_KEY"))
    
    // 1. Smart Autocomplete
    val autocompleteResult = client.places.autocomplete("Landmark 81")
    println(autocompleteResult)

    // 2. Fetch specific Place Detail
    // The previous Session Token propagates here seamlessly!
    val placeResult = client.places.placeDetail("A-valid-place-id-here")
    println(placeResult)
}
```

## 🧠 Features

- **Places & Geocoding**: Forward, reverse resolution.
- **Advanced Navigation**: Matrix routing and TSP `optimizedRoute`.
    ```kotlin
    val loc1 = mapOf("lat" to 21.03624, "lon" to 105.77142)
    val loc2 = mapOf("lat" to 21.03326, "lon" to 105.78743)
    val res = client.navigation.optimizedRoute(listOf(loc1, loc2, loc1))
    ```
- **Error Mapping**: Throws `NDAMapsError` extracting API server status codes properly.

## 🛑 Structure of Exceptions

```kotlin
import vn.ndamaps.sdk.errors.NDAMapsError

try {
    client.forcodes.decode("INVALID-CODE-XYZ")
} catch (e: NDAMapsError) {
    println("API Failed specifically with code: ${e.code}") // "INVALID_FORCODE"
    println("HTTP Status corresponding to fail: ${e.statusCode}")
}
```

## 📜 License
MIT License.
