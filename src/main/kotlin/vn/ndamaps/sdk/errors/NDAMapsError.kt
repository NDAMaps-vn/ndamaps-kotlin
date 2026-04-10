package vn.ndamaps.sdk.errors

class NDAMapsError(
    val code: String,
    message: String,
    val statusCode: Int
) : Exception(message)
