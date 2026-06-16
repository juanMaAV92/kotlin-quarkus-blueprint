package com.juanmaav.blueprint.infra.persistence

import com.juanmaav.blueprint.core.domain.Page
import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.port.output.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory adapter for [UserRepository].
 *
 * Keeps the blueprint runnable with zero infrastructure. Replace it with a real
 * adapter (e.g. Hibernate Reactive Panache or a JDBC repository) without touching
 * the core — the use cases depend only on the [UserRepository] port.
 */
@ApplicationScoped
class InMemoryUserRepository : UserRepository {
    private val store = ConcurrentHashMap<String, User>()

    override suspend fun save(user: User): User {
        val id = user.id ?: UUID.randomUUID().toString()
        val stored = user.copy(id = id)
        store[id] = stored
        return stored
    }

    override suspend fun findById(id: String): User? = store[id]

    override suspend fun findAll(
        page: Int,
        limit: Int,
    ): Page<User> {
        val safePage = if (page < 1) 1 else page
        val safeLimit = limit.coerceIn(1, MAX_LIMIT)

        val all = store.values.sortedBy { it.id }
        val from = (safePage - 1) * safeLimit
        val items =
            if (from >= all.size) {
                emptyList()
            } else {
                all.subList(from, minOf(from + safeLimit, all.size)).toList()
            }

        return Page(items = items, page = safePage, limit = safeLimit, total = all.size.toLong())
    }

    override suspend fun deleteById(id: String) {
        store.remove(id)
    }

    private companion object {
        const val MAX_LIMIT = 100
    }
}
