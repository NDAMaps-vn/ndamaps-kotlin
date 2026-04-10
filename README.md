# NDAMaps Kotlin SDK

Official Kotlin SDK for NDAMaps REST APIs.

## Installation
Add to your `build.gradle.kts`:
```kotlin
dependencies {
    implementation("vn.ndamaps:sdk-kotlin:0.1.0")
}
```

## Usage
```kotlin
import vn.ndamaps.sdk.ClientOptions
import vn.ndamaps.sdk.NDAMapsClient

fun main() {
    val client = NDAMapsClient(ClientOptions(apiKey = "YOUR_API_KEY"))
    
    val res = client.places.autocomplete("Hồ Hoàn Kiếm")
    println(res)
}
```
