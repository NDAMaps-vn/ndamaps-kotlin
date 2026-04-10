package vn.ndamaps.sdk

import java.util.UUID
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SessionManager {
    private var token: String? = null
    private var createdAt: Long = 0
    private val ttlMs: Long = 300_000 // 5 minutes
    private val lock = ReentrantLock()

    fun getOrCreate(): String {
        lock.withLock {
            val now = System.currentTimeMillis()
            if (token != null && (now - createdAt) <= ttlMs) {
                return token!!
            }
            val newToken = UUID.randomUUID().toString()
            token = newToken
            createdAt = now
            return newToken
        }
    }

    fun getCurrent(): String? {
        lock.withLock {
            val now = System.currentTimeMillis()
            if (token != null && (now - createdAt) <= ttlMs) {
                return token
            }
            token = null
            return null
        }
    }

    fun reset() {
        lock.withLock {
            token = null
        }
    }
}
