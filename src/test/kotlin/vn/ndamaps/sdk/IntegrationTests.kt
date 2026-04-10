package vn.ndamaps.sdk

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IntegrationTests {
    companion object {
        var client: NDAMapsClient? = null

        @JvmStatic
        @BeforeAll
        fun setUp() {
            val apiKey = System.getenv("NDAMAPS_API_KEY")
            if (apiKey != null) {
                client = NDAMapsClient(ClientOptions(apiKey = apiKey))
            }
        }
    }

    @Test
    fun testAutocomplete() {
        val c = client ?: return
        val res = c.places.autocomplete("Hồ Hoàn Kiếm")
        assertEquals("OK", res.get("status").asString)
        assertTrue(res.has("predictions"))
    }

    @Test
    fun testOptimizedRoute() {
        val c = client ?: return
        val l1 = mapOf("lat" to 21.03624, "lon" to 105.77142)
        val l2 = mapOf("lat" to 21.03326, "lon" to 105.78743)
        val l3 = mapOf("lat" to 21.00329, "lon" to 105.81834)
        
        val res = c.navigation.optimizedRoute(listOf(l1, l2, l3, l1))
        assertTrue(res.has("trip"))
        assertEquals(4, res.getAsJsonObject("trip").getAsJsonArray("locations").size())
    }
}
