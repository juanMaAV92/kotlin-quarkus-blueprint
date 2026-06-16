package com.juanmaav.blueprint.core.port.output

import com.juanmaav.blueprint.core.domain.Page
import com.juanmaav.blueprint.core.domain.User

/**
 * Output port for user persistence.
 *
 * The core depends on this contract, not on a concrete database. Swapping storage
 * (or mocking it in tests) means providing a different adapter — use cases don't change.
 */
interface UserRepository {
    suspend fun save(user: User): User

    /** Returns the user, or null when no record matches. */
    suspend fun findById(id: String): User?

    /** Returns a page of users plus the total count. */
    suspend fun findAll(
        page: Int,
        limit: Int,
    ): Page<User>

    suspend fun deleteById(id: String)
}
